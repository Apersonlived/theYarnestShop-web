package com.theYarnestShop.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
		asyncSupported = true, 
		urlPatterns = { 
				"/About"
		})
public class AboutController extends HttpServlet{
	private static final long serialVersionUID = 1L;


    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("WEB-INF/jspfiles/aboutus.jsp").forward(request, response);
	}

}
