package org.gsm.oneapi.server.payment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.controller.TransactionFunctions;
import mife.sandbox.model.PaymentImpl;
import mife.sandbox.model.entities.ChargeAmountRequest;
import mife.sandbox.model.entities.User;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.dialog.mife.server.SessionManager;
import org.gsm.oneapi.responseobject.payment.AmountTransaction;
import org.gsm.oneapi.server.ValidationRule;

public class ListTransactionsServlet extends PaymentServlet {
    
    static Logger logger = Logger.getLogger(AmountChargeServlet.class);
    private static final long serialVersionUID = -6237772242372106922L;
    private final String[] validationRules = {"payment", "V1", "*", "transactions"};
    SessionManager sessionMgr = new SessionManager();

    public void init() throws ServletException {
        logger.debug("AmountChargeServlet initialised");
        sessionMgr.start();
    }
    
    private PaymentImpl payimpl;
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        dumpRequestDetails(request, logger);


        payimpl = new PaymentImpl();
        
        String sandboxusr = request.getHeader("sandbox");
        User user = null;
        if (sandboxusr == null) {
            sandboxusr = getProfileIdFromRequest(request);
        }

        user = payimpl.getUser(sandboxusr);
        
        String[] requestParts = getRequestParts(request);
        
        if (validateRequest(request, response, requestParts, validationRules)) {
            
            String endUserId = requestParts[2];
            
            ValidationRule[] rules = {
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId),};
            
            if (checkRequestParameters(response, rules)) {
                
                
                String endUserIdString = getLastMobileNumber(endUserId);
                List<ChargeAmountRequest> transList = TransactionFunctions.getListTransactionsData(user.getId(), endUserIdString);
                
                List<AmountTransaction> txlist = new ArrayList<AmountTransaction>();
                if(transList != null){
                for(ChargeAmountRequest temp: transList){
                    AmountTransaction tx = new AmountTransaction();
                    tx.setEndUserID(temp.getEndUserId());
                    tx.setReferenceCode(temp.getReferenceCode());
                    tx.setServerReferenceCode(temp.getOriginalServerReferenceCode());
                    tx.setResourceURL(temp.getNotifyURL());
                    tx.setTransactionOperationStatus(temp.getTransactionOperationStatus());
                    
                    AmountTransaction.ChargingInformation paymentInfo = new AmountTransaction.ChargingInformation();
                    paymentInfo.setAmount(temp.getAmount());
                    paymentInfo.setCurrency(temp.getCurrency());
                    paymentInfo.setDescription(temp.getDescription());
                    
                    tx.setPaymentAmount(paymentInfo);
                    
                    txlist.add(tx);
                }
                }
                String resourceURL = getRequestHostnameAndContext(request) + request.getServletPath() + "/payment/V1/" + urlEncode(endUserId) + "/transactions";
                ObjectMapper mapper = new ObjectMapper();
                String jsonResponse = "{\"paymentTransactionList\": {\"amountTransaction\":" + mapper.writeValueAsString(txlist) + ",\"resourceURL\":"+mapper.writeValueAsString(resourceURL)+"}}";

                sendJSONResponse(response, jsonResponse, OK, null);
            }
        }
    }
    
}
