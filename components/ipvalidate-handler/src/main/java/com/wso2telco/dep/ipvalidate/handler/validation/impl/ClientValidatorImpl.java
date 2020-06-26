package com.wso2telco.dep.ipvalidate.handler.validation.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityException;

import com.wso2telco.dep.ipvalidate.handler.validation.ClientValidator;
import com.wso2telco.dep.ipvalidate.handler.validation.CustomValidator;
import com.wso2telco.dep.ipvalidate.handler.validation.configuration.IPValidationProperties;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.RequestData;

public class ClientValidatorImpl implements ClientValidator {

	private static final Log log = LogFactory.getLog(ClientValidatorImpl.class);

	@Override
	public boolean validateRequest(RequestData requestData) throws APISecurityException {

		boolean status = false;

		try {

			log.debug("Start validate request : " + requestData);
			log.info("Start validate request : " + requestData);

			String validationclasses = IPValidationProperties.getValidatorClasses();
			String[] strValidationClassList = validationclasses.split(",");
			CustomValidator previousValidation = null;
			for (String strvalidationclass : strValidationClassList) {				
				log.debug("Validate class " + strvalidationclass);
				log.info("Validate class " + strvalidationclass);
				CustomValidator nextValidation = (CustomValidator) Class.forName(strvalidationclass).newInstance();
				if (previousValidation != null) {
					previousValidation.setNextValidator(nextValidation);
				}
				previousValidation = nextValidation;

				status = previousValidation.doValidation(requestData);
				log.debug("Validate class " + strvalidationclass + " response : " + status);
				log.info("Validate class " + strvalidationclass + " response : " + status);
				if(!status)
				{
					break;
				}
			}
		} catch (APISecurityException e) {
			log.error(e);
			throw e;
		} catch (InstantiationException e) {
			log.error(e);
			throw new APISecurityException(IPValidationProperties.getValidationFalidErrCode(),
					IPValidationProperties.getValidationFalidErrMsg());
		} catch (IllegalAccessException e) {
			log.error(e);
			throw new APISecurityException(IPValidationProperties.getValidationFalidErrCode(),
					IPValidationProperties.getValidationFalidErrMsg());
		} catch (ClassNotFoundException e) {
			log.error(e);
			throw new APISecurityException(IPValidationProperties.getValidationFalidErrCode(),
					IPValidationProperties.getValidationFalidErrMsg());
		}

		return status;
	}

}
