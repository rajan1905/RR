package transactions;

import entity.dao.Account;
import entity.dto.TransactionDTO;
import repository.AccountRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.logging.Logger;

public class TransactionUtility {
    private static final Logger LOGGER = Logger.getAnonymousLogger();

    /*private static Function<Account, AccountDTO> buildAccountDTO = account -> AccountDTO.builder()
            .firstName(account.getFirstName())
            .middleName(account.getMiddleName())
            .lastName(account.getLastName())
            .accountNo(account.getAccountNo())
            .balance(account.getBalance())
            .build();*/

    public static BiFunction<Long, AccountRepository, Account> findAccount = ((accountNo, repository) -> {
        Account account = null;
        try{
            account = repository.getAccountByAccountNo(accountNo);
        }catch (Exception e) { e.printStackTrace(); }

        return account;
    });

    public static BiFunction<Map<Account, Double>, AccountRepository, Boolean> updateAccount = ((accounts, repository) -> {
        AtomicBoolean result = new AtomicBoolean(true);

        accounts.keySet()
                .stream()
                .forEach(account -> {
                    account.setBalance(accounts.get(account));
                    repository.updateAccountBalance(account);
                    LOGGER.info("Updated account = "+ account.getAccountNo() + " with balance : "+accounts.get(account));
                });


        return result.get();
    });

    public static BiFunction<TransactionDTO, AccountRepository, Boolean> performTransaction =
            ((transactionDTO, repository) -> {
                Boolean result = false;
                boolean isTransactionValid = TransactionRules.CHECK_TRANSACTION_VALID.apply(transactionDTO);
                if(isTransactionValid) {
                    Account accountTo = findAccount.apply(transactionDTO.getCrTo(), repository);
                    Account accountFrom = findAccount.apply(transactionDTO.getCrFrom(), repository);
                    Map<Account, Double> updates = new HashMap<>();
                    if (accountFrom != null && accountTo != null) {
                        if (transactionDTO.getAmount() < 0) {
                            updates.put(accountFrom, accountFrom.getBalance() + transactionDTO.getAmount());
                            updates.put(accountTo, accountTo.getBalance() - transactionDTO.getAmount());
                        } else {
                            updates.put(accountFrom, accountFrom.getBalance() - transactionDTO.getAmount());
                            updates.put(accountTo, accountTo.getBalance() + transactionDTO.getAmount());
                        }
                        result = updateAccount.apply(updates, repository);
                        if (result) LOGGER.info("Transaction completed");
                        else LOGGER.info("Transaction not complete");
                    } else {
                        LOGGER.info("No account found with account number : " + transactionDTO.getCrTo());
                    }
                }
                else{
                    // To-Do : Message could be more refined
                    LOGGER.info("Transaction not valid");
                }
                return result;
    });


}
