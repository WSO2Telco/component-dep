package com.wso2telco.dep.custom.jwt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.impl.dto.Environment;
import org.wso2.carbon.apimgt.impl.internal.ServiceReferenceHolder;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.core.util.CryptoUtil;
import org.wso2.carbon.core.util.KeyStoreManager;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import java.net.URL;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomAPIUtil {

    private static final Log log = LogFactory.getLog(CustomAPIUtil.class);
    private static ConcurrentHashMap<Integer, Key> privateKeys = new ConcurrentHashMap();
    private static ConcurrentHashMap<Integer, Certificate> publicCerts = new ConcurrentHashMap();

    private static String getGatewayendpoint(String transports) {
        Map<String, Environment> gatewayEnvironments = ServiceReferenceHolder.getInstance().getAPIManagerConfigurationService().getAPIManagerConfiguration().getApiGatewayEnvironments();
        String gatewayURLs;
        if (gatewayEnvironments.size() > 1) {
            Iterator var3 = gatewayEnvironments.values().iterator();

            Environment environment;
            do {
                if (!var3.hasNext()) {
                    var3 = gatewayEnvironments.values().iterator();

                    do {
                        if (!var3.hasNext()) {
                            var3 = gatewayEnvironments.values().iterator();

                            do {
                                if (!var3.hasNext()) {
                                    return null;
                                }

                                environment = (Environment)var3.next();
                            } while(!"sandbox".equals(environment.getType()));

                            gatewayURLs = environment.getServerURL();
                            return extractHTTPSEndpoint(gatewayURLs, transports);
                        }

                        environment = (Environment)var3.next();
                    } while(!"production".equals(environment.getType()));

                    gatewayURLs = environment.getServerURL();
                    return extractHTTPSEndpoint(gatewayURLs, transports);
                }

                environment = (Environment)var3.next();
            } while(!"hybrid".equals(environment.getType()));

            gatewayURLs = environment.getServerURL();
            return extractHTTPSEndpoint(gatewayURLs, transports);
        } else {
            gatewayURLs = ((Environment)gatewayEnvironments.values().toArray()[0]).getServerURL();
            return extractHTTPSEndpoint(gatewayURLs, transports);
        }
    }

    private static String extractHTTPSEndpoint(String gatewayURLs, String transports) {
        String gatewayHTTPURL = null;
        String gatewayHTTPSURL = null;
        boolean httpsEnabled = false;
        String[] gatewayURLsArray = gatewayURLs.split(",");
        String[] transportsArray = transports.split(",");
        String[] var8 = transportsArray;
        int var9 = transportsArray.length;

        int var10;
        String url;
        for(var10 = 0; var10 < var9; ++var10) {
            url = var8[var10];
            if (url.startsWith("https")) {
                httpsEnabled = true;
            }
        }

        String gatewayURL;
        if (gatewayURLsArray.length > 1) {
            var8 = gatewayURLsArray;
            var9 = gatewayURLsArray.length;

            for(var10 = 0; var10 < var9; ++var10) {
                url = var8[var10];
                if (url.startsWith("https:")) {
                    gatewayHTTPSURL = url;
                } else if (!url.startsWith("ws:")) {
                    gatewayHTTPURL = url;
                }
            }

            if (httpsEnabled) {
                gatewayURL = gatewayHTTPSURL;
            } else {
                gatewayURL = gatewayHTTPURL;
            }
        } else {
            gatewayURL = gatewayURLs;
        }

        return gatewayURL;
    }

    protected static  String getIssuerDomainName() {
        String host = "UnIdentified";
        try {
            String url = getGatewayendpoint("http,https");
            URL urlObject = new URL(url);
            host = urlObject.getHost();
        } catch (Exception e) {
            log.error("Error occurred while extracting the Key manager Domain, UnIdentified will be used", e);
        }
        return host;
    }

    protected static String[] getUserRoles(String tenantName, String endUserName) {
        UserRealm realm = ServiceReferenceHolder.getUserRealm();
        if (realm == null) {
            try {
                realm = IdentityTenantUtil.getRealm(tenantName, endUserName);
            } catch (IdentityException e) {
                log.error("Error occurred while creating the Realm, null will be used", e);
            }
            ServiceReferenceHolder.setUserRealm(realm);
        }
        String[] userRoles = APIUtil.getListOfRolesQuietly(endUserName);
        return userRoles;
    }


    //Return JWS required for passing to java.security.Signature class for getting the Signature object
    protected static String getJCSFromJWS(String JWS) {
        //noinspection Since15
        if (JWS != null || !JWS.trim().isEmpty()) {
            if (JWS.length() != 5) {
                return JWS;
            } else {
                String keylength = JWS.substring(2, 5);
                if (JWS.startsWith("HS")) {
                    return "HmacSHA" + keylength;
                } else if (JWS.startsWith("RS")) {
                    return "SHA" + keylength + "withRSA";
                } else if (JWS.startsWith("ES")) {
                    return "SHA" + keylength + "withECDSA";
                }
            }
        }
        return "SHA256withRSA";// default value will be returned, if above all failed to return
    }

    protected static Key getSSLPrivateKeyForSigning(String endUserName) throws Exception {
        Key privateKey = null;
        String tenantDomain = MultitenantUtils.getTenantDomain(endUserName);
        int tenantId = APIUtil.getTenantId(endUserName);
        if (!privateKeys.containsKey(tenantId)) {
            APIUtil.loadTenantRegistry(tenantId);
            KeyStoreManager tenantKSM = KeyStoreManager.getInstance(tenantId);
            if (!extractCustomKeyStoreEnabledStatus(tenantId)) {
                if (!"carbon.super".equals(tenantDomain)) {
                    String ksName = tenantDomain.trim().replace('.', '-');
                    String jksName = ksName + ".jks";
                    privateKey = tenantKSM.getPrivateKey(jksName, tenantDomain);
                } else {
                    try {
                        privateKey = tenantKSM.getDefaultPrivateKey();
                    } catch (Exception ex) {
                        log.error("Error while obtaining private key for super tenant", ex);
                    }
                }
            } else {
                CryptoUtil cryptoUtil = CryptoUtil.getDefaultCryptoUtil();
                String password = cryptoUtil.encryptAndBase64Encode("wso2carbon".getBytes());
                KeyStore keystore = tenantKSM.getKeyStore("mutualSSL.jks");

                String alias = "connect.axpsouthbound.com";

                privateKey = keystore.getKey(alias, "wso2carbon".toCharArray());
                //privateKey = tenantKSM.getPrivateKey("mutualSSL.jks", tenantDomain);
            }
            if (privateKey != null) {
                privateKeys.put(tenantId, privateKey);
            }
        } else {
            privateKey = (Key) privateKeys.get(tenantId);
        }
        return privateKey;
    }

    protected static Certificate getSSLPublicKeyForSigning(String endUserName) throws Exception {
        Certificate publicCert;
        String tenantDomain = MultitenantUtils.getTenantDomain(endUserName);
        int tenantId = APIUtil.getTenantId(endUserName);
        if (!publicCerts.containsKey(tenantId)) {
            APIUtil.loadTenantRegistry(tenantId);
            KeyStoreManager tenantKSM = KeyStoreManager.getInstance(tenantId);
            if (!extractCustomKeyStoreEnabledStatus(tenantId)) {
                if (!"carbon.super".equals(tenantDomain)) {
                    String ksName = tenantDomain.trim().replace('.', '-');
                    String jksName = ksName + ".jks";
                    KeyStore keyStore = tenantKSM.getKeyStore(jksName);
                    publicCert = keyStore.getCertificate(tenantDomain);
                } else {
                    publicCert = tenantKSM.getDefaultPrimaryCertificate();
                }
            } else {
                CryptoUtil cryptoUtil = CryptoUtil.getDefaultCryptoUtil();
                String password = cryptoUtil.encryptAndBase64Encode("wso2carbon".getBytes());
                KeyStore keystore = tenantKSM.getKeyStore("mutualSSL.jks");
                String alias = "connect.axpsouthbound.com";
                publicCert = keystore.getCertificate(alias);
            }
            if (publicCert != null) {
                publicCerts.put(tenantId, publicCert);
            }
        } else {
            publicCert = publicCerts.get(tenantId);
        }
        return publicCert;
    }

    private static boolean extractCustomKeyStoreEnabledStatus(int tenantId) throws RegistryException {
        String CUSTOM_KEYSTORE_ENABLED = "ssl.with.custom.keystore";
        boolean isCustomKeyStoreEnabled = false;

        String customKeyStoreEnabledEntryPath = "/apimgt/" + CUSTOM_KEYSTORE_ENABLED;
        UserRegistry registry = ServiceReferenceHolder.getInstance().getRegistryService().getGovernanceSystemRegistry(tenantId);
        if (registry.resourceExists(customKeyStoreEnabledEntryPath)) {
            String customKeystoreEnabled = new String((byte[]) registry.get(customKeyStoreEnabledEntryPath).getContent());
            if (customKeystoreEnabled != null && !customKeystoreEnabled.trim().isEmpty()) {
                isCustomKeyStoreEnabled = Boolean.valueOf(customKeystoreEnabled);
            }
        }
        return isCustomKeyStoreEnabled;
    }
}
