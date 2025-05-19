package com.theYarnestShop.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.theYarnestShop.config.DatabaseConfig;
import com.theYarnestShop.dao.OrderDAO;
import com.theYarnestShop.dao.ProductDAO;
import com.theYarnestShop.model.OrderModel;
import com.theYarnestShop.model.ProductModel;

/**
 * Servlet that handles order requests,
 * It supports session and cookie-based login validation.
 * 
 * @author Ekata Baral
 */
@WebServlet("/Order")
public class OrderController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Handles order placement request
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Log cookies
		StringBuilder cookies = new StringBuilder("Cookies: ");
		if (request.getCookies() != null) {
			for (var cookie : request.getCookies()) {
				cookies.append(cookie.getName()).append("=").append(cookie.getValue()).append(", ");
			}
		}

		HttpSession session = request.getSession(false); // Use existing session only
		Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;

		// Log session attributes
		if (session != null) {
			StringBuilder sessionAttrs = new StringBuilder("Session Attributes: ");
			Enumeration<String> attrNames = session.getAttributeNames();
			while (attrNames.hasMoreElements()) {
				String attrName = attrNames.nextElement();
				sessionAttrs.append(attrName).append("=").append(session.getAttribute(attrName)).append(", ");
			}
		}

		String productId = request.getParameter("productId");
		String quantityStr = request.getParameter("quantity");
		String productPriceStr = request.getParameter("productPrice");

		if (userId == null) {
			session = request.getSession(true); // Create session to store error
			session.setAttribute("errorMessage", "Please log in to place an order.");
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}

		if (productId == null || quantityStr == null || productPriceStr == null) {
			session.setAttribute("errorMessage", "Invalid order request.");
			response.sendRedirect(request.getContextPath() + "/Products");
			return;
		}

		try {
			int quantity = Integer.parseInt(quantityStr);
			float productPrice = Float.parseFloat(productPriceStr);
			float totalPrice = productPrice * quantity;

			if (quantity <= 0) {
				session.setAttribute("errorMessage", "Invalid quantity selected.");
				response.sendRedirect(request.getContextPath() + "/Products");
				return;
			}

			String orderId = UUID.randomUUID().toString();
			String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			OrderModel order = new OrderModel(orderId, productId, orderDate, quantity, totalPrice);

			Connection conn = null;
			try {
				conn = DatabaseConfig.getDatabaseConnection();
				ProductDAO productDAO = new ProductDAO(conn);
				OrderDAO orderDAO = new OrderDAO(conn);

				ProductModel product = productDAO.getProductById(productId);
				if (product == null) {
					session.setAttribute("errorMessage", "Product not found.");
					response.sendRedirect(request.getContextPath() + "/Products");
					return;
				}

				boolean orderPlaced = orderDAO.placeOrder(userId, order, productDAO);

				if (orderPlaced) {
					session.setAttribute("orderSuccess", true);
				} else {
					session.setAttribute("errorMessage", "Not enough stock available.");
				}
			} catch (SQLException | ClassNotFoundException e) {
				session.setAttribute("errorMessage",
						"An error occurred while processing your order: " + e.getMessage());
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
					}
				}
			}
		} catch (NumberFormatException e) {
			session.setAttribute("errorMessage", "Invalid input provided.");
		}

		String redirectUrl = request.getContextPath() + "/Products";
		response.sendRedirect(redirectUrl);
	}
}