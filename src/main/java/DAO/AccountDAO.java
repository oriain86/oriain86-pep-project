package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.Optional;



public class AccountDAO {
    
    /*
     * Data access methods using CRUD Operations
     */

    // Method to READ an account record by specific username

    public Optional<Account> getAccountByUsername(String username) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(new Account(
                        resultSet.getInt("account_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }



    // Method to CREATE a new account record.

    public Optional<Account> createAccount(Account account) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            int rows = statement.executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    account.setAccount_id(generatedKeys.getInt(1));
                    return Optional.of(account);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }



    // Method to validate if User Exists in the database

    public boolean isValidUser(int userId) {
        String sql = "SELECT COUNT(*) FROM account WHERE account_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Returns true if a user with the given ID exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
