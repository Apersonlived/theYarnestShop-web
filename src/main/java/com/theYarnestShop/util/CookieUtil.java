package com.theYarnestShop.util;

import java.util.Arrays;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Utility class to manage cookies for this website
 * provides methods to add, retrieve, and delete cookies
 */
public class CookieUtil {

	/**
     * Adds a cookie with the specified name, value, and maximum age setting the limit for the cookie
     *
     * @param res the HttpServletResponse to add the cookie to
     * @param name     name of the cookie
     * @param value    value of the cookie
     * @param maxAge   maximum age of the cookie in seconds
     */
	public static void addCookie(HttpServletResponse res, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setPath("/");
		res.addCookie(cookie);
	}
	
	/**
     * Retrieves a cookie by its name from the HttpServletRequest
     *
     * @param req the HttpServletRequest to get the cookie from
     * @param name    name of the cookie to retrieve
     * @returns the Cookie object if found, otherwise returns null
     */
    public static Cookie getCookie(HttpServletRequest req, String name) {
        if (req.getCookies() != null) {
            return Arrays.stream(req.getCookies())
                    .filter(cookie -> name.equals(cookie.getName()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    /**
     * Deletes a cookie by setting its max age to 0.
     *
     * @param res the HttpServletResponse to add the deletion cookie to
     * @param name     name of the cookie to delete
     */
    public static void deleteCookie(HttpServletResponse res, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/"); // Making the cookie universal throughout the site
        res.addCookie(cookie);
    }
}
