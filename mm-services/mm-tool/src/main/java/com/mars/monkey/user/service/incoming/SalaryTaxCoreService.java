package com.mars.monkey.user.service.incoming;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mars.monkey.utils.io.IOUtil;
import com.mars.monkey.utils.transform.JsonTransformUtil;
import com.mars.monkey.user.model.incoming.IncomingDetail;
import com.mars.monkey.user.model.incoming.IncomingInfo;
import com.mars.monkey.user.model.incoming.Insurance;
import com.mars.monkey.user.model.incoming.Tax;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created on 3/25/2019.
 *
 * @author eyoufzh
 */
@Service
public class SalaryTaxCoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalaryTaxCoreService.class);
    private List<Tax> taxs;
    private Map<String, Insurance> insuranceRates;

    @PostConstruct
    private void loadconfig() {
        try {
            String tasJson = IOUtil.streamToString(this.getClass().getResourceAsStream("/incoming/tax.json"));

            taxs = JsonTransformUtil.readValue(tasJson, new TypeReference<List<Tax>>() {
            });

            String insurancJson = IOUtil.streamToString(this.getClass().getResourceAsStream("/incoming/insurance.json"));
            insuranceRates = JsonTransformUtil.readValue(insurancJson, new TypeReference<Map<String, Insurance>>() {
            });
        } catch (IOException e) {
            LOGGER.warn("Failed to close Closeable", e);
        }
    }

    private Tax getTax(Double amount) {
        for (Tax tax : taxs) {
            if (tax.getMin() < amount && amount <= tax.getMax()) {
                LOGGER.info("Tax: [ {} ~ {} ] : {}%  - {} ", tax.getMin(), tax.getMax(), tax.getTaxRate(), tax.getDeduction());
                return tax;
            }
        }
        LOGGER.warn("Failed to find the correct tax.");
        return null;
    }

    private Insurance getInsuranceRate(String location) {
        return insuranceRates.get(location);
    }

    private Double valueRate(Double in, Double rate) {
        BigDecimal inB = new BigDecimal(in);
        BigDecimal rateB = new BigDecimal(rate);
        return inB.multiply(rateB).divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public Map<String, Object> cal(IncomingInfo incomingInfo) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> socialFundDetail = calSocialFundDetail(incomingInfo);
        result.put("socialFundDetail", socialFundDetail);
        Double socialFundTotal = Double.valueOf(socialFundDetail.get("socialFundTotal").toString());
        result.put("integerIncomingDetail", calIncomingDetail(incomingInfo.getBeforeTaxSalary(), socialFundTotal));
        return result;

    }

    private Map<String, Object> calSocialFundDetail(IncomingInfo incomingInfo) {

        Map<String, Object> socialFundDetail = new HashMap<>();

        Insurance insuranceRate = getInsuranceRate(incomingInfo.getLocation());

        Double pensionInsurance = valueRate(incomingInfo.getSocialFundBaseAmount(), insuranceRate.getPensionInsuranceRate());
        Double medicalInsurance = valueRate(incomingInfo.getSocialFundBaseAmount(), insuranceRate.getMedicalInsuranceRate());
        Double unemploymentInsurance = valueRate(incomingInfo.getSocialFundBaseAmount(), insuranceRate.getUnemploymentInsuranceRate());
        Double housingFoud = valueRate(incomingInfo.getHousingFundBaseAmount(), insuranceRate.getHousingFundRate());
        Double additionalHousingFoud = valueRate(incomingInfo.getHousingFundBaseAmount(), incomingInfo.getAdditionalHousingFoudRate());

        Double socialFundTotal = pensionInsurance + medicalInsurance + unemploymentInsurance + housingFoud + additionalHousingFoud;
        socialFundDetail.put("pensionInsurance", pensionInsurance);
        socialFundDetail.put("medicalInsurance", medicalInsurance);
        socialFundDetail.put("unemploymentInsurance", unemploymentInsurance);
        socialFundDetail.put("housingFoud", housingFoud);
        socialFundDetail.put("additionalHousingFoud", additionalHousingFoud);
        socialFundDetail.put("socialFundTotal", socialFundTotal);

        return socialFundDetail;
    }

    private Map<Integer, IncomingDetail> calIncomingDetail(Double beforeTaxSalary, Double socialFund) {
        Double tIndividualIcomingTax = 0.00;
        Double taxBase = 5000.00;
        Map<Integer, IncomingDetail> integerIncomingDetailMap = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            Double tBaseAmount = (beforeTaxSalary - taxBase - socialFund) * month;
            Tax tax = getTax(tBaseAmount);
            Double currentTax = tBaseAmount * (tax.getTaxRate() / 100) - tax.getDeduction() - tIndividualIcomingTax;
            tIndividualIcomingTax += currentTax;
            IncomingDetail incomingDetail = new IncomingDetail();
            incomingDetail.setBeforeTaxSalary(beforeTaxSalary);
            incomingDetail.setIndividualIncomeTax(new BigDecimal(currentTax).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            incomingDetail.setAfterTaxSalary(new BigDecimal(beforeTaxSalary - currentTax - socialFund).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            integerIncomingDetailMap.put(month, incomingDetail);
        }
        return integerIncomingDetailMap;
    }

}
