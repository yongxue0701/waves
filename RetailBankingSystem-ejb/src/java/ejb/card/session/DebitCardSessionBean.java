/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.DebitCard;
import ejb.card.entity.DebitCardType;
import ejb.deposit.entity.BankAccount;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jingyuan
 */
@Stateless
public class DebitCardSessionBean implements DebitCardSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    private DebitCardSessionBeanLocal debitCardSessionBeanLocal;

    @PersistenceContext
    private EntityManager em;

    @Override
    public String createDebitCard(String bankAccountNum, String cardHolderName, String applicationDate, String cardTypeName) {
        DebitCard debitCard = new DebitCard();
        debitCard.setCardHolderName(cardHolderName);

        BankAccount depositAccount = findDepositAccountByAccountNum(bankAccountNum);
        debitCard.setBankAccount(depositAccount);

        DebitCardType debitCardType = findCardTypeByTypeName(cardTypeName);
        debitCard.setDebitCardType(debitCardType);

        String cardNum = generateCardNum();
        debitCard.setCardNum(cardNum);

        String debitCardSecurityCode = generateCardSecurityCode();
        try {
            System.out.println("debug cardNum:"+cardNum);
            System.out.println("debug csc initial:"+debitCardSecurityCode);
            String hashedDebitCardSecurityCode = md5Hashing(debitCardSecurityCode + cardNum.substring(0, 3));
            System.out.println("debug:"+hashedDebitCardSecurityCode);
            debitCard.setCardSecurityCode(hashedDebitCardSecurityCode);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DebitCardSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

//        String[] applicationDateToString = changeDateFormat(applicationDate);
        String[] applicationDateToString = applicationDate.split("/");
        String applicationYear = applicationDateToString[2];
        String applicationMonth = applicationDateToString[1];

        int expiryYearToInt = Integer.valueOf(applicationYear) + 5;
        String expiryYear = String.valueOf(expiryYearToInt);
        String debitCardExpiryDate = applicationMonth + "/" + expiryYear;

        debitCard.setCardExpiryDate(debitCardExpiryDate);
        
        debitCard.setStatus("not activated");
        
        debitCard.setTransactionLimit(500);
        

        em.persist(debitCard);
        return "success";

    }

    @Override
    public String debitCardNumValiadation(String debitCardNum, String cardHolderName, String debitCardSecurityCode) {
        try {
            //check if the debitCard exist by debit card number
            if (getCardByCardNum(debitCardNum) == null) {
                return "debit card not exist";
            } else {
                DebitCard findDebitCard = getCardByCardNum(debitCardNum);
                System.out.println("debug!! card holder name " + findDebitCard.getCardHolderName());
                if (!findDebitCard.getCardHolderName().equals(cardHolderName)) {
                    return "cardHolderName not match";
                } else {
                    String hashedCSC;
                    System.out.println("debug check hashed csc - csc:"+debitCardSecurityCode);
                    System.out.println("debug check hashed csc - debitCardNum:"+debitCardNum);
                    hashedCSC = md5Hashing(debitCardSecurityCode + debitCardNum.substring(0, 3));
                    System.out.println("debug check hashed csc:"+hashedCSC);
                    if (!findDebitCard.getCardSecurityCode().equals(hashedCSC)) {
                        return "csc not match";
                    } else {
                        findDebitCard.setStatus("activated");
                        return "valid";
                    }

                }//card holder name match

            }//debit card exist
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DebitCardSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private BankAccount findDepositAccountByAccountNum(String bankAccountNum) {
        Query query = em.createQuery("SELECT b FROM BankAccount b WHERE b.bankAccountNum = :accountNum");
        query.setParameter("accountNum", bankAccountNum);

        BankAccount findBankAccount = (BankAccount) query.getSingleResult();
        return findBankAccount;
    }

    private String generateCardNum() {
        System.out.println("*");
        System.out.println("****** card/DebitCardSessionBean: generateDebitCardNumber() ******");
//        SecureRandom random = new SecureRandom();
//        String cardNumber = new BigInteger(48, random).toString(8);
//
//        while (getCardByCardNum(cardNumber) != null) {
//            cardNumber = new BigInteger(48, random).toString(8);
//        }
        String cardNumber = generateNdigitNumber(16);
        //check duplicate
        while (getCardByCardNum(cardNumber) != null) {
            cardNumber = generateNdigitNumber(16);
        }

        System.out.println("****** card/DebitCardSessionBean: generateDebitCardNumber(): debit card number generated" + cardNumber);
        return cardNumber;

    }

    private String generateNdigitNumber(int n) {
        Random rnd = new Random();

        final char[] ch = new char[n];
        for (int i = 0; i < n; i++) {
            ch[i] = (char) ('0' + (i == 0 ? rnd.nextInt(9) + 1 : rnd.nextInt(10)));
        }
        String nDigitNumber = String.valueOf(ch);
        return nDigitNumber;
    }

    private DebitCard getCardByCardNum(String cardNum) {
        Query query = em.createQuery("SELECT d FROM DebitCard d WHERE d.cardNum = :cardNum");
        query.setParameter("cardNum", cardNum);

        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            DebitCard findDebitCard = (DebitCard) query.getResultList().get(0);
            return findDebitCard;
        }
    }

    private DebitCard getCardBySecurityCode(String cardSecurityCode) {
        Query query = em.createQuery("SELECT d FROM DebitCard d WHERE d.cardSecurityCode = :cardSecurityCode");
        query.setParameter("cardSecurityCode", cardSecurityCode);

        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            DebitCard findDebitCard = (DebitCard) query.getResultList().get(0);
            return findDebitCard;
        }
    }

    private String generateCardSecurityCode() {
        System.out.println("*");
        System.out.println("****** card/DebitCardSessionBean: generateCardSecurityCode() ******");
        //no need to check duplicate for debit card security code
        String csc = generateNdigitNumber(3);

        System.out.println("****** card/DebitCardSessionBean: generateCardSecurityCode(): debit CSC generated " + csc);
        return csc;
    }

    private DebitCardType findCardTypeByTypeName(String cardTypeName) {
        Query query = em.createQuery("SELECT d FROM DebitCardType d WHERE d.debitCardTypeName = :cardTypeName");
        query.setParameter("cardTypeName", cardTypeName);

        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            DebitCardType findDebitCardType = (DebitCardType) query.getResultList().get(0);
            System.out.println("****** card/DebitCardSessionBean: find cardType: " + findDebitCardType);
            return findDebitCardType;
        }
    }

    @Override
    public String checkDebitCardTypeForDepositAccount(String bankAccountNum, String cardTypeName) {
        System.out.println("debug debitCardsOfType " + cardTypeName);
        System.out.println("debug debitCardsOfAccount " + bankAccountNum);

        BankAccount depositAccount = findDepositAccountByAccountNum(bankAccountNum);
        System.out.println("debug depositAccount " + depositAccount);
        DebitCardType debitCardType = findCardTypeByTypeName(cardTypeName);
        System.out.println("debug debit card type " + debitCardType);
        System.out.println("debug debitCardsOfType " + debitCardType.getDebitCards());
        System.out.println("debug debitCardsOfAccount" + depositAccount.getDebitCards());
        if (debitCardType.getDebitCards() == null || depositAccount.getDebitCards() == null) {
            return "not existing";
        } else {
            List<DebitCard> debitCardsOfType = debitCardType.getDebitCards();
            List<DebitCard> debitCardsOfAccount = depositAccount.getDebitCards();

            for (int i = 0; i < debitCardsOfAccount.size(); i++) {
                DebitCard debitCard = debitCardsOfAccount.get(i);
                if (debitCardsOfType.contains(debitCard)) {
                    return "existing";
                }
            }

            return "not existing";
        }
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        System.out.println("md5 hashing- string to hash "+stringToHash);
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }
}