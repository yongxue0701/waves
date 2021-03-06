package managedbean.deposit.employee;

import ejb.bi.session.DepositAccountOpenSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.TransactionSessionBeanLocal;
import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "employeeTransferDoneManagedBean")
@RequestScoped

public class EmployeeTransferDoneManagedBean {

    @EJB
    private DepositAccountOpenSessionBeanLocal depositAccountOpenSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    private String fromAccount;
    private String fromCurrency;
    private String toAccount;
    private String toCurrency;
    private Double transferAmt;
    private String fromBankAccountNumWithType;
    private String toBankAccountNumWithType;
    private Long newTransactionId;
    private String fromAccountAvailableBalance;
    private String fromAccountTotalBalance;

    private String statusMessage;
    private String customerIdentificationNum;

    private Map<String, String> fromAccounts = new HashMap<String, String>();

    private ExternalContext ec;

    private String customerName;

    public EmployeeTransferDoneManagedBean() {
    }

    @PostConstruct
    public void init() {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);
        customerName = customerBasic.getCustomerName();

        List<BankAccount> bankAccounts = bankAccountSessionBeanLocal.retrieveBankAccountByCusIC(customerBasic.getCustomerIdentificationNum());
        fromAccounts = new HashMap<String, String>();

        for (int i = 0; i < bankAccounts.size(); i++) {
            fromAccounts.put(bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum(), bankAccounts.get(i).getBankAccountType() + "-" + bankAccounts.get(i).getBankAccountNum());
        }
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public Double getTransferAmt() {
        return transferAmt;
    }

    public void setTransferAmt(Double transferAmt) {
        this.transferAmt = transferAmt;
    }

    public String getFromBankAccountNumWithType() {
        return fromBankAccountNumWithType;
    }

    public void setFromBankAccountNumWithType(String fromBankAccountNumWithType) {
        this.fromBankAccountNumWithType = fromBankAccountNumWithType;
    }

    public String getToBankAccountNumWithType() {
        return toBankAccountNumWithType;
    }

    public void setToBankAccountNumWithType(String toBankAccountNumWithType) {
        this.toBankAccountNumWithType = toBankAccountNumWithType;
    }

    public Long getNewTransactionId() {
        return newTransactionId;
    }

    public void setNewTransactionId(Long newTransactionId) {
        this.newTransactionId = newTransactionId;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public String getFromAccountAvailableBalance() {
        return fromAccountAvailableBalance;
    }

    public void setFromAccountAvailableBalance(String fromAccountAvailableBalance) {
        this.fromAccountAvailableBalance = fromAccountAvailableBalance;
    }

    public String getFromAccountTotalBalance() {
        return fromAccountTotalBalance;
    }

    public void setFromAccountTotalBalance(String fromAccountTotalBalance) {
        this.fromAccountTotalBalance = fromAccountTotalBalance;
    }

    public Map<String, String> getFromAccounts() {
        return fromAccounts;
    }

    public void setFromAccounts(Map<String, String> fromAccounts) {
        this.fromAccounts = fromAccounts;
    }

    public void transfer() throws IOException {

        fromAccount = handleAccountString(fromBankAccountNumWithType);

        BankAccount bankAccountFrom = bankAccountSessionBeanLocal.retrieveBankAccountByNum(fromAccount);
        BankAccount bankAccountTo = bankAccountSessionBeanLocal.retrieveBankAccountByNum(toAccount);

        String activationCheck;
        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (bankAccountFrom.getBankAccountId() == null || bankAccountTo.getBankAccountId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your deposit account does not exist.", "Failed!"));
        } else if (bankAccountTo.getBankAccountType().equals("Fixed Deposit Account")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer is not allowed transferring fund to a fixed deposit account. ", "Failed!"));
        } else {

            if (Double.valueOf(bankAccountFrom.getTransferBalance()) < transferAmt) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Transfer amount has been exceeded your daily transfer limit.", "Failed!"));
            } else {

                if (fromAccount.equals(toAccount)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Fund transfer cannot be done within the same accounts.", "Failed!"));
                } else {
                    if (bankAccountFrom.getBankAccountStatus().equals("Active") && bankAccountTo.getBankAccountStatus().equals("Inactive")) {

                        activationCheck = transactionSessionBeanLocal.checkAccountActivation(bankAccountTo.getBankAccountNum(), transferAmt.toString());

                        if (activationCheck.equals("Insufficient")) {
                            if (bankAccountTo.getBankAccountType().equals("Bonus Savings Account")) {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Minimum initial deposit amount is S$3000", "Failed"));
                            } else if (bankAccountTo.getBankAccountType().equals("Basic Savings Account")) {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Minimum initial deposit amount is S$1", "Failed"));
                            } else if (bankAccountTo.getBankAccountType().equals("Fixed Deposit Account")) {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!Minimum initial deposit amount is S$1000", "Failed"));
                            }
                        } else if (activationCheck.equals("Contact")) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please contact us at 800 820 8820 or visit our branch.", "Failed"));
                        } else if (activationCheck.equals("Declare")) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please declare your fixed deposit period first.", "Failed"));
                        } else if (activationCheck.equals("Activated")) {
                            Double diffAmt = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance()) - transferAmt;

                            toBankAccountNumWithType = toAccount + "-" + bankAccountTo.getBankAccountType();
                            fromBankAccountNumWithType = fromAccount + "-" + bankAccountFrom.getBankAccountType();

                            if (diffAmt >= 0) {

                                Double currentAvailableBalance = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance());
                                Double currentTotalBalance = Double.valueOf(bankAccountFrom.getTotalBankAccountBalance());

                                newTransactionId = transactionSessionBeanLocal.fundTransfer(fromAccount, toAccount, transferAmt.toString());
                                statusMessage = "Your transaction has been completed.";
                                loggingSessionBeanLocal.createNewLogging("employee", getEmployeeViaSessionMap(), "Fund transfer", "successful", customerName);

                                Calendar cal = Calendar.getInstance();
                                depositAccountOpenSessionBeanLocal.addNewDepositAccountOpen(cal.getTimeInMillis(), cal.getTime().toString());

                                Double fromAccountAvailableBalanceDouble = currentAvailableBalance - transferAmt;
                                Double fromAccountTotalBalanceDouble = currentTotalBalance - transferAmt;

                                fromAccountAvailableBalance = fromAccountAvailableBalanceDouble.toString();
                                fromAccountTotalBalance = fromAccountTotalBalanceDouble.toString();

                                ec.getFlash().put("statusMessage", statusMessage);
                                ec.getFlash().put("newTransactionId", newTransactionId);
                                ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
                                ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
                                ec.getFlash().put("transferAmt", transferAmt);
                                ec.getFlash().put("fromAccount", fromAccount);
                                ec.getFlash().put("toAccount", toAccount);
                                ec.getFlash().put("fromAccountAvailableBalance", fromAccountAvailableBalance);
                                ec.getFlash().put("fromAccountTotalBalance", fromAccountTotalBalance);

                                ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/deposit/employeeTransferFinished.xhtml?faces-redirect=true");
                            } else {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Your account balance is insufficient.", "Failed!"));
                            }
                        }
                    } else if (bankAccountFrom.getBankAccountStatus().equals("Inactive") && bankAccountTo.getBankAccountStatus().equals("Active")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! You account(from) has not been activated.", "Failed!"));
                    } else if (bankAccountFrom.getBankAccountStatus().equals("Active") && bankAccountTo.getBankAccountStatus().equals("Active")) {
                        Double diffAmt = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance()) - transferAmt;

                        toBankAccountNumWithType = toAccount + "-" + bankAccountTo.getBankAccountType();
                        fromBankAccountNumWithType = fromAccount + "-" + bankAccountFrom.getBankAccountType();

                        if (diffAmt >= 0) {

                            Double currentAvailableBalance = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance());
                            Double currentTotalBalance = Double.valueOf(bankAccountFrom.getTotalBankAccountBalance());

                            newTransactionId = transactionSessionBeanLocal.fundTransfer(fromAccount, toAccount, transferAmt.toString());
                            statusMessage = "Your transaction has been completed.";
                            loggingSessionBeanLocal.createNewLogging("employee", getEmployeeViaSessionMap(), "Fund transfer", "successful", customerName);

                            Double fromAccountAvailableBalanceDouble = currentAvailableBalance - transferAmt;
                            Double fromAccountTotalBalanceDouble = currentTotalBalance - transferAmt;

                            fromAccountAvailableBalance = fromAccountAvailableBalanceDouble.toString();
                            fromAccountTotalBalance = fromAccountTotalBalanceDouble.toString();

                            ec.getFlash().put("statusMessage", statusMessage);
                            ec.getFlash().put("newTransactionId", newTransactionId);
                            ec.getFlash().put("toBankAccountNumWithType", toBankAccountNumWithType);
                            ec.getFlash().put("fromBankAccountNumWithType", fromBankAccountNumWithType);
                            ec.getFlash().put("transferAmt", transferAmt);
                            ec.getFlash().put("fromAccount", fromAccount);
                            ec.getFlash().put("toAccount", toAccount);
                            ec.getFlash().put("fromAccountAvailableBalance", fromAccountAvailableBalance);
                            ec.getFlash().put("fromAccountTotalBalance", fromAccountTotalBalance);

                            ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/deposit/employeeTransferFinished.xhtml?faces-redirect=true");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Your account balance is insufficient.", "Failed!"));
                        }
                    } else if (bankAccountFrom.getBankAccountStatus().equals("Inactive") && bankAccountTo.getBankAccountStatus().equals("Inactive")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Both of accounts have not been activated.", "Failed!"));
                    }
                }
            }
        }
    }

    private Long getEmployeeViaSessionMap() {
        Long employeeId;
        FacesContext context = FacesContext.getCurrentInstance();
        Employee employee = (Employee) context.getExternalContext().getSessionMap().get("employee");
        employeeId = employee.getEmployeeId();

        return employeeId;
    }

    private String handleAccountString(String bankAccountNumWithType) {

        String[] bankAccountNums = bankAccountNumWithType.split("-");
        String bankAccountNum = bankAccountNums[1];

        return bankAccountNum;
    }
}
