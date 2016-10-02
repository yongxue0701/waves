package managedbean.infrastructure;

import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.session.EmployeeAdminSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

@Named(value = "systemAdminEditUserAccountManagedBean")
@SessionScoped

public class SystemAdminEditUserAccountManagedBean implements Serializable {

    @EJB
    private EmployeeAdminSessionBeanLocal employeeAdminSessionBeanLocal;
    
    private String targetEmployeeName;
    private String targetEmployeeDepartment;
    private String targetEmployeePosition;
    private String targetEmployeeMobile;
    private String targetEmployeeEmail;
    private Set<String> selectedRoles;
    private List<String> roles;
    private Employee employee;
    private Long employeeId;
    private List<String> departments;
    private List<String> positions;

    private ExternalContext ec;

    /**
     * Creates a new instance of SystemAdminEditUserAccountManagedBean
     */
    public void updateAccountInfo() {
        System.out.println("edit");
        System.out.println(employeeId);
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();

        System.err.println("********** update Account info");
        for (String sr : selectedRoles) {
            System.err.println("********** sr: " + sr);
        }
        employeeAdminSessionBeanLocal.updateEmployeeAccount(employeeId, targetEmployeeName, targetEmployeeDepartment, targetEmployeePosition, targetEmployeeMobile, targetEmployeeEmail, selectedRoles);
      

        System.out.println("clear");

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "User account has been successfully updated!", "account updated");
        context.addMessage(null, message);
        System.out.println("*** AccountManagedBean: account updated");

        employee = null;
        employeeId = null;
        targetEmployeeName = null;
        targetEmployeeDepartment = null;
        targetEmployeePosition = null;
        targetEmployeeMobile = null;
        targetEmployeeEmail = null;
        selectedRoles = null;

    }

    public SystemAdminEditUserAccountManagedBean() {
    }

    public String getTargetEmployeeName() {

        employee = employeeAdminSessionBeanLocal.getEmployeeById(employeeId);
        targetEmployeeName = employee.getEmployeeName();
       
        return targetEmployeeName;
    }

    public void setTargetEmployeeName(String targetEmployeeName) {
        this.targetEmployeeName = targetEmployeeName;
    }

    public String getTargetEmployeeDepartment() {

        employee = employeeAdminSessionBeanLocal.getEmployeeById(employeeId);
        targetEmployeeDepartment = employee.getEmployeeDepartment();

        return targetEmployeeDepartment;
    }

    public void setTargetEmployeeDepartment(String targetEmployeeDepartment) {
        this.targetEmployeeDepartment = targetEmployeeDepartment;
    }

    public String getTargetEmployeePosition() {

        employee = employeeAdminSessionBeanLocal.getEmployeeById(employeeId);
        targetEmployeePosition = employee.getEmployeePosition();

        return targetEmployeePosition;
    }

    public void setTargetEmployeePosition(String targetEmployeePosition) {
        this.targetEmployeePosition = targetEmployeePosition;
    }

    public String getTargetEmployeeMobile() {
        System.out.println("get");
//        if (targetEmployeeMobile == null) {
        employee = employeeAdminSessionBeanLocal.getEmployeeById(employeeId);
        System.out.println(employee);
        targetEmployeeMobile = employee.getEmployeeMobileNum();

//        }
        return targetEmployeeMobile;
    }

    public void setTargetEmployeeMobile(String targetEmployeeMobile) {
        this.targetEmployeeMobile = targetEmployeeMobile;
    }

    public String getTargetEmployeeEmail() {

        employee = employeeAdminSessionBeanLocal.getEmployeeById(employeeId);
        targetEmployeeEmail = employee.getEmployeeEmail();

        return targetEmployeeEmail;
    }

    public void setTargetEmployeeEmail(String targetEmployeeEmail) {
        this.targetEmployeeEmail = targetEmployeeEmail;
    }

    public Set<String> getSelectedRoles() {
        if (selectedRoles == null && employeeId != null) {
            selectedRoles = employeeAdminSessionBeanLocal.getSelectedRoles(employeeId);
        }
        return selectedRoles;
    }

    public void setSelectedRoles(Set<String> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public List<String> getRoles() {
        if (roles == null) {
            roles = employeeAdminSessionBeanLocal.getRoles();
        }
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Long getEmployeeId() {
        System.out.println("***** SystemAdminEditAccountManagedBean get employee id" + employeeId);
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        System.out.println("***** SystemAdminEditAccountManagedBean set employee id" + employeeId);
        this.employeeId = employeeId;
    }

    public List<String> getDepartments() {
        if (departments == null) {
            departments = employeeAdminSessionBeanLocal.getEmployeeDepartments();
        }
        return departments;
    }

    public List<String> getPositions() {

        if (positions == null) {
            positions = employeeAdminSessionBeanLocal.getEmployeePositions();
        }
        return positions;
    }

 
}
