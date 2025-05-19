package com.theYarnestShop.filter;

import java.io.IOException;

import com.theYarnestShop.util.CookieUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter that handles authentication and authorization for all requests.
 * Redirects users based on their role and login status.
 */
@WebFilter(asyncSupported = true, urlPatterns = { "/*" })
public class AuthenticationFilter implements Filter {

	// Define constant paths for access control
	private static final String LOGIN = "/Login";
	private static final String REGISTER = "/Register";
	private static final String HOME = "/Home";
	private static final String ROOT = "/";
	private static final String SEARCH = "/Search";
	private static final String ABOUT = "/About";
	private static final String USERS = "/Users";
	private static final String USERPROFILE = "/Profile";
	private static final String ADMINPROFILE = "/AdminProfile";
	private static final String DASHBOARD = "/Dashboard";
	private static final String PRODUCTLIST = "/ProductList";
	private static final String ADDPRODUCT = "/AddProduct";
	private static final String UPDATEPRODUCT = "/EditProduct";
	private static final String PRODUCTS = "/Products";
	private static final String ORDER = "/Order";

	/**
	 * Initializes the filter (not used).
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// No initialization required
	}

	/**
	 * Main filter logic. Authenticates user via cookies and authorizes
	 * based on role (admin/customer). Redirects unauthorized users.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String uri = req.getRequestURI();
		String contextPath = req.getContextPath();

		// Normalize URI by removing context path
		if (contextPath.length() > 0 && uri.startsWith(contextPath)) {
			uri = uri.substring(contextPath.length());
		}

		// Allow public static resources to bypass filtering
		if (uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png") ||
			uri.endsWith(".jpeg") || uri.endsWith(".ttf") || uri.endsWith(".JPG") ||
			uri.endsWith(".jpg")) {
			chain.doFilter(request, response);
			return;
		}

		// Get login status and role from cookies
		Cookie usernameCookie = CookieUtil.getCookie(req, "username");
		Cookie roleCookie = CookieUtil.getCookie(req, "role");

		boolean isLoggedIn = usernameCookie != null;
		String role = roleCookie != null ? roleCookie.getValue() : null;

		// Allow access to public pages without login
		if (uri.endsWith(LOGIN) || uri.endsWith(REGISTER) || uri.endsWith(HOME) || uri.endsWith(ROOT)) {
			chain.doFilter(request, response);
			return;
		}

		// Redirect to login if not logged in or role is undefined
		if (!isLoggedIn || role == null) {
			res.sendRedirect(contextPath + LOGIN);
			return;
		}

		// Role-based access control for admin
		if ("admin".equals(role)) {
			if (uri.equals(LOGIN) || uri.equals(REGISTER)) {
				res.sendRedirect(contextPath + DASHBOARD);
			} else if (uri.equals(DASHBOARD) || uri.equals(USERS) || uri.equals(PRODUCTLIST)
					|| uri.equals(ADMINPROFILE) || uri.equals(ADDPRODUCT) || uri.equals(UPDATEPRODUCT)) {
				chain.doFilter(request, response);
			} else {
				res.sendRedirect(contextPath + DASHBOARD);
			}
			return;
		}

		// Role-based access control for customer
		if ("customer".equals(role)) {
			if (uri.equals(LOGIN) || uri.equals(REGISTER)) {
				res.sendRedirect(contextPath + HOME);
			} else if (uri.equals(HOME) || uri.equals(PRODUCTS) || uri.equals(SEARCH)
					|| uri.equals(ABOUT) || uri.equals(USERPROFILE) || uri.equals(ROOT)
					|| uri.equals(ORDER)) {
				chain.doFilter(request, response);
			} else {
				res.sendRedirect(contextPath + HOME);
			}
			return;
		}

		// Default fallback: redirect to login
		res.sendRedirect(contextPath + LOGIN);
	}

	/**
	 * Destroys the filter (not used).
	 */
	@Override
	public void destroy() {
		// No cleanup required
		Filter.super.destroy();
	}
}
