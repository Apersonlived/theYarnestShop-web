package com.theYarnestShop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.theYarnestShop.model.ProductModel;

/**
 * This class handles all database operations related to products.
 * It allows inserting, updating, deleting, and retrieving product details
 * from the products table in the database.
 */
public class ProductDAO {
    private Connection conn;

    /**
     * Constructor to initialize the DAO with a database connection.
     * @param conn The active database connection
     */
    public ProductDAO(Connection conn) throws SQLException {
        this.conn = conn;
    }

    /**
     * Updates an existing product in the database using its ID.
     * Takes a ProductModel object and updates all its fields.
     */
    public void updateProduct(ProductModel product) throws SQLException {
        String sql = "UPDATE products SET product_name = ?, category = ?, description = ?, price = ?, image = ?, stock = ? WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProduct_name());
            stmt.setString(2, product.getCategory());
            stmt.setString(3, product.getDescription());
            stmt.setFloat(4, product.getPrice());
            stmt.setString(5, product.getImage());
            stmt.setInt(6, product.getStock());
            stmt.setString(7, product.getProduct_id());
            stmt.executeUpdate();
        }
    }

    /**
     * Inserts a new product into the database.
     * Takes a ProductModel object and saves its fields into the database.
     */
    public void insertProduct(ProductModel product) throws SQLException {
        String sql = "INSERT INTO products (product_id, product_name, category, description, price, image, stock) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProduct_id());
            stmt.setString(2, product.getProduct_name());
            stmt.setString(3, product.getCategory());
            stmt.setString(4, product.getDescription());
            stmt.setFloat(5, product.getPrice());
            stmt.setString(6, product.getImage());
            stmt.setInt(7, product.getStock());
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves a product from the database based on its ID.
     * Returns the ProductModel if found, or null otherwise.
     */
    public ProductModel getProductById(String productId) throws SQLException {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ProductModel product = new ProductModel();
                    product.setProduct_id(rs.getString("product_id"));
                    product.setProduct_name(rs.getString("product_name"));
                    product.setCategory(rs.getString("category"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getFloat("price"));
                    product.setImage(rs.getString("image"));
                    product.setStock(rs.getInt("stock"));
                    return product;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all products from the database.
     * Returns a list of ProductModel objects.
     */
    public List<ProductModel> getAllProducts() throws SQLException {
        List<ProductModel> productList = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ProductModel product = new ProductModel();
                product.setProduct_id(rs.getString("product_id"));
                product.setProduct_name(rs.getString("product_name"));
                product.setCategory(rs.getString("category"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getFloat("price"));
                product.setImage(rs.getString("image"));
                product.setStock(rs.getInt("stock"));
                productList.add(product);
            }
        }
        return productList;
    }

    /**
     * Deletes a product from the database using its ID.
     * Executes a DELETE statement on the product_id.
     */
    public void deleteProduct(String productId) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productId);
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves products from the database that match a specific category.
     * Returns a list of products filtered by category.
     */
    public List<ProductModel> getProductsByCategory(String category) throws SQLException {
        List<ProductModel> productList = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProductModel product = new ProductModel();
                    product.setProduct_id(rs.getString("product_id"));
                    product.setProduct_name(rs.getString("product_name"));
                    product.setCategory(rs.getString("category"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getFloat("price"));
                    product.setImage(rs.getString("image"));
                    product.setStock(rs.getInt("stock"));
                    productList.add(product);
                }
            }
        }
        return productList;
    }

    /**
     * Retrieves a product from the database by its name.
     * Returns a ProductModel if a match is found, null otherwise.
     */
    public ProductModel getProductByName(String productName) throws SQLException {
        String sql = "SELECT * FROM products WHERE product_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ProductModel product = new ProductModel();
                    product.setProduct_id(rs.getString("product_id"));
                    product.setProduct_name(rs.getString("product_name"));
                    product.setCategory(rs.getString("category"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getFloat("price"));
                    product.setImage(rs.getString("image"));
                    product.setStock(rs.getInt("stock"));
                    return product;
                }
            }
        }
        return null;
    }

    /**
     * Searches for products whose name contains the given keyword.
     * Returns a list of matching ProductModel objects.
     */
    public List<ProductModel> searchProducts(String keyword) throws SQLException {
        List<ProductModel> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE product_name LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProductModel p = new ProductModel();
                p.setProduct_id(rs.getString("product_id"));
                p.setProduct_name(rs.getString("product_name"));
                p.setCategory(rs.getString("category"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getFloat("price"));
                p.setImage(rs.getString("image"));
                p.setStock(rs.getInt("stock"));
                list.add(p);
            }
        }
        return list;
    }

    /**
     * Returns the current database connection.
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * Closes the database connection if it's open.
     */
    public void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
