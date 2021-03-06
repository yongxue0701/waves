/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.customer.employee;

import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.infrastructure.entity.Employee;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author aaa
 */
@Named(value = "rMSearchCustomerAdvancedManagedBean")
@RequestScoped
public class RMSearchCustomerAdvancedManagedBean implements Serializable {

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    private Employee em = new Employee();
    private List<CustomerBasic> customerBasics;
    private List<CustomerAdvanced> customerAdvanceds;
    private CustomerAdvanced customerAdvanced;

    private String customerOnlineBankingAccountNum;

    private String customerEmploymentDetails;
    private String customerFamilyInfo;
    private String customerCreditReport;
    private String customerFinancialRiskRating;
    private String customerFinanacialGoals;
    private String customerFinanacialAssets;

    public RMSearchCustomerAdvancedManagedBean() {
    }

    public Employee getEm() {
        return em;
    }

    public void setEm(Employee em) {
        this.em = em;
    }

    public List<CustomerBasic> getCustomerBasics() {
        return customerBasics;
    }

    public void setCustomerBasics(List<CustomerBasic> customerBasics) {
        this.customerBasics = customerBasics;
    }

    public List<CustomerAdvanced> getCustomerAdvanceds() {
        System.out.println("getcustomerlistfromemployeeid");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        em = (Employee) ec.getSessionMap().get("employee");
        customerAdvanceds = em.getCustomerAdvanced();
        System.out.println("DEBUG" + customerAdvanceds);
        return customerAdvanceds;
    }

    public void setCustomerAdvanceds(List<CustomerAdvanced> customerAdvanceds) {
        this.customerAdvanceds = customerAdvanceds;
    }

    public CustomerAdvanced getCustomerAdvanced() {
        return customerAdvanced;
    }

    public void setCustomerAdvanced(CustomerAdvanced customerAdvanced) {
        this.customerAdvanced = customerAdvanced;
    }

    public String getCustomerOnlineBankingAccountNum() {
        return customerOnlineBankingAccountNum;
    }

    public void setCustomerOnlineBankingAccountNum(String customerOnlineBankingAccountNum) {
        this.customerOnlineBankingAccountNum = customerOnlineBankingAccountNum;
    }

    public String getCustomerEmploymentDetails() {
        return customerEmploymentDetails;
    }

    public void setCustomerEmploymentDetails(String customerEmploymentDetails) {
        this.customerEmploymentDetails = customerEmploymentDetails;
    }

    public String getCustomerFamilyInfo() {
        return customerFamilyInfo;
    }

    public void setCustomerFamilyInfo(String customerFamilyInfo) {
        this.customerFamilyInfo = customerFamilyInfo;
    }

    public String getCustomerCreditReport() {
        return customerCreditReport;
    }

    public void setCustomerCreditReport(String customerCreditReport) {
        this.customerCreditReport = customerCreditReport;
    }

    public String getCustomerFinancialRiskRating() {
        return customerFinancialRiskRating;
    }

    public void setCustomerFinancialRiskRating(String customerFinancialRiskRating) {
        this.customerFinancialRiskRating = customerFinancialRiskRating;
    }

    public String getCustomerFinanacialGoals() {
        return customerFinanacialGoals;
    }

    public void setCustomerFinanacialGoals(String customerFinanacialGoals) {
        this.customerFinanacialGoals = customerFinanacialGoals;
    }

    public String getCustomerFinanacialAssets() {
        return customerFinanacialAssets;
    }

    public void setCustomerFinanacialAssets(String customerFinanacialAssets) {
        this.customerFinanacialAssets = customerFinanacialAssets;
    }

    public void updateCustomerAdvancedProfile(Long id) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("customerAdvancedId", id);

        context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/web/internalSystem/CRM/RMUpdateCustomerAdvanced.xhtml?faces-redirect=true");

    }

    public void redirectToPortfolio(Long customerBasicId) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("customerBasicId", customerBasicId);

        context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/web/internalSystem/wealth/RMViewCustomerPortfolio.xhtml?faces-redirect=true");
    }

}
