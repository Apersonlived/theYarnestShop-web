package com.theYarnestShop.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.theYarnestShop.config.DatabaseConfig;
import com.theYarnestShop.dao.ProductDAO;
import com.theYarnestShop.model.ProductModel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet controller for listing and deleting products.
 * Handles both product display (GET) and delete operations (POST).
 * @ author Ekata Baral
 */
@WebServlet("/ProductList")
public class ProductListController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDao;

    /**
     * Initializes the ProductDAO with a database connection.
     * 
     * @throws ServletException if database connection fails
     */
    @Override
    public void init() throws ServletException {
        try {
            productDao = new ProductDAO(DatabaseConfig.getDatabaseConnection());
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    /**
     * Closes the database connection when the servlet is destroyed.
     */
    @Override
    public void destroy() {
        try {
            if (productDao != null) {
                productDao.closeConnection();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    /**
     * Handles GET requests to display the product list.
     * Supports optional search via "query" parameter.
     * 
     * @param request  HTTP request containing optional "query" parameter
     * @param response HTTP response forwarding to product list JSP
     * @throws ServletException if servlet error occurs
     * @throws IOException      if I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<ProductModel> productList = null;
        try {
            String query = request.getParameter("query");
            if (query != null && !query.trim().isEmpty()) {
                productList = productDao.searchProducts(query.trim());
                System.out.println("Searched for '" + query + "'. Found " + productList.size() + " products.");
            } else {
                productList = productDao.getAllProducts();
                System.out.println("Fetched all products. Total: " + productList.size());
            }

            request.setAttribute("productList", productList);
            request.getRequestDispatcher("/WEB-INF/jspfiles/productlist.jsp").forward(request, response);
        } catch (SQLException e) {
            System.out.println("SQL Error while fetching products: " + e.getMessage());
            productList = new ArrayList<>(); // Default to empty list on SQL error
            request.setAttribute("productList", productList);
            request.setAttribute("errorMessage", "Failed to fetch products due to a database error.");
            request.getRequestDispatcher("/WEB-INF/jspfiles/productlist.jsp").forward(request, response);
        }
    }

    /**
     * Handles POST requests to delete a product based on provided productId.
     * The deletion is triggered when the "action" parameter equals "delete".
     * 
     * @param request  HTTP request containing "action" and "productId"
     * @param response HTTP response redirecting back to ProductList
     * @throws ServletException if servlet error occurs
     * @throws IOException      if I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            String productId = request.getParameter("productId");

            if (productId != null && !productId.trim().isEmpty()) {
                try {
                    productDao.deleteProduct(productId);
                    HttpSession session = request.getSession();
                    session.setAttribute("successMessage", "Product successfully deleted!");
                } catch (SQLException e) {
                    System.err.println("Error deleting product: " + e.getMessage());
                    HttpSession session = request.getSession();
                    session.setAttribute("errorMessage", "Failed to delete product: " + e.getMessage());
                }
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("errorMessage", "Invalid product ID provided for deletion.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/ProductList");
    }
}
