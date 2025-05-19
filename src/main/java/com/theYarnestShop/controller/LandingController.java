package com.theYarnestShop.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *  @author Ekata Baral
 */
@WebServlet(
		asyncSupported = true, 
		urlPatterns = { 
				"/Landing"
		})

public class LandingController extends HttpServlet{
	private static final long serialVersionUID = 1L;


    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("WEB-INF/jspfiles/landing.jsp").forward(request, response);
	}

}
