import database.DbConnection;
import entity.dao.Account;
import entity.dto.AccountDTO;
import entity.dto.TransactionDTO;
import org.junit.Before;
import org.junit.Test;
import repository.AccountRepository;
import repository.AccountRepositoryImpl;
import transactions.TransactionRules;
import transactions.TransactionUtility;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTesting {

    private AccountRepository repository ;
    private Long one = new Long(1);
    private Long two =  new Long(2);
    private Account xA, yA;

    @Before
    public void setup(){
        repository = new AccountRepositoryImpl(DbConnection.getEntityManager());
        TransactionRules.init(DbConnection.getEntityManager());
    }


    public void clearDB(){
        if(xA != null) repository.deleteAccount(xA);
        if(xA != null) repository.deleteAccount(yA);
        AccountDTO x = AccountDTO.builder()
                .accountNo(one)
                .balance(new Double(100))
                .firstName("Joe")
                .lastName("X")
                .build();

         xA = getAccountFromDTO(x);

        AccountDTO y = AccountDTO.builder()
                .accountNo(two)
                .balance(new Double(999))
                .firstName("Alice")
                .lastName("Y")
                .build();

        yA = getAccountFromDTO(y);

        repository.saveAccount(xA);
        repository.saveAccount(yA);
    }

    @Test
    public void transferFromXToYWithValidRules(){
        clearDB();
        TransactionDTO transaction = TransactionDTO.builder()
                .amount(new Double(50))
                .crFrom(one)
                .crTo(two)
                .build();

        TransactionUtility.performTransaction.apply(transaction, repository);

        Account xN = TransactionUtility.findAccount.apply(one, repository);
        Account yN = TransactionUtility.findAccount.apply(two, repository);


        assertEquals(xN.getBalance(), new Double(50));
        assertEquals(yN.getBalance(), new Double(1049));
    }

    @Test
    public void transferFromXToYWithFromAndToAsSameAccount(){
        clearDB();
        TransactionDTO transaction = TransactionDTO.builder()
                .amount(new Double(0))
                .crFrom(one)
                .crTo(one)
                .build();

        TransactionUtility.performTransaction.apply(transaction, repository);

        Account xN = TransactionUtility.findAccount.apply(one, repository);
        Account yN = TransactionUtility.findAccount.apply(two, repository);


        assertEquals(xN.getBalance(), new Double(100));
        assertEquals(yN.getBalance(), new Double(999));
    }

    @Test
    public void transferFromXToYWithToAccountNotPresent(){
        clearDB();
        TransactionDTO transaction = TransactionDTO.builder()
                .amount(new Double(0))
                .crFrom(one)
                .crTo(new Long(123))
                .build();

        TransactionUtility.performTransaction.apply(transaction, repository);

        Account xN = TransactionUtility.findAccount.apply(one, repository);
        Account yN = TransactionUtility.findAccount.apply(two, repository);


        assertEquals(xN.getBalance(), new Double(100));
        assertEquals(yN.getBalance(), new Double(999));
    }

    @Test
    public void transferFromXToYWithZeroAsTransactionBalance(){
        clearDB();
        TransactionDTO transaction = TransactionDTO.builder()
                .amount(new Double(0))
                .crFrom(one)
                .crTo(two)
                .build();

        TransactionUtility.performTransaction.apply(transaction, repository);

        Account xN = TransactionUtility.findAccount.apply(one, repository);
        Account yN = TransactionUtility.findAccount.apply(two, repository);


        assertEquals(xN.getBalance(), new Double(100));
        assertEquals(yN.getBalance(), new Double(999));
    }

    private Account getAccountFromDTO(AccountDTO accountDTO){
        Account account = new Account();
        account.setFirstName(accountDTO.getFirstName());
        account.setMiddleName(accountDTO.getMiddleName());
        account.setLastName(accountDTO.getLastName());
        account.setAccountNo(accountDTO.getAccountNo());
        account.setBalance(accountDTO.getBalance());
        return account;
    }
}
