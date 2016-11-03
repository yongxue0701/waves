/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import javax.persistence.Entity;
import javax.persistence.Temporal;

/**
 *
 * @author hanfengwei
 */
@Entity
public class EducationLoanApplication extends LoanApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String educationInstitution;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date educationStarton;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date educationEndon;
    private int courseDuration;
    private int courseFee;
    
    
    public void create(double amountRequired, int periodRequired,
            String institution, Date educationStarton, Date educationEndon, int duration, int courseFee, String employmentStatus){
        this.setAmountRequired(amountRequired);
        this.setPeriodRequired(periodRequired);
        this.setLoanType("Education Loan");
        this.setApplicationDate(new Date());
        this.setApplicationStatus("pending");
        
        this.setEducationInstitution(institution);
        this.setEducationStarton(educationStarton);
        this.setEducationEndon(educationEndon);
        this.setCourseDuration(duration);
        this.setCourseFee(courseFee);
        HashMap docs = new HashMap();
        docs.put("identification", true);
        docs.put("acceptance", true);
        docs.put("invoice", true);
        if (employmentStatus.equals("Self-Employed")) {
            docs.put("selfEmployedTax", true);
            docs.put("employeeTax", false);
        } else {
            docs.put("selfEmployedTax", false);
            docs.put("employeeTax", true);
        } 
        this.setUploads(docs);
    }

    public String getEducationInstitution() {
        return educationInstitution;
    }

    public void setEducationInstitution(String educationInstitution) {
        this.educationInstitution = educationInstitution;
    }

    public Date getEducationStarton() {
        return educationStarton;
    }

    public void setEducationStarton(Date educationStarton) {
        this.educationStarton = educationStarton;
    }

    public Date getEducationEndon() {
        return educationEndon;
    }

    public void setEducationEndon(Date educationEndon) {
        this.educationEndon = educationEndon;
    }

    public int getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(int courseDuration) {
        this.courseDuration = courseDuration;
    }

    public int getCourseFee() {
        return courseFee;
    }

    public void setCourseFee(int courseFee) {
        this.courseFee = courseFee;
    }
    
    
}
