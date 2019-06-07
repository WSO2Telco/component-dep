package com.wso2telco.dep.apimlogging;

import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.Tag;
import org.wso2.carbon.registry.core.jdbc.handlers.Handler;
import org.wso2.carbon.registry.core.jdbc.handlers.RequestContext;
import org.wso2.carbon.registry.core.session.CurrentSession;
import org.wso2.carbon.registry.core.utils.RegistryUtils;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class APILogHandler extends Handler {

    private static final Log log = LogFactory.getLog(APILogHandler.class);
	private static Log auditLog = CarbonConstants.AUDIT_LOG;
    private static String curUser = "";
    
    public void put(RequestContext requestContext) {
        String newResourceContent = "";
        try {
        	curUser = CurrentSession.getUser();
        	//accessing the new resource
            Resource updatedResource = requestContext.getResource();
            Object content = updatedResource.getContent();
            if (updatedResource.getContent() instanceof byte[]) {
            	newResourceContent = RegistryUtils.decodeBytes((byte[]) updatedResource.getContent());
            } else {
            	newResourceContent = updatedResource.getContent().toString();
            }
        } catch	(Exception e) {
            log.error("error accessing the new resource", e);
        }
        if (!newResourceContent.equalsIgnoreCase("")) {
            try {
                //accessing the old resource
                String RESOURCE_PATH = String.valueOf(requestContext.getResourcePath());
                Resource oldResource = null;
                Registry registry = requestContext.getRegistry();
                if (registry.resourceExists(RESOURCE_PATH)) {
                	oldResource = registry.get(RESOURCE_PATH);

                    Object contentOld = oldResource.getContent();
                    String oldResourceContent = "";
                    if (oldResource.getContent() instanceof byte[]) {
                        oldResourceContent = RegistryUtils.decodeBytes((byte[]) oldResource.getContent());
                    } else {
                        oldResourceContent = oldResource.getContent().toString();
                    }

                    String oldResourceContentUptoOverview = oldResourceContent.substring(0, oldResourceContent.indexOf("</overview>") + "</overview>".length()) + "</metadata>";
                    String newResourceContentUptoOverview = newResourceContent.substring(0, newResourceContent.indexOf("</overview>") + "</overview>".length()) + "</metadata>";
                    
                    if (!oldResourceContentUptoOverview.equals(newResourceContentUptoOverview)) {
                        String logEntry = getLogEntryComparingResources(oldResourceContentUptoOverview, newResourceContentUptoOverview);
                    	if (!logEntry.equals("")) {
                        	log.info(logEntry);
                        	auditLog.info(logEntry);
                    	}
                    } else {
                        //log for create new version
                    	String logEntry = getLogEntryCreateNewVersion(newResourceContentUptoOverview);
                    	if (!logEntry.equals("")) {
                        	log.info(logEntry);
                        	auditLog.info(logEntry);
                    	}
                    }
                } else {
                	//Do nothing since this is in API add mode
                }
            } catch	(Exception e) {
                log.error("error on put method", e);
            }
        }
    }

    public void delete(RequestContext requestContext) {
        try {
        	curUser = CurrentSession.getUser();
            //accessing the resource
            String RESOURCE_PATH = String.valueOf(requestContext.getResourcePath());
            Resource oldResource = requestContext.getRegistry().get(RESOURCE_PATH);
            Object contentOld = oldResource.getContent();
            String oldResourceContent = RegistryUtils.decodeBytes((byte[]) oldResource.getContent());
            if (oldResource.getContent() instanceof byte[]) {
                oldResourceContent = RegistryUtils.decodeBytes((byte[]) oldResource.getContent());
            } else {
                oldResourceContent = oldResource.getContent().toString();
            }

            String logEntry = getLogEntryForDeletedResource(oldResourceContent);
            log.info(logEntry);
            auditLog.info(logEntry);
        } catch	(Exception e) {
            log.error("error on delete method", e);
        }
    }
    
	public void applyTag(RequestContext requestContext) throws RegistryException {
		try {
			/*
			String RESOURCE_PATH = String.valueOf(requestContext.getResourcePath());
			Tag[] tags = requestContext.getRegistry().getTags(RESOURCE_PATH);
			for (Tag tag : tags) {
				log.info(tag);
			}
			*/
			String tag = requestContext.getTag();
            String logEntry = "Added tag - " + tag;
            log.info(logEntry);
            auditLog.info(logEntry);
		} catch (Exception e) {
			log.error("error on applyTag method", e);
		}
	}
    
	public void removeTag(RequestContext requestContext) throws RegistryException {
		try {
			/*
			String RESOURCE_PATH = String.valueOf(requestContext.getResourcePath());		
			Tag[] tags = requestContext.getRegistry().getTags(RESOURCE_PATH);
			for (Tag tag : tags) {
				log.info(tag);
			}
			*/
			String tag = requestContext.getTag();
            String logEntry = "Removed tag - " + tag;
            log.info(logEntry);
            auditLog.info(logEntry);
		} catch (Exception e) {
			log.error("error accessing ", e);
		}
	}
    
	private static String getLogEntryComparingResources(String oldResourceContentUptoOverview, String newResourceContentUptoOverview) {
		String logEntry = "";
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource inputSource = new InputSource(new StringReader(oldResourceContentUptoOverview));
			Document document = builder.parse(inputSource);
			Element rootElement = document.getDocumentElement();

			DocumentBuilderFactory factoryNew = DocumentBuilderFactory.newInstance();
			DocumentBuilder builderNew = factoryNew.newDocumentBuilder();
			InputSource inputSourceNew = new InputSource(new StringReader(newResourceContentUptoOverview));
			Document documentNew = builderNew.parse(inputSourceNew);
			Element rootElementNew = documentNew.getDocumentElement();
			
			NodeList list = rootElement.getElementsByTagName("overview");
			if (list != null && list.getLength() > 0) {
				NodeList elementList = list.item(0).getChildNodes();

				String status = getElementValue(rootElementNew, "status");
				String apiName = getElementValue(rootElementNew, "name");
				for (int i = 0; i < elementList.getLength(); i++) {
					Node node = elementList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						String nodeName = node.getNodeName();
						String nodeValue = node.getTextContent();
						String nodeValueNew = getMatchingElementValue(rootElementNew, nodeName);
						if (!nodeValue.equals(nodeValueNew)) {
							if (logEntry.equals("")) {
								if (status.equals("CREATED")) {
									logEntry += "API Created by " + curUser;
								} else {
									logEntry += "API Updated by " + curUser;
								}
					        	logEntry += " | name - " + apiName;
							}
				        	logEntry += " | " + nodeName + " - " + nodeValue + " > " + nodeValueNew;
						}
					}
				}
			}            
		} catch (Exception e) {
            log.error("error comparing the resources", e);
		}
		return logEntry;
	}
	
	private static String getMatchingElementValue(Element rootElement, String requiredNodeName) {
		String result = "";
		NodeList list = rootElement.getElementsByTagName("overview");
		if (list != null && list.getLength() > 0) {
			NodeList elementList = list.item(0).getChildNodes();

			for (int i = 0; i < elementList.getLength(); i++) {
				Node node = elementList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String nodeName = node.getNodeName();
					String nodeValue = node.getTextContent();
					if (nodeName.equals(requiredNodeName)) {
						result = nodeValue;
						break;
					}
				}
			}
		}
		return result;
	}
	
	private static String getLogEntryCreateNewVersion(String newResourceContentUptoOverview) {
		String logEntry = "";
		try {
			DocumentBuilderFactory factoryNew = DocumentBuilderFactory.newInstance();
			DocumentBuilder builderNew = factoryNew.newDocumentBuilder();
			InputSource inputSourceNew = new InputSource(new StringReader(newResourceContentUptoOverview));
			Document documentNew = builderNew.parse(inputSourceNew);
			Element rootElementNew = documentNew.getDocumentElement();

			String status = getElementValue(rootElementNew, "status");
			if (status.equals("CREATED")) {
				String apiName = getElementValue(rootElementNew, "name");
				String context = getElementValue(rootElementNew, "context");
				String version = getElementValue(rootElementNew, "version");
				logEntry += "API name - " + apiName;
	        	logEntry += " | user - " + curUser;
	        	logEntry += " | version - " + version;
	        	logEntry += " | context - " + context;
	        	logEntry += " | status - " + status;
			} else {
				//Do nothing
			}           
		} catch (Exception e) {
            log.error("error on getLogEntryCreateNewVersion method", e);
		}
		return logEntry;
	}
    
	private static String getLogEntryForDeletedResource(String oldResourceContentUptoOverview) {
		String logEntry = "";
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource inputSource = new InputSource(new StringReader(oldResourceContentUptoOverview));
			Document document = builder.parse(inputSource);
			Element rootElement = document.getDocumentElement();

			if (logEntry.equals("")) {
				logEntry += "API Deleted by " + curUser;
			}
			String apiName = getElementValue(rootElement, "name");
			String apiDescription = getElementValue(rootElement, "description");
			String context = getElementValue(rootElement, "context");
			String version = getElementValue(rootElement, "version");
        	logEntry += " | name - " + apiName;
        	logEntry += " | description - " + apiName;
        	logEntry += " | version - " + version;
        	logEntry += " | context - " + context;
        	
		} catch (Exception e) {
            log.error("error getting log from deleted resource", e);
		}
		return logEntry;
	}
	
	private static String getElementValue(Element rootElement, String requiredNodeName) {
		String result = "";
		NodeList list = rootElement.getElementsByTagName("overview");
		if (list != null && list.getLength() > 0) {
			NodeList elementList = list.item(0).getChildNodes();

			for (int i = 0; i < elementList.getLength(); i++) {
				Node node = elementList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String nodeName = node.getNodeName();
					if (nodeName.equals(requiredNodeName)) {
						String nodeValue = node.getTextContent();
						result = nodeValue;
						break;
					}
				}
			}
		}
		return result;
	}

}
