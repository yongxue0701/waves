package ejb.payment.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class SACHMasterBankAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long masterBankAccountId;
    private String bankName;
    private String masterBankAccountNum;
    private String masterBankAccountBalance;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "sachMasterBankAccount")
    private List<SACHMasterAccountTransaction> sachMasterAccountTransaction;
    
    public Long getMasterBankAccountId() {
        return masterBankAccountId;
    }

    public void setMasterBankAccountId(Long masterBankAccountId) {
        this.masterBankAccountId = masterBankAccountId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getMasterBankAccountNum() {
        return masterBankAccountNum;
    }

    public void setMasterBankAccountNum(String masterBankAccountNum) {
        this.masterBankAccountNum = masterBankAccountNum;
    }

    public String getMasterBankAccountBalance() {
        return masterBankAccountBalance;
    }

    public void setMasterBankAccountBalance(String masterBankAccountBalance) {
        this.masterBankAccountBalance = masterBankAccountBalance;
    }

    public List<SACHMasterAccountTransaction> getSachMasterAccountTransaction() {
        return sachMasterAccountTransaction;
    }

    public void setSachMasterAccountTransaction(List<SACHMasterAccountTransaction> sachMasterAccountTransaction) {
        this.sachMasterAccountTransaction = sachMasterAccountTransaction;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (masterBankAccountId != null ? masterBankAccountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SACHMasterBankAccount)) {
            return false;
        }
        SACHMasterBankAccount other = (SACHMasterBankAccount) object;
        if ((this.masterBankAccountId == null && other.masterBankAccountId != null) || (this.masterBankAccountId != null && !this.masterBankAccountId.equals(other.masterBankAccountId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.payment.entity.MasterBankAccount[ id=" + masterBankAccountId + " ]";
    }
    
}
