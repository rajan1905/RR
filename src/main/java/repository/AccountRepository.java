package repository;

import entity.dao.Account;

public interface AccountRepository {
    Account getAccountByAccountNo(Long id);
    Account saveAccount(Account account);
    Account updateAccountBalance(Account account);
    void deleteAccount(Account account);
}
