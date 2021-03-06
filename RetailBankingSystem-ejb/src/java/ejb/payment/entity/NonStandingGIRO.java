package ejb.payment.entity;

import java.io.Serializable;
import javax.persistence.Entity;

@Entity
public class NonStandingGIRO extends GIRO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String paymentAmt;
    private String paymentFrequency;
    private String nonStandingStatus;
    private boolean buttonRender;
    private String billingOrganizationName;
    private String billReference;

    public String getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(String paymentAmt) {
        this.paymentAmt = paymentAmt;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public String getNonStandingStatus() {
        return nonStandingStatus;
    }

    public void setNonStandingStatus(String nonStandingStatus) {
        this.nonStandingStatus = nonStandingStatus;
    }

    public boolean isButtonRender() {
        return buttonRender;
    }

    public void setButtonRender(boolean buttonRender) {
        this.buttonRender = buttonRender;
    }

    public String getBillingOrganizationName() {
        return billingOrganizationName;
    }

    public void setBillingOrganizationName(String billingOrganizationName) {
        this.billingOrganizationName = billingOrganizationName;
    }

    public String getBillReference() {
        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }
    
}
