package com.wso2telco.dep.noauth.handler;

import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.rest.AbstractHandler;

public class TestAuth extends AbstractHandler implements ManagedLifecycle{

	@Override
	public boolean handleRequest(MessageContext messageContext) {
		// TODO Auto-generated method stub
		
		System.out.println("Super handleRequest==========================");
		return false;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext) {
		// TODO Auto-generated method stub
		
		System.out.println("Super handleResponse==========================");
		return false;
	}

	@Override
	public void init(SynapseEnvironment se) {
		// TODO Auto-generated method stub
		System.out.println("Super init==========================");
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("Super destroy==========================");
		
	}

}
