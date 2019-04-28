package com.mars.monkey.user.model.incoming;

/**
 * Created on 3/25/2019.
 */
public class IncomingDetail {
    private Double beforeTaxSalary;
//    private Double pensionInsurance;
//    private Double medicalInsurance;
//    private Double unemploymentInsurance;
//    private Double housingFoud;
//    private Double additionalHousingFoud;
    private Double individualIncomeTax;
    private Double afterTaxSalary;

    public Double getBeforeTaxSalary() {
        return beforeTaxSalary;
    }

    public void setBeforeTaxSalary(Double beforeTaxSalary) {
        this.beforeTaxSalary = beforeTaxSalary;
    }

//    public Double getPensionInsurance() {
    //        return pensionInsurance;
    //    }
    //
    //    public void setPensionInsurance(Double pensionInsurance) {
    //        this.pensionInsurance = pensionInsurance;
    //    }
    //
    //    public Double getMedicalInsurance() {
    //        return medicalInsurance;
    //    }
    //
    //    public void setMedicalInsurance(Double medicalInsurance) {
    //        this.medicalInsurance = medicalInsurance;
    //    }
    //
    //    public Double getUnemploymentInsurance() {
    //        return unemploymentInsurance;
    //    }
    //
    //    public void setUnemploymentInsurance(Double unemploymentInsurance) {
    //        this.unemploymentInsurance = unemploymentInsurance;
    //    }
    //
    //    public Double getHousingFoud() {
    //        return housingFoud;
    //    }
    //
    //    public void setHousingFoud(Double housingFoud) {
    //        this.housingFoud = housingFoud;
    //    }
    //
    //    public Double getAdditionalHousingFoud() {
    //        return additionalHousingFoud;
    //    }
    //
    //    public void setAdditionalHousingFoud(Double additionalHousingFoud) {
    //        this.additionalHousingFoud = additionalHousingFoud;
    //    }

    public Double getIndividualIncomeTax() {
        return individualIncomeTax;
    }

    public void setIndividualIncomeTax(Double individualIncomeTax) {
        this.individualIncomeTax = individualIncomeTax;
    }

    public Double getAfterTaxSalary() {
        return afterTaxSalary;
    }

    public void setAfterTaxSalary(Double afterTaxSalary) {
        this.afterTaxSalary = afterTaxSalary;
    }
}
