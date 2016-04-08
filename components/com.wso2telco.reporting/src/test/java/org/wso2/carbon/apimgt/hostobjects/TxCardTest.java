package org.wso2.carbon.apimgt.hostobjects;

import java.util.HashMap;

import com.wso2telco.custom.hostobjects.ApiTxCard;

public class TxCardTest {
	
	public static void main(String args[]){
		ApiTxCard card = new ApiTxCard();
		HashMap<Integer, Object> txCardTemp = card.getTxCardTemp();
		//TxCardDAO currentDao = (TxCardDAO) txCardTemp.get(new Integer(100));
		//System.out.println(currentDao.getHeaderList().size()+"");
	}
	
}
