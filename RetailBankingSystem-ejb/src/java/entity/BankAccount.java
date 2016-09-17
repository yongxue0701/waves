package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class BankAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bankAccountId;
    
    @Column(unique=true)
    private String bankAccountNum;
    
    private String bankAccountPwd;
    private String bankAccountType;
    private String bankAccountBalance;
    private String transferDailyLimit;
    private String transferBalance;
    private String bankAccountStatus;
    
    @OneToMany(cascade={CascadeType.ALL},fetch=FetchType.EAGER,mappedBy="bankAccount")
    private List<AccTransaction> accTransaction;
    
    @ManyToOne(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
    private CustomerBasic customerBasic;
    
    @OneToOne(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
    private Interest interest;
    
    public Long getBankAccountId() {
        return bankAccountId;
    }
    
    public void setBankAccountId(Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }
    
    public String getBankAccountNum() {
        return bankAccountNum;
    }
    
    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }
    
    public String getBankAccountPwd() {
        return bankAccountPwd;
    }
    
    public void setBankAccountPwd(String bankAccountPwd) {
        this.bankAccountPwd = bankAccountPwd;
    }
    
    public String getBankAccountType() {
        return bankAccountType;
    }
    
    public void setBankAccountTyep(String bankAccountType) {
        this.bankAccountType = bankAccountType;
    }
    
    public List<AccTransaction> getAccTransaction() {
        return accTransaction;
    }
    
    public void setAccTransaction(List<AccTransaction> transaction) {
        this.accTransaction = transaction;
    }

    public CustomerBasic getCustomerBasic() {
        return customerBasic;
    }

    public void setCustomerBasic(CustomerBasic customerBasic) {
        this.customerBasic = customerBasic;
    }

    public String getBankAccountBalance() {
        return bankAccountBalance;
    }

    public void setBankAccountBalance(String bankAccountBalance) {
        this.bankAccountBalance = bankAccountBalance;
    }

    public Interest getInterest() {
        return interest;
    }

    public void setInterest(Interest interest) {
        this.interest = interest;
    }

    public String getTransferDailyLimit() {
        return transferDailyLimit;
    }

    public void setTransferDailyLimit(String transferDailyLimit) {
        this.transferDailyLimit = transferDailyLimit;
    }

    public String getTransferBalance() {
        return transferBalance;
    }

    public void setTransferBalance(String transferBalance) {
        this.transferBalance = transferBalance;
    }

    public String getBankAccountStatus() {
        return bankAccountStatus;
    }

    public void setBankAccountStatus(String bankAccountStatus) {
        this.bankAccountStatus = bankAccountStatus;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bankAccountId != null ? bankAccountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BankAccount)) {
            return false;
        }
        BankAccount other = (BankAccount) object;
        if ((this.bankAccountId == null && other.bankAccountId != null) || (this.bankAccountId != null 
                && !this.bankAccountId.equals(other.bankAccountId))) 
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BankAccount[ id=" + bankAccountId + " ]";
    }
}
