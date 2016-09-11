/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.CustomerBasic;
import javax.ejb.Local;

/**
 *
 * @author hanfengwei
 */
@Local
public interface AdminSessionBeanLocal {
    public String createOnlineBankingAccount(Long customerId);

    public String login(String customerAccount, String password);
    
    public CustomerBasic getCustomerByOnlineBankingAccount(String customerAccount);
}