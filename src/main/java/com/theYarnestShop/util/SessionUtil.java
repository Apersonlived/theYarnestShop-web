package com.theYarnestShop.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Session utility class to manage the HTTP sessions
 * provides methods to set, get and remove session attributes, or to invalidate sessions
 */
public class SessionUtil {
	/**
     * Sets an attribute in the session.
     *
     * @param req the servlet request where the session is obtained from
     * @param key     the key where the attribute is stored
     * @param value   the value of the attribute that is stored in the session
     */
    public static void setAttribute(HttpServletRequest req, String key, Object value) {
        HttpSession session = req.getSession(true);
        session.setAttribute(key, value);
    }
    
    /**
     * Retrieves an attribute from the session.
     *
     * @param req the servlet request where the session is obtained from
     * @param key     the key where the attribute is retrieved
     * @return the attribute value, or null if the attribute does not exist or if the session is invalid
     */
    public static Object getAttribute(HttpServletRequest req, String key) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            return session.getAttribute(key);
        }
        return null;
    }

    /**
     * Removes an attribute from the session.
     *
     *@param req the servlet request where the session is obtained from
     * @param key     the key where the attribute is to be removed from

     */
    public static void removeAttribute(HttpServletRequest req, String key) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute(key);
        }
    }

    /**
     * Invalidates the current session.
     *
     *@param req the servlet request where the session is obtained from
     */
    public static void invalidateSession(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}

