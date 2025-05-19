package com.theYarnestShop.dao;

import com.theYarnestShop.config.DatabaseConfig;
import com.theYarnestShop.model.UserModel;
import java.sql.*;

/**
 * Data Access Object (DAO) for performing operations on the users table.
 */
public class UserDAO {

    /**
     * Retrieves a user by their username from the database.
     * 
     * @param username The username of the user to retrieve.
     * @return A populated UserModel object if found, or null otherwise.
     * @throws SQLException If a database access error occurs.
     * @throws ClassNotFoundException If the database driver class is not found.
     */
    public UserModel getUserByUsername(String username) throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM users WHERE user_name = ?";
        try (Connection conn = DatabaseConfig.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserModel(
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("user_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("password"),
                    rs.getString("image_path")
                );
            }
        }
        return null;
    }

    /**
     * Updates an existing user's information in the database.
     * 
     * @param user A UserModel object containing the updated user information.
     * @return true if the user was successfully updated; false otherwise.
     * @throws SQLException If a database access error occurs.
     * @throws ClassNotFoundException If the database driver class is not found.
     */
    public boolean updateUser(UserModel user) throws SQLException, ClassNotFoundException {
        String query = "UPDATE users SET full_name = ?, user_name = ?, email = ?, phone = ?, address = ?, image_path = ?, password = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getFull_name());
            stmt.setString(2, user.getUser_name());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getImage_path());
            stmt.setString(7, user.getPassword());
            stmt.setInt(8, user.getUser_id());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Deletes a user from the database based on their username.
     * 
     * @param username The username of the user to delete.
     * @return true if the user was successfully deleted; false otherwise.
     * @throws SQLException If a database access error occurs.
     * @throws ClassNotFoundException If the database driver class is not found.
     */
    public boolean deleteUser(String username) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM users WHERE user_name = ?";
        try (Connection conn = DatabaseConfig.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
