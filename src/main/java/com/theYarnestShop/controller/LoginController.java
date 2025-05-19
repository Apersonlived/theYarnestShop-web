package com.theYarnestShop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.theYarnestShop.model.UserModel;
import com.theYarnestShop.services.LoginService;
import com.theYarnestShop.util.CookieUtil;
import com.theYarnestShop.util.RedirectionUtil;
import com.theYarnestShop.util.SessionUtil;
import com.theYarnestShop.util.ValidationUtil;

/**
 * 
 * @author Ekata Baral
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/Login" })
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ValidationUtil validationUtil;
    private LoginService loginService;

    /**
     * Constructor initializes the LoginService.
     */
    public LoginController() {
        this.loginService = new LoginService();
        this.validationUtil = new ValidationUtil();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!response.isCommitted()) {
            request.getRequestDispatcher(RedirectionUtil.loginUrl).forward(request, response);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest req, HttpServletResponse res)
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        UserModel user = new UserModel(username, password);
        UserModel authenticatedUser = loginService.loginUser(user);

        if (!validationUtil.isNullOrEmpty(username) && !validationUtil.isNullOrEmpty(password)) {
            if (authenticatedUser != null) {
                // Set session attributes
                SessionUtil.setAttribute(req, "username", username);
                SessionUtil.setAttribute(req, "user", authenticatedUser);
                SessionUtil.setAttribute(req, "userId", authenticatedUser.getUser_id()); // Set user_id in session

                // Set cookies
                CookieUtil.addCookie(res, "username", username, 60 * 30);

                if (username.equals("ekata")) {
                    SessionUtil.setAttribute(req, "role", "admin");
                    CookieUtil.addCookie(res, "role", "admin", 60 * 30);
                    res.sendRedirect(req.getContextPath() + "/Dashboard");
                } else {
                    SessionUtil.setAttribute(req, "role", "customer");
                    CookieUtil.addCookie(res, "role", "customer", 60 * 30);
                    res.sendRedirect(req.getContextPath() + "/Products");
                }
            } else {
                System.out.println("❌ Login failed for user: " + username);
                handleLoginFailure(req, res, authenticatedUser != null);
            }
        } else {
            System.out.println("❌ Invalid input for user: " + username);
            handleLoginFailure(req, res, false);
        }
    }

    /**
     * Handles login failures by setting attributes and forwarding to the login page.
     *
     * @param req         HttpServletRequest object
     * @param res         HttpServletResponse object
     * @param loginStatus Boolean indicating the login status
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    private void handleLoginFailure(HttpServletRequest req, HttpServletResponse resp, Boolean loginStatus)
            throws ServletException, IOException {
        String errorMessage;
        if (loginStatus == null) {
            errorMessage = "User does not exist.";
        } else if (!loginStatus) {
            errorMessage = "User credential mismatch. Please try again!";
        } else {
            errorMessage = "Our server is under maintenance. Please try again later!";
        }
        req.setAttribute("error", errorMessage);
        req.getRequestDispatcher(RedirectionUtil.loginUrl).forward(req, resp);
    }
}