/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.customer.session;

import ejb.customer.entity.FollowUp;
import javax.ejb.Local;

/**
 *
 * @author aaa
 */
@Local
public interface FollowUpSessionBeanLocal {
    public FollowUp retrieveFollowUpById(Long followUpId);
    
    public String deleteFollowUp (Long followUpId);
    
}
