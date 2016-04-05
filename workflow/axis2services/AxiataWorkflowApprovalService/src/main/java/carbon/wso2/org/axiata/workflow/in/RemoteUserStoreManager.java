package carbon.wso2.org.axiata.workflow.in;

public interface RemoteUserStoreManager {

	String[] getUserListOfRole(String role);
	String getUserClaimValue(String userName, String claim);
}
