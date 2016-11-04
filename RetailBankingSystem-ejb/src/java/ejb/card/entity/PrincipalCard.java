/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 *
 * @author aaa
 */
@Entity
public class PrincipalCard extends CreditCard implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double creditLimit;
    private double outstandingBalance; 
       
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "principalCard")
    private List<SupplementaryCard> supplementaryCards;

    public List<SupplementaryCard> getSupplementaryCards() {
        return supplementaryCards;
    }

    public void setSupplementaryCards(List<SupplementaryCard> supplementaryCards) {
        this.supplementaryCards = supplementaryCards;
    }
    
    public void addSupplementaryCard (SupplementaryCard sc){
        supplementaryCards.add(sc);
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    
}
