/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.reportingservice.southbound;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jaxen.JaxenException;
import org.json.JSONObject;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.API;
import org.wso2.carbon.apimgt.api.model.APIKey;
import org.wso2.carbon.apimgt.api.model.Application;
import org.wso2.carbon.apimgt.api.model.SubscribedAPI;
import org.wso2.carbon.apimgt.api.model.Subscriber;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.dto.APIInfoDTO;
import org.wso2.carbon.apimgt.usage.client.exception.APIMgtUsageQueryServiceClientException;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

import com.wso2telco.dep.reportingservice.APIRequestDTO;
import com.wso2telco.dep.reportingservice.APIResponseDTO;
import com.wso2telco.dep.reportingservice.ApiTxCard;
import com.wso2telco.dep.reportingservice.BillingSubscription;
import com.wso2telco.dep.reportingservice.HostObjectConstants;
import com.wso2telco.dep.reportingservice.PaymentRequestDTO;
import com.wso2telco.dep.reportingservice.Tax;
import com.wso2telco.dep.reportingservice.dao.Approval;
import com.wso2telco.dep.reportingservice.dao.BillingDAO;
import com.wso2telco.dep.reportingservice.dao.OperatorDAO;
import com.wso2telco.dep.reportingservice.dao.TaxDAO;
import com.wso2telco.dep.reportingservice.dao.TxCardDAO;
import com.wso2telco.dep.reportingservice.internal.HostObjectComponent;
import com.wso2telco.dep.reportingservice.util.CategoryEntity;
import com.wso2telco.dep.reportingservice.util.ChargeRate;
import com.wso2telco.dep.reportingservice.util.CommissionPercentagesDTO;
import com.wso2telco.dep.reportingservice.util.OperatorDetailsEntity;
import com.wso2telco.dep.reportingservice.util.RateCommission;
import com.wso2telco.dep.reportingservice.util.RateKey;
import com.wso2telco.dep.reportingservice.util.RateType;
import com.wso2telco.dep.reportingservice.util.SubCategory;
import com.wso2telco.dep.reportingservice.util.SurchargeEntity;
import com.wso2telco.dep.reportingservice.util.UsageTiers;

// TODO: Auto-generated Javadoc
/**
 * The Class SbHostObjectUtils.
 */
public class SbHostObjectUtils {

	/** The Constant log. */
	private static final Log log = LogFactory.getLog(SbHostObjectUtils.class);
	
	/** The Constant ALL_SUBSCRIBERS_KEYWORD. */
	private static final String ALL_SUBSCRIBERS_KEYWORD = "__ALL__";
	
	/** The Constant ALL_APPLICATIONS_KEYWORD. */
	private static final String ALL_APPLICATIONS_KEYWORD = "__ALL__";
	
	/** The Constant CAT_DEFAULT. */
	private static final String CAT_DEFAULT = "__default__";
	
	/** The Constant DISPLAY_DEFAULT. */
	private static final String DISPLAY_DEFAULT = "Default";

	/**
	 * Check data publishing enabled.
	 *
	 * @return true, if successful
	 */
	public static boolean checkDataPublishingEnabled() {

		APIManagerConfiguration configuration = HostObjectComponent
				.getAPIManagerConfiguration();
		String enabledStr = configuration
				.getFirstProperty(APIConstants.API_USAGE_ENABLED);
		return enabledStr != null && Boolean.parseBoolean(enabledStr);
	}

	/**
	 * Gets the billing subscriptions for user.
	 *
	 * @param username the username
	 * @param year the year
	 * @param month the month
	 * @return the billing subscriptions for user
	 * @throws APIManagementException the API management exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 */
	public static Map<Application, Set<BillingSubscription>> getBillingSubscriptionsForUser(
			String username, String year, String month)
			throws APIManagementException,
			APIMgtUsageQueryServiceClientException {
		Subscriber subscriber = new Subscriber(username);
		ApiMgtDAO apiMgtDAO = new ApiMgtDAO();
		// Adding null as param
		Set<SubscribedAPI> apis = apiMgtDAO.getSubscribedAPIs(subscriber, null);
		Map<Application, Set<BillingSubscription>> billingDetails = null;
		if (apis != null) {
			billingDetails = new HashMap<Application, Set<BillingSubscription>>();
			log.info("::: Subscribed APIs not null :::");
			for (SubscribedAPI api : apis) {
				log.info("::: Subscribed API : "
						+ api.getApplication().getName() + " , "
						+ api.getTier().getName() + " , " + api.getApiId());
				if (!isSubscriptionValidForMonth(api, year, month)) {
					continue;
				}
				if (billingDetails.containsKey(api.getApplication())) {
					billingDetails.get(api.getApplication()).add(
							new BillingSubscription(api));
				} else {
					Set<BillingSubscription> set = new HashSet<BillingSubscription>();
					set.add(new BillingSubscription(api));
					billingDetails.put(api.getApplication(), set);
				}
			}
		} else {
			log.info("::: Subscribed APIS null");
		}

		return billingDetails;
	}

	/**
	 * Checks if is subscription valid for month.
	 *
	 * @param subAPI the sub api
	 * @param year the year
	 * @param month the month
	 * @return true, if is subscription valid for month
	 * @throws APIManagementException the API management exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 */
	private static boolean isSubscriptionValidForMonth(SubscribedAPI subAPI,
			String year, String month) throws APIManagementException,
			APIMgtUsageQueryServiceClientException {
		java.util.Date createdTime = BillingDAO
				.getSubscriptionCreatedTime(subAPI.getApplication().getId(),
						subAPI.getApiId());
		Date reportDate = Date.valueOf(year + "-" + month + "-01");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(reportDate);

		// compare created time with 1st day of next month
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DATE,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		if (createdTime != null) {
			return createdTime.before(calendar.getTime());
		} else {
			return false;
		}

	}

	/**
	 * Gets the billing subscriptions for user app.
	 *
	 * @param username the username
	 * @param application the application
	 * @param txnapi the txnapi
	 * @return the billing subscriptions for user app
	 * @throws APIManagementException the API management exception
	 */
	public static SubscribedAPI getBillingSubscriptionsForUserApp(
			String username, String application, String txnapi)
			throws APIManagementException {
		Subscriber subscriber = new Subscriber(username);
		SubscribedAPI retSub = null;
		ApiMgtDAO apiMgtDAO = new ApiMgtDAO();
		Set<SubscribedAPI> apis = apiMgtDAO.getSubscribedAPIs(subscriber,
				application);
		if (apis != null) {

			log.info("::: Subscribed APIs not null :::");
			for (SubscribedAPI api : apis) {
				log.info("::: Subscribed API : "
						+ api.getApplication().getName() + " , "
						+ api.getTier().getName() + " , " + api.getApiId());
				if (api.getApiId().getApiName().equalsIgnoreCase(txnapi)) {
					retSub = api;
					break;
				}
			}
		} else {
			log.info("::: Subscribed APIS null");
		}

		return retSub;
	}

	/**
	 * Gets the app key.
	 *
	 * @param app the app
	 * @param keyType the key type
	 * @return the app key
	 */
	public static APIKey getAppKey(Application app, String keyType) {
		List<APIKey> apiKeys = app.getKeys();
		return getKeyOfType(apiKeys, keyType);
	}

	/**
	 * Gets the key of type.
	 *
	 * @param apiKeys the api keys
	 * @param keyType the key type
	 * @return the key of type
	 */
	public static APIKey getKeyOfType(List<APIKey> apiKeys, String keyType) {
		for (APIKey key : apiKeys) {
			if (keyType.equals(key.getType())) {
				return key;
			}
		}
		return null;
	}

	/**
	 * Apply charging plan.
	 *
	 * @param billingSubs the billing subs
	 * @param rateCard the rate card
	 * @param year the year
	 * @param month the month
	 * @throws Exception 
	 */
	public static void applyChargingPlan(
			Map<Application, Set<BillingSubscription>> billingSubs,
			Map<RateKey, ChargeRate> rateCard, String year, String month)
			throws Exception {

		Set<Map.Entry<Application, Set<BillingSubscription>>> entries = billingSubs
				.entrySet();

		for (Map.Entry<Application, Set<BillingSubscription>> entry : entries) {
			Set<BillingSubscription> subscriptions = entry.getValue();
			for (BillingSubscription subscription : subscriptions) {

				String appName = subscription.getApplication().getName();

				String subscriber = subscription.getSubscriber().getName();
				String apiName = subscription.getApiId().getApiName();
				subscription.setApiIdInt(BillingDAO
						.getApiId(subscription.getApiId()));
				subscription.setYear(year);
				if (month != null && month.length() == 1) {
					month = "0" + month;
				}
				subscription.setMonth(month);

				log.debug("Applying charging plan for API - " + apiName);

				populateOperatorDetailsOfSubscription(subscription, rateCard);
				populateCategoryViseAPICounts(subscription);
				calculateChargeForSubscription(subscription, rateCard);

			}
		}
	}

	/**
	 * Bill component.
	 *
	 * @param rate the rate
	 * @param rateCard the rate card
	 * @param operatorSubscription the operator subscription
	 * @param subsYear the subs year
	 * @param subsMonth the subs month
	 * @param application the application
	 * @param ApiName the api name
	 * @param apiVersion the api version
	 * @param categoryEntry the category entry
	 * @param appId the app id
	 * @param apiId the api id
	 * @param subsId the subs id
	 * @throws Exception 
	 */
	private static void billComponent(ChargeRate rate,
			Map<RateKey, ChargeRate> rateCard,
			BillingSubscription.OperatorSubscription operatorSubscription,
			String subsYear, String subsMonth, Application application,
			String ApiName, String apiVersion,
			Map.Entry<CategoryCharge, BilledCharge> categoryEntry, int appId,
			int apiId, String subsId) throws Exception {

		String billCategory = categoryEntry.getKey().getCategory();
		String billSubCategory = categoryEntry.getKey().getSubcategory();
		BigDecimal billRate = rate.getValue();
		Object SubsRate = null;

		switch (rate.getType()) {

		case CONSTANT:
			// apply charge for category

			SubsRate = getRateSubcategory(rate, billCategory, billSubCategory);
			if (SubsRate != null) {
				billRate = new BigDecimal((String) SubsRate);
			}
			categoryEntry.getValue().setPrice(billRate);
			applyTaxForBlockCharging(categoryEntry, rate, subsYear, subsMonth);
			break;

		case QUOTA:

			Map<String, String> rateAttributes = (Map<String, String>) rate
					.getRateAttributes();

			SubsRate = getRateSubcategory(rate, billCategory, billSubCategory);

			if (SubsRate != null) {
				rateAttributes = (Map<String, String>) SubsRate;
			}

			if (rateAttributes == null
					|| !rateAttributes
							.containsKey(HostObjectConstants.RATE_ATTRIBUTE_MAX_COUNT
									.toString())
					|| !rateAttributes
							.containsKey(HostObjectConstants.RATE_ATTRIBUTE_EXCESS_RATE
									.toString())
					|| !rateAttributes
							.containsKey(HostObjectConstants.RATE_ATTRIBUTE_DEFAULT_RATE
									.toString())) {
				throw new APIManagementException(
						"Attributes required for QUOTA charging are not specified in rate-card.xml");
			}

			int maxCount = Integer.parseInt(rateAttributes
					.get(HostObjectConstants.RATE_ATTRIBUTE_MAX_COUNT
							.toString()));
			BigDecimal excessRate = new BigDecimal(
					rateAttributes
							.get(HostObjectConstants.RATE_ATTRIBUTE_EXCESS_RATE
									.toString()));
			BigDecimal defaultRate = new BigDecimal(
					rateAttributes
							.get(HostObjectConstants.RATE_ATTRIBUTE_DEFAULT_RATE
									.toString()));

			if (categoryEntry.getValue().getCount() > maxCount) {
				int excess = categoryEntry.getValue().getCount() - maxCount;
				BigDecimal charge = excessRate.multiply(
						BigDecimal.valueOf(excess)).add(defaultRate);
				categoryEntry.getValue().setPrice(charge);
			} else {
				categoryEntry.getValue().setPrice(defaultRate);
			}
			applyTaxForBlockCharging(categoryEntry, rate, subsYear, subsMonth);
			break;

		case MULTITIER:
			int totalRequestCount = categoryEntry.getValue().getCount();

			BigDecimal price = BigDecimal.ZERO;
			int tierCount = 0;
			List<UsageTiers> usageTier = rate.getUsageTiers();
			SubsRate = getRateSubcategory(rate, billCategory, billSubCategory);
			if (SubsRate != null) {
				usageTier = (List<UsageTiers>) SubsRate;
			}

			calculateTiersCharges(usageTier, rateCard, totalRequestCount,
					tierCount, operatorSubscription, subsYear, subsMonth,
					application, ApiName, apiVersion, categoryEntry, appId,
					apiId, subsId);
			break;

		case PERCENTAGE:
			APIKey prodKey = getAppKey(application,
					APIConstants.API_KEY_TYPE_PRODUCTION);
			if (prodKey != null) {
				String consumerKey = prodKey.getConsumerKey();
				String api_version = ApiName + ":v" + apiVersion;
				Set<PaymentRequestDTO> paymentRequestSet = BillingDAO
						.getPaymentAmounts(Short.parseShort(subsYear),
								Short.parseShort(subsMonth), consumerKey,
								api_version,
								operatorSubscription.getOperator(),
								operatorSubscription.getOperationId(),
								categoryEntry.getKey().getCategory(),
								categoryEntry.getKey().getSubcategory());
				applyChargesForPaymentApi(operatorSubscription,
						paymentRequestSet, categoryEntry, appId, apiId, subsId);
			}
			break;

		case SUBSCRIPTION:
			// Update the Handler to count the subscribers operator wise
			int noOfSubscribers = categoryEntry.getValue().getCount();
			if (SubsRate != null) {
				billRate = new BigDecimal((String) SubsRate);
			}
			categoryEntry.getValue().setPrice(
					billRate.multiply(new BigDecimal(noOfSubscribers)));
			applyTaxForBlockCharging(categoryEntry, rate, subsYear, subsMonth);
			break;

		case PER_REQUEST:
			applyChargesWithTax(subsYear, subsMonth, application, ApiName,
					apiVersion, operatorSubscription, categoryEntry, rate);
			break;

		default:
			break;
		}

	}

	/**
	 * Gets the rate subcategory.
	 *
	 * @param rate the rate
	 * @param billCategory the bill category
	 * @param billSubCategory the bill sub category
	 * @return the rate subcategory
	 * @throws APIManagementException the API management exception
	 */
	private static Object getRateSubcategory(ChargeRate rate,
			String billCategory, String billSubCategory)
			throws APIManagementException {

		Object categoryRate = null;
		String subcategory = CAT_DEFAULT;
		if ((billSubCategory != null) && (!billSubCategory.isEmpty())) {
			subcategory = billSubCategory;
		}

		if (!rate.getCategoryBasedVal()) {
			return categoryRate;
		}

		if ((billCategory != null) && (!billCategory.isEmpty())) {

			if (!rate.getCategories().containsKey(billCategory)) {
				throw new APIManagementException(
						"Attributes required for charging are not specified in rate-card.xml");
			}
			Map<String, Object> subcategorymap = (Map<String, Object>) rate
					.getCategories().get(billCategory);
			if (!subcategorymap.containsKey(subcategory)) {
				throw new APIManagementException(
						"Attributes required for charging are not specified in rate-card.xml");
			}
			categoryRate = subcategorymap.get(subcategory);

		}
		return categoryRate;
	}

	/**
	 * Calculate charge for subscription.
	 *
	 * @param subscription the subscription
	 * @param rateCard the rate card
	 * @throws Exception 
	 */
	private static void calculateChargeForSubscription(
			BillingSubscription subscription, Map<RateKey, ChargeRate> rateCard)
			throws Exception {

		for (BillingSubscription.OperatorSubscription operatorSubscription : subscription
				.getOperatorSubscriptionList()) {
			ChargeRate rate = operatorSubscription.getRate();
			if (rate == null) {
				continue;
			}
			Map<CategoryCharge, BilledCharge> map = operatorSubscription
					.getCategoryCharges();

			// check if category via charging available
			if ((map == null) || (map.isEmpty())) {

				if (rate.getType().equals(RateType.CONSTANT)) {
					map = new HashMap<CategoryCharge, BilledCharge>();
					map.put(new CategoryCharge(operatorSubscription
							.getOperationId(), "", ""), new BilledCharge(0));
				} else {
					// no data to bill
					continue;
				}
			}

			for (Map.Entry<CategoryCharge, BilledCharge> entry : map.entrySet()) {
				billComponent(rate, rateCard, operatorSubscription,
						subscription.getYear(), subscription.getMonth(),
						subscription.getApplication(), subscription.getApiId()
								.getApiName(), subscription.getApiId()
								.getVersion(), entry, subscription
								.getApplication().getId(),
						subscription.getApiIdInt(), subscription
								.getSubscriber().getName());

				// Accoumilate totals
				operatorSubscription.addPrice(entry.getValue().getPrice());
				operatorSubscription.addAdscom(entry.getValue().getAdscom());
				operatorSubscription.addOpcom(entry.getValue().getOpcom());
				operatorSubscription.addSpcom(entry.getValue().getSpcom());
				operatorSubscription.addTaxValue(entry.getValue().getTax());
				operatorSubscription.addCount(entry.getValue().getCount());
			}
		}

	}

	/**
	 * Apply charges for payment api.
	 *
	 * @param opSubscription the op subscription
	 * @param paymentRequestSet the payment request set
	 * @param categoryEntry the category entry
	 * @param appId the app id
	 * @param apiId the api id
	 * @param subId the sub id
	 * @throws Exception 
	 */
	private static void applyChargesForPaymentApi(
			BillingSubscription.OperatorSubscription opSubscription,
			Set<PaymentRequestDTO> paymentRequestSet,
			Map.Entry<CategoryCharge, BilledCharge> categoryEntry, int appId,
			int apiId, String subId) throws Exception {

		ChargeRate rate = opSubscription.getRate();
		String billCategory = categoryEntry.getKey().getCategory();
		String billSubCategory = categoryEntry.getKey().getSubcategory();
		BigDecimal billRate = rate.getValue();

		boolean CategoryBased = rate.getCategoryBasedVal();

		List<Tax> taxList = TaxDAO.getTaxesForTaxList(rate
				.getTaxList());
		Map<String, CommissionPercentagesDTO> commisionMap = null;

		if (!CategoryBased) {
			commisionMap = BillingDAO.getCommissionPercentages(
					subId, appId);
		}

		BigDecimal totalspcom = BigDecimal.ZERO;
		BigDecimal totalCharge = BigDecimal.ZERO;
		BigDecimal totaladscom = BigDecimal.ZERO;
		BigDecimal totalopcom = BigDecimal.ZERO;
		BigDecimal totalTax = BigDecimal.ZERO;

		// get this flag from rate card

		for (PaymentRequestDTO paymentRequest : paymentRequestSet) {
			totalCharge = totalCharge.add(paymentRequest.getAmount());
			BigDecimal spcom = BigDecimal.ZERO;
			BigDecimal adscom = BigDecimal.ZERO;
			BigDecimal opcom = BigDecimal.ZERO;

			String category = paymentRequest.getCategory();
			String subcategory = paymentRequest.getSubcategory();
			String merchant = paymentRequest.getMerchant();

			BigDecimal adscomPercnt = rate.getCommission().getAdsCommission()
					.divide(new BigDecimal(100));
			BigDecimal spcomPercnt = rate.getCommission().getSpCommission()
					.divide(new BigDecimal(100));
			BigDecimal opcomPercnt = rate.getCommission().getOpcoCommission()
					.divide(new BigDecimal(100));

			if (CategoryBased) {
				Object SubsRate = getRateSubcategory(rate, billCategory,
						billSubCategory);
				if (SubsRate != null) {
					RateCommission commisionRates = (RateCommission) SubsRate;
					adscomPercnt = commisionRates.getAdsCommission().divide(
							new BigDecimal(100));
					spcomPercnt = commisionRates.getSpCommission().divide(
							new BigDecimal(100));
					opcomPercnt = commisionRates.getOpcoCommission().divide(
							new BigDecimal(100));
				}

			} else {
				if (commisionMap.containsKey(merchant)) {
					adscomPercnt = commisionMap.get(merchant)
							.getAdsCommission().divide(new BigDecimal(100));
					spcomPercnt = commisionMap.get(merchant).getSpCommission()
							.divide(new BigDecimal(100));
					opcomPercnt = commisionMap.get(merchant)
							.getOpcoCommission().divide(new BigDecimal(100));
				} else {
					throw new APIManagementException(
							"Payment Categoreis required for MERCHANT based charging are not specified in rate-card.xml");
				}
			}

			spcom = paymentRequest.getAmount().multiply(spcomPercnt);
			totalspcom = totalspcom.add(spcom);

			opcom = paymentRequest.getAmount().multiply(opcomPercnt);
			totalopcom = totalopcom.add(opcom);

			adscom = paymentRequest.getAmount().multiply(adscomPercnt);
			totaladscom = totaladscom.add(adscom);

			Date date = new Date(paymentRequest.getDate().getTime());
			BigDecimal totalReqTax = BigDecimal.ZERO;
			for (Tax tax : taxList) {
				// check if the date of payment request falls between this tax
				// validity period
				if (!date.before(tax.getEffective_from())
						&& !date.after(tax.getEffective_to())) {
					totalReqTax = totalReqTax.add(tax.getValue().multiply(
							paymentRequest.getAmount()));
				}
			}
			totalTax = totalTax.add(totalReqTax);

			if (!CategoryBased) {
				if (opSubscription.getMerchantCharges().containsKey(merchant)) {
					opSubscription.getMerchantCharges().get(merchant)
							.addAdscom(adscom);
					opSubscription.getMerchantCharges().get(merchant)
							.addOpcom(opcom);
					opSubscription.getMerchantCharges().get(merchant)
							.addSpcom(spcom);
					opSubscription.getMerchantCharges().get(merchant)
							.addPrice(spcom);
					opSubscription.getMerchantCharges().get(merchant)
							.addTax(totalReqTax);
					opSubscription.getMerchantCharges().get(merchant)
							.addCount(1);
				} else {
					BilledCharge billedCharge = new BilledCharge(0);
					billedCharge.addAdscom(adscom);
					billedCharge.addOpcom(opcom);
					billedCharge.addSpcom(spcom);
					billedCharge.addPrice(spcom);
					billedCharge.addTax(totalReqTax);
					billedCharge.addCount(1);
					opSubscription.getMerchantCharges().put(merchant,
							billedCharge);
				}
			}
		}

		// Get the percentage from the rate value

		// apply category wise charge percentage
		
		categoryEntry.getValue().addAdscom(totaladscom);
		categoryEntry.getValue().addOpcom(totalopcom);
		categoryEntry.getValue().addSpcom(totalspcom);
		categoryEntry.getValue().addPrice(totalspcom);
		categoryEntry.getValue().setTax(totalTax);
	}

	/**
	 * Apply payment charges by category.
	 *
	 * @param opSubscription the op subscription
	 * @param categoryCharge the category charge
	 * @param paymentRequestSet the payment request set
	 * @throws Exception 
	 */
	private static void applyPaymentChargesByCategory(
			BillingSubscription.OperatorSubscription opSubscription,
			CategoryCharge categoryCharge,
			Set<PaymentRequestDTO> paymentRequestSet)
			throws Exception {

		ChargeRate rate = opSubscription.getRate();
		List<Tax> taxList = TaxDAO.getTaxesForTaxList(rate
				.getTaxList());
		BigDecimal totalCharge = BigDecimal.ZERO;
		BigDecimal totalPrice = BigDecimal.ZERO;
		BigDecimal totalTax = BigDecimal.ZERO;

		for (PaymentRequestDTO paymentRequest : paymentRequestSet) {
			totalCharge = totalCharge.add(paymentRequest.getAmount());
			BigDecimal price = BigDecimal.ZERO;

			CategoryEntity rateCategories = new CategoryEntity();

			if (rateCategories == null) {
				throw new APIManagementException(
						"Payment Categoreis required for QUOTA charging are not specified in rate-card.xml");
			}
			BigDecimal catpercent = rate.getValue().divide(new BigDecimal(100));

			Date date = new Date(paymentRequest.getDate().getTime());
			for (Tax tax : taxList) {
				// check if the date of payment request falls between this tax
				// validity period
				if (!date.before(tax.getEffective_from())
						&& !date.after(tax.getEffective_to())) {
					// totalTax += taxFraction x paymentAmount
					totalTax = totalTax.add(tax.getValue().multiply(price));
				}
			}
		}

		// Get the percentage from the rate value

		// apply category wise charge percentage
	}

	/**
	 * Apply charges with tax.
	 *
	 * @param apiYear the api year
	 * @param apiMonth the api month
	 * @param application the application
	 * @param apiName the api name
	 * @param apiVersion the api version
	 * @param operatorSub the operator sub
	 * @param CatEntry the cat entry
	 * @param rate the rate
	 * @throws Exception 
	 */
	private static void applyChargesWithTax(String apiYear, String apiMonth,
			Application application, String apiName, String apiVersion,
			BillingSubscription.OperatorSubscription operatorSub,
			Map.Entry<CategoryCharge, BilledCharge> CatEntry, ChargeRate rate)
			throws Exception {
		String month = apiMonth;
		String year = apiYear;
		boolean isSurcharge = false;

		if (application == null) {
			throw new APIManagementException("no key generated for this api");
		}
		APIKey prodKey = getAppKey(application,
				APIConstants.API_KEY_TYPE_PRODUCTION);

		Set<APIRequestDTO> requestTimes = new HashSet<APIRequestDTO>();
		if (prodKey != null) {
			String api_version = apiName + ":v" + apiVersion;

			requestTimes = TaxDAO
					.getAPIRequestTimesForSubscription(Short.parseShort(year),
							Short.parseShort(month), apiName, api_version,
							prodKey.getConsumerKey(),
							operatorSub.getOperator(), operatorSub
									.getOperationId(), CatEntry.getKey()
									.getCategory(), CatEntry.getKey()
									.getSubcategory());

		}

		String billCategory = CatEntry.getKey().getCategory();
		String billSubCategory = CatEntry.getKey().getSubcategory();
		BigDecimal billRate = rate.getValue();
		BigDecimal OpscomPercnt = null;

		Object SubsRate = getRateSubcategory(rate, billCategory,
				billSubCategory);
		if (SubsRate != null) {
			billRate = new BigDecimal((String) SubsRate);
		}

		// Surcharge value
		if (rate.getSurchargeEntity() != null) {
			billRate = new BigDecimal(rate.getSurchargeEntity()
					.getSurchargeElementValue());
			OpscomPercnt = new BigDecimal(rate.getSurchargeEntity()
					.getSurchargeElementOpco()).divide(new BigDecimal(100));
			isSurcharge = true;
		}

		List<Tax> taxList = TaxDAO.getTaxesForTaxList(rate
				.getTaxList());
		BigDecimal totalCharge = BigDecimal.ZERO;
		BigDecimal totalTax = BigDecimal.ZERO;
		BigDecimal totalOpcom = BigDecimal.ZERO;
		BigDecimal totalAdscom = BigDecimal.ZERO;

		int reqCount = 0;
		for (APIRequestDTO req : requestTimes) {

			if (reqCount >= CatEntry.getValue().getCount()) {
				break;
			}

			BigDecimal charge = billRate.multiply(new BigDecimal(req
					.getRequestCount()));
			if (isSurcharge) {
				BigDecimal opcoCommision = billRate.multiply(OpscomPercnt);
				totalOpcom = totalOpcom.add(opcoCommision);
				totalAdscom = totalAdscom.add(charge.subtract(opcoCommision));
			} else {
				totalCharge = totalCharge.add(charge);
			}

			Date date = req.getDate();
			for (Tax tax : taxList) {

				// check if the date of payment request falls between this tax
				// validity period
				if (!date.before(tax.getEffective_from())
						&& !date.after(tax.getEffective_to())) {
					totalTax = totalTax.add(tax.getValue().multiply(charge));
				}
			}
			reqCount++;
		}

		CatEntry.getValue().addPrice(totalCharge);
		CatEntry.getValue().addTax(totalTax);
		CatEntry.getValue().addOpcom(totalOpcom);
		CatEntry.getValue().addAdscom(totalAdscom);

	}

	/**
	 * Apply tax for block charging.
	 *
	 * @param CatEntry the cat entry
	 * @param rate the rate
	 * @param year the year
	 * @param month the month
	 * @throws Exception 
	 */
	private static void applyTaxForBlockCharging(
			Map.Entry<CategoryCharge, BilledCharge> CatEntry, ChargeRate rate,
			String year, String month) throws Exception {

		List<Tax> taxList = TaxDAO.getTaxesForTaxList(rate
				.getTaxList());
		CategoryCharge categorycharge = CatEntry.getKey();
		BilledCharge billed = CatEntry.getValue();

		BigDecimal totalTax = BigDecimal.ZERO;
		Date billingDate = Date.valueOf(year + "-" + month + "-01"); // start of
																		// the
																		// month

		for (Tax tax : taxList) {
			// select the taxes applicable at the billing date
			if (!billingDate.before(tax.getEffective_from())
					&& !billingDate.after(tax.getEffective_to())) {
				totalTax = totalTax.add(tax.getValue().multiply(
						billed.getPrice()));
			}
		}

		CatEntry.getValue().setTax(totalTax);
	}

	/**
	 * Charge subscriber for month.
	 *
	 * @param subscriber the subscriber
	 * @param year the year
	 * @param month the month
	 * @param rateCard the rate card
	 * @return the map
	 * @throws Exception 
	 */
	public static Map<Application, Set<BillingSubscription>> chargeSubscriberForMonth(
			String subscriber, String year, String month,
			Map<RateKey, ChargeRate> rateCard) throws Exception {
		Map<Application, Set<BillingSubscription>> billingSubs = getBillingSubscriptionsForUser(
				subscriber, year, month);
		// inside charge plan application.
		applyChargingPlan(billingSubs, rateCard, year, month);

		return billingSubs;
	}

	/**
	 * Populate operator details of subscription.
	 *
	 * @param subscription the subscription
	 * @param rateCard the rate card
	 * @throws APIManagementException the API management exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 */
	private static void populateOperatorDetailsOfSubscription(
			BillingSubscription subscription, Map<RateKey, ChargeRate> rateCard)
			throws APIManagementException,
			APIMgtUsageQueryServiceClientException {
		int appId = subscription.getApplication().getId();
		int apiId = subscription.getApiIdInt();
		List<BillingSubscription.OperatorSubscription> opSubscriptionList = new ArrayList<BillingSubscription.OperatorSubscription>();

		List<OperatorDetailsEntity> operatorMap = BillingDAO
				.getOperatorDetailsOfSubscription(appId, apiId);
		for (OperatorDetailsEntity operatorDetail : operatorMap) {

			String operator = operatorDetail.getOperatorName();
			String rateName = operatorDetail.getRateName();
			int operationId = operatorDetail.getOperationId();
			log.debug("appId:" + appId + ",apiId:" + apiId + ",operator:"
					+ operator + ",rateName:" + rateName + ",operation:"
					+ operationId);

			BillingSubscription.OperatorSubscription opSub = new BillingSubscription.OperatorSubscription(
					operator);
			ChargeRate rate = rateCard.get(new RateKey(operator, subscription
					.getApiId().getApiName(), rateName, String
					.valueOf(operationId)));
			if (rate == null) {
				throw new APIManagementException(
						"Couldn't generate report. Rate assignment is faulty.");
			}
			opSub.setRate(rate);
			opSub.setOperationId(operationId);
			opSubscriptionList.add(opSub);
		}
		subscription.setOperatorSubscriptionList(opSubscriptionList);
	}

	/**
	 * Populate category vise api counts.
	 *
	 * @param subscription the subscription
	 * @throws APIManagementException the API management exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 */
	private static void populateCategoryViseAPICounts(
			BillingSubscription subscription) throws APIManagementException,
			APIMgtUsageQueryServiceClientException {

		Application application = subscription.getApplication();
		if (application == null) {
			throw new APIManagementException("No key generated for this api");
		}
		APIKey prodKey = getAppKey(application,
				APIConstants.API_KEY_TYPE_PRODUCTION);

		Map<CategoryCharge, BilledCharge> categoryCharges = null;
		for (BillingSubscription.OperatorSubscription operatorSubscription : subscription
				.getOperatorSubscriptionList()) {

			if (prodKey != null) {
				categoryCharges = BillingDAO
						.getAPICountsForSubscription(prodKey.getConsumerKey(),
								Short.parseShort(subscription.getYear()),
								Short.parseShort(subscription.getMonth()),
								subscription.getApiId().getApiName(),
								subscription.getApiId().getVersion(),
								operatorSubscription.getOperator(),
								operatorSubscription.getOperationId());
			}

			if (categoryCharges != null) {
				operatorSubscription.setCategoryCharges(categoryCharges);
			}
		}
	}

	/**
	 * Gets the response times for subscriber.
	 *
	 * @param username the username
	 * @return the response times for subscriber
	 * @throws APIManagementException the API management exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 */
	public static Map<String, String> getResponseTimesForSubscriber(
			String username) throws APIManagementException,
			APIMgtUsageQueryServiceClientException {
		log.debug("Starting getResponseTimesForSubscriber funtion with name username "
				+ username);
		Subscriber subscriber = new Subscriber(username);
		Map<String, String> responseTimes = new HashMap<String, String>();
		ApiMgtDAO apiMgtDAO = new ApiMgtDAO();
		// Adding null for param
		Set<SubscribedAPI> apis = apiMgtDAO.getSubscribedAPIs(subscriber, null);
		log.debug("apis count, " + apis.size());
		String api_version = null;
		for (SubscribedAPI api : apis) {
			log.debug("api_name " + api.getApiId().getApiName());
			log.debug("api_version " + api.getApiId().getVersion());
			api_version = api.getApiId().getApiName() + ":v"
					+ api.getApiId().getVersion();
			log.debug("api_version " + api_version);
			String ResponseTimeForAPI = BillingDAO
					.getResponseTimeForAPI(api_version);
			log.debug("ResponseTimeForAPI: " + ResponseTimeForAPI);
			if (ResponseTimeForAPI != null) {
				log.debug("ResponseTimeForAPI was updated ");
				responseTimes.put(api_version, BillingDAO
						.getResponseTimeForAPI(api_version));
			}
		}

		return responseTimes;
	}

	/**
	 * Gets the all response times.
	 *
	 * @param opName the op name
	 * @param username the username
	 * @param application the application
	 * @param appId the app id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the all response times
	 * @throws APIManagementException the API management exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 */
	public static Map<String, List<APIResponseDTO>> getAllResponseTimes(
			String opName, String username, String application, String appId,
			String fromDate, String toDate) throws APIManagementException,
			APIMgtUsageQueryServiceClientException {
		log.debug("Starting getAllResponseTimes function with username "
				+ username + " for app " + application + " from " + fromDate
				+ " to " + toDate);
		Map<String, List<APIResponseDTO>> responseTimes = new HashMap<String, List<APIResponseDTO>>();
		ApiMgtDAO apiMgtDAO = new ApiMgtDAO();

		if (username.equals(ALL_SUBSCRIBERS_KEYWORD)) {
			List<API> allAPIs = APIManagerFactory.getInstance()
					.getAPIConsumer().getAllAPIs();
			for (API api : allAPIs) {
				String nameVersion = api.getId().getApiName() + ":v"
						+ api.getId().getVersion();
				List<APIResponseDTO> responseDTOs = BillingDAO
						.getAllResponseTimesForAPI(opName, appId, nameVersion,
								fromDate, toDate);
				responseTimes.put(nameVersion, responseDTOs);
			}
		} else if (application.equals(ALL_APPLICATIONS_KEYWORD)) {
			Subscriber subscriber = new Subscriber(username);
			// Adding null for param
			Set<SubscribedAPI> subscribedAPIs = apiMgtDAO.getSubscribedAPIs(
					subscriber, null);
			for (SubscribedAPI api : subscribedAPIs) {
				String nameVersion = api.getApiId().getApiName() + ":v"
						+ api.getApiId().getVersion();
				List<APIResponseDTO> responseDTOs = BillingDAO
						.getAllResponseTimesForAPI(opName, appId, nameVersion,
								fromDate, toDate);
				responseTimes.put(nameVersion, responseDTOs);
			}
		} else {
			Subscriber subscriber = new Subscriber(username);
			Set<SubscribedAPI> subscribedAPIs = apiMgtDAO.getSubscribedAPIs(
					subscriber, application);
			for (SubscribedAPI api : subscribedAPIs) {
				String nameVersion = api.getApiId().getApiName() + ":v"
						+ api.getApiId().getVersion();
				List<APIResponseDTO> responseDTOs = BillingDAO
						.getAllResponseTimesForAPI(opName, appId, nameVersion,
								fromDate, toDate);
				responseTimes.put(nameVersion, responseDTOs);
			}
		}

		return responseTimes;
	}

	/**
	 * Gets the report location.
	 *
	 * @param subscriber the subscriber
	 * @param period the period
	 * @return the report location
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static FileWriter getReportLocation(String subscriber, String period)
			throws IOException {
		String year = null;
		String month = null;
		if (period != null) {
			String[] periodArray = period.split("-");
			year = periodArray[0];
			month = periodArray[1];
		}
		String carbonHome = System.getProperty("carbon.home");
		String fileName = subscriber + "-" + period + ".csv";
		File reports = new File(carbonHome + "/reports/");

		if (!reports.exists()) {
			reports.mkdirs();
			reports = new File(reports.getAbsoluteFile() + "/" + fileName);
			reports.createNewFile();
		} else {
			reports = new File(reports.getAbsoluteFile() + "/" + fileName);
			if (!reports.exists()) {
				reports.createNewFile();
			}
		}
		return new FileWriter(reports.getAbsolutePath());
	}

	/**
	 * Gets the custom api traffic report location.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param subscriberName the subscriber name
	 * @param operator the operator
	 * @param api the api
	 * @return the custom api traffic report location
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static FileWriter getCustomApiTrafficReportLocation(String fromDate,
			String toDate, String subscriberName, String operator, String api)
			throws IOException {

		String carbonHome = System.getProperty("carbon.home");
		String fileName = fromDate + "-" + toDate + "-" + subscriberName + "-"
				+ operator + "-" + api + "-" + ".csv";
		File reports = new File(carbonHome + "/reports/");

		if (!reports.exists()) {
			reports.mkdirs();
			reports = new File(reports.getAbsoluteFile() + "/" + fileName);
			reports.createNewFile();
		} else {
			reports = new File(reports.getAbsoluteFile() + "/" + fileName);
			if (!reports.exists()) {
				reports.createNewFile();
			}
		}
		return new FileWriter(reports.getAbsolutePath());
	}

	/**
	 * Gets the CSV file.
	 *
	 * @param subscriber the subscriber
	 * @param period the period
	 * @return the CSV file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static File getCSVFile(String subscriber, String period)
			throws IOException {

		String carbonHome = System.getProperty("carbon.home");
		String fileName = subscriber + "-" + period + ".csv";
		File reports = new File(carbonHome + "/reports/");

		if (!reports.exists()) {
			reports.mkdirs();
			reports = new File(reports.getAbsoluteFile() + "/" + fileName);
			reports.createNewFile();
		} else {
			reports = new File(reports.getAbsoluteFile() + "/" + fileName);
			if (!reports.exists()) {
				reports.createNewFile();
			}
		}
		return reports;
	}

	/**
	 * Gets the custom csv file.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param subscriberName the subscriber name
	 * @param operator the operator
	 * @param api the api
	 * @return the custom csv file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static File getCustomCSVFile(String fromDate, String toDate,
			String subscriberName, String operator, String api)
			throws IOException {

		String carbonHome = System.getProperty("carbon.home");
		String fileName = fromDate + "-" + toDate + "-" + subscriberName + "-"
				+ operator + "-" + api + "-" + ".csv";
		File reports = new File(carbonHome + "/reports/");

		if (!reports.exists()) {
			reports.mkdirs();
			reports = new File(reports.getAbsoluteFile() + "/" + fileName);
			reports.createNewFile();
		} else {
			reports = new File(reports.getAbsoluteFile() + "/" + fileName);
			if (!reports.exists()) {
				reports.createNewFile();
			}
		}
		return reports;
	}

	/**
	 * Generate costper apisummary.
	 *
	 * @param isPersistReport the is persist report
	 * @param subscriberName the subscriber name
	 * @param period the period
	 * @param opcode the opcode
	 * @param application the application
	 * @return the native array
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static NativeArray generateCostperApisummary(
			boolean isPersistReport, String subscriberName, String period,
			String opcode, String application) throws IOException,
			SQLException, APIMgtUsageQueryServiceClientException,
			APIManagementException {
		String year = null;
		String month = null;
		if (period != null) {
			String[] periodArray = period.split("-");
			year = periodArray[0];
			month = periodArray[1];
		}

		return null;
	}

	/**
	 * Generate customr care data report.
	 *
	 * @param isPersistReport the is persist report
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param msisdn the msisdn
	 * @param subscriberName the subscriber name
	 * @param operator the operator
	 * @param app the app
	 * @param api the api
	 * @param stLimit the st limit
	 * @param endLimit the end limit
	 * @param timeOffset the time offset
	 * @return the native array
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static NativeArray generateCustomrCareDataReport(
			boolean isPersistReport, String fromDate, String toDate,
			String msisdn, String subscriberName, String operator, String app,
			String api, String stLimit, String endLimit, String timeOffset)
			throws SQLException, APIMgtUsageQueryServiceClientException,
			APIManagementException {

		NativeArray nativeArray = new NativeArray(0);
		List<String[]> user_data_all = SbHostObjectUtils
				.getFilteredCustomerCareReport(fromDate, toDate, msisdn,
						subscriberName, operator, app, api, stLimit, endLimit,
						timeOffset);

		int i = 0;

		for (String[] user_data : user_data_all) {
			NativeObject userDataObj = new NativeObject();
			userDataObj.put("date", userDataObj, user_data[0]);
			userDataObj.put("jsonBody", userDataObj, user_data[1]);
			userDataObj.put("api", userDataObj, user_data[2]);
			nativeArray.put(i, nativeArray, userDataObj);
			i++;
		}
		return nativeArray;
	}

	/**
	 * Generate customr care data record count.
	 *
	 * @param isPersistReport the is persist report
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param msisdn the msisdn
	 * @param subscriberName the subscriber name
	 * @param operator the operator
	 * @param app the app
	 * @param api the api
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static String generateCustomrCareDataRecordCount(
			boolean isPersistReport, String fromDate, String toDate,
			String msisdn, String subscriberName, String operator, String app,
			String api) throws IOException, SQLException,
			APIMgtUsageQueryServiceClientException, APIManagementException {

		String user_data_count = BillingDAO
				.getCustomerCareReportDataCount(fromDate, toDate, msisdn,
						subscriberName, operator, app, api);

		return user_data_count;
	}

	/**
	 * Generate custom traffic report.
	 *
	 * @param isPersistReport the is persist report
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param subscriberName the subscriber name
	 * @param operator the operator
	 * @param api the api
	 * @param timeOffset the time offset
	 * @param resType the res type
	 * @return the native array
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static NativeArray generateCustomTrafficReport(
			boolean isPersistReport, String fromDate, String toDate,
			String subscriberName, String operator, String api,
			String timeOffset, String resType) throws IOException,
			SQLException, APIMgtUsageQueryServiceClientException,
			APIManagementException {

		// This is to test
		int operationType = Integer.valueOf(api);

		FileWriter fileWriter = getCustomApiTrafficReportLocation(fromDate,
				toDate, subscriberName, operator, String.valueOf(operationType));
		NativeArray nativeArray = new NativeArray(0);
		TxCardDAO currentDao = null;

		try {
			ApiTxCard card = new ApiTxCard();
			HashMap<Integer, Object> txCardTemp = card.getTxCardTemp();
			currentDao = (TxCardDAO) txCardTemp.get(new Integer(operationType));

		} catch (Exception e) {
			System.out.println("Reading TxCard :: " + e);
			System.out
					.println("::: Default parameters assigned to overcome the error :::");
		}

		if (currentDao != null && isPersistReport) {

			// Loop for Header parameters
			List<String> headerList = currentDao.getHeaderList();
			String titleStr = "";
			String[] headerArray = new String[headerList.size()];
			for (int i = 0; i < headerList.size(); i++) {
				titleStr = titleStr + "%s,";
				headerArray[i] = headerList.get(i);
			}
			titleStr = titleStr + "%n";
			fileWriter.write(String.format(titleStr, headerArray));

			// Loop for Data parameters
			ResultSet rs = BillingDAO.getTxLogData(fromDate,
					toDate, subscriberName, operator, operationType, resType);

			List<TxCardDAO.DataList> dataList = currentDao.getDataList();
			String[] dataArray = new String[dataList.size()];
			while (rs.next()) {
				for (int d = 0; d < dataList.size(); d++) {
					TxCardDAO.DataList tempDL = dataList.get(d);
					if (tempDL.getManipulationType().equals("FIELD")) {
						// DB field manipulation
						dataArray[d] = rs.getString(tempDL
								.getManipulationField());
						nativeArray.put(d, nativeArray,
								rs.getString(tempDL.getManipulationField()));
					} else if (tempDL.getManipulationType().equals("TIME")) {
						// DB field manipulation
						log.debug("Time offset - " + timeOffset);
						String timeInDb = rs.getString(tempDL
								.getManipulationField());
						log.debug("Time in Db - " + timeInDb);
						String convertedTime = BillingDAO
								.convertToLocalTime(timeOffset, timeInDb);
						log.debug("convertedTime - " + convertedTime);
						dataArray[d] = convertedTime;
						nativeArray.put(d, nativeArray, convertedTime);
					} else if (tempDL.getManipulationType().equals("CONST")) {
						// Append constant value
						dataArray[d] = tempDL.getManipulationRegEx();
						nativeArray.put(d, nativeArray,
								tempDL.getManipulationRegEx());
					} else if (tempDL.getManipulationType().equals("JSON")) {
						// JSON manipulation
						String regEx = tempDL.getManipulationRegEx();
						String dataValue = "";
						if (regEx != null) {
							String[] jsonPath = regEx.split("/");
							try {
								JSONObject homeJson = new JSONObject(
										rs.getString(tempDL
												.getManipulationField()));
								JSONObject swapJson = null;
								for (int elemId = 0; elemId < jsonPath.length; elemId++) {
									if (elemId == 0) {
										swapJson = (JSONObject) homeJson
												.get(jsonPath[elemId]);
									} else if (elemId == jsonPath.length - 1) {
										dataValue = swapJson
												.getString(jsonPath[elemId]);
									} else {
										swapJson = (JSONObject) swapJson
												.get(jsonPath[elemId]);
									}
								}
							} catch (Exception e) {
								System.out.println("Reading JSON for TxLog :: "
										+ e);
							}
						}
						dataArray[d] = dataValue;
						nativeArray.put(d, nativeArray, dataValue);
					} else {
						// If non of the above
						dataArray[d] = "";
						nativeArray.put(d, nativeArray, "");
					}

				}
				fileWriter.write(String.format(titleStr, dataArray));
			}
			rs.close();
		}

		fileWriter.flush();
		fileWriter.close();

		return nativeArray;

	}

	/**
	 * Generate reportof subscriber.
	 *
	 * @param isPersistReport the is persist report
	 * @param subscriberName the subscriber name
	 * @param period the period
	 * @param rateCard the rate card
	 * @param operatorName the operator name
	 * @return the native array
	 * @throws Exception 
	 */
	public static NativeArray generateReportofSubscriber(
			boolean isPersistReport, String subscriberName, String period,
			Map<RateKey, ChargeRate> rateCard, String operatorName)
			throws Exception {

		String year = null;
		String month = null;
		if (period != null) {
			String[] periodArray = period.split("-");
			year = periodArray[0];
			month = periodArray[1];
		}
		FileWriter fileWriter = null;

		if (isPersistReport) {
			fileWriter = getReportLocation(subscriberName, period);
			
			fileWriter
					.write(String
							.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s,%s,%s,%s, %s, %s, %s, %s, %s %n",
									"Subscriber", "Application", "API",
									"Version", "Operator", "Operation", "Plan",
									"Currency", "Merchant", "Category",
									"SubCategory", "Tier", "Count",
									"Operator Charges", "Usage Charge", "Tax",
									"Credit", "Grand Total"));
		}

		NativeArray subscribers = new NativeArray(0);

		if (subscriberName.equals(ALL_SUBSCRIBERS_KEYWORD)) {
			List<String> subscriberNames = SbHostObjectUtils
					.getAllSubscribers();

			for (String sub : subscriberNames) {
				Map<Application, Set<BillingSubscription>> billingSubs = SbHostObjectUtils
						.chargeSubscriberForMonth(sub, year, month, rateCard);

				NativeObject subscriber = new NativeObject();
				subscriber.put("subscriber", subscriber, sub);
				NativeArray applications = new NativeArray(0);

				if (isPersistReport && fileWriter != null) {
					fileWriter
							.write(String
									.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s,%s,%s, %s, %s, %s, %s, %s %n",
											sub, "", "", "", "", "", "", "",
											"", "", "", "", "", "", "", "", ""));
				}

				// declare variable for the report
				double totalAmount = 0.0;
				double ataxAmout = 0.0;
				double acreditAmount = 0.0;
				double acOpAmount = 0.0;
				double grandTotal = 0.0;

				for (Map.Entry<Application, Set<BillingSubscription>> billingEntries : billingSubs
						.entrySet()) {

					NativeObject application = new NativeObject();
					String applicationName = "";

					NativeArray subscriptionAPIs = new NativeArray(0);

					int a = 0;
					for (BillingSubscription billingSubscription : billingEntries
							.getValue()) {

						NativeObject subscriptionAPI = new NativeObject();
						subscriptionAPI.put("subscriptionapi", subscriptionAPI,
								billingSubscription.getApiId().getApiName());
						subscriptionAPI.put("subscriptionapiversion",
								subscriptionAPI, billingSubscription.getApiId()
										.getVersion());

						applicationName = billingEntries.getKey().getName();

						if (a == 0 && isPersistReport && fileWriter != null) {
							fileWriter
									.write(String
											.format("%s, %s, %s,%s, %s, %s, %s, %s, %s, %s, %s,%s, %s, %s, %s, %s, %s %n",
													"", applicationName, "",
													"", "", "", "", "", "", "",
													"", "", "", "", "", "", ""));
						}

						if (isPersistReport && fileWriter != null) {
							fileWriter
									.write(String
											.format("%s, %s, %s, %s, %s,%s, %s, %s, %s, %s, %s,%s, %s, %s, %s, %s, %s %n",
													"", "", billingSubscription
															.getApiId()
															.getApiName(),
													billingSubscription
															.getApiId()
															.getVersion(), "",
													"", "", "", "", "", "", "",
													"", "", "", "", ""));
						}

						NativeArray subscriptionOperators = new NativeArray(0);
						for (BillingSubscription.OperatorSubscription operatorSub : billingSubscription
								.getOperatorSubscriptionList()) {
							if ((subscriberName.equalsIgnoreCase("__All__"))
									|| (operatorSub.getOperator()
											.equalsIgnoreCase(subscriberName))) {

								NativeObject subscriptionOperator = new NativeObject();
								subscriptionOperator.put("operator",
										subscriptionOperator,
										operatorSub.getOperator());
								subscriptionOperator.put("ratename",
										subscriptionOperator, operatorSub
												.getRate().getName());
								subscriptionOperator.put("count",
										subscriptionOperator,
										operatorSub.getCount());
								subscriptionOperator.put("price",
										subscriptionOperator,
										operatorSub.getPrice());
								subscriptionOperator.put("tax",
										subscriptionOperator,
										operatorSub.getTaxValue());
								subscriptionOperator.put("credit",
										subscriptionOperator,
										operatorSub.getAdscom());
								subscriptionOperator
										.put("operationid",
												subscriptionOperator,
												BillingDAO
														.getOperationNameById(operatorSub
																.getOperationId()));
								subscriptionOperator.put("currency",
										subscriptionOperator, operatorSub
												.getRate().getCurrency());
								subscriptionOperator.put("opcom",
										subscriptionOperator,
										operatorSub.getOpcom());

								subscriptionOperators.put(
										subscriptionOperators.size(),
										subscriptionOperators,
										subscriptionOperator);

								if (isPersistReport && fileWriter != null) {
									String currencyStr = operatorSub.getRate()
											.getCurrency() + " ";
									fileWriter
											.write(String
													.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s,%s,%s, %s, %s, %s, %s, %s %n",
															"",
															"",
															"",
															"",
															operatorSub
																	.getOperator(),
															BillingDAO
																	.getOperationNameById(operatorSub
																			.getOperationId()),
															operatorSub
																	.getRate()
																	.getName(),
															currencyStr,
															"",
															"",
															"",
															"",
															operatorSub
																	.getCount(),
															operatorSub
																	.getOpcom(),
															operatorSub
																	.getPrice(),
															operatorSub
																	.getTaxValue(),
															operatorSub
																	.getAdscom()));

									// print merchant wise breakdown
									Map<String, BilledCharge> mtmap = operatorSub
											.getMerchantCharges();
									if (mtmap.size() > 0) {

										for (Map.Entry<String, BilledCharge> entry : mtmap
												.entrySet()) {
											fileWriter
													.write(String
															.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s,%s,%s, %s, %s, %s, %s, %s %n",
																	"",
																	"",
																	"",
																	"",
																	"",
																	"",
																	"",
																	currencyStr,
																	nvlDefault(entry
																			.getKey()),
																	"",
																	"",
																	"",
																	entry.getValue()
																			.getCount(),
																	entry.getValue()
																			.getOpcom(),
																	entry.getValue()
																			.getPrice(),
																	entry.getValue()
																			.getTax(),
																	entry.getValue()
																			.getAdscom()));
										}
									} else {
										// print category, subcategory breakdown
										Map<CategoryCharge, BilledCharge> ctmap = operatorSub
												.getCategoryCharges();
										for (Map.Entry<CategoryCharge, BilledCharge> entry : ctmap
												.entrySet()) {
											fileWriter
													.write(String
															.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s,%s, %s, %s, %s, %s, %s, %s %n",
																	"",
																	"",
																	"",
																	"",
																	"",
																	"",
																	"",
																	"",
																	"",
																	nvlDefault(entry
																			.getKey()
																			.getCategory()),
																	nvlDefault(entry
																			.getKey()
																			.getSubcategory()),
																	"",
																	entry.getValue()
																			.getCount(),
																	entry.getValue()
																			.getOpcom(),
																	entry.getValue()
																			.getPrice(),
																	entry.getValue()
																			.getTax(),
																	entry.getValue()
																			.getAdscom()));

											// tier wise breakdown
											Map<String, BilledCharge> timap = entry
													.getValue()
													.getTierCharges();
											for (Map.Entry<String, BilledCharge> tientry : timap
													.entrySet()) {
												fileWriter
														.write(String
																.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s,%s, %s, %s, %s, %s, %s, %s %n",
																		"",
																		"",
																		"",
																		"",
																		"",
																		"",
																		"",
																		"",
																		"",
																		"",
																		"",
																		tientry.getKey(),
																		tientry.getValue()
																				.getCount(),
																		tientry.getValue()
																				.getOpcom(),
																		tientry.getValue()
																				.getPrice(),
																		tientry.getValue()
																				.getTax(),
																		tientry.getValue()
																				.getAdscom()));
											}

										}
									}
								}

								totalAmount = totalAmount
										+ operatorSub.getPrice().doubleValue();
								ataxAmout = ataxAmout
										+ operatorSub.getTaxValue()
												.doubleValue();
								acreditAmount = acreditAmount
										+ operatorSub.getAdscom().doubleValue();
								acOpAmount = acOpAmount
										+ operatorSub.getOpcom().doubleValue();
								grandTotal = totalAmount + ataxAmout;
							} else {
								continue;
							}
						}
						subscriptionAPI.put("operators", subscriptionAPI,
								subscriptionOperators);

						subscriptionAPIs.put(subscriptionAPIs.size(),
								subscriptionAPIs, subscriptionAPI);

						a++;

					}

					application.put("applicationname", application,
							applicationName);
					application.put("subscriptions", application,
							subscriptionAPIs);
					applications.put(applications.size(), applications,
							application);

				}

				if (isPersistReport && fileWriter != null) {
					fileWriter
							.write(String
									.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s,%s, %s,%s, %s, %s, %s, %s, %s %n",
											"", "", "", "", "", "", "", "", "",
											"", "", "", "Total Amount",
											acOpAmount, totalAmount, ataxAmout,
											acreditAmount, grandTotal));
				}

				if (applications.size() > 0) {
					subscriber.put("applications", subscriber, applications);
					subscribers
							.put(subscribers.size(), subscribers, subscriber);
				}

			}
		} else {

			NativeObject subscriber = new NativeObject();
			subscriber.put("subscriber", subscriber, subscriberName);
			NativeArray applications = new NativeArray(0);

			Map<Application, Set<BillingSubscription>> billingSubs = SbHostObjectUtils
					.chargeSubscriberForMonth(subscriberName, year, month,
							rateCard);
			if (isPersistReport) {
				fileWriter
						.write(String
								.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s,%s, %s, %s, %s, %s, %s, %s %n",
										subscriberName, "", "", "", "", "", "",
										"", "", "", "", "", "", "", "", "", ""));
			}

			if (operatorName != null) {
				System.out.println("");
			} else {
			}

			for (Map.Entry<Application, Set<BillingSubscription>> billingEntries : billingSubs
					.entrySet()) {

				NativeObject application = new NativeObject();
				String applicationName = "";

				NativeArray subscriptionAPIs = new NativeArray(0);

				if (isPersistReport && fileWriter != null) {
					fileWriter
							.write(String
									.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s,%s, %s, %s, %s, %s, %s, %s %n",
											"", billingEntries.getKey()
													.getName(), "", "", "", "",
											"", "", "", "", "", "", "", "", "",
											"", ""));
				}

				for (BillingSubscription billingSubscription : billingEntries
						.getValue()) {
					if (isPersistReport && fileWriter != null) {
						fileWriter
								.write(String
										.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s,%s, %s, %s, %s, %s, %s, %s %n",
												"", "", billingSubscription
														.getApiId()
														.getApiName(),
												billingSubscription.getApiId()
														.getVersion(), "", "",
												"", "", "", "", "", "", "", "",
												"", "", ""));
					}

					NativeObject subscriptionAPI = new NativeObject();
					subscriptionAPI.put("subscriptionapi", subscriptionAPI,
							billingSubscription.getApiId().getApiName());
					subscriptionAPI.put("subscriptionapiversion",
							subscriptionAPI, billingSubscription.getApiId()
									.getVersion());

					applicationName = billingEntries.getKey().getName();

					NativeArray subscriptionOperators = new NativeArray(0);
					for (BillingSubscription.OperatorSubscription operatorSub : billingSubscription
							.getOperatorSubscriptionList()) {

						log.debug("operatorSub:" + operatorSub.toString());
						if ((operatorName.equalsIgnoreCase("__All__"))
								|| (operatorSub.getOperator()
										.equalsIgnoreCase(operatorName))) {

							NativeObject subscriptionOperator = new NativeObject();
							subscriptionOperator.put("operator",
									subscriptionOperator,
									operatorSub.getOperator());
							subscriptionOperator.put("ratename",
									subscriptionOperator, operatorSub.getRate()
											.getName());
							subscriptionOperator.put("count",
									subscriptionOperator,
									operatorSub.getCount());
							subscriptionOperator.put("price",
									subscriptionOperator,
									operatorSub.getPrice());
							subscriptionOperator.put("tax",
									subscriptionOperator,
									operatorSub.getTaxValue());
							subscriptionOperator.put("operationid",
									subscriptionOperator,
									BillingDAO
											.getOperationNameById(operatorSub
													.getOperationId()));
							subscriptionOperator.put("credit",
									subscriptionOperator,
									operatorSub.getAdscom());
							subscriptionOperator.put("opcom",
									subscriptionOperator,
									operatorSub.getOpcom());
							subscriptionOperator.put("currency",
									subscriptionOperator, operatorSub.getRate()
											.getCurrency());

							subscriptionOperators
									.put(subscriptionOperators.size(),
											subscriptionOperators,
											subscriptionOperator);

							if (isPersistReport && fileWriter != null) {
								String currencyStr = operatorSub.getRate()
										.getCurrency() + " ";
								fileWriter
										.write(String
												.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s,%s, %s, %s, %s, %s, %s, %s %n",
														"",
														"",
														"",
														"",
														operatorSub
																.getOperator(),
														BillingDAO
																.getOperationNameById(operatorSub
																		.getOperationId()),
														operatorSub.getRate()
																.getName(),
														currencyStr, "", "",
														"", "", operatorSub
																.getCount(),
														operatorSub.getOpcom(),
														operatorSub.getPrice(),
														operatorSub
																.getTaxValue(),
														operatorSub.getAdscom()));

								// print merchant wise breakdown
								Map<String, BilledCharge> mtmap = operatorSub
										.getMerchantCharges();
								if (mtmap.size() > 0) {
									for (Map.Entry<String, BilledCharge> entry : mtmap
											.entrySet()) {
										fileWriter
												.write(String
														.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s,%s, %s, %s, %s, %s, %s, %s %n",
																"",
																"",
																"",
																"",
																"",
																"",
																"",
																"",
																nvlDefault(entry
																		.getKey()),
																"",
																"",
																"",
																entry.getValue()
																		.getCount(),
																entry.getValue()
																		.getOpcom(),
																entry.getValue()
																		.getPrice(),
																entry.getValue()
																		.getTax(),
																entry.getValue()
																		.getAdscom()));
									}
								} else {
									// print category, subcategory breakdown
									Map<CategoryCharge, BilledCharge> ctmap = operatorSub
											.getCategoryCharges();
									for (Map.Entry<CategoryCharge, BilledCharge> entry : ctmap
											.entrySet()) {
										fileWriter
												.write(String
														.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s,%s, %s, %s, %s, %s, %s, %s %n",
																"",
																"",
																"",
																"",
																"",
																"",
																"",
																"",
																"",
																nvlDefault(entry
																		.getKey()
																		.getCategory()),
																nvlDefault(entry
																		.getKey()
																		.getSubcategory()),
																"",
																entry.getValue()
																		.getCount(),
																entry.getValue()
																		.getOpcom(),
																entry.getValue()
																		.getPrice(),
																entry.getValue()
																		.getTax(),
																entry.getValue()
																		.getAdscom()));

										// tier wise breakdown
										Map<String, BilledCharge> timap = entry
												.getValue().getTierCharges();
										for (Map.Entry<String, BilledCharge> tientry : timap
												.entrySet()) {
											fileWriter
													.write(String
															.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s,%s, %s, %s, %s, %s, %s, %s %n",
																	"",
																	"",
																	"",
																	"",
																	"",
																	"",
																	"",
																	"",
																	"",
																	"",
																	"",
																	tientry.getKey(),
																	tientry.getValue()
																			.getCount(),
																	tientry.getValue()
																			.getOpcom(),
																	tientry.getValue()
																			.getPrice(),
																	tientry.getValue()
																			.getTax(),
																	tientry.getValue()
																			.getAdscom()));
										}

									}
								}
							}

						} else {
							continue;
						}
					}
					subscriptionAPI.put("operators", subscriptionAPI,
							subscriptionOperators);

					subscriptionAPIs.put(subscriptionAPIs.size(),
							subscriptionAPIs, subscriptionAPI);
				}

				application
						.put("applicationname", application, applicationName);
				application.put("subscriptions", application, subscriptionAPIs);

				applications
						.put(applications.size(), applications, application);

			}

			subscriber.put("applications", subscriber, applications);
			subscribers.put(subscribers.size(), subscribers, subscriber);

		}

		if (fileWriter != null) {
			fileWriter.close();
		}

		return subscribers;
	}

	/**
	 * Checks if is user admin user.
	 *
	 * @param username the username
	 * @return true, if is user admin user
	 */
	public static boolean isUserAdminUser(String username) {
		String adminUserName = HostObjectComponent.getRealmService()
				.getBootstrapRealmConfiguration().getAdminUserName();
		log.info("::: AdminUser Name ::: " + adminUserName);
		return adminUserName.equals(username);
	}

	/**
	 * Gets the all subscribers.
	 *
	 * @return the all subscribers
	 * @throws Exception 
	 */
	public static List<String> getAllSubscribers() throws Exception {
		List<String> subscriptions = BillingDAO
				.getAllSubscriptions();
		Collections.sort(subscriptions, String.CASE_INSENSITIVE_ORDER);
		return subscriptions;
	}

	/**
	 * Gets the operation types.
	 *
	 * @return the operation types
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<String[]> getOperationTypes() throws SQLException,
			APIMgtUsageQueryServiceClientException, APIManagementException {
		List<String[]> txTypes = BillingDAO.getAllOperationTypes();
		return txTypes;
	}

	/**
	 * Gets the all operators.
	 *
	 * @return the all operators
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 */
	public static List<String> getAllOperators() throws SQLException,
			APIMgtUsageQueryServiceClientException {
		List<String> operators = OperatorDAO.getAllOperators();
		return operators;
	}

	/**
	 * Gets the operatorbreakdown.
	 *
	 * @param applicationid the applicationid
	 * @param year the year
	 * @param month the month
	 * @param subscriber the subscriber
	 * @param api the api
	 * @return the operatorbreakdown
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static Map<String, Integer> getOperatorbreakdown(
			String applicationid, String year, String month, String subscriber,
			String api) throws SQLException,
			APIMgtUsageQueryServiceClientException, APIManagementException {

		String consumerkey = "%";
		if (applicationid != null) {
			consumerkey = BillingDAO.getApplicationconsumer(
					Integer.parseInt(applicationid),
					APIConstants.API_KEY_TYPE_PRODUCTION);
		}

		Map<String, Integer> usageCounts = BillingDAO
				.getAPICountsForApplicationOpco(consumerkey, year, month,
						subscriber, api);

		return usageCounts;

	}

	/**
	 * Gets the application name by id.
	 *
	 * @param applicationid the applicationid
	 * @return the application name by id
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static String getApplicationNameById(String applicationid)
			throws SQLException, APIMgtUsageQueryServiceClientException,
			APIManagementException {

		String appName = BillingDAO.getApplicationName(
				Integer.parseInt(applicationid),
				APIConstants.API_KEY_TYPE_PRODUCTION);

		return appName;

	}

	/**
	 * Gets the total api traffic for pie chart.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param subscriber the subscriber
	 * @param operator the operator
	 * @param applicationId the application id
	 * @return the total api traffic for pie chart
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<String[]> getTotalAPITrafficForPieChart(String fromDate,
			String toDate, String subscriber, String operator, int applicationId)
			throws SQLException, APIMgtUsageQueryServiceClientException,
			APIManagementException {
		List<String[]> api_request = BillingDAO
				.getTotalAPITrafficForPieChart(fromDate, toDate, subscriber,
						operator, applicationId);
		return api_request;
	}

	/**
	 * Gets the total api traffic for histogram.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param subscriber the subscriber
	 * @param operator the operator
	 * @param applicationId the application id
	 * @param api the api
	 * @return the total api traffic for histogram
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<String[]> getTotalAPITrafficForHistogram(
			String fromDate, String toDate, String subscriber, String operator,
			int applicationId, String api) throws SQLException,
			APIMgtUsageQueryServiceClientException, APIManagementException {
		List<String[]> api_request = BillingDAO
				.getTotalAPITrafficForHistogram(fromDate, toDate, subscriber,
						operator, applicationId, api);
		return api_request;
	}

	/**
	 * Gets the operator wise api traffic for pie chart.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param subscriber the subscriber
	 * @param api the api
	 * @param applicationId the application id
	 * @return the operator wise api traffic for pie chart
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<String[]> getOperatorWiseAPITrafficForPieChart(
			String fromDate, String toDate, String subscriber, String api,
			int applicationId) throws SQLException,
			APIMgtUsageQueryServiceClientException, APIManagementException {
		List<String[]> api_request = BillingDAO
				.getOperatorWiseAPITrafficForPieChart(fromDate, toDate,
						subscriber, api, applicationId);
		return api_request;
	}

	/**
	 * Gets the approval history.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param subscriber the subscriber
	 * @param api the api
	 * @param applicationId the application id
	 * @param operator the operator
	 * @return the approval history
	 * @throws Exception 
	 */
	public static List<String[]> getApprovalHistory(String fromDate,
			String toDate, String subscriber, String api, int applicationId,
			String operator) throws Exception {
		List<String[]> api_request = BillingDAO
				.getApprovalHistory(fromDate, toDate, subscriber, api,
						applicationId, operator);
		return api_request;
	}

	/**
	 * Gets the approval history app.
	 *
	 * @param applicationId the application id
	 * @param operator the operator
	 * @return the approval history app
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<Approval> getApprovalHistoryApp(int applicationId,
			String operator) throws SQLException,
			APIMgtUsageQueryServiceClientException, APIManagementException {
		List<Approval> api_request = BillingDAO
				.getApprovalHistoryApp(applicationId, operator);
		return api_request;
	}

	/**
	 * Gets the all ap is.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param subscriber the subscriber
	 * @param operator the operator
	 * @param applicationId the application id
	 * @param api the api
	 * @return the all ap is
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<String[]> getAllAPIs(String fromDate, String toDate,
			String subscriber, String operator, int applicationId, String api)
			throws SQLException, APIMgtUsageQueryServiceClientException,
			APIManagementException {
		List<String[]> apis = BillingDAO.getAllAPIs(fromDate,
				toDate, subscriber, operator, applicationId, api);
		return apis;
	}

	/**
	 * Gets the all error response codes.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param subscriber the subscriber
	 * @param operator the operator
	 * @param applicationId the application id
	 * @param api the api
	 * @return the all error response codes
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<String[]> getAllErrorResponseCodes(String fromDate,
			String toDate, String subscriber, String operator,
			int applicationId, String api) throws SQLException,
			APIMgtUsageQueryServiceClientException, APIManagementException {
		List<String[]> resCodes = BillingDAO
				.getAllErrorResponseCodes(fromDate, toDate, subscriber,
						operator, applicationId, api);
		return resCodes;
	}

	/**
	 * Gets the subscribers by operator.
	 *
	 * @param operatorName the operator name
	 * @return the subscribers by operator
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<String> getSubscribersByOperator(String operatorName)
			throws SQLException, APIMgtUsageQueryServiceClientException,
			APIManagementException {
		List<Integer> applicationIds = OperatorDAO
				.getApplicationsByOperator(operatorName);
		List<String> subscribers = new ArrayList<String>();
		List<String> tempSubscribers = new ArrayList<String>();
		for (Integer applicationId : applicationIds) {
			ApiMgtDAO apiMgtDAO = new ApiMgtDAO();
			try {
				Application application = apiMgtDAO
						.getApplicationById(applicationId);
				int tempSubscriberId = application.getSubscriber().getId();
				String tempSubscriberName = apiMgtDAO.getSubscriber(
						tempSubscriberId).getName();
				tempSubscribers.add(tempSubscriberName);
			} catch (NullPointerException ne) {
				continue;
			}
		}
		subscribers.addAll(removeDuplicateWithOrder(tempSubscribers));
		return subscribers;
	}

	/**
	 * Gets the applications by subscriber.
	 *
	 * @param subscriberName the subscriber name
	 * @return the applications by subscriber
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<String[]> getApplicationsBySubscriber(
			String subscriberName) throws SQLException,
			APIMgtUsageQueryServiceClientException, APIManagementException {
		List<String[]> applicationsList = new ArrayList<String[]>();
		ApiMgtDAO apiMgtDAO = new ApiMgtDAO();
		// Adding null for param
		Application[] applications = apiMgtDAO.getApplications(new Subscriber(
				subscriberName), null);
		for (Application application : applications) {
			String[] tempApplicationDetails = {
					Integer.toString(application.getId()),
					application.getName() };
			applicationsList.add(tempApplicationDetails);
		}
		return applicationsList;
	}

	/**
	 * Gets the operators by subscriber.
	 *
	 * @param subscriberName the subscriber name
	 * @return the operators by subscriber
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<String> getOperatorsBySubscriber(String subscriberName)
			throws SQLException, APIMgtUsageQueryServiceClientException,
			APIManagementException {
		List<String> operatorList = new ArrayList<String>();
		List<String> tempOperatorList = new ArrayList<String>();
		ApiMgtDAO apiMgtDAO = new ApiMgtDAO();
		// Adding null for param
		Application[] applications = apiMgtDAO.getApplications(new Subscriber(
				subscriberName), null);
		if (applications != null && applications.length > 0) {
			for (Application application : applications) {
				int tempApplicationId = application.getId();
				List<String> tempOperatorNames = new ArrayList<String>();
				tempOperatorNames = OperatorDAO
						.getOperatorNamesByApplication(tempApplicationId);
				for (String operator : tempOperatorNames) {
					String tempOperator = operator;
					tempOperatorList.add(tempOperator);
				}
			}
		} else {
			log.info("Application list for the provided subscriber is null or empty.");
		}
		operatorList.addAll(removeDuplicateWithOrder(tempOperatorList));
		return operatorList;
	}

	/**
	 * Removes the duplicate with order.
	 *
	 * @param arlList the arl list
	 * @return the list
	 */
	public static List<String> removeDuplicateWithOrder(List<String> arlList) {
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = arlList.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element)) {
				newList.add(element);
			}
		}
		arlList.clear();
		arlList.addAll(newList);
		return arlList;
	}

	/**
	 * Gets the AP is by subscriber.
	 *
	 * @param subscriberName the subscriber name
	 * @return the AP is by subscriber
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 * @throws IdentityException the identity exception
	 */
	public static List<String> getAPIsBySubscriber(String subscriberName)
			throws SQLException, APIMgtUsageQueryServiceClientException,
			APIManagementException, IdentityException {
		List<String> apiList = new ArrayList<String>();
		List<String> tempAPIList = new ArrayList<String>();
		ApiMgtDAO apiMgtDAO = new ApiMgtDAO();
		APIInfoDTO[] subscribedAPIsOfUser = apiMgtDAO
				.getSubscribedAPIsOfUser(subscriberName);
		for (APIInfoDTO aPIInfoDTO : subscribedAPIsOfUser) {
			tempAPIList.add(aPIInfoDTO.getApiName());
		}

		apiList.addAll(removeDuplicateWithOrder(tempAPIList));
		return apiList;
	}

	/**
	 * Gets the API wise traffic for report.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param subscriber the subscriber
	 * @param operator the operator
	 * @param api the api
	 * @return the API wise traffic for report
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<String[]> getAPIWiseTrafficForReport(String fromDate,
			String toDate, String subscriber, String operator, String api)
			throws SQLException, APIMgtUsageQueryServiceClientException,
			APIManagementException {
		List<String[]> api_request_data = BillingDAO
				.getAPIWiseTrafficForReport(fromDate, toDate, subscriber,
						operator, api);
		return api_request_data;
	}

	/**
	 * Gets the filtered customer care report.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param msisdn the msisdn
	 * @param subscriber the subscriber
	 * @param operator the operator
	 * @param app the app
	 * @param api the api
	 * @param stLimit the st limit
	 * @param endLimit the end limit
	 * @param timeOffset the time offset
	 * @return the filtered customer care report
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<String[]> getFilteredCustomerCareReport(String fromDate,
			String toDate, String msisdn, String subscriber, String operator,
			String app, String api, String stLimit, String endLimit,
			String timeOffset) throws SQLException,
			APIMgtUsageQueryServiceClientException, APIManagementException {
		List<String[]> api_request_data = BillingDAO
				.getCustomerCareReportData(fromDate, toDate, msisdn,
						subscriber, operator, app, api, stLimit, endLimit,
						timeOffset);
		return api_request_data;
	}

	/**
	 * Gets the API wise traffic for report charging.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param subscriber the subscriber
	 * @param operator the operator
	 * @param api the api
	 * @return the API wise traffic for report charging
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<String[]> getAPIWiseTrafficForReportCharging(
			String fromDate, String toDate, String subscriber, String operator,
			String api) throws SQLException,
			APIMgtUsageQueryServiceClientException, APIManagementException {
		List<String[]> charging_request_data = BillingDAO
				.getAPIWiseTrafficForReportCharging(fromDate, toDate,
						subscriber, operator, api);
		return charging_request_data;
	}

	 
	/**
	 * Gets the report.
	 *
	 * @param suscriber the suscriber
	 * @param period the period
	 * @return the report
	 */
	public static String getReport(String suscriber, String period) {
		String fileContent = "";
		BufferedReader bufferedReader = null;
		try {
			File file = getCSVFile(suscriber, period);
			bufferedReader = new BufferedReader(new FileReader(file));

			StringBuilder fileAppender = new StringBuilder();

			int c = -1;

			while ((c = bufferedReader.read()) != -1) {
				char ch = (char) c;
				fileAppender.append(ch);
			}

			fileContent = fileAppender.toString();

		} catch (IOException e) {
			log.error("report file reading error : " + e.getMessage());
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				log.error("Buuffered Reader closing error : " + e.getMessage());
			}
		}
		return fileContent;
	}

	/**
	 * Gets the custom report.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param subscriberName the subscriber name
	 * @param operator the operator
	 * @param api the api
	 * @return the custom report
	 */
	public static String getCustomReport(String fromDate, String toDate,
			String subscriberName, String operator, String api) {
		String fileContent = "";
		BufferedReader bufferedReader = null;
		try {
			File file = getCustomCSVFile(fromDate, toDate, subscriberName,
					operator, api);
			bufferedReader = new BufferedReader(new FileReader(file));

			StringBuilder fileAppender = new StringBuilder();

			int c = -1;

			while ((c = bufferedReader.read()) != -1) {
				char ch = (char) c;
				fileAppender.append(ch);
			}
			fileContent = fileAppender.toString();

		} catch (IOException e) {
			log.error("report file reading error : " + e.getMessage());
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				log.error("Buuffered Reader closing error : " + e.getMessage());
			}
		}
		return fileContent;
	}

	/**
	 * Gets the error response codes for pie chart.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param subscriber the subscriber
	 * @param operator the operator
	 * @param applicationId the application id
	 * @param api the api
	 * @return the error response codes for pie chart
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<String[]> getErrorResponseCodesForPieChart(
			String fromDate, String toDate, String subscriber, String operator,
			int applicationId, String api) throws SQLException,
			APIMgtUsageQueryServiceClientException, APIManagementException {
		List<String[]> api_request = BillingDAO
				.getErrorResponseCodesForPieChart(fromDate, toDate, subscriber,
						operator, applicationId, api);
		return api_request;
	}

	/**
	 * Gets the error response codes for histogram.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param subscriber the subscriber
	 * @param operator the operator
	 * @param applicationId the application id
	 * @param api the api
	 * @return the error response codes for histogram
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<String[]> getErrorResponseCodesForHistogram(
			String fromDate, String toDate, String subscriber, String operator,
			int applicationId, String api) throws SQLException,
			APIMgtUsageQueryServiceClientException, APIManagementException {
		List<String[]> api_response_codes = BillingDAO
				.getErrorResponseCodesForHistogram(fromDate, toDate,
						subscriber, operator, applicationId, api);
		return api_response_codes;
	}

	 
	/**
	 * Gets the rate card.
	 *
	 * @return the rate card
	 * @throws APIManagementException the API management exception
	 */
	public static Map<RateKey, ChargeRate> getRateCard()
			throws APIManagementException {
		Map<RateKey, ChargeRate> ratecard = new HashMap<RateKey, ChargeRate>();
		try {
			Registry registry = HostObjectComponent.getRegistryService()
					.getGovernanceSystemRegistry();
			if (registry
					.resourceExists(HostObjectConstants.SB_RATE_CARD_LOCATION)) {
				Resource resource = registry
						.get(HostObjectConstants.SB_RATE_CARD_LOCATION);

				String content = new String((byte[]) resource.getContent());
				OMElement element = AXIOMUtil.stringToOM(content);

				OMElement opsElement = element
						.getFirstChildWithName(HostObjectConstants.OPERATORS_ELEMENT);
				Iterator operators = opsElement
						.getChildrenWithName(HostObjectConstants.OPERATOR_ELEMENT);

				while (operators.hasNext()) {
					OMElement operator = (OMElement) operators.next();
					String operatorName = operator
							.getAttributeValue(HostObjectConstants.NAME_ATTRIBUTE);

					Iterator apis = operator
							.getChildrenWithName(HostObjectConstants.API_ELEMENT);
					while (apis.hasNext()) {
						OMElement api = (OMElement) apis.next();
						String apiName = api
								.getAttributeValue(HostObjectConstants.NAME_ATTRIBUTE);

						Iterator operations = api
								.getChildrenWithName(HostObjectConstants.OPERATION_ELEMENT);
						while (operations.hasNext()) {
							OMElement operation = (OMElement) operations.next();
							String operationName = operation
									.getAttributeValue(HostObjectConstants.NAME_ATTRIBUTE);

							Iterator rates = operation
									.getChildrenWithName(HostObjectConstants.RATE_ELEMENT);
							while (rates.hasNext()) {
								OMElement rate = (OMElement) rates.next();
								OMElement name = rate
										.getFirstChildWithName(HostObjectConstants.RATE_NAME_ELEMENT);
								OMElement currency = rate
										.getFirstChildWithName(HostObjectConstants.RATE_CURRENCY_ELEMENT);
								OMElement value = rate
										.getFirstChildWithName(HostObjectConstants.RATE_VALUE_ELEMENT);
								OMElement type = rate
										.getFirstChildWithName(HostObjectConstants.RATE_TYPE_ELEMENT);

								ChargeRate chargeRate = new ChargeRate(
										name.getText());// <Name>
								chargeRate.setCurrency(currency.getText());// <Currency>
								chargeRate.setValue(new BigDecimal(value
										.getText()));// <Value>
								chargeRate.setType(RateType.getEnum(type
										.getText()));// <Type>
								String isDefault = null;
								if (rate.getAttribute(HostObjectConstants.RATE_DEFAULT_ATTRIBUTE) != null) {
									isDefault = rate
											.getAttributeValue(HostObjectConstants.RATE_DEFAULT_ATTRIBUTE);
								}
								chargeRate.setDefault(Boolean
										.parseBoolean(isDefault));// default
																	// attr

								// Set default values from rate operation level

								OMElement rootAttr = rate
										.getFirstChildWithName(HostObjectConstants.RATE_ATTRIBUTES_ELEMENT);
								if (rootAttr != null) {
									Map<String, String> attributesMap = new HashMap<String, String>();
									for (Iterator childElements = rootAttr
											.getChildElements(); childElements
											.hasNext();) {
										OMElement attrElem = (OMElement) childElements
												.next();
										String attrName = attrElem
												.getLocalName();
										String attrValue = attrElem.getText();
										attributesMap.put(attrName, attrValue);
									}
									chargeRate.setRateAttributes(attributesMap);
								}

								OMElement categoryBased = rate
										.getFirstChildWithName(HostObjectConstants.RATE_CATEGORY_BASE_ELEMENT);
								OMElement surchargeElement = rate
										.getFirstChildWithName(HostObjectConstants.RATE_SURCHARGE_ELEMENT);
								if (surchargeElement != null) {
									SurchargeEntity surchargeEntity = new SurchargeEntity();
									String surchargeElementValue = surchargeElement
											.getAttributeValue(HostObjectConstants.TIER_VALUE_ELEMENT);
									surchargeEntity
											.setSurchargeElementValue(surchargeElementValue);
									String surchargeElementAds = surchargeElement
											.getAttributeValue(HostObjectConstants.RATE_ADS_COMMISSION);
									surchargeEntity
											.setSurchargeElementAds(surchargeElementAds);
									String surchargeElementOpco = surchargeElement
											.getAttributeValue(HostObjectConstants.RATE_OPCO_COMMISSION);
									surchargeEntity
											.setSurchargeElementOpco(surchargeElementOpco);
									chargeRate
											.setSurchargeEntity(surchargeEntity);// <Surcharge>
								}

								boolean categoryBasedVal = false;
								if (categoryBased != null) {
									categoryBasedVal = Boolean
											.valueOf(categoryBased.getText());
								}
								chargeRate
										.setCategoryBasedVal(categoryBasedVal);// <CategorBase>
								OMElement commission = rate
										.getFirstChildWithName(HostObjectConstants.RATE_COMMISSION);
								if (commission != null) {
									OMElement spPercentageElem = commission
											.getFirstChildWithName(HostObjectConstants.RATE_SP_COMMISSION);
									RateCommission cc = new RateCommission();
									Double spPercentage = 0.0;
									if (spPercentageElem != null) {
										spPercentage = Double
												.valueOf(spPercentageElem
														.getText());
										cc.setSpCommission(new BigDecimal(
												spPercentage));
									}
									OMElement adsPercentageElem = commission
											.getFirstChildWithName(HostObjectConstants.RATE_ADS_COMMISSION);
									Double adsPercentage = 0.0;
									if (adsPercentageElem != null) {
										adsPercentage = Double
												.valueOf(adsPercentageElem
														.getText());
										cc.setAdsCommission(new BigDecimal(
												adsPercentage));
									}
									OMElement opcoPercentageElem = commission
											.getFirstChildWithName(HostObjectConstants.RATE_OPCO_COMMISSION);
									Double opcoPercentage = 0.0;
									if (opcoPercentageElem != null) {
										opcoPercentage = Double
												.valueOf(opcoPercentageElem
														.getText());
										cc.setOpcoCommission(new BigDecimal(
												opcoPercentage));
									}
									chargeRate.setCommission(cc);// <Commission>
								}

								OMElement usageTiers = rate
										.getFirstChildWithName(HostObjectConstants.RATE_USAGE_TIERS_ELEMENT);
								if (usageTiers != null) {
									Iterator tiers = usageTiers
											.getChildrenWithName(HostObjectConstants.RATE_USAGE_TIER_ELEMENT);
									List<UsageTiers> tierEntities = new ArrayList<UsageTiers>();
									while (tiers.hasNext()) {
										OMElement tier = (OMElement) tiers
												.next();
										UsageTiers usageTiersEntity = new UsageTiers();
										String tierRate = tier
												.getAttributeValue(HostObjectConstants.TIER_RATE_ELEMENT);
										String tierName = tier
												.getAttributeValue(HostObjectConstants.TIER_NAME_ELEMENT);
										usageTiersEntity.setRateId(tierRate);
										usageTiersEntity.setTierName(tierName);

										OMElement minElement = tier
												.getFirstChildWithName(HostObjectConstants.RATE_RANGE_MIN);
										OMElement maxElement = tier
												.getFirstChildWithName(HostObjectConstants.RATE_RANGE_MAX);
										String tierMinValue = minElement
												.getAttributeValue(HostObjectConstants.TIER_VALUE_ELEMENT);
										String tierMaxValue = maxElement
												.getAttributeValue(HostObjectConstants.TIER_VALUE_ELEMENT);
										usageTiersEntity.setMin(tierMinValue);
										usageTiersEntity.setMax(tierMaxValue);
										tierEntities.add(usageTiersEntity);
									}
									chargeRate.setUsageTiers(tierEntities);
								}

								Map<String, Object> categoryEntityMap = new HashMap<String, Object>();
								Iterator categoryIter = rate
										.getChildrenWithName(HostObjectConstants.RATE_CATEGORY_ELEMENT);
								while (categoryIter.hasNext()) {
									OMElement category = (OMElement) categoryIter
											.next();

									if (category != null) {
										List<CategoryEntity> categoryEntityList = new ArrayList<CategoryEntity>();

										String categoryName = category
												.getAttributeValue(HostObjectConstants.NAME_ATTRIBUTE);
										String categoryRate = category
												.getAttributeValue(HostObjectConstants.TIER_RATE_ELEMENT);
										Map<String, Object> subCategoryEntityMap = new HashMap<String, Object>();

										subCategoryEntityMap.put("__default__",
												categoryRate);
										categoryEntityMap.put(categoryName,
												subCategoryEntityMap);

										OMElement attributes = category
												.getFirstChildWithName(HostObjectConstants.RATE_ATTRIBUTES_ELEMENT);
										if (attributes != null) {
											Map<String, String> attributesMap = new HashMap<String, String>();
											for (Iterator childElements = attributes
													.getChildElements(); childElements
													.hasNext();) {
												OMElement attrElem = (OMElement) childElements
														.next();
												String attrName = attrElem
														.getLocalName();
												String attrValue = attrElem
														.getText();
												attributesMap.put(attrName,
														attrValue);
											}
											subCategoryEntityMap.put(
													"__default__",
													attributesMap);
											categoryEntityMap.put(categoryName,
													subCategoryEntityMap);
										}
										// CATEGORY USAGE TIERS
										OMElement categoryUsageTiers = category
												.getFirstChildWithName(HostObjectConstants.RATE_USAGE_TIERS_ELEMENT);
										if (categoryUsageTiers != null) {
											Iterator tiers = categoryUsageTiers
													.getChildrenWithName(HostObjectConstants.RATE_USAGE_TIER_ELEMENT);
											List<UsageTiers> tierEntities = new ArrayList<UsageTiers>();
											while (tiers.hasNext()) {
												OMElement tier = (OMElement) tiers
														.next();
												UsageTiers usageTiersEntity = new UsageTiers();
												String tierRate = tier
														.getAttributeValue(HostObjectConstants.TIER_RATE_ELEMENT);
												String tierName = tier
														.getAttributeValue(HostObjectConstants.TIER_NAME_ELEMENT);
												usageTiersEntity
														.setRateId(tierRate);
												usageTiersEntity
														.setTierName(tierName);

												OMElement minElement = tier
														.getFirstChildWithName(HostObjectConstants.RATE_RANGE_MIN);
												OMElement maxElement = tier
														.getFirstChildWithName(HostObjectConstants.RATE_RANGE_MAX);
												String tierMinValue = minElement
														.getAttributeValue(HostObjectConstants.TIER_VALUE_ELEMENT);
												String tierMaxValue = maxElement
														.getAttributeValue(HostObjectConstants.TIER_VALUE_ELEMENT);
												usageTiersEntity
														.setMin(tierMinValue);
												usageTiersEntity
														.setMax(tierMaxValue);
												tierEntities
														.add(usageTiersEntity);
											}
											subCategoryEntityMap
													.put("__default__",
															tierEntities);
											categoryEntityMap.put(categoryName,
													subCategoryEntityMap);
										}
										OMElement categoryCommission = category
												.getFirstChildWithName(HostObjectConstants.RATE_COMMISSION);
										RateCommission cc = new RateCommission();
										if (categoryCommission != null) {
											OMElement spPercentageElem = categoryCommission
													.getFirstChildWithName(HostObjectConstants.RATE_SP_COMMISSION);
											Double spPercentage = 0.0;
											if (spPercentageElem != null) {
												spPercentage = Double
														.valueOf(spPercentageElem
																.getText());
												cc.setSpCommission(new BigDecimal(
														spPercentage));
											}
											OMElement adsPercentageElem = categoryCommission
													.getFirstChildWithName(HostObjectConstants.RATE_ADS_COMMISSION);
											Double adsPercentage = 0.0;
											if (adsPercentageElem != null) {
												adsPercentage = Double
														.valueOf(adsPercentageElem
																.getText());
												cc.setAdsCommission(new BigDecimal(
														adsPercentage));
											}
											OMElement opcoPercentageElem = categoryCommission
													.getFirstChildWithName(HostObjectConstants.RATE_OPCO_COMMISSION);
											Double opcoPercentage = 0.0;
											if (opcoPercentageElem != null) {
												opcoPercentage = Double
														.valueOf(opcoPercentageElem
																.getText());
												cc.setOpcoCommission(new BigDecimal(
														opcoPercentage));
											}

											subCategoryEntityMap.put(
													"__default__", cc);
											categoryEntityMap.put(categoryName,
													subCategoryEntityMap);
										}

										// ========================SUB
										// CATEGORY====================================
										Iterator subCategories = category
												.getChildrenWithName(HostObjectConstants.RATE_SUB_CATEGORY_ELEMENT);
										List<SubCategory> subCategoriesMapList = new ArrayList<SubCategory>();
										while (subCategories.hasNext()) {
											OMElement subCategory = (OMElement) subCategories
													.next();
											String subCategoryname = subCategory
													.getAttributeValue(HostObjectConstants.NAME_ATTRIBUTE);
											String subCategoryrate = subCategory
													.getAttributeValue(HostObjectConstants.TIER_RATE_ELEMENT);

											subCategoryEntityMap.put(
													subCategoryname,
													subCategoryrate);
											categoryEntityMap.put(categoryName,
													subCategoryEntityMap);

											Map<String, String> subCategoriesMap = new HashMap<String, String>();
											OMElement subAttributes = subCategory
													.getFirstChildWithName(HostObjectConstants.RATE_ATTRIBUTES_ELEMENT);
											if (subAttributes != null) {
												for (Iterator childElements = subAttributes
														.getChildElements(); childElements
														.hasNext();) {
													OMElement attrElem = (OMElement) childElements
															.next();
													String attrName = attrElem
															.getLocalName();
													String attrValue = attrElem
															.getText();
													subCategoriesMap
															.put(attrName,
																	attrValue);
												}
												subCategoryEntityMap.put(
														subCategoryname,
														subCategoriesMap);
												categoryEntityMap.put(
														categoryName,
														subCategoryEntityMap);
											}

											OMElement subCategoryUsageTiers = subCategory
													.getFirstChildWithName(HostObjectConstants.RATE_USAGE_TIERS_ELEMENT);
											if (subCategoryUsageTiers != null) {
												Iterator tiers = subCategoryUsageTiers
														.getChildrenWithName(HostObjectConstants.RATE_USAGE_TIER_ELEMENT);
												List<UsageTiers> tierEntities = new ArrayList<UsageTiers>();
												while (tiers.hasNext()) {
													OMElement tier = (OMElement) tiers
															.next();
													UsageTiers usageTiersEntity = new UsageTiers();
													String tierRate = tier
															.getAttributeValue(HostObjectConstants.TIER_RATE_ELEMENT);
													String tierName = tier
															.getAttributeValue(HostObjectConstants.TIER_NAME_ELEMENT);
													usageTiersEntity
															.setRateId(tierRate);
													usageTiersEntity
															.setTierName(tierName);

													OMElement minElement = tier
															.getFirstChildWithName(HostObjectConstants.RATE_RANGE_MIN);
													OMElement maxElement = tier
															.getFirstChildWithName(HostObjectConstants.RATE_RANGE_MAX);
													String tierMinValue = minElement
															.getAttributeValue(HostObjectConstants.TIER_VALUE_ELEMENT);
													String tierMaxValue = maxElement
															.getAttributeValue(HostObjectConstants.TIER_VALUE_ELEMENT);
													usageTiersEntity
															.setMin(tierMinValue);
													usageTiersEntity
															.setMax(tierMaxValue);
													tierEntities
															.add(usageTiersEntity);
												}
												subCategoryEntityMap.put(
														subCategoryname,
														tierEntities);
												categoryEntityMap.put(
														categoryName,
														subCategoryEntityMap);
											}
											OMElement subCategoryCommission = subCategory
													.getFirstChildWithName(HostObjectConstants.RATE_COMMISSION);
											RateCommission subCC = new RateCommission();
											if (subCategoryCommission != null) {
												OMElement spPercentageElem = subCategoryCommission
														.getFirstChildWithName(HostObjectConstants.RATE_SP_COMMISSION);
												Double spPercentage = 0.0;
												if (spPercentageElem != null) {
													spPercentage = Double
															.valueOf(spPercentageElem
																	.getText());
													subCC.setSpCommission(new BigDecimal(
															spPercentage));
												}
												OMElement adsPercentageElem = subCategoryCommission
														.getFirstChildWithName(HostObjectConstants.RATE_ADS_COMMISSION);
												Double adsPercentage = 0.0;
												if (adsPercentageElem != null) {
													adsPercentage = Double
															.valueOf(adsPercentageElem
																	.getText());
													subCC.setAdsCommission(new BigDecimal(
															adsPercentage));
												}
												OMElement opcoPercentageElem = subCategoryCommission
														.getFirstChildWithName(HostObjectConstants.RATE_OPCO_COMMISSION);
												Double opcoPercentage = 0.0;
												if (opcoPercentageElem != null) {
													opcoPercentage = Double
															.valueOf(opcoPercentageElem
																	.getText());
													subCC.setOpcoCommission(new BigDecimal(
															opcoPercentage));
												}

												subCategoryEntityMap.put(
														subCategoryname, subCC);
												categoryEntityMap.put(
														categoryName,
														subCategoryEntityMap);
											}
										}
										// ========================SUB
										// CATEGORY====================================
										
										chargeRate
												.setCategories(categoryEntityMap);
									}
								}

								OMElement taxes = rate
										.getFirstChildWithName(HostObjectConstants.RATE_TAXES_ELEMENT);
								if (taxes != null) {
									Iterator tax = taxes
											.getChildrenWithName(HostObjectConstants.RATE_TAX_ELEMENT);
									List<String> taxList = new ArrayList<String>();
									while (tax.hasNext()) {
										OMElement taxElement = (OMElement) tax
												.next();
										taxList.add(taxElement.getText());
									}
									chargeRate.setTaxList(taxList);
								}
								RateKey rateKey = new RateKey(operatorName,
										apiName, name.getText(), operationName);
								ratecard.put(rateKey, chargeRate);
							}
						}
					}
				}
			}

		} catch (RegistryException e) {
			String msg = "Error while retrieving rate card from registry";
			log.error(msg, e);
			throw new APIManagementException(msg, e);
		} catch (XMLStreamException e) {
			String msg = "Malformed XML found in the rate card resource";
			log.error(msg, e);
			throw new APIManagementException(msg, e);
		}
		return ratecard;
	}

	 
	/**
	 * Gets the rates for operator api.
	 *
	 * @param operatorName the operator name
	 * @param apiName the api name
	 * @return the rates for operator api
	 * @throws APIManagementException the API management exception
	 */
	public static List<ChargeRate> getRatesForOperatorApi(String operatorName,
			String apiName) throws APIManagementException {
		List<ChargeRate> rateList = new ArrayList<ChargeRate>();
		try {
			Registry registry = HostObjectComponent.getRegistryService()
					.getGovernanceSystemRegistry();
			if (registry
					.resourceExists(HostObjectConstants.SB_RATE_CARD_LOCATION)) {
				Resource resource = registry
						.get(HostObjectConstants.SB_RATE_CARD_LOCATION);
				String content = new String((byte[]) resource.getContent());
				OMElement element = AXIOMUtil.stringToOM(content);
				OMElement opsElement = element
						.getFirstChildWithName(HostObjectConstants.OPERATORS_ELEMENT);

				String xpath = "//" + HostObjectConstants.OPERATORS_ELEMENT
						+ "/"
						+ HostObjectConstants.OPERATOR_ELEMENT.getLocalPart()
						+ "[@"
						+ HostObjectConstants.NAME_ATTRIBUTE.getLocalPart()
						+ "='" + operatorName + "']/"
						+ HostObjectConstants.API_ELEMENT.getLocalPart() + "[@"
						+ HostObjectConstants.NAME_ATTRIBUTE.getLocalPart()
						+ "='" + apiName + "']/"
						+ HostObjectConstants.RATE_ELEMENT.getLocalPart();

				AXIOMXPath axiomxPath = new AXIOMXPath(xpath);
				List rateNodes = axiomxPath.selectNodes(opsElement);

				for (Object rateNode : rateNodes) {
					OMElement rate = (OMElement) rateNode;
					OMElement name = rate
							.getFirstChildWithName(HostObjectConstants.RATE_NAME_ELEMENT);
					OMElement currency = rate
							.getFirstChildWithName(HostObjectConstants.RATE_CURRENCY_ELEMENT);
					OMElement value = rate
							.getFirstChildWithName(HostObjectConstants.RATE_VALUE_ELEMENT);
					OMElement type = rate
							.getFirstChildWithName(HostObjectConstants.RATE_TYPE_ELEMENT);
					ChargeRate chargeRate = new ChargeRate(name.getText());
					chargeRate.setCurrency(currency.getText());
					chargeRate.setValue(new BigDecimal(value.getText()));
					chargeRate.setType(RateType.getEnum(type.getText()));

					String isDefault = null;
					if (rate.getAttribute(HostObjectConstants.RATE_DEFAULT_ATTRIBUTE) != null) {
						isDefault = rate
								.getAttributeValue(HostObjectConstants.RATE_DEFAULT_ATTRIBUTE);
					}
					chargeRate.setDefault(Boolean.parseBoolean(isDefault));

					OMElement attributes = rate
							.getFirstChildWithName(HostObjectConstants.RATE_ATTRIBUTES_ELEMENT);
					if (attributes != null) {
						Map<String, String> attributesMap = new HashMap<String, String>();
						for (Iterator childElements = attributes
								.getChildElements(); childElements.hasNext();) {
							OMElement attrElem = (OMElement) childElements
									.next();
							String attrName = attrElem.getLocalName();
							String attrValue = attrElem.getText();
							attributesMap.put(attrName, attrValue);
						}
						chargeRate.setRateAttributes(attributesMap);
					}

					OMElement categories = rate
							.getFirstChildWithName(HostObjectConstants.RATE_CATEGORY_ELEMENT);
					String categoryName = categories
							.getAttributeValue(HostObjectConstants.NAME_ATTRIBUTE);
					String categoryRate = categories
							.getAttributeValue(HostObjectConstants.TIER_RATE_ELEMENT);

					CategoryEntity categoryEntity = new CategoryEntity();
					categoryEntity.setName(categoryName);
					categoryEntity.setRate(categoryRate);
					if (categories != null) {
						Map<String, BigDecimal> categoriesMap = new HashMap<String, BigDecimal>();
						for (Iterator childElements = categories
								.getChildElements(); childElements.hasNext();) {
							OMElement subCatElem = (OMElement) childElements
									.next();
							String subCategoryName = subCatElem
									.getAttributeValue(HostObjectConstants.NAME_ATTRIBUTE);
							BigDecimal subCategoryRate = BigDecimal
									.valueOf(Integer.valueOf(subCatElem
											.getAttributeValue(HostObjectConstants.TIER_RATE_ELEMENT)));
							categoriesMap.put(subCategoryName, subCategoryRate);
						}
						categoryEntity.setSubCategoryMap(categoriesMap);
					}

					OMElement taxesElement = rate
							.getFirstChildWithName(HostObjectConstants.RATE_TAXES_ELEMENT);
					if (taxesElement != null) {
						List<String> taxList = new ArrayList<String>();
						for (Iterator taxes = taxesElement
								.getChildrenWithName(HostObjectConstants.RATE_TAX_ELEMENT); taxes
								.hasNext();) {
							OMElement taxElem = (OMElement) taxes.next();
							taxList.add(taxElem.getText());
						}
						chargeRate.setTaxList(taxList);
					}

					rateList.add(chargeRate);
				}
			}
		} catch (RegistryException e) {
			String msg = "Error while retrieving rate card from registry";
			log.error(msg, e);
			throw new APIManagementException(msg, e);
		} catch (XMLStreamException e) {
			String msg = "Malformed XML found in the rate card resource";
			log.error(msg, e);
			throw new APIManagementException(msg, e);
		} catch (JaxenException e) {
			String msg = "Malformed XML found in the rate card resource";
			log.error(msg, e);
			throw new APIManagementException(msg, e);
		}
		return rateList;
	}

	/**
	 * Gets the total api traffic for line chart.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param subscriber the subscriber
	 * @param operator the operator
	 * @param applicationId the application id
	 * @param api the api
	 * @return the total api traffic for line chart
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<String[]> getTotalAPITrafficForLineChart(
			String fromDate, String toDate, String subscriber, String operator,
			int applicationId, String api) throws SQLException,
			APIMgtUsageQueryServiceClientException, APIManagementException {

		List<String[]> api_request = BillingDAO
				.getTotalAPITrafficForLineChart(fromDate, toDate, subscriber,
						operator, applicationId, api);
		return api_request;
	}

	/**
	 * Gets the total api response time for line chart.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param subscriber the subscriber
	 * @param operator the operator
	 * @param timeRange the time range
	 * @return the total api response time for line chart
	 * @throws SQLException the SQL exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 * @throws APIManagementException the API management exception
	 */
	public static List<APIResponseDTO> getTotalAPIResponseTimeForLineChart(
			String fromDate, String toDate, String subscriber, String operator,
			String timeRange) throws SQLException,
			APIMgtUsageQueryServiceClientException, APIManagementException {

		List<APIResponseDTO> apiResponse = BillingDAO
				.getAllResponseTimesForAllAPIs(operator, subscriber, fromDate,
						toDate, timeRange);
		return apiResponse;
	}

	/**
	 * Gets the all response times by date.
	 *
	 * @param opName the op name
	 * @param username the username
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the all response times by date
	 * @throws APIManagementException the API management exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 */
	public static Map<String, List<APIResponseDTO>> getAllResponseTimesByDate(
			String opName, String username, String fromDate, String toDate)
			throws APIManagementException,
			APIMgtUsageQueryServiceClientException {

		Map<String, List<APIResponseDTO>> responseTimes = new HashMap<String, List<APIResponseDTO>>();
		List<API> allAPIs = APIManagerFactory.getInstance().getAPIConsumer()
				.getAllAPIs();
		for (API api : allAPIs) {
			String apiName = api.getId().getApiName();
			List<APIResponseDTO> responseDTOs = BillingDAO
					.getAllResponseTimesForAPIbyDate(opName, username,
							fromDate, toDate, apiName);
			responseTimes.put(apiName, responseDTOs);
		}
		return responseTimes;
	}

	/**
	 * Gets the time consumption for all ap is.
	 *
	 * @param opName the op name
	 * @param username the username
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the time consumption for all ap is
	 * @throws APIManagementException the API management exception
	 * @throws APIMgtUsageQueryServiceClientException the API mgt usage query service client exception
	 */
	public static Map<String, String[]> getTimeConsumptionForAllAPIs(
			String opName, String username, String fromDate, String toDate)
			throws APIManagementException,
			APIMgtUsageQueryServiceClientException {

		Map<String, String[]> responseTimes = new HashMap<String, String[]>();
		List<API> allAPIs = APIManagerFactory.getInstance().getAPIConsumer()
				.getAllAPIs();
		for (API api : allAPIs) {
			String apiName = api.getId().getApiName();
			String[] responseData = BillingDAO
					.getTimeConsumptionByAPI(opName, username, fromDate,
							toDate, apiName);
			responseTimes.put(apiName, responseData);
		}
		return responseTimes;
	}

	/**
	 * Gets the rate card north bound.
	 *
	 * @return the rate card north bound
	 * @throws APIManagementException the API management exception
	 */
	public static Map<RateKey, ChargeRate> getRateCardNorthBound()
			throws APIManagementException {
		return null;
	}

	/**
	 * Calculate tiers charges.
	 *
	 * @param selectedUsageTier the selected usage tier
	 * @param rateCard the rate card
	 * @param totalRequestCount the total request count
	 * @param tierCount the tier count
	 * @param operatorSubscription the operator subscription
	 * @param subsYear the subs year
	 * @param subsMonth the subs month
	 * @param application the application
	 * @param ApiName the api name
	 * @param apiVersion the api version
	 * @param categoryEntry the category entry
	 * @param appId the app id
	 * @param apiId the api id
	 * @param subsId the subs id
	 * @throws Exception 
	 */
	private static void calculateTiersCharges(
			List<UsageTiers> selectedUsageTier,
			Map<RateKey, ChargeRate> rateCard, int totalRequestCount,
			int tierCount,
			BillingSubscription.OperatorSubscription operatorSubscription,
			String subsYear, String subsMonth, Application application,
			String ApiName, String apiVersion,
			Map.Entry<CategoryCharge, BilledCharge> categoryEntry, int appId,
			int apiId, String subsId) throws Exception {

		BigDecimal totCharge = new BigDecimal(0);
		int operationId = operatorSubscription.getOperationId();
		int min, max = 0;

		int chargedCount = 0;
		BilledCharge CategoryCharge = categoryEntry.getValue();

		for (UsageTiers usageTiers : selectedUsageTier) {

			min = Integer.parseInt(usageTiers.getMin());
			max = Integer.parseInt(usageTiers.getMax());
			String tierName = usageTiers.getTierName();
			String rateId = usageTiers.getRateId();
			BilledCharge tempBilledCharge = null;
			String logTier = tierName + ":" + rateId;

			if (chargedCount >= totalRequestCount) {
				break;
			}
			if (min <= totalRequestCount && totalRequestCount <= max) {
				tempBilledCharge = new BilledCharge(totalRequestCount
						- chargedCount);
			} else if (totalRequestCount > max) {
				tempBilledCharge = new BilledCharge(max - chargedCount);
			}

			ChargeRate subRate = rateCard.get(new RateKey(operatorSubscription
					.getOperator(), ApiName, rateId, String
					.valueOf(operationId)));
			if (subRate == null) {
				throw new APIManagementException(
						"Attributes required for Charging are not specified in rate-card.xml");
			}
			categoryEntry.setValue(tempBilledCharge);
			billComponent(subRate, rateCard, operatorSubscription, subsYear,
					subsMonth, application, ApiName, apiVersion, categoryEntry,
					appId, apiId, subsId);
			chargedCount = chargedCount + categoryEntry.getValue().getCount();

			// accumilate chages
			CategoryCharge.addPrice(categoryEntry.getValue().getPrice());
			CategoryCharge.addTax(categoryEntry.getValue().getTax());

			// accumilate multitier chargers

			if (CategoryCharge.getTierCharges().containsKey(logTier)) {

				CategoryCharge.getTierCharges().get(logTier)
						.addPrice(categoryEntry.getValue().getPrice());
				CategoryCharge.getTierCharges().get(logTier)
						.addTax(categoryEntry.getValue().getTax());
				CategoryCharge.getTierCharges().get(logTier)
						.addCount(categoryEntry.getValue().getCount());
			} else {
				BilledCharge billedCharge = new BilledCharge(categoryEntry
						.getValue().getCount());
				billedCharge.addPrice(categoryEntry.getValue().getPrice());
				billedCharge.addTax(categoryEntry.getValue().getTax());
				CategoryCharge.getTierCharges().put(logTier, billedCharge);
			}
		}

		categoryEntry.setValue(CategoryCharge);

	}

	/**
	 * Nvl default.
	 *
	 * @param val the val
	 * @return the string
	 */
	private static String nvlDefault(String val) {
		return (val == null || val.isEmpty()) ? DISPLAY_DEFAULT : val;
	}
}
