/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.infrastructure;

import ejb.customer.entity.CustomerBasic;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import ejb.infrastructure.session.SMSSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 *
 * @author hanfengwei
 */
@Named(value = "sMSManagedBean")
@ViewScoped
public class SMSManagedBean implements Serializable{
    @EJB
    private SMSSessionBeanLocal sMSSessionBeanLocal;
    private String customerOTP;
    private String originalOTP;

    /**
     * Creates a new instance of SMSManagedBean
     */
    public SMSManagedBean() {
    }
    
    public void sendOTP(ActionEvent event){
        System.out.println("-");
        System.out.println("====== infrastructure/SMSManagedBean: sendOTP() ======");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic thisCustomer = (CustomerBasic) ec.getSessionMap().get("customer");
//        String phoneNum = thisCustomer.getCustomerMobile();
        originalOTP = sMSSessionBeanLocal.sendOTP("customer", "83114121");
//        System.out.println("====== infrastructure/SMSManagedBean: sendOTP(): OTP sent to customer" + phoneNum);
    }

    public String getCustomerOTP() {
        return customerOTP;
    }

    public void setCustomerOTP(String customerOTP) {
        this.customerOTP = customerOTP;
    }

    public String getOriginalOTP() {
        return originalOTP;
    }

    public void setOriginalOTP(String originalOTP) {
        this.originalOTP = originalOTP;
    }
    
    public void verifyOTP(ActionEvent event) throws IOException{
        System.out.println("-");
        System.out.println("====== infrastructure/SMSManagedBean: verifyOTP() ======");
        FacesContext context = FacesContext.getCurrentInstance();
        if(originalOTP == null){
            System.out.println("====== infrastructure/SMSManagedBean: verifyOTP(): no original OTP found");
            //ask customer to request for OTP again
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Send OTP failed: ", "Please click on the \"Resend OTP\" button to send OTP again to your registered mobile."));
        }else if(originalOTP.equals(customerOTP)){
            System.out.println("====== infrastructure/SMSManagedBean: verifyOTP(): OTP correct - redirect customer to destination page");
            customerOTP = null;
            originalOTP = null;
            //redirect customer to the destination page
            context.getExternalContext().getSessionMap().put("isVerified", "true");
            System.out.println("====== infrastructure/SMSManagedBean: verifyOTP(): customer is verified");
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/web/onlineBanking/OTP");
        }else{
            System.out.println("====== infrastructure/SMSManagedBean: verifyOTP(): OTP does not match");
            customerOTP = null;
            context.getExternalContext().getSessionMap().put("isVerified", "false");
            System.out.println("====== infrastructure/SMSManagedBean: verifyOTP(): set customer verified status to false");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OTP does not match: ", "That is an invalid iBanking OTP. Please re-enter."));
        }
    }
    
}
