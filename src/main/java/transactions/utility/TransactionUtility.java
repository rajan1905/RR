package transactions.utility;

import entity.dao.Account;
import entity.dto.AccountDTO;
import entity.dto.TransactionDTO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;

public class TransactionUtility {
    private static final Logger LOGGER = Logger.getAnonymousLogger();

    private static Function<Account, AccountDTO> buildAccountDTO = account -> AccountDTO.builder()
            .firstName(account.getFirstName())
            .middleName(account.getMiddleName())
            .lastName(account.getLastName())
            .accountNo(account.getAccountNo())
            .balance(account.getBalance())
            .build();

    public static BiFunction<Integer, EntityManager, AccountDTO> findAccount = ((accountNo, entityManager) -> {
        AccountDTO accountDTO = null;
        try{
            String getQuery = "SELECT p FROM Account p where ACCOUNTNO="+accountNo;
            TypedQuery<Account> q1 = entityManager.createQuery(getQuery, Account.class);
            Account account = q1.getSingleResult();
            accountDTO = buildAccountDTO.apply(account);
        }catch (Exception e) { e.printStackTrace(); }
        return accountDTO;
    });

    public static BiFunction<Map<AccountDTO, Double>, EntityManager, Boolean> updateAccount = ((accounts, entityManager) -> {
        AtomicInteger result = new AtomicInteger();

        accounts.keySet()
                .stream()
                .forEach(accountDTO -> {
                    entityManager.getTransaction().begin();
                    Query q1 = entityManager.createQuery("update Account set BALANCE="+
                            accounts.get(accountDTO)+" where ACCOUNTNO="+accountDTO.getAccountNo());

                    result.getAndAdd(q1.executeUpdate());
                    entityManager.getTransaction().commit();
                    LOGGER.info("Updated account = "+ accountDTO.getAccountNo() + " with balance : "+accounts.get(accountDTO));
                });
        return result.get() == accounts.keySet().size();
    });

    public static BiFunction<TransactionDTO, EntityManager, Boolean> performTransaction =
            ((transactionDTO, em) -> {
                AccountDTO accountToDTO = findAccount.apply(transactionDTO.getCrTo(), em);
                AccountDTO accountFromDTO = findAccount.apply(transactionDTO.getCrFrom(), em);
                Boolean result = false;
                Map<AccountDTO, Double> updates = new HashMap<>();
                if(accountFromDTO != null && accountToDTO != null){
                    if(transactionDTO.getAmount() < 0){
                        updates.put(accountFromDTO, accountFromDTO.getBalance()+transactionDTO.getAmount());
                        updates.put(accountToDTO, accountToDTO.getBalance()-transactionDTO.getAmount());
                    }
                    else{
                        updates.put(accountFromDTO, accountFromDTO.getBalance() - transactionDTO.getAmount());
                        updates.put(accountToDTO, accountToDTO.getBalance() + transactionDTO.getAmount());
                    }
                     result = updateAccount.apply(updates, em);
                    if(result) LOGGER.info("Transaction completed");
                    else LOGGER.info("Transaction not complete");
                }
                else{
                    LOGGER.info("No account found with account number : "+transactionDTO.getCrTo());
                }

                return result;
    });


}
