package com.theYarnestShop.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation for handling Contact Us page.
 * It provides functionality to display the contact form (GET)
 * and handle the form submission (POST).
 */
@WebServlet("/Contact")
public class ContactController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles GET requests to the Contact page.
     * Forwards the request to contactus.jsp.
     *
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException      if input/output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Display the contact form to the user
        request.getRequestDispatcher("WEB-INF/jspfiles/contactus.jsp").forward(request, response);
    }

    /**
     * Handles POST requests from the Contact form.
     * Validates input, logs the form data, and sends a success or error response.
     *
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException      if input/output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Extract form input values
            String fullName = request.getParameter("fullname");
            String email = request.getParameter("email");
            String message = request.getParameter("message");

            // 2. Validate required fields
            if (fullName == null || fullName.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Name and email are required fields.");
            }

            // 3. Log the contact form submission (for admin/audit/debug purposes)
            System.out.println("Contact form submitted:");
            System.out.println("Name: " + fullName);
            System.out.println("Email: " + email);
            System.out.println("Message: " + message);

            // 4. Add a success message to the request scope
            request.setAttribute("successMessage", "Thank you for contacting us! We'll get back to you soon.");

            // 5. Forward back to the contact form page with success notification
            request.getRequestDispatcher("/WEB-INF/jspfiles/contactus.jsp").forward(request, response);

        } catch (Exception e) {
            // Log and handle error during form processing
            System.err.println("Error processing contact form: " + e.getMessage());
            e.printStackTrace();

            // Set error message in request
            request.setAttribute("errorMessage", "There was an error processing your request. Please try again.");
            request.setAttribute("errorDetails", e.getMessage());

            // Forward to error page
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
