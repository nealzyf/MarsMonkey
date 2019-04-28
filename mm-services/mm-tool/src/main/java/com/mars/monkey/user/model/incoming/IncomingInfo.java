package com.mars.monkey.user.model.incoming;

/**
 * Created on 3/25/2019.
 */
public class IncomingInfo {

    private Double beforeTaxSalary;
    String location;
    Double socialFundBaseAmount;
    Double housingFundBaseAmount;
    Double additionalHousingFoudRate;

    public Double getBeforeTaxSalary() {
        return beforeTaxSalary;
    }

    public void setBeforeTaxSalary(Double beforeTaxSalary) {
        this.beforeTaxSalary = beforeTaxSalary;
    }

    public String getLocation() {
        return location;
    }

    public Double getSocialFundBaseAmount() {
        return socialFundBaseAmount;
    }

    public void setSocialFundBaseAmount(Double socialFundBaseAmount) {
        this.socialFundBaseAmount = socialFundBaseAmount;
    }

    public Double getHousingFundBaseAmount() {
        return housingFundBaseAmount;
    }

    public void setHousingFundBaseAmount(Double housingFundBaseAmount) {
        this.housingFundBaseAmount = housingFundBaseAmount;
    }

    public Double getAdditionalHousingFoudRate() {
        return additionalHousingFoudRate;
    }

    public void setAdditionalHousingFoudRate(Double additionalHousingFoudRate) {
        this.additionalHousingFoudRate = additionalHousingFoudRate;
    }
}
