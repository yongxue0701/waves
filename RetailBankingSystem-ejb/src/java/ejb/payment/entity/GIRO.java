package ejb.payment.entity;

import ejb.customer.entity.CustomerBasic;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class GIRO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long giroId;
    private String billingOrganization;
    private String billReference;
    private String paymentLimit;
    private String customerName;
    private String customerMobile;
    private String bankAccountNum;
    private String bankAccountNumWithType;
    private String giroStatus;

    @ManyToOne(cascade={CascadeType.PERSIST},fetch=FetchType.EAGER)
    private CustomerBasic customerBasic;
    
    public Long getGiroId() {
        return giroId;
    }

    public void setGiroId(Long giroId) {
        this.giroId = giroId;
    }

    public String getBillingOrganization() {
        return billingOrganization;
    }

    public void setBillingOrganization(String billingOrganization) {
        this.billingOrganization = billingOrganization;
    }

    public String getBillReference() {
        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }

    public String getPaymentLimit() {
        return paymentLimit;
    }

    public void setPaymentLimit(String paymentLimit) {
        this.paymentLimit = paymentLimit;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public CustomerBasic getCustomerBasic() {
        return customerBasic;
    }

    public void setCustomerBasic(CustomerBasic customerBasic) {
        this.customerBasic = customerBasic;
    }

    public String getGiroStatus() {
        return giroStatus;
    }

    public void setGiroStatus(String giroStatus) {
        this.giroStatus = giroStatus;
    }

    public String getBankAccountNumWithType() {
        return bankAccountNumWithType;
    }

    public void setBankAccountNumWithType(String bankAccountNumWithType) {
        this.bankAccountNumWithType = bankAccountNumWithType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (giroId != null ? giroId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GIRO)) {
            return false;
        }
        GIRO other = (GIRO) object;
        if ((this.giroId == null && other.giroId != null) || (this.giroId != null && !this.giroId.equals(other.giroId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.payment.entity.GIRO[ id=" + giroId + " ]";
    }
    
}
