package managedbean.customer;

import ejb.customer.entity.EnquiryCase;
import ejb.customer.entity.FollowUp;
import ejb.customer.entity.Issue;
import ejb.customer.session.EnquirySessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "enquiryManagerManagedBean")
@SessionScoped

public class EnquiryManagerManagedBean implements Serializable {

    @EJB
    private EnquirySessionBeanLocal enquirySessionBeanLocal;

    private Long caseId;
    private Long followUpId;
    private String caseReply;
    private String followUpSolution;
    private String departmentTo;
    private String issueProblem;
    private List<FollowUp> selectedFollowUp;
    private List<FollowUp> followUps;

    private String ableToReply = " ";
    private String ableToReply2 = " ";

    private boolean visible1;
    private boolean visible2;
    private boolean visible3;
    private boolean visible4;


    public EnquiryManagerManagedBean() {
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public Long getFollowUpId() {
        return followUpId;
    }

    public void setFollowUpId(Long followUpId) {
        this.followUpId = followUpId;
    }

    public String getAbleToReply() {
        return ableToReply;
    }

    public void setAbleToReply(String ableToReply) {
        this.ableToReply = ableToReply;
    }

    public boolean isVisible1() {
        return visible1;
    }

    public void setVisible1(boolean visible1) {
        this.visible1 = visible1;
    }

    public boolean isVisible2() {
        return visible2;
    }

    public void setVisible2(boolean visible2) {
        this.visible2 = visible2;
    }

    public String getCaseReply() {
        return caseReply;
    }

    public void setCaseReply(String caseReply) {
        this.caseReply = caseReply;
    }

    public String getFollowUpSolution() {
        return followUpSolution;
    }

    public void setFollowUpSolution(String followUpSolution) {
        this.followUpSolution = followUpSolution;
    }

    public String getDepartmentTo() {
        return departmentTo;
    }

    public void setDepartmentTo(String departmentTo) {
        this.departmentTo = departmentTo;
    }

    public String getIssueProblem() {
        return issueProblem;
    }

    public void setIssueProblem(String issueProblem) {
        this.issueProblem = issueProblem;
    }

    public String getAbleToReply2() {
        return ableToReply2;
    }

    public void setAbleToReply2(String ableToReply2) {
        this.ableToReply2 = ableToReply2;
    }

    public boolean isVisible3() {
        return visible3;
    }

    public void setVisible3(boolean visible3) {
        this.visible3 = visible3;
    }

    public boolean isVisible4() {
        return visible4;
    }

    public void setVisible4(boolean visible4) {
        this.visible4 = visible4;
    }

    public List<FollowUp> getSelectedFollowUp() {
        return selectedFollowUp;
    }

    public void setSelectedFollowUp(List<FollowUp> selectedFollowUp) {
        this.selectedFollowUp = selectedFollowUp;
    }

    public List<FollowUp> getFollowUps() {
        return followUps;
    }

    public void setFollowUps(List<FollowUp> followUps) {
        this.followUps = followUps;
    }

    public List<EnquiryCase> getPendingCases() {

        List<EnquiryCase> enquiryCases = enquirySessionBeanLocal.getAllPendingCustomerEnquiry();

        return enquiryCases;
    }

    public String getCaseDetailById() {
        return enquirySessionBeanLocal.getCustomerEnquiryDetail(caseId);
    }

//    public String getFollowUpDetailById() {
//        return enquirySessionBeanLocal.getCustomerFollowUpDetail(followUpId);
//    }

    public List<Issue> getCaseIssueById() {
        return enquirySessionBeanLocal.getCaseIssue(caseId);
    }

    public List<FollowUp> getCaseFollowUpById() {
        followUps = enquirySessionBeanLocal.getCaseFollowUp(caseId);
        return followUps;
    }
//
//    public List<Issue> getFollowUpIssueById() {
//        return enquirySessionBeanLocal.getFollowUpIssue(followUpId);
//    }

    public void replyToCase() throws IOException {
        String msg;
        msg = enquirySessionBeanLocal.replyCustomerCase(caseId, caseReply, selectedFollowUp, followUps);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg, " "));

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        if (msg == "1") {
            caseId = null;
            caseReply = null;
            selectedFollowUp = null;
            followUps = null;
            ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/enquirymanagerReplyCaseDone.xhtml");
        }
    }

//    public void replyToFollowUp() throws IOException {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(enquirySessionBeanLocal.replyCustomerFollowUp(followUpId, followUpSolution), " "));
//        followUpId = null;
//        followUpSolution = null;
//        FacesContext context = FacesContext.getCurrentInstance();
//        ExternalContext ec = context.getExternalContext();
//        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/enquirymanagerReplyCaseDone.xhtml");
//    }
    public void addIssue() throws IOException {
        System.out.println("addIssue");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(enquirySessionBeanLocal.addNewCaseIssue(caseId, departmentTo, issueProblem, followUps), " "));
        departmentTo = null;
        issueProblem = null;
    }

    public void saveIssue() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        context.addMessage(null, new FacesMessage(enquirySessionBeanLocal.addNewCaseIssue(caseId, departmentTo, issueProblem, followUps), " "));
        caseId = null;
        departmentTo = null;
        issueProblem = null;
        followUps = null;
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/enquirymanagerSubmitDone.xhtml");
    }

//    public void addFollowUpIssue() throws IOException {
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.addMessage(null, new FacesMessage(enquirySessionBeanLocal.addNewFollowUpIssue(followUpId, departmentTo, issueProblem), " "));
//        departmentTo = null;
//        issueProblem = null;
//    }
//    public void saveFollowUpIssue() throws IOException {
//        FacesContext context = FacesContext.getCurrentInstance();
//        ExternalContext ec = context.getExternalContext();
//        context.addMessage(null, new FacesMessage(enquirySessionBeanLocal.addNewFollowUpIssue(followUpId, departmentTo, issueProblem), " "));
//        followUpId = null;
//        departmentTo = null;
//        issueProblem = null;
//        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/enquirymanagerSubmitDone.xhtml");
//    }
    public void redirectToViewEnquiryDone() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/enquirymanagerViewEnquiryDone.xhtml");
    }

    public void redirectToReplyCase() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/enquirymanagerReplyCase.xhtml");
    }

    public void redirectToReplyFollowUp() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/enquirymanagerReplyFollowUp.xhtml");
    }

    public String caseIssueCreated(Long caseId) {
        return enquirySessionBeanLocal.caseIssueIsCreated(caseId);
    }

//    public String followUpIssueCreated(Long followUpId) {
//        return enquirySessionBeanLocal.followUpIssueIsCreated(followUpId);
//    }

    public String caseIssueReplied(Long caseId) {
        return enquirySessionBeanLocal.caseIssueAllReplied(caseId);
    }
//
//    public String followUpIssueReplied(Long followUpId) {
//        return enquirySessionBeanLocal.followUpIssueAllReplied(followUpId);
//    }

    public void show1() {

        if (ableToReply.equals("Yes")) {
            visible1 = true;
        } else {
            visible1 = false;
        }
//        ableToReply = null;

    }

    public void show2() {

        if (ableToReply.equals("No")) {
            visible2 = true;
        } else {
            visible2 = false;
        }
    }

    public void show3() {

        if (ableToReply2.equals("Yes")) {
            visible3 = true;
        } else {
            visible3 = false;
        }
    }

    public void show4() {

        if (ableToReply2.equals("No")) {
            visible4 = true;
        } else {
            visible4 = false;
        }
    }

}
