package com.theYarnestShop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.theYarnestShop.model.OrderModel;
import com.theYarnestShop.model.ProductModel;

public class OrderDAO {
    private Connection conn;
    private static final Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());

    public OrderDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Places an order by inserting records into Customer_Orders and Orders tables,
     * and updates the product stock.
     * @param userId The ID of the user placing the order
     * @param order The OrderModel containing order details
     * @param productDAO The ProductDAO to check and update stock
     * @return true if the order is successfully placed, false if insufficient stock
     * @throws SQLException If a database error occurs
     */
    public boolean placeOrder(int userId, OrderModel order, ProductDAO productDAO) throws SQLException {
        LOGGER.info("Attempting to place order: orderId=" + order.getOrder_id() + ", productId=" + order.getProduct_id() + ", userId=" + userId);

        // Check product stock
        ProductModel product = productDAO.getProductById(order.getProduct_id());
        if (product == null) {
            LOGGER.warning("Product not found: productId=" + order.getProduct_id());
            return false;
        }
        int currentStock = product.getStock();
        if (currentStock < order.getQuantity()) {
            LOGGER.warning("Insufficient stock for productId=" + order.getProduct_id() + ": required=" + order.getQuantity() + ", available=" + currentStock);
            return false;
        }

        String orderId = order.getOrder_id();
        boolean success = false;

        try {
            conn.setAutoCommit(false);
            LOGGER.info("Starting transaction for orderId=" + orderId);

            // Insert into Customer_Orders
            String sqlCustomerOrder = "INSERT INTO Customer_Orders (order_id, user_id) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCustomerOrder)) {
                stmt.setString(1, orderId);
                stmt.setInt(2, userId);
                stmt.executeUpdate();
                LOGGER.info("Inserted into Customer_Orders: orderId=" + orderId + ", userId=" + userId);
            }

            // Insert into Orders
            String sqlOrder = "INSERT INTO Orders (product_id, order_id, order_date, quantity, total_price) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlOrder)) {
                stmt.setString(1, order.getProduct_id());
                stmt.setString(2, orderId);
                stmt.setString(3, order.getOrder_date());
                stmt.setInt(4, order.getQuantity());
                stmt.setFloat(5, order.getTotal_price());
                stmt.executeUpdate();
                LOGGER.info("Inserted into Orders: orderId=" + orderId + ", productId=" + order.getProduct_id());
            }

            // Update product stock
            String sqlUpdateStock = "UPDATE Products SET stock = stock - ? WHERE product_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdateStock)) {
                stmt.setInt(1, order.getQuantity());
                stmt.setString(2, order.getProduct_id());
                int rowsAffected = stmt.executeUpdate();
                LOGGER.info("Updated stock for productId=" + order.getProduct_id() + ": rowsAffected=" + rowsAffected);
            }

            conn.commit();
            LOGGER.info("Transaction committed for orderId=" + orderId);
            success = true;
        } catch (SQLException e) {
            LOGGER.severe("Database error during order placement for orderId=" + orderId + ": " + e.getMessage());
            try {
                conn.rollback();
                LOGGER.info("Transaction rolled back for orderId=" + orderId);
            } catch (SQLException rollbackEx) {
                LOGGER.severe("Rollback failed for orderId=" + orderId + ": " + rollbackEx.getMessage());
            }
            throw e;
        } finally {
            try {
                conn.setAutoCommit(true);
                LOGGER.info("Auto-commit restored for orderId=" + orderId);
            } catch (SQLException e) {
                LOGGER.severe("Error restoring auto-commit for orderId=" + orderId + ": " + e.getMessage());
            }
        }

        return success;
    }

    public Connection getConnection() {
        return conn;
    }

    public void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
            LOGGER.info("Database connection closed");
        }
    }
}