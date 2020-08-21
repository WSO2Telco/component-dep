# amdocs-egw-igw-customizations

--------------------------------------------------
custom-jwt-generator

1.The custom JWT needs to be enabled by following at api-manager.xml.
<JWTConfiguration>

<JWTGeneratorImpl>com.wso2telco.dep.custom.jwt.CustomJWTTokenGenerator</JWTGeneratorImpl>

</JWTConfiguration>


2.This is jwt format customized based on the amdocs requirements


3.This customization have enabled feature to use ES384 signing algorithms via the configuration at api-manager.xml. By default it will be using SHA256withRSA.But amdocs are using default implementation.
<JWTConfiguration>

<SignatureAlgorithm>ES384</SignatureAlgorithm>

</JWTConfiguration>


4.If custom keystore is required to be used for sigining it need to be added to the registry and registry flag need to be created. In amdocs usecase default is used so following is not required.

Registry flag should be as following path and value
path ==> /_system/governance/apimgt/ssl.with.custom.keystore
value ===> true

And the keystore needs to be added, with properties mentioned for the resource. 

path ===> /_system/governance/repository/security/key-stores/mutualSSL.jks

properties
type ===> jks
resource.source ===> AdminConsole
password ===> this needs to be generated via CryptoUtil.getDefaultCryptoUtil().encryptAndBase64Encode("wso2carbon".getBytes())

--------------------------------------------------
