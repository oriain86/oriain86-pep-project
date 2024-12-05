package Service;

import DAO.AccountDAO;
import Model.Account;
import java.util.Optional;



public class AccountService {


    private final AccountDAO accountDAO = new AccountDAO();



    // Method that registers a new account. (Method that calls the accountDAO.getAccountByUsername function)


    public Optional<Account> registerAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().isEmpty()
                || account.getPassword() == null || account.getPassword().length() < 4) {
            return Optional.empty();
        }
        if (accountDAO.getAccountByUsername(account.getUsername()).isPresent()) {
            return Optional.empty();
        }
        return accountDAO.createAccount(account);
    }
    

    
    // Method that retrieves a account by the username. (Method that calls the accountDAO.getAccountByUsername function)

    public Optional<Account> loginAccount(String username, String password) {
        Optional<Account> account = accountDAO.getAccountByUsername(username);
        return account.filter(acc -> acc.getPassword().equals(password));
    }
}
