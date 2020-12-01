package com.wso2telco.dep.custom.jwt;

import com.nimbusds.jwt.JWTClaimsSet;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.API;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.dto.APIKeyValidationInfoDTO;
import org.wso2.carbon.apimgt.impl.internal.ServiceReferenceHolder;
import org.wso2.carbon.apimgt.impl.token.ClaimsRetriever;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.keymgt.service.TokenValidationContext;
import org.wso2.carbon.apimgt.keymgt.token.JWTGenerator;
import org.wso2.carbon.governance.api.generic.GenericArtifactManager;
import org.wso2.carbon.governance.api.generic.dataobjects.GenericArtifact;
import org.wso2.carbon.registry.core.RegistryConstants;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.*;

public class CustomJWTTokenGenerator extends JWTGenerator {

    private static final Log log = LogFactory.getLog(CustomJWTTokenGenerator.class);
    private String signatureAlgorithm = "SHA256withRSA";
    private String jwtHeaderName;

    public CustomJWTTokenGenerator() {
        this.signatureAlgorithm = ServiceReferenceHolder.getInstance().getAPIManagerConfigurationService().getAPIManagerConfiguration().getFirstProperty("JWTConfiguration.SignatureAlgorithm");
        if (this.signatureAlgorithm == null || !"NONE".equals(this.signatureAlgorithm) && !"SHA256withRSA".equals(this.signatureAlgorithm) && !"ES384".equals(this.signatureAlgorithm)) {
            this.signatureAlgorithm = "SHA256withRSA";
        }
        
        this.jwtHeaderName = ServiceReferenceHolder.getInstance().getAPIManagerConfigurationService().getAPIManagerConfiguration().getFirstProperty("JWTConfiguration.JWTHeader");

    }

    public Map<String, Object> getCustomClaims(TokenValidationContext validationContext, long currentTime) {
        long ttl = this.getTTL()*1000;
        long expireIn = currentTime + ttl;
        APIKeyValidationInfoDTO apiKeyValidationInfoDTO = validationContext.getValidationInfoDTO();
        String endUserName = apiKeyValidationInfoDTO.getEndUserName();
        int tenantId = APIUtil.getTenantId(endUserName);
        String tenantName = APIUtil.getTenantDomainFromTenantId(tenantId);
        String[] userRoles = CustomAPIUtil.getUserRoles(tenantName, endUserName);
        String[] formattedUserRoles = CommonUtils.formatStringsToAlphaNum(userRoles);
        Map<String, Object> claims = new LinkedHashMap(3);

        claims.put("roles", formattedUserRoles);
        claims.put("exp", new Date(expireIn));
        claims.put("iat", new Date(currentTime));
        claims.put("oit", new Date(currentTime));
        return claims;
    }

    private Map<String, String> getStandardClaims(TokenValidationContext validationContext, long currentTime) throws APIManagementException {

    	
        APIKeyValidationInfoDTO apiKeyValidationInfoDTO = validationContext.getValidationInfoDTO();
        String endUserName = apiKeyValidationInfoDTO.getEndUserName();
        String subscriber = apiKeyValidationInfoDTO.getSubscriber();
        String applicationId = apiKeyValidationInfoDTO.getApplicationId();
        int tenantId = APIUtil.getTenantId(endUserName);
        String accessToken = validationContext.getAccessToken();        
        String dialect = getClaimDialect(validationContext);        

        Map<String, String> claims = new LinkedHashMap(9);

        claims.put("sub", "apigw_proxy");
        claims.put("iss", CustomAPIUtil.getIssuerDomainName());
        claims.put("jti", accessToken + String.valueOf(currentTime));
        claims.put("tenid", String.valueOf(tenantId));
        claims.put("salesChannel", getSalesChannel(validationContext, endUserName));
        claims.put(dialect + "/subscriber", subscriber);
        claims.put(dialect + "/applicationid", applicationId);
        claims.put(dialect + "/version", validationContext.getVersion());
        claims.put(dialect + "/enduser", APIUtil.getUserNameWithTenantSuffix(endUserName));
        return claims;
    }


    @Override
    public String buildHeader(TokenValidationContext tokenValidationContext) throws APIManagementException {
        String jwtHeader = null;
        if ("NONE".equals(this.signatureAlgorithm)) {
            StringBuilder jwtHeaderBuilder = new StringBuilder();
            jwtHeaderBuilder.append("{\"typ\":\"JWT\",");
            jwtHeaderBuilder.append("\"alg\":\"");
            jwtHeaderBuilder.append(this.getJWSCompliantAlgorithmCode("NONE"));
            jwtHeaderBuilder.append('"');
            jwtHeaderBuilder.append('}');
            jwtHeader = jwtHeaderBuilder.toString();
        } else if ("SHA256withRSA".equals(this.signatureAlgorithm)|| "ES384".equals(this.signatureAlgorithm)) {
            jwtHeader = this.addCertToHeader(tokenValidationContext.getValidationInfoDTO().getEndUserName());
        }

        return jwtHeader;
    }

    @Override
    protected String addCertToHeader(String endUserName) throws APIManagementException {
        String error;
        try {
            Certificate publicCert=CustomAPIUtil.getSSLPublicKeyForSigning(endUserName);
            MessageDigest digestValue = MessageDigest.getInstance("SHA-1");
            if (publicCert != null) {
                byte[] der = publicCert.getEncoded();
                digestValue.update(der);
                byte[] digestInBytes = digestValue.digest();
                org.apache.commons.codec.binary.Base64 base64 = new Base64(true);
                String base64UrlEncodedThumbPrint = base64.encodeToString(digestInBytes).trim();
                StringBuilder jwtHeader = new StringBuilder();
                jwtHeader.append("{\"typ\":\"JWT\",");
                jwtHeader.append("\"alg\":\"");
                jwtHeader.append(this.getJWSCompliantAlgorithmCode(this.signatureAlgorithm));
                jwtHeader.append("\",");
                jwtHeader.append("\"x5t\":\"");
                jwtHeader.append(base64UrlEncodedThumbPrint);
                jwtHeader.append('"');
                jwtHeader.append('}');
                return jwtHeader.toString();
            } else {
                error = "Error in obtaining tenant's keystore";
                throw new APIManagementException(error);
            }
        } catch (KeyStoreException var11) {
            error = "Error in obtaining tenant's keystore";
            throw new APIManagementException(error, var11);
        } catch (CertificateEncodingException var12) {
            error = "Error in generating public cert thumbprint";
            throw new APIManagementException(error, var12);
        } catch (NoSuchAlgorithmException var13) {
            error = "Error in generating public cert thumbprint";
            throw new APIManagementException(error, var13);
        } catch (Exception var14) {
            error = "Error in obtaining tenant's keystore";
            throw new APIManagementException(error, var14);
        }
    }

    @Override
    public String buildBody(TokenValidationContext validationContext) throws APIManagementException {
        String userAttributeSeparator = ",";
        long currentTime = System.currentTimeMillis();
        Map<String, Object> claims = new LinkedHashMap<String, Object>();
        Map<String, String> standardClaims = this.getStandardClaims(validationContext, currentTime);
        Map<String, Object> customClaims = this.getCustomClaims(validationContext, currentTime);
        if (standardClaims != null) {
            claims.putAll(standardClaims);
        }
        if (customClaims != null) {
            claims.putAll(customClaims);
        }
        if (claims != null) {
            JWTClaimsSet.Builder jwtClaimsSetBuilder = new JWTClaimsSet.Builder();
            Iterator it = (new TreeSet(claims.keySet())).iterator();
            while (true) {
                while (it.hasNext()) {
                    String claimURI = (String) it.next();
                    Object claimVal = claims.get(claimURI);
                    List<String> claimList = new ArrayList();
                    jwtClaimsSetBuilder.claim(claimURI, claimVal);
                }
                return jwtClaimsSetBuilder.build().toJSONObject().toJSONString();
            }
        } else {
            return null;
        }
    }

    @Override
    public String generateToken(TokenValidationContext validationContext) throws APIManagementException {
        String jwtHeader = this.buildHeader(validationContext);
        String base64UrlEncodedHeader = "";
        if (jwtHeader != null) {
            base64UrlEncodedHeader = this.encode(jwtHeader.getBytes());
        }

        String jwtBody = this.buildBody(validationContext);
        String base64UrlEncodedBody = "";
        if (jwtBody != null) {
            base64UrlEncodedBody = this.encode(jwtBody.getBytes());
        }
        
        String prefix = appendPrefixIfExist();

        if ("SHA256withRSA".equals(this.signatureAlgorithm) || "ES384".equals(this.signatureAlgorithm)) {
            String assertion = base64UrlEncodedHeader + '.' + base64UrlEncodedBody;
            byte[] signedAssertion = this.signJWT(assertion, validationContext.getValidationInfoDTO().getEndUserName());
            if (log.isDebugEnabled()) {
                log.debug("signed assertion value : " + new String(signedAssertion));
            }

            String base64UrlEncodedAssertion = this.encode(signedAssertion);
            return prefix + base64UrlEncodedHeader + '.' + base64UrlEncodedBody + '.' + base64UrlEncodedAssertion;
            
        } else {
        	return prefix + base64UrlEncodedHeader + '.' + base64UrlEncodedBody + '.';
        }
    }


    @Override
    public byte[] signJWT(String assertion, String endUserName) throws APIManagementException {
        String tenantDomain = null;

        String error;
        try {
            tenantDomain = MultitenantUtils.getTenantDomain(endUserName);
            int tenantId = APIUtil.getTenantId(endUserName);
            Key privateKey = CustomAPIUtil.getSSLPrivateKeyForSigning(endUserName);
            String jwsSignatureAlgorithm = CustomAPIUtil.getJCSFromJWS(this.signatureAlgorithm);
            Signature signature = Signature.getInstance(jwsSignatureAlgorithm);
            signature.initSign((PrivateKey) privateKey);
            byte[] dataInBytes = assertion.getBytes();
            signature.update(dataInBytes);
            return signature.sign();
        } catch (NoSuchAlgorithmException ex1) {
            error = "Signature algorithm not found.";
            throw new APIManagementException(error, ex1);
        } catch (InvalidKeyException ex2) {
            error = "Invalid private key provided for the signature";
            throw new APIManagementException(error, ex2);
        } catch (SignatureException ex3) {
            error = "Error in signature";
            throw new APIManagementException(error, ex3);
        } catch (Exception ex4) {
            error = "Error in loading tenant registry for " + tenantDomain;
            throw new APIManagementException(error, ex4);
        }
    }
    
    private String appendPrefixIfExist() throws APIManagementException
    {
    	String prefix = "";
    	if(this.jwtHeaderName != null) {
	    	if(this.jwtHeaderName.equalsIgnoreCase("Authorization")) {
	    		prefix = "Bearer ";
	    	}
    	}
    	else {
    		throw new APIManagementException("JWTHeader value is null");
    	}
    	
    	return prefix;
    }
    
    private String getClaimDialect(TokenValidationContext validationContext) throws APIManagementException
    {
    	String dialect = null;
    	ClaimsRetriever claimsRetriever = getClaimsRetriever();
        if (claimsRetriever != null) {
            dialect = claimsRetriever.getDialectURI(validationContext.getValidationInfoDTO().getEndUserName());
        } else {
            dialect = getDialectURI();
        }
        
        return dialect;
    }
    
    private API getAPI(TokenValidationContext validationContext) throws APIManagementException {
        API api=null;
        try {
            APIKeyValidationInfoDTO apiKeyValidationInfoDTO = validationContext.getValidationInfoDTO();
            String endUserName = apiKeyValidationInfoDTO.getEndUserName();
            int tenantId = APIUtil.getTenantId(endUserName);
            String providerName = apiKeyValidationInfoDTO.getApiPublisher();
            String apiPath = APIConstants.API_LOCATION + RegistryConstants.PATH_SEPARATOR +
                    providerName + RegistryConstants.PATH_SEPARATOR +
                    apiKeyValidationInfoDTO.getApiName() + RegistryConstants.PATH_SEPARATOR +
                    validationContext.getVersion() + RegistryConstants.PATH_SEPARATOR +
                    APIConstants.API_KEY;
            UserRegistry registry = ServiceReferenceHolder.getInstance().getRegistryService().getGovernanceSystemRegistry(tenantId);
            GenericArtifactManager artifactManager = APIUtil.getArtifactManager(registry, APIConstants.API_KEY);
            Resource resource = registry.get(apiPath);
            GenericArtifact artifact = artifactManager.getGenericArtifact(resource.getUUID());
            api = APIUtil.getAPI(artifact,registry);
        }catch (Exception e){
            throw new APIManagementException("Error occurred while fetching the API from registry",e);
        }
        return api;
    }
    
    private String getSalesChannel(TokenValidationContext validationContext, String userName) throws APIManagementException
	{
		String salesChannel = null;
		try {
			API api = getAPI(validationContext);
			JSONObject additionalProperties = api.getAdditionalProperties();
			Set<String> propertyKeys = additionalProperties.keySet();
			Iterator<String> it = propertyKeys.iterator();
			while (it.hasNext()) {
				String propertyKey = it.next();
				if (propertyKey != null && propertyKey.equalsIgnoreCase("SalesChannel")) {
					salesChannel = additionalProperties.get(propertyKey).toString();
					break;
				}
			}
		} catch (Exception ex) {
			log.warn("Error occurred while fetching salesChannel property from API", ex);
		}

		if (salesChannel == null || salesChannel.trim().isEmpty()) {
			String claimUrl = getClaimDialect(validationContext) + "/saleschannel";
			salesChannel = CommonUtils.getClaimValueFromDialect(userName, claimUrl);
		}
		return salesChannel;
	}
}
