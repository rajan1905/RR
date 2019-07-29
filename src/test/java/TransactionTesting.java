import database.DbConnection;
import entity.dao.Account;
import entity.dto.AccountDTO;
import entity.dto.TransactionDTO;
import org.junit.jupiter.api.Test;
import transactions.utility.TransactionUtility;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTesting {
    @Test
    public void transferFromXToY(){
        AccountDTO x = AccountDTO.builder()
                .accountNo(1)
                .balance(new Double(100))
                .firstName("Joe")
                .lastName("X")
                .build();
        Account xA = new Account(x.getFirstName(),
                x.getMiddleName(), x.getLastName(), x.getAccountNo(), x.getBalance());
        AccountDTO y = AccountDTO.builder()
                .accountNo(2)
                .balance(new Double(999))
                .firstName("Alice")
                .lastName("Y")
                .build();
        Account yA = new Account(y.getFirstName(),
                y.getMiddleName(), y.getLastName(), y.getAccountNo(), y.getBalance());
        DbConnection.init();
        DbConnection.em.getTransaction().begin();
        DbConnection.em.persist(xA);
        DbConnection.em.persist(yA);
        DbConnection.em.getTransaction().commit();
        TransactionDTO transaction = TransactionDTO.builder()
                .amount(new Double(50))
                .crFrom(1)
                .crTo(2)
                .build();

        TransactionUtility.performTransaction.apply(transaction, DbConnection.em);

        DbConnection.em.getTransaction().begin();

        AccountDTO xN = TransactionUtility.findAccount.apply(1, DbConnection.em);
        AccountDTO yN = TransactionUtility.findAccount.apply(2, DbConnection.em);
        DbConnection.em.getTransaction().commit();

        assertEquals(xN.getBalance(), new Double(50));
        assertEquals(yN.getBalance(), new Double(1049));
        DbConnection.em.close();
    }
}
