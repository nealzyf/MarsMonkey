package com.mars.monkey.user.model.incoming;

/**
 * Created on 3/26/2019.
 */
public class Insurance {
    private Double pensionInsuranceRate;
    private Double medicalInsuranceRate;
    private Double unemploymentInsuranceRate;
    private Double housingFundRate;

    public Double getPensionInsuranceRate() {
        return pensionInsuranceRate;
    }

    public void setPensionInsuranceRate(Double pensionInsuranceRate) {
        this.pensionInsuranceRate = pensionInsuranceRate;
    }

    public Double getMedicalInsuranceRate() {
        return medicalInsuranceRate;
    }

    public void setMedicalInsuranceRate(Double medicalInsuranceRate) {
        this.medicalInsuranceRate = medicalInsuranceRate;
    }

    public Double getUnemploymentInsuranceRate() {
        return unemploymentInsuranceRate;
    }

    public void setUnemploymentInsuranceRate(Double unemploymentInsuranceRate) {
        this.unemploymentInsuranceRate = unemploymentInsuranceRate;
    }

    public Double getHousingFundRate() {
        return housingFundRate;
    }

    public void setHousingFundRate(Double housingFundRate) {
        this.housingFundRate = housingFundRate;
    }
}
