package session.stateless;

import entity.CustomerBasic;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hanfengwei
 */
@Stateless
public class AdminSessionBean implements AdminSessionBeanLocal {
    @EJB
    private EmailSessionBeanLocal emailSessionBeanLocal;

    @PersistenceContext
    private EntityManager em;
    

    @Override
    public String createOnlineBankingAccount(Long customerId) {
        CustomerBasic customer = em.find(CustomerBasic.class, customerId);
        String account = null;
        String password = null;

        if (isNewCustomer(customer)) {
            //generate online banking account number
            account = generateAccountNumber(customer);
            System.out.println("*** adminSessionBean: generateAccountNumber(): online banking account number generated");
            
            //generate random password
            password = generatePassword();
            System.out.println("*** adminSessionBean: generatePassword(): online banking account password generated");
            
            //create customer account
            try {
                customer.setCustomerOnlineBankingAccountNum(account);
                String hashedPassword = password;
                hashedPassword = md5Hashing(password + customer.getCustomerIdentificationNum().substring(0, 3));
                customer.setCustomerOnlineBankingPassword(hashedPassword);
                em.flush();
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(AdminSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //generate email
            emailSessionBeanLocal.sendEmail(customer, "openAccount", true, password);
            System.out.println("*** adminSessionBean: email sent to customer (online banking account created)");
            return account + "," + password;
        } else {
            emailSessionBeanLocal.sendEmail(customer, "openAccount", false, password);
            System.out.println("*** adminSessionBean: email sent to customer (not a new customer)");
            return "not a new customer";
        }
    }

    //Check if it's a new customer
    private boolean isNewCustomer(CustomerBasic customer) {
        return customer.getCustomerOnlineBankingAccountNum() == null;
    }

    //Generate initial password for customer online banking account
    private String generatePassword() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(40, random).toString(32);
    }

    //Generate initial account number for customer online banking account
    private String generateAccountNumber(CustomerBasic customer) {
        String hash = customer.getCustomerIdentificationNum();
        System.out.println(hash.concat(customer.getCustomerName().substring(0, 3)));
        try {
            return md5Hashing(hash);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AdminSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            return Integer.toString(hash.hashCode());
        }
    }

    //Do customer login
    @Override
    public String login(String customerAccount, String password) {
        System.out.println("*** adminSessionBean: login(accountNum,password)");

        Query query = em.createQuery("SELECT c FROM CustomerBasic c WHERE c.customerOnlineBankingAccountNum = :accountNum");
        query.setParameter("accountNum", customerAccount);

        try {
            CustomerBasic thisCustomer = (CustomerBasic) query.getSingleResult();
            password = md5Hashing(password + thisCustomer.getCustomerIdentificationNum().substring(0, 3));
            System.out.println("?????????"+password);
            if (thisCustomer.getCustomerOnlineBankingPassword().equals(password)) {
                System.out.println("*** adminSessionBean: login(): valid account and password" + ": account " + thisCustomer.getCustomerOnlineBankingAccountNum());
                return "loggedIn";
            } else {
                System.out.println("*** adminSessionBean: login(): invalid password");
                return "invalidPassword";
            }
        } catch (NoResultException ex) {
            System.out.println("*** adminSessionBean: login(): invalid account");
            return "invalidAccount";
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("!!! adminSessionBean: failed to hash password");
            return "";
        }
    }
    
    @Override
    public CustomerBasic getCustomerByOnlineBankingAccount(String customerAccount){
        System.out.println("adminSessionBean: getCustomerByOnlineBankingAccount(): start");
        Query query = em.createQuery("SELECT c FROM CustomerBasic c WHERE c.customerOnlineBankingAccountNum = :accountNum");
        query.setParameter("accountNum", customerAccount);
        return (CustomerBasic) query.getSingleResult();
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }
}