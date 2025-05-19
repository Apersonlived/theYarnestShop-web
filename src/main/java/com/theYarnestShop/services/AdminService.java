package com.theYarnestShop.services;

import java.sql.*;
import java.util.*;
import com.theYarnestShop.config.DatabaseConfig;
import com.theYarnestShop.model.ProductModel;

/**
 * AdminService provides administrative reporting and product management functions
 * such as sales tracking, stock updates, and user statistics.
 */
public class AdminService {
    private Connection dbConn;
    private boolean isConnectionError = false;

    /**
     * Constructor initializes the database connection.
     * Sets a flag if the connection fails.
     */
    public AdminService() {
        try {
            dbConn = DatabaseConfig.getDatabaseConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            isConnectionError = true;
        }
    }

    /**
     * Calculates total sales for the current month.
     * 
     * @return Monthly sales revenue.
     * @throws SQLException if a database error occurs.
     */
    public double getMonthlySales() throws SQLException {
        if (isConnectionError) throw new SQLException("Database connection error");

        String query = "SELECT SUM(total_price) FROM orders " +
                       "WHERE MONTH(order_date) = MONTH(CURRENT_DATE()) " +
                       "AND YEAR(order_date) = YEAR(CURRENT_DATE())";
        try (Statement stmt = dbConn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    /**
     * Fetches products with stock quantity less than 5.
     * 
     * @return List of low-stock ProductModel instances.
     * @throws SQLException if a database error occurs.
     */
    public List<ProductModel> getLowStockProducts() throws SQLException {
        if (isConnectionError) throw new SQLException("Database connection error");

        List<ProductModel> products = new ArrayList<>();
        String query = "SELECT product_id, product_name, stock FROM products WHERE stock < 5";
        try (PreparedStatement stmt = dbConn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(new ProductModel(
                    rs.getString("product_id"),
                    rs.getString("product_name"),
                    null, null, 0, null, rs.getInt("stock")
                ));
            }
        }
        return products;
    }

    /**
     * Retrieves top 5 bestselling products.
     * 
     * @return Map of product name to quantity sold.
     * @throws SQLException if a database error occurs.
     */
    public Map<String, Integer> getBestsellers() throws SQLException {
        if (isConnectionError) throw new SQLException("Database connection error");

        Map<String, Integer> bestsellers = new LinkedHashMap<>();
        String query = "SELECT p.product_name, SUM(op.quantity) as total_sold " +
                       "FROM orders op " +
                       "JOIN products p ON op.product_id = p.product_id " +
                       "GROUP BY p.product_name ORDER BY total_sold DESC LIMIT 5";
        try (PreparedStatement stmt = dbConn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                bestsellers.put(rs.getString("product_name"), rs.getInt("total_sold"));
            }
        }
        return bestsellers;
    }

    /**
     * Counts the total number of users.
     * 
     * @return Total user count.
     * @throws SQLException if a database error occurs.
     */
    public int getTotalUsersCount() throws SQLException {
        if (isConnectionError) throw new SQLException("Database connection error");

        String query = "SELECT COUNT(*) FROM users";
        try (Statement stmt = dbConn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Calculates monthly revenue for the current year.
     * 
     * @return Map of month name to revenue.
     * @throws SQLException if a database error occurs.
     */
    public Map<String, Double> getYearlyRevenue() throws SQLException {
        if (isConnectionError) throw new SQLException("Database connection error");

        Map<String, Double> revenue = new LinkedHashMap<>();
        String query = "SELECT MONTHNAME(order_date) as month, SUM(total_price) as revenue " +
                       "FROM orders " +
                       "WHERE YEAR(order_date) = YEAR(CURRENT_DATE()) " +
                       "GROUP BY MONTH(order_date)";
        try (PreparedStatement stmt = dbConn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                revenue.put(rs.getString("month"), rs.getDouble("revenue"));
            }
        }
        return revenue;
    }

    /**
     * Closes the database connection.
     */
    public void close() {
        try {
            if (dbConn != null && !dbConn.isClosed()) {
                dbConn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
