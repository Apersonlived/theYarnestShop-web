package com.theYarnestShop.services;

import java.sql.Connection;
import java.sql.SQLException;

import com.theYarnestShop.config.DatabaseConfig;
import com.theYarnestShop.dao.UserDAO;
import com.theYarnestShop.model.UserModel;
import com.theYarnestShop.util.PasswordUtil;

public class LoginService {
    private Connection databaseConfig;
    private boolean isConnectionError = false;
    private UserDAO userDAO;

    public LoginService() {
        try {
            databaseConfig = DatabaseConfig.getDatabaseConnection();
            userDAO = new UserDAO();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            isConnectionError = true;
        }
    }

    /**
     * Validates the user credentials against the database records and returns the user if valid.
     *
     * @param user the UserModel object containing user credentials
     * @return UserModel if credentials are valid, null otherwise
     */
    public UserModel loginUser(UserModel user) {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return null;
        }

        try {
            UserModel dbUser = userDAO.getUserByUsername(user.getUser_name());
            if (dbUser != null && validatePassword(dbUser, user)) {
                return dbUser; // Return user with user_id
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    /**
     * Validates the password against the database record.
     *
     * @param dbUser the UserModel from the database
     * @param user the UserModel containing user credentials
     * @return true if the passwords match, false otherwise
     */
    private boolean validatePassword(UserModel dbUser, UserModel user) {
        String dbPassword = dbUser.getPassword();
        String dbUsername = dbUser.getUser_name();
        return dbUsername.equals(user.getUser_name()) &&
               PasswordUtil.decrypt(dbPassword, dbUsername).equals(user.getPassword());
    }
}