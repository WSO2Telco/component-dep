package mife.sandbox.model.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User implements Serializable {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    
    @Column(name="user_name", unique = true)
    private String userName;
    
    @Column(name="user_status")
    private int userStatus;
    
    @OneToMany(mappedBy="user")
    private List<ManageNumber> numberList;
    
    @OneToMany(mappedBy="user")
    private List<SenderAddress> senderAddressList;
    
    @OneToMany(mappedBy="user")
    private List<Payment> paymentList;
    
    @OneToMany(mappedBy="user")
    private List<Sms> smsList;
    
    @OneToMany(mappedBy="user")
    private List<SMSSubscription> smsSubscriptionList;
    
    @OneToMany(mappedBy="user")
    private List<ChargeAmountRequest> chargeAmountRequestList;
    
    @OneToMany(mappedBy="user")
    private List<SMSRequestLog> sendSMSRequestList;
    
    @OneToMany(mappedBy="user")
    private List<SubscribeSMSRequest> subscribeSMSRequestList;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the userStatus
     */
    public int getUserStatus() {
        return userStatus;
    }

    /**
     * @param userStatus the userStatus to set
     */
    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * @return the senderAddressList
     */
    public List<SenderAddress> getSenderAddressList() {
        return senderAddressList;
    }

    /**
     * @param senderAddressList the senderAddressList to set
     */
    public void setSenderAddressList(List<SenderAddress> senderAddressList) {
        this.senderAddressList = senderAddressList;
    }

    /**
     * @return the paymentList
     */
    public List<Payment> getPaymentList() {
        return paymentList;
    }

    /**
     * @param paymentList the paymentList to set
     */
    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    /**
     * @return the smsList
     */
    public List<Sms> getSmsList() {
        return smsList;
    }

    /**
     * @param smsList the smsList to set
     */
    public void setSmsList(List<Sms> smsList) {
        this.smsList = smsList;
    }

    /**
     * @return the smsSubscriptionList
     */
    public List<SMSSubscription> getSmsSubscriptionList() {
        return smsSubscriptionList;
    }

    /**
     * @param smsSubscriptionList the smsSubscriptionList to set
     */
    public void setSmsSubscriptionList(List<SMSSubscription> smsSubscriptionList) {
        this.smsSubscriptionList = smsSubscriptionList;
    }

    /**
     * @return the chargeAmountRequestList
     */
    public List<ChargeAmountRequest> getChargeAmountRequestList() {
        return chargeAmountRequestList;
    }

    /**
     * @param chargeAmountRequestList the chargeAmountRequestList to set
     */
    public void setChargeAmountRequestList(List<ChargeAmountRequest> chargeAmountRequestList) {
        this.chargeAmountRequestList = chargeAmountRequestList;
    }

    /**
     * @return the sendSMSRequestList
     */
    public List<SMSRequestLog> getSendSMSRequestList() {
        return sendSMSRequestList;
    }

    /**
     * @param sendSMSRequestList the sendSMSRequestList to set
     */
    public void setSendSMSRequestList(List<SMSRequestLog> sendSMSRequestList) {
        this.sendSMSRequestList = sendSMSRequestList;
    }

    /**
     * @return the subscribeSMSRequestList
     */
    public List<SubscribeSMSRequest> getSubscribeSMSRequestList() {
        return subscribeSMSRequestList;
    }

    /**
     * @param subscribeSMSRequestList the subscribeSMSRequestList to set
     */
    public void setSubscribeSMSRequestList(List<SubscribeSMSRequest> subscribeSMSRequestList) {
        this.subscribeSMSRequestList = subscribeSMSRequestList;
    }

    /**
     * @return the numberList
     */
    public List<ManageNumber> getNumberList() {
        return numberList;
    }

    /**
     * @param numberList the numberList to set
     */
    public void setNumberList(List<ManageNumber> numberList) {
        this.numberList = numberList;
    }

    
    
}
