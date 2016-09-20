package managedbean.deposit;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "depositDoneManagedBean")
@RequestScoped

public class DepositDoneManagedBean {
    
    private String statusMessage;
    private String depositAccountNum;
    private String depositAmt;
    
    public DepositDoneManagedBean() {
    }
    
    @PostConstruct
    public void init()
    {
        statusMessage = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("statusMessage").toString();
        depositAccountNum = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("depositAccountNum").toString();
        depositAmt = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("depositAmt").toString();
    }
    
    public String getStatusMessage() {
        return statusMessage;
    }
    
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getDepositAccountNum() {
        return depositAccountNum;
    }

    public void setDepositAccountNum(String depositAccountNum) {
        this.depositAccountNum = depositAccountNum;
    }

    public String getDepositAmt() {
        return depositAmt;
    }

    public void setDepositAmt(String depositAmt) {
        this.depositAmt = depositAmt;
    }
}