package com.theYarnestShop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.theYarnestShop.model.UserModel;
import com.theYarnestShop.services.UsersService;
import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class UsersController
 * 
 * This controller handles the retrieval and display of all user records from the database.
 * It interacts with the UsersService to fetch user data and forwards it to the JSP view layer.
 * In case of errors, it forwards the request to an error page with an appropriate message.
 * 
 * URL mapping: /Users
 * 
 * @author Ekata Baral
 */
@WebServlet("/Users")
public class UsersController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UsersService userService;

    /**
     * Initializes the UsersController servlet and sets up the UsersService.
     * Called once when the servlet is first loaded.
     *
     * @throws ServletException if initialization fails
     */
    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UsersService();
    }

    /**
     * Handles HTTP GET requests to retrieve and display a list of all users.
     * If retrieval is successful, forwards the data to users.jsp.
     * If an error occurs, forwards the request to an error.jsp page.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            List<UserModel> users = userService.getAllUsers();
            if (users == null) {
                throw new Exception("Unable to retrieve users from database");
            }
            request.setAttribute("users", users);
            request.getRequestDispatcher("/WEB-INF/jspfiles/users.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error loading user list: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jspfiles/error.jsp").forward(request, response);
        }
    }

    /**
     * Releases resources used by the servlet. Called once when the servlet is being taken out of service.
     * Closes the UsersService.
     */
    @Override
    public void destroy() {
        if (userService != null) {
            userService.close();
        }
        super.destroy();
    }
}
