package session.stateless;

import entity.Payee;
import java.util.List;
import javax.ejb.Local;

@Local
public interface PayeeSessionLocal {
    public Long addNewPayee(String payeeName,String payeeAccountNum,String payeeAccountType,
            String lastTransactionDate,Long customerBasicId);
    public String deletePayee(String payeeAccountNum);
    public Payee retrievePayeeById(Long payeeId);
    public Payee retrievePayeeByName(String payeeName);
    public List<Payee> retrievePayeeByCusId(Long customerBasicId);
    public Payee retrievePayeeByNum(String payeeAccountNum);
    public void updateLastTransactionDate(String bankAccountNum);
}
