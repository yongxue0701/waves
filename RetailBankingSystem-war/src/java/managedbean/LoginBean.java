/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.CustomerBasic;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import session.stateless.AdminSessionBeanLocal;

/**
 *
 * @author hanfengwei
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    @EJB
    private AdminSessionBeanLocal adminSessionBeanLocal;

    private String customerAccount;
    private String customerPassword;
    private String newCustomerAccount;
    private String newCustomerPassword;
    private CustomerBasic customer;
    private int loginAttempts;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }

    /**
     *
     * @param event
     */
    public void doLogin(ActionEvent event) throws IOException, NoSuchAlgorithmException {
//        adminSessionBeanLocal.createOnlineBankingAccount(Long.valueOf(1));

        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();
        customer = adminSessionBeanLocal.getCustomerByOnlineBankingAccount(customerAccount);
        
        if (customer == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid account number: ", "Please check your account number.");
            context.addMessage(null, message);
            System.out.println("*** loginBean: invalid account");
        } else {
            //encrypt the customerPassword first
            customerPassword = md5Hashing(customerPassword + customer.getCustomerIdentificationNum().substring(0, 3));
            String status = adminSessionBeanLocal.login(customerAccount, customerPassword);
            
            switch (status) {
                case "loggedIn":
                    System.out.println("*** loginBean: loggedIn");
                    context.getExternalContext().getSessionMap().put("customer", getCustomer());
                    if (customer.getCustomerStatus().equals("new")) {
                        context.getExternalContext().redirect("accountActivation.xhtml?faces-redirect=true");
                    } else {
                        context.getExternalContext().redirect("home.xhtml?faces-redirect=true");
                    }
                    break;
                case "invalidPassword":
                    customerPassword = "";
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid password: ", "Please enter your passsword again.");
                    context.addMessage(null, message);
                    loginAttempts++;
                    System.out.println("*** loginBean: invalid password, attempts: " + loginAttempts);
                    break;
                default:
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid account: ", "Please check your account number.");
                    context.addMessage(null, message);
                    System.out.println("*** loginBean: invalid account");
                    break;
            }
        }

    }

    public void doLogout(ActionEvent event) throws IOException {
        System.out.println("*** loginBean: doLogout");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();

        String serverName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
        String serverPort = "8080";
        ec.redirect("http://" + serverName + ":" + serverPort + ec.getRequestContextPath() + "/index.xhtml?faces-redirect=true");
    }

    public void timeoutLogout() throws IOException {
        System.out.println("*** loginBean: logout");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        String serverName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
        String serverPort = "8080";
        ec.redirect("http://" + serverName + ":" + serverPort + ec.getRequestContextPath() + "/timeout.xhtml?faces-redirect=true");
    }

    public void activateOnlineBankingAccount(ActionEvent event) throws NoSuchAlgorithmException {
        System.out.println("*** loginBean: activateOnlineBankingAccount");
        newCustomerPassword = md5Hashing(newCustomerPassword + customer.getCustomerIdentificationNum().substring(0, 3));
        String activationStatus = adminSessionBeanLocal.updateOnlineBankingAccount(newCustomerAccount, newCustomerPassword, customer.getCustomerBasicId());
    }

    /**
     * @return the customerAccount
     */
    public String getCustomerAccount() {
        return customerAccount;
    }

    /**
     * @param customerAccount the customerAccount to set
     */
    public void setCustomerAccount(String customerAccount) {
        this.customerAccount = customerAccount;
    }

    /**
     * @return the customerPassword
     */
    public String getCustomerPassword() {
        return customerPassword;
    }

    /**
     * @param customerPassword the customerPassword to set
     */
    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    /**
     * @return the customer
     */
    public CustomerBasic getCustomer() {
        customer = adminSessionBeanLocal.getCustomerByOnlineBankingAccount(customerAccount);
        return customer;
    }

    public String getNewCustomerAccount() {
        return newCustomerAccount;
    }

    public void setNewCustomerAccount(String newCustomerAccount) {
        this.newCustomerAccount = newCustomerAccount;
    }

    public String getNewCustomerPassword() {
        return newCustomerPassword;
    }

    public void setNewCustomerPassword(String newCustomerPassword) {
        this.newCustomerPassword = newCustomerPassword;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(CustomerBasic customer) {
        this.customer = customer;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }
}
