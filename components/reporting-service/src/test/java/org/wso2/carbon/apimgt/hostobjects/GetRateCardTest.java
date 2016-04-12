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
package org.wso2.carbon.apimgt.hostobjects;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.wso2.carbon.apimgt.impl.APIConstants;

public class GetRateCardTest {

    @SuppressWarnings("rawtypes")
    public static void main(String[] args) throws IOException {
        Map<RateKey, ChargeRate> ratecard = new HashMap<RateKey, ChargeRate>();
        String content = new String(Files.readAllBytes(Paths.get("D:/RateCardNewxml.xml")));
        try {
            OMElement element = AXIOMUtil.stringToOM(content);
            //===========================================================================================================================================================

            OMElement opsElement = element.getFirstChildWithName(HostObjectConstants.OPERATORS_ELEMENT);
            Iterator operators = opsElement.getChildrenWithName(HostObjectConstants.OPERATOR_ELEMENT);

            while (operators.hasNext()) {
                OMElement operator = (OMElement) operators.next();
                String operatorName = operator.getAttributeValue(HostObjectConstants.NAME_ATTRIBUTE);

                Iterator apis = operator.getChildrenWithName(HostObjectConstants.API_ELEMENT);
                while (apis.hasNext()) {
                    OMElement api = (OMElement) apis.next();
                    String apiName = api.getAttributeValue(HostObjectConstants.NAME_ATTRIBUTE);

                    //PRIYANKA_06608
                    Iterator operations = api.getChildrenWithName(HostObjectConstants.OPERATION_ELEMENT);
                    while (operations.hasNext()) {
                        OMElement operation = (OMElement) operations.next();
                        String operationName = operation.getAttributeValue(HostObjectConstants.NAME_ATTRIBUTE);

                        Iterator rates = operation.getChildrenWithName(HostObjectConstants.RATE_ELEMENT);
                        while (rates.hasNext()) {
                            OMElement rate = (OMElement) rates.next();
                            OMElement name = rate.getFirstChildWithName(HostObjectConstants.RATE_NAME_ELEMENT);
                            OMElement currency = rate.getFirstChildWithName(HostObjectConstants.RATE_CURRENCY_ELEMENT);
                            OMElement value = rate.getFirstChildWithName(HostObjectConstants.RATE_VALUE_ELEMENT);
                            OMElement type = rate.getFirstChildWithName(HostObjectConstants.RATE_TYPE_ELEMENT);

                            ChargeRate chargeRate = new ChargeRate(name.getText());//<Name>
                            chargeRate.setCurrency(currency.getText());//<Currency>
                            chargeRate.setValue(new BigDecimal(value.getText()));//<Value>
                            chargeRate.setType(RateType.getEnum(type.getText()));//<Type>
                            String isDefault = null;
                            if (rate.getAttribute(HostObjectConstants.RATE_DEFAULT_ATTRIBUTE) != null) {
                                isDefault = rate.getAttributeValue(HostObjectConstants.RATE_DEFAULT_ATTRIBUTE);
                            }
                            chargeRate.setDefault(Boolean.parseBoolean(isDefault));//default attr


                            Iterator categoryIter = rate.getChildrenWithName(HostObjectConstants.RATE_CATEGORY_ELEMENT);

                            Map<String, Object> categoryEntityMap = new HashMap<String, Object>();

                            while (categoryIter.hasNext()) {
                                OMElement category = (OMElement) categoryIter.next();


                                OMElement categoryBased = rate.getFirstChildWithName(HostObjectConstants.RATE_CATEGORY_BASE_ELEMENT);
                                OMElement surchargeElement = rate.getFirstChildWithName(HostObjectConstants.RATE_SURCHARGE_ELEMENT);
                                if (surchargeElement != null) {
                                    SurchargeEntity surchargeEntity = new SurchargeEntity();
                                    String surchargeElementValue = surchargeElement.getAttributeValue(HostObjectConstants.TIER_VALUE_ELEMENT);
                                    surchargeEntity.setSurchargeElementValue(surchargeElementValue);
                                    String surchargeElementAds = surchargeElement.getAttributeValue(HostObjectConstants.RATE_ADS_COMMISSION);
                                    surchargeEntity.setSurchargeElementAds(surchargeElementAds);
                                    String surchargeElementOpco = surchargeElement.getAttributeValue(HostObjectConstants.RATE_OPCO_COMMISSION);
                                    surchargeEntity.setSurchargeElementOpco(surchargeElementOpco);
                                    chargeRate.setSurchargeEntity(surchargeEntity);//<Surcharge>
                                }

                                boolean categoryBasedVal = false;
                                if (categoryBased != null) {
                                    categoryBasedVal = Boolean.valueOf(categoryBased.getText());
                                }
                                chargeRate.setCategoryBasedVal(categoryBasedVal);//<CategorBase>
                                OMElement commission = rate.getFirstChildWithName(HostObjectConstants.RATE_COMMISSION);
                                if (commission != null) {
                                    OMElement spPercentageElem = commission.getFirstChildWithName(HostObjectConstants.RATE_SP_COMMISSION);
                                    RateCommission cc = new RateCommission();
                                    Double spPercentage = 0.0;
                                    if (spPercentageElem != null) {
                                        spPercentage = Double.valueOf(spPercentageElem.getText());
                                        cc.setSpCommission(spPercentage);
                                    }
                                    OMElement adsPercentageElem = commission.getFirstChildWithName(HostObjectConstants.RATE_ADS_COMMISSION);
                                    Double adsPercentage = 0.0;
                                    if (adsPercentageElem != null) {
                                        adsPercentage = Double.valueOf(adsPercentageElem.getText());
                                        cc.setAdsCommission(adsPercentage);
                                    }
                                    OMElement opcoPercentageElem = commission.getFirstChildWithName(HostObjectConstants.RATE_OPCO_COMMISSION);
                                    Double opcoPercentage = 0.0;
                                    if (opcoPercentageElem != null) {
                                        opcoPercentage = Double.valueOf(opcoPercentageElem.getText());
                                        cc.setOpcoCommission(opcoPercentage);
                                    }
                                    chargeRate.setCommission(cc);//<Commission>
                                }

                                OMElement usageTiers = rate.getFirstChildWithName(HostObjectConstants.RATE_USAGE_TIERS_ELEMENT);
                                if (usageTiers != null) {
                                    Iterator tiers = usageTiers.getChildrenWithName(HostObjectConstants.RATE_USAGE_TIER_ELEMENT);
                                    List<UsageTiers> tierEntities = new ArrayList<UsageTiers>();
                                    while (tiers.hasNext()) {
                                        OMElement tier = (OMElement) tiers.next();
                                        UsageTiers usageTiersEntity = new UsageTiers();
                                        String tierRate = tier.getAttributeValue(HostObjectConstants.TIER_RATE_ELEMENT);
                                        usageTiersEntity.setRateId(tierRate);
                                        OMElement minElement = tier.getFirstChildWithName(HostObjectConstants.RATE_RANGE_MIN);
                                        OMElement maxElement = tier.getFirstChildWithName(HostObjectConstants.RATE_RANGE_MAX);
                                        String tierMinValue = minElement.getAttributeValue(HostObjectConstants.TIER_VALUE_ELEMENT);
                                        String tierMaxValue = maxElement.getAttributeValue(HostObjectConstants.TIER_VALUE_ELEMENT);
                                        usageTiersEntity.setMin(tierMinValue);
                                        usageTiersEntity.setMax(tierMaxValue);
                                        tierEntities.add(usageTiersEntity);
                                    }
                                    chargeRate.setUsageTiers(tierEntities);
                                }


                                if (category != null) {

                                    String categoryName = category.getAttributeValue(HostObjectConstants.NAME_ATTRIBUTE);
                                    String categoryRate = category.getAttributeValue(HostObjectConstants.TIER_RATE_ELEMENT);


                                    Map<String, Object> categoryMap = new HashMap<String, Object>();
                                    categoryMap.put("__default__", categoryRate);
                                    categoryEntityMap.put(categoryName, categoryMap);

                                    OMElement attributes = category.getFirstChildWithName(HostObjectConstants.RATE_ATTRIBUTES_ELEMENT);
                                    if (attributes != null) {
                                        Map<String, Object> categoryAttrMap = new HashMap<String, Object>();
                                        Map<String, String> attributesMap = new HashMap<String, String>();
                                        for (Iterator childElements = attributes.getChildElements(); childElements.hasNext();) {
                                            OMElement attrElem = (OMElement) childElements.next();
                                            String attrName = attrElem.getLocalName();
                                            String attrValue = attrElem.getText();
                                            attributesMap.put(attrName, attrValue);
                                        }
                                        //chargeRate.setRateAttributes(attributesMap);//<Attributes>
                                        categoryAttrMap.put("__default__", attributesMap);
                                        categoryEntityMap.put(categoryName, categoryAttrMap);
                                    }
                                    //CATEGORY USAGE TIERS
                                    OMElement categoryUsageTiers = category.getFirstChildWithName(HostObjectConstants.RATE_USAGE_TIERS_ELEMENT);
                                    if (categoryUsageTiers != null) {
                                        Iterator tiers = categoryUsageTiers.getChildrenWithName(HostObjectConstants.RATE_USAGE_TIER_ELEMENT);
                                        Map<String, Object> usageTiersMap = new HashMap<String, Object>();
                                        List<UsageTiers> tierEntities = new ArrayList<UsageTiers>();
                                        while (tiers.hasNext()) {
                                            OMElement tier = (OMElement) tiers.next();
                                            UsageTiers usageTiersEntity = new UsageTiers();
                                            String tierRate = tier.getAttributeValue(HostObjectConstants.TIER_RATE_ELEMENT);
                                            usageTiersEntity.setRateId(tierRate);
                                            OMElement minElement = tier.getFirstChildWithName(HostObjectConstants.RATE_RANGE_MIN);
                                            OMElement maxElement = tier.getFirstChildWithName(HostObjectConstants.RATE_RANGE_MAX);
                                            String tierMinValue = minElement.getAttributeValue(HostObjectConstants.TIER_VALUE_ELEMENT);
                                            String tierMaxValue = maxElement.getAttributeValue(HostObjectConstants.TIER_VALUE_ELEMENT);
                                            usageTiersEntity.setMin(tierMinValue);
                                            usageTiersEntity.setMax(tierMaxValue);
                                            tierEntities.add(usageTiersEntity);
                                        }
                                        usageTiersMap.put("__default__", tierEntities);
                                        categoryEntityMap.put(categoryName, usageTiersMap);
                                    }

                                    OMElement categoryCommission = category.getFirstChildWithName(HostObjectConstants.RATE_COMMISSION);
                                    CategoryCommission cc = new CategoryCommission();
                                    if (categoryCommission != null) {
                                        Map<String, Object> catCommissionMap = new HashMap<String, Object>();

                                        OMElement spPercentageElem = categoryCommission.getFirstChildWithName(HostObjectConstants.RATE_SP_COMMISSION);
                                        Double spPercentage = 0.0;
                                        if (spPercentageElem != null) {
                                            spPercentage = Double.valueOf(spPercentageElem.getText());
                                            cc.setSpCommission(spPercentage);
                                        }
                                        OMElement adsPercentageElem = categoryCommission.getFirstChildWithName(HostObjectConstants.RATE_ADS_COMMISSION);
                                        Double adsPercentage = 0.0;
                                        if (adsPercentageElem != null) {
                                            adsPercentage = Double.valueOf(adsPercentageElem.getText());
                                            cc.setAdsCommission(adsPercentage);
                                        }
                                        OMElement opcoPercentageElem = categoryCommission.getFirstChildWithName(HostObjectConstants.RATE_OPCO_COMMISSION);
                                        Double opcoPercentage = 0.0;
                                        if (opcoPercentageElem != null) {
                                            opcoPercentage = Double.valueOf(opcoPercentageElem.getText());
                                            cc.setOpcoCommission(opcoPercentage);
                                        }

                                        //categoryEntity.setCategoryCommission(cc);
                                        catCommissionMap.put("__default__", cc);
                                        categoryEntityMap.put(categoryName, catCommissionMap);
                                    }

                                    //========================SUB CATEGORY====================================
                                    Iterator subCategories = category.getChildrenWithName(HostObjectConstants.RATE_SUB_CATEGORY_ELEMENT);
                                    Map<String, Object> subCategoryMap = new HashMap<String, Object>();
                                    while (subCategories.hasNext()) {//subcategory iteration
                                        OMElement subCategory = (OMElement) subCategories.next();
                                        String subCategoryname = subCategory.getAttributeValue(HostObjectConstants.NAME_ATTRIBUTE);
                                        String subCategoryrate = subCategory.getAttributeValue(HostObjectConstants.TIER_RATE_ELEMENT);


                                        SubCategory subCategoryEntity = new SubCategory();
                                        subCategoryEntity.setName(subCategoryname);
                                        if (subCategoryrate != null) {
                                            subCategoryEntity.setRate(Double.valueOf(subCategoryrate));
                                        }
                                        subCategoryMap.put(subCategoryname, subCategoryEntity);
                                        categoryEntityMap.put(categoryName, subCategoryMap);

                                        Map<String, Object> subCategoryAttrMap = new HashMap<String, Object>();
                                        Map<String, BigDecimal> subCategoriesMap = new HashMap<String, BigDecimal>();
                                        OMElement subAttributes = category.getFirstChildWithName(HostObjectConstants.RATE_ATTRIBUTES_ELEMENT);
                                        if (subAttributes != null) {
                                            for (Iterator childElements = subAttributes.getChildElements(); childElements.hasNext();) {
                                                OMElement attrElem = (OMElement) childElements.next();
                                                String attrName = attrElem.getLocalName();
                                                BigDecimal attrValue = BigDecimal.valueOf(Long.valueOf(attrElem.getText()));
                                                subCategoriesMap.put(attrName, attrValue);
                                            }
                                            subCategoryAttrMap.put(subCategoryname, subCategoriesMap);
                                            categoryEntityMap.put(categoryName, subCategoryAttrMap);
                                        }



                                        //CATEGORY USAGE TIERS
                                        OMElement subCategoryUsageTiers = subCategory.getFirstChildWithName(HostObjectConstants.RATE_USAGE_TIERS_ELEMENT);
                                        if (subCategoryUsageTiers != null) {
                                            Iterator tiers = subCategoryUsageTiers.getChildrenWithName(HostObjectConstants.RATE_USAGE_TIER_ELEMENT);
                                            Map<String, Object> subCategoryTiersMap = new HashMap<String, Object>();
                                            List<UsageTiers> tierEntities = new ArrayList<UsageTiers>();
                                            while (tiers.hasNext()) {
                                                OMElement tier = (OMElement) tiers.next();
                                                UsageTiers usageTiersEntity = new UsageTiers();
                                                String tierRate = tier.getAttributeValue(HostObjectConstants.TIER_RATE_ELEMENT);
                                                usageTiersEntity.setRateId(tierRate);
                                                OMElement minElement = tier.getFirstChildWithName(HostObjectConstants.RATE_RANGE_MIN);
                                                OMElement maxElement = tier.getFirstChildWithName(HostObjectConstants.RATE_RANGE_MAX);
                                                String tierMinValue = minElement.getAttributeValue(HostObjectConstants.TIER_VALUE_ELEMENT);
                                                String tierMaxValue = maxElement.getAttributeValue(HostObjectConstants.TIER_VALUE_ELEMENT);
                                                usageTiersEntity.setMin(tierMinValue);
                                                usageTiersEntity.setMax(tierMaxValue);
                                                tierEntities.add(usageTiersEntity);
                                            }
                                            subCategoryTiersMap.put(subCategoryname, tierEntities);
                                            categoryEntityMap.put(categoryName, subCategoryTiersMap);
                                        }

                                        OMElement subCategoryCommission = subCategory.getFirstChildWithName(HostObjectConstants.RATE_COMMISSION);
                                        Map<String, Object> subCategoryCommMap = new HashMap<String, Object>();
                                        SubCategoryCommission subCC = new SubCategoryCommission();
                                        if (subCategoryCommission != null) {
                                            OMElement spPercentageElem = subCategoryCommission.getFirstChildWithName(HostObjectConstants.RATE_SP_COMMISSION);
                                            Double spPercentage = 0.0;
                                            if (spPercentageElem != null) {
                                                spPercentage = Double.valueOf(spPercentageElem.getText());
                                                subCC.setSpCommission(spPercentage);
                                            }
                                            OMElement adsPercentageElem = subCategoryCommission.getFirstChildWithName(HostObjectConstants.RATE_ADS_COMMISSION);
                                            Double adsPercentage = 0.0;
                                            if (adsPercentageElem != null) {
                                                adsPercentage = Double.valueOf(adsPercentageElem.getText());
                                                subCC.setAdsCommission(adsPercentage);
                                            }
                                            OMElement opcoPercentageElem = subCategoryCommission.getFirstChildWithName(HostObjectConstants.RATE_OPCO_COMMISSION);
                                            Double opcoPercentage = 0.0;
                                            if (opcoPercentageElem != null) {
                                                opcoPercentage = Double.valueOf(opcoPercentageElem.getText());
                                                subCC.setOpcoCommission(opcoPercentage);
                                            }

                                            subCategoryCommMap.put(subCategoryname, subCC);
                                            categoryEntityMap.put(categoryName, subCategoryCommMap);
                                        }
                                    }
                                    //========================SUB CATEGORY====================================							

                                    //chargeRate.setCategoryEntityList(categoryEntityList);

                                    chargeRate.setCategories(categoryEntityMap);
                                }
                            }

                            OMElement taxes = rate.getFirstChildWithName(HostObjectConstants.RATE_TAXES_ELEMENT);
                            if (taxes != null) {
                                Iterator tax = taxes.getChildrenWithName(HostObjectConstants.RATE_TAX_ELEMENT);
                                List<String> taxList = new ArrayList<String>();
                                while (tax.hasNext()) {
                                    OMElement taxElement = (OMElement) tax.next();
                                    taxList.add(taxElement.getText());
                                }
                                chargeRate.setTaxList(taxList);
                            }
                            RateKey rateKey = new RateKey(operatorName, apiName, name.getText(), operationName);
                            ratecard.put(rateKey, chargeRate);
                        }
                    }
                }
            }

            //===========================================================================================================================================================


        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }
}

 
class RateKey {

    private final String operator;
    private final String apiName;
    private final String rateName;
    private final String operationName;

     
    public RateKey(String operator, String apiName, String rateName, String operationName) {
        this.operator = operator;
        this.apiName = apiName;
        this.rateName = rateName;
        this.operationName = operationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RateKey rateKey = (RateKey) o;

        if (apiName != null ? !apiName.equalsIgnoreCase(rateKey.apiName) : rateKey.apiName != null) {
            return false;
        }
        if (operator != null ? !operator.equalsIgnoreCase(rateKey.operator) : rateKey.operator != null) {
            return false;
        }
        if (rateName != null ? !rateName.equalsIgnoreCase(rateKey.rateName) : rateKey.rateName != null) {
            return false;
        }
        if (operationName != null ? !operationName.equalsIgnoreCase(rateKey.operationName) : rateKey.operationName != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = operator != null ? operator.toLowerCase(Locale.ENGLISH).hashCode() : 0;
        result = 31 * result + (apiName != null ? apiName.toLowerCase(Locale.ENGLISH).hashCode() : 0);
        result = 31 * result + (rateName != null ? rateName.toLowerCase(Locale.ENGLISH).hashCode() : 0);
        result = 31 * result + (operationName != null ? operationName.toLowerCase(Locale.ENGLISH).hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RateKey{"
                + "operator='" + operator + '\''
                + ", apiName='" + apiName + '\''
                + ", rateName='" + rateName + '\''
                + ", operationName='" + operationName + '\''
                + '}';
    }
}

class ChargeRate {

    private String name;
    private String currency;
    private BigDecimal value;
    private RateType type;
    private Map<String, Object> categories;
    private List<String> taxList;
    private boolean isDefault = false;
    private Map<String, String> rateAttributes;
    private List<RateRange> rateRanges;
    private RateCommission commission;
    private Boolean categoryBasedVal;
    private List<UsageTiers> usageTiers;
    private RefundEntity RefundList;
    private SurchargeEntity surchargeEntity;
    private List<CategoryEntity> categoryEntityList;

    public SurchargeEntity getSurchargeEntity() {
        return surchargeEntity;
    }

    public void setSurchargeEntity(SurchargeEntity surchargeEntity) {
        this.surchargeEntity = surchargeEntity;
    }

    public Boolean getCategoryBasedVal() {
        return categoryBasedVal;
    }

    public void setCategoryBasedVal(Boolean categoryBasedVal) {
        this.categoryBasedVal = categoryBasedVal;
    }

    public List<UsageTiers> getUsageTiers() {
        return usageTiers;
    }

    public RateCommission getCommission() {
        return commission;
    }

    public void setCommission(RateCommission commission) {
        this.commission = commission;
    }

    public RefundEntity getRefundList() {
        return RefundList;
    }

    public void setRefundList(RefundEntity refundList) {
        RefundList = refundList;
    }

    public void setUsageTiers(List<UsageTiers> tiersEntities) {
        this.usageTiers = tiersEntities;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChargeRate(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public RateType getType() {
        return type;
    }

    public void setType(RateType type) {
        this.type = type;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Map<String, String> getRateAttributes() {
        return rateAttributes;
    }

    public void setRateAttributes(Map<String, String> rateAttributes) {
        this.rateAttributes = rateAttributes;
    }

    public List<RateRange> getRateRanges() {
        return rateRanges;
    }

    public void setRateRanges(List<RateRange> rateRanges) {
        this.rateRanges = rateRanges;
    }

    public List<String> getTaxList() {
        return taxList;
    }

    public void setTaxList(List<String> taxList) {
        this.taxList = taxList;
    }

    public Map<String, Object> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Object> categoryEntityMap) {
        this.categories = categoryEntityMap;
    }

    @Override
    public String toString() {
        return "ChargeRate{"
                + "name='" + name + '\''
                + ", currency='" + currency + '\''
                + ", value=" + value
                + ", type=" + type
                + ", isDefault=" + isDefault
                + ", rateAttributes=" + rateAttributes
                + ", taxList=" + taxList
                + '}';
    }

    public List<CategoryEntity> getCategoryEntityList() {
        return categoryEntityList;
    }

    public void setCategoryEntityList(List<CategoryEntity> categoryEntityList) {
        this.categoryEntityList = categoryEntityList;
    }
}

class HostObjectConstants {

    public static final String RATE_CARD_LOCATION = APIConstants.API_APPLICATION_DATA_LOCATION + "/rate-card.xml";
    public static final QName OPERATORS_ELEMENT = new QName("Operators");
    public static final QName OPERATOR_ELEMENT = new QName("Operator");
    public static final QName OPERATION_ELEMENT = new QName("Operation");
    public static final QName API_ELEMENT = new QName("API");
    public static final QName NAME_ATTRIBUTE = new QName("name");
    public static final QName RATE_ELEMENT = new QName("Rate");
    public static final QName TIER_RATE_ELEMENT = new QName("rate");
    public static final QName RATE_NAME_ELEMENT = new QName("Name");
    public static final QName RATE_CURRENCY_ELEMENT = new QName("Currency");
    public static final QName RATE_VALUE_ELEMENT = new QName("Value");
    public static final QName TIER_VALUE_ELEMENT = new QName("value");
    public static final QName RATE_TYPE_ELEMENT = new QName("Type");
    public static final QName RATE_DEFAULT_ATTRIBUTE = new QName("default");
    public static final QName RATE_ATTRIBUTES_ELEMENT = new QName("Attributes");
    public static final QName RATE_USAGE_TIERS_ELEMENT = new QName("UsageTiers");
    public static final QName RATE_USAGE_TIER_ELEMENT = new QName("Tier");
    public static final QName RATE_RANGE_MIN = new QName("Min");
    public static final QName RATE_RANGE_MAX = new QName("Max");
    public static final QName RATE_SURCHARGE_ELEMENT = new QName("Surcharge");
    public static final QName RATE_ADS_ELEMENT = new QName("Ads");
    public static final QName RATE_RANGES_ELEMENT = new QName("Ranges");
    public static final QName RATE_RANGE_ELEMENT = new QName("Range");
    public static final QName RATE_CATEGORY_ELEMENT = new QName("Category");
    public static final QName RATE_CATEGORY_BASE_ELEMENT = new QName("CategorBase");
    public static final QName RATE_SUB_CATEGORY_ELEMENT = new QName("SubCategory");
    public static final QName RATE_TAXES_ELEMENT = new QName("Taxes");
    public static final QName RATE_TAX_ELEMENT = new QName("Tax");
    public static final QName RATE_REFUND_ELEMENT = new QName("Refund");
    public static final String SUBSCRIPTION_OPCO_RATES_TABLE = "subscription_rates";
    public static final String SB_RESPONSE_SUMMARY_TABLE = "SB_API_RESPONSE_SUMMARY";
    public static final QName RATE_COMMISSION = new QName("Commission");
    public static final QName RATE_SP_COMMISSION = new QName("Sp");
    public static final QName RATE_ADS_COMMISSION = new QName("Ads");
    public static final QName RATE_OPCO_COMMISSION = new QName("Opco");
    public static final QName RATE_ATTRIBUTE_MAX_COUNT = new QName("MaxCount");
    public static final QName RATE_ATTRIBUTE_EXCESS_RATE = new QName("ExcessRate");
    public static final QName RATE_RANGE_FROM = new QName("From");
    public static final QName RATE_RANGE_TO = new QName("To");
    public static final String DATE_LAST_MONTH = "last month";
    public static final String DATE_LAST_YEAR = "last year";
    public static final String DATE_LAST_WEEK = "last week";
    public static final String DATE_LAST_DAY = "last day";
    public static final String ALL_SUBSCRIBERS = "__ALL__";
    public static final String ALL_APIS = "__ALL__";
    public static final String ALL_OPERATORS = "__ALL__";
    public static final int ALL_APPLICATIONS = 0;
}

class UsageTiers {

    private String rateId;
    private String min;
    private String max;

    public String getRateId() {
        return rateId;
    }

    public void setRateId(String rateId) {
        this.rateId = rateId;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}

class RefundEntity {

    private String name;
    private List<String> refundList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRefundList() {
        return refundList;
    }

    public void setRefundList(List<String> refundList) {
        this.refundList = refundList;
    }
}

class RateRange {

    private BigDecimal from;
    BigDecimal to;
    BigDecimal value;

    public BigDecimal getFrom() {
        return from;
    }

    public void setFrom(BigDecimal from) {
        this.from = from;
    }

    public BigDecimal getTo() {
        return to;
    }

    public void setTo(BigDecimal to) {
        this.to = to;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}

class TierEntity {

    private String tierRate;
    private HashMap<String, Integer> tiers;

    public String getTierRate() {
        return tierRate;
    }

    public void setTierRate(String tierRate) {
        this.tierRate = tierRate;
    }

    public HashMap<String, Integer> getTiers() {
        return tiers;
    }

    public void setTiers(HashMap<String, Integer> tiers) {
        this.tiers = tiers;
    }
}

//===========================COMMISSION==================================
class RateCommission {

    private Double spCommission;
    private Double adsCommission;
    private Double opcoCommission;

    public Double getSpCommission() {
        return spCommission;
    }

    public void setSpCommission(Double spCommission) {
        this.spCommission = spCommission;
    }

    public Double getAdsCommission() {
        return adsCommission;
    }

    public void setAdsCommission(Double adsCommission) {
        this.adsCommission = adsCommission;
    }

    public Double getOpcoCommission() {
        return opcoCommission;
    }

    public void setOpcoCommission(Double opcoCommission) {
        this.opcoCommission = opcoCommission;
    }
}

class CategoryCommission {

    private Double spCommission;
    private Double adsCommission;
    private Double opcoCommission;

    public Double getSpCommission() {
        return spCommission;
    }

    public void setSpCommission(Double spCommission) {
        this.spCommission = spCommission;
    }

    public Double getAdsCommission() {
        return adsCommission;
    }

    public void setAdsCommission(Double adsCommission) {
        this.adsCommission = adsCommission;
    }

    public Double getOpcoCommission() {
        return opcoCommission;
    }

    public void setOpcoCommission(Double opcoCommission) {
        this.opcoCommission = opcoCommission;
    }
}

class SubCategoryCommission {

    private Double spCommission;
    private Double adsCommission;
    private Double opcoCommission;

    public Double getSpCommission() {
        return spCommission;
    }

    public void setSpCommission(Double spCommission) {
        this.spCommission = spCommission;
    }

    public Double getAdsCommission() {
        return adsCommission;
    }

    public void setAdsCommission(Double adsCommission) {
        this.adsCommission = adsCommission;
    }

    public Double getOpcoCommission() {
        return opcoCommission;
    }

    public void setOpcoCommission(Double opcoCommission) {
        this.opcoCommission = opcoCommission;
    }
}

//===========================COMMISSION==================================
class SurchargeEntity {

    private String surchargeElementValue;
    private String surchargeElementAds;
    private String surchargeElementOpco;

    public String getSurchargeElementValue() {
        return surchargeElementValue;
    }

    public void setSurchargeElementValue(String surchargeElementValue) {
        this.surchargeElementValue = surchargeElementValue;
    }

    public String getSurchargeElementAds() {
        return surchargeElementAds;
    }

    public void setSurchargeElementAds(String surchargeElementAds) {
        this.surchargeElementAds = surchargeElementAds;
    }

    public String getSurchargeElementOpco() {
        return surchargeElementOpco;
    }

    public void setSurchargeElementOpco(String surchargeElementOpco) {
        this.surchargeElementOpco = surchargeElementOpco;
    }
}

//=============================================CATEGORY ====== SUB CATEGORY =====================
class CategoryEntity {

    private String name;
    private String rate;
    private List<SubCategory> subCategory;
    private List<UsageTiers> usageTiers;

    public List<UsageTiers> getUsageTiers() {
        return usageTiers;
    }

    public void setUsageTiers(List<UsageTiers> usageTiers) {
        this.usageTiers = usageTiers;
    }

    public List<SubCategory> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(List<SubCategory> subCategory) {
        this.subCategory = subCategory;
    }
    private CategoryCommission categoryCommission;

    public CategoryCommission getCategoryCommission() {
        return categoryCommission;
    }

    public void setCategoryCommission(CategoryCommission categoryCommission) {
        this.categoryCommission = categoryCommission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}

class SubCategory {

    private String name;
    private Double rate;
    private SubCategoryCommission subCategoryCommission;
    private List<UsageTiers> usageTiers;

    public List<UsageTiers> getUsageTiers() {
        return usageTiers;
    }

    public void setUsageTiers(List<UsageTiers> usageTiers) {
        this.usageTiers = usageTiers;
    }
    private HashMap<String, String> subCategoryMap;//Sub category Name & Rate

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public HashMap<String, String> getSubCategoryMap() {
        return subCategoryMap;
    }

    public void setSubCategoryMap(HashMap<String, String> subCategoryMap) {
        this.subCategoryMap = subCategoryMap;
    }

    public SubCategoryCommission getSubCategoryCommission() {
        return subCategoryCommission;
    }

    public void setSubCategoryCommission(SubCategoryCommission subCategoryCommission) {
        this.subCategoryCommission = subCategoryCommission;
    }
}

//=============================================CATEGORY ====== SUB CATEGORY =====================
enum RateType {

     
    CONSTANT("CONSTANT"),
     
    PERCENTAGE("PERCENTAGE"),
     
    PER_REQUEST("PER_REQUEST"),
     
    QUOTA("QUOTA"),
     
    SUBSCRIPTION("SUBSCRIPTION"),
     
    RANGE("RANGE"),
     
    MULTITIER("MULTITIER");
    private String name;

    RateType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static RateType getEnum(String name) {
        for (RateType r : RateType.values()) {
            if ((r.name).equalsIgnoreCase(name)) {
                return r;
            }
        }
        return null;
    }
}
