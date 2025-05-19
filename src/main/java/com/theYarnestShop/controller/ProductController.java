package com.theYarnestShop.controller;

import com.theYarnestShop.dao.ProductDAO;
import com.theYarnestShop.model.ProductModel;
import com.theYarnestShop.util.CookieUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet that handles product listing requests such as viewing all products,
 * searching, and filtering by category. It supports session and cookie-based login validation.
 * 
 * @author Ekata Baral
 */
@WebServlet("/Products")
public class ProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDao;

    /**
     * Initializes the servlet and sets up the ProductDAO with a database connection.
     * 
     * @throws ServletException if database connection fails
     */
    @Override
    public void init() throws ServletException {
        try {
            productDao = new ProductDAO(com.theYarnestShop.config.DatabaseConfig.getDatabaseConnection());
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    /**
     * Handles GET requests for the product page.
     * Loads products based on optional search or category parameters.
     * If the user is not logged in via session or cookie, redirects to login page.
     * 
     * @param request  HttpServletRequest containing parameters like query or category
     * @param response HttpServletResponse used to send the response or redirect
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException      if an input/output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String userName = null;

        // Check session or cookie for login validation
        if (session != null && session.getAttribute("username") != null) {
            userName = (String) session.getAttribute("username");
        } else {
            Cookie usernameCookie = CookieUtil.getCookie(request, "username");
            if (usernameCookie != null) {
                userName = usernameCookie.getValue();
                session = request.getSession();
                session.setAttribute("username", userName);
            }
        }

        // If user is not logged in, redirect to login page
        if (userName == null) {
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        // Retrieve products based on query or category
        List<ProductModel> productList = null;
        try {
            String query = request.getParameter("query");
            String category = request.getParameter("category");

            if (query != null && !query.trim().isEmpty()) {
                productList = productDao.searchProducts(query.trim());
                if (category != null && !category.trim().isEmpty()) {
                    productList = productList.stream()
                            .filter(p -> p.getCategory().equalsIgnoreCase(category.trim()))
                            .toList();
                }
            } else if (category != null && !category.trim().isEmpty()) {
                productList = productDao.getProductsByCategory(category.trim());
            } else {
                productList = productDao.getAllProducts();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Failed to load products: " + e.getMessage());
        }

        request.setAttribute("productList", productList);
        request.setAttribute("username", userName);
        request.getRequestDispatcher("/WEB-INF/jspfiles/products.jsp").forward(request, response);
    }

    /**
     * Handles POST requests by delegating them to the doGet method.
     * This allows product search/filtering functionality to work on POST as well.
     * 
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Closes the ProductDAO database connection when the servlet is destroyed.
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
}
