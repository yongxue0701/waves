package ejb.deposit.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;

import javax.persistence.ManyToOne;

@Entity
public class AccTransaction implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private String transactionDate;
    private String transactionCode;
    private String transactionRef;
    private String accountDebit;
    private String accountCredit;
    private Long transactionDateMilis;
    
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private BankAccount bankAccount;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public String getAccountDebit() {
        return accountDebit;
    }

    public void setAccountDebit(String accountDebit) {
        this.accountDebit = accountDebit;
    }

    public String getAccountCredit() {
        return accountCredit;
    }

    public void setAccountCredit(String accountCredit) {
        this.accountCredit = accountCredit;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Long getTransactionDateMilis() {
        return transactionDateMilis;
    }

    public void setTransactionDateMilis(Long transactionDateMilis) {
        this.transactionDateMilis = transactionDateMilis;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionId != null ? transactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccTransaction)) {
            return false;
        }
        AccTransaction other = (AccTransaction) object;
        if ((this.transactionId == null && other.transactionId != null) || (this.transactionId != null
                && !this.transactionId.equals(other.transactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AccTransaction[ id=" + transactionId + " ]";
    }
}
