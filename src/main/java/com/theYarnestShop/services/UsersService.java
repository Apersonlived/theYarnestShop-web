package com.theYarnestShop.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.theYarnestShop.config.DatabaseConfig;
import com.theYarnestShop.model.UserModel;

import jakarta.servlet.ServletException;

public class UsersService {
    private Connection connection;
    
    public UsersService() throws ServletException {
        try {
            this.connection = DatabaseConfig.getDatabaseConnection();
            if (this.connection == null || this.connection.isClosed()) {
                throw new ServletException("Failed to establish database connection");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            throw new ServletException("Database connection error", ex);
        }
    }
    
    public List<UserModel> getAllUsers() throws SQLException {
        List<UserModel> users = new ArrayList<>();
        String query = "SELECT user_id, full_name, user_name, email, phone, address, image_path FROM users";
        
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet result = stmt.executeQuery()) {
            
            while (result.next()) {
                users.add(extractUserFromResultSet(result));
            }
        }
        return users;
    }
    
    private UserModel extractUserFromResultSet(ResultSet result) throws SQLException {
        UserModel user = new UserModel();
        user.setUser_id(result.getInt("user_id"));
        user.setFull_name(result.getString("full_name"));
        user.setUser_name(result.getString("user_name"));
        user.setEmail(result.getString("email"));
        user.setPhone(result.getString("phone"));
        user.setAddress(result.getString("address"));
        user.setImage_path(result.getString("image_path"));
        return user;
    }
    
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}