package com.theYarnestShop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.theYarnestShop.model.UserModel;
import com.theYarnestShop.dao.UserDAO;
import com.theYarnestShop.util.ImageUtil;
import com.theYarnestShop.util.ValidationUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/Profile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
		maxFileSize = 5 * 1024 * 1024, // 5MB
		maxRequestSize = 10 * 1024 * 1024 // 10MB
)
public class ProfileController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;
	private ValidationUtil validationUtil;
	private ImageUtil imageUtil = new ImageUtil();

	@Override
	public void init() throws ServletException {
		userDAO = new UserDAO();
		validationUtil = new ValidationUtil();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = (String) request.getSession().getAttribute("username");

		try {
			if (username != null) {
				UserModel user = userDAO.getUserByUsername(username);
				if (user != null) {
					if (user.getImage_path() != null && !user.getImage_path().isEmpty()) {
						String applicationPath = request.getServletContext().getRealPath("");
						String imageFullPath = applicationPath + "resources" + File.separator + "image" + File.separator
								+ "user" + File.separator + user.getImage_path();
						File imageFile = new File(imageFullPath);

						if (!imageFile.exists()) {
							System.out.println("WARNING: Profile image file does not exist at: " + imageFullPath);
							user.setImage_path("default_avatar.png");
						}
					} else {
						user.setImage_path("default_avatar.png");
					}
					request.setAttribute("user", user);
					System.out.println("User fetched: " + user.getUser_name());
				} else {
					request.setAttribute("error", "User not found!");
				}
			} else {
				request.setAttribute("error", "No user logged in!");
				response.sendRedirect(request.getContextPath() + "/Login");
				return;
			}
		} catch (SQLException | ClassNotFoundException e) {
			request.setAttribute("error", "Error loading profile: " + e.getMessage());
		}
		request.getRequestDispatcher("/WEB-INF/jspfiles/profile.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		try {
			if (session == null || session.getAttribute("username") == null) {
				response.sendRedirect(request.getContextPath() + "/Login");
				return;
			}

			// Extract parameters
			String username = request.getParameter("username");
			String fullName = request.getParameter("fullName");
			String phone = request.getParameter("phoneNumber");
			String address = request.getParameter("address");
			String email = request.getParameter("email");
			String userIdStr = request.getParameter("userId");

			// Validate userId
			if (userIdStr == null || userIdStr.trim().isEmpty()) {
				throw new IllegalArgumentException("User ID is required");
			}
			Integer userId = Integer.parseInt(userIdStr);

			// Validate parameters
			String validationError = validateProfileData(fullName, username, email, phone, address);
			if (validationError != null) {
				UserModel user = new UserModel();
				user.setUser_id(userId);
				user.setFull_name(fullName);
				user.setPhone(phone);
				user.setAddress(address);
				user.setEmail(email);
				user.setUser_name(username);
				request.setAttribute("user", user);
				request.setAttribute("error", validationError);
				request.getRequestDispatcher("/WEB-INF/jspfiles/profile.jsp").forward(request, response);
				return;
			}

			// Handle image upload
			Part image = request.getPart("myfile");
			String imageName = imageUtil.getImageNameFromPart(image);
			String rootPath = "C:\\Users\\hp\\eclipse-workspace\\theYarnestShop\\src\\main\\webapp\\resources\\image/";
			String uploadSubdirectory = "user";
			boolean uploadSuccess = imageUtil.uploadImage(image, rootPath, uploadSubdirectory);

			if (!uploadSuccess) {
				System.out.println("Image upload failed for file: " + imageName);
			} else {
				System.out.println("Image uploaded: " + uploadSuccess + "for file" + imageName);
			}

			// Fetch existing user to preserve password and image_path
			UserModel user = userDAO.getUserByUsername((String) session.getAttribute("username"));
			if (user == null) {
				throw new IllegalArgumentException("User not found");
			}

			// Update user fields
			user.setUser_id(userId);
			user.setFull_name(fullName);
			user.setUser_name(username);
			user.setPhone(phone);
			user.setAddress(address);
			user.setEmail(email);
			if (imageName != null) {
				user.setImage_path(imageName);
			} else {
				user.setImage_path(imageName);
			}

			// Update user via UserDAO
			boolean success = userDAO.updateUser(user);
			if (!success) {
				throw new SQLException("Failed to update profile");
			}

			// Update session
			session.setAttribute("username", user.getUser_name());

			request.setAttribute("message", "Profile updated successfully!");
			response.sendRedirect(request.getContextPath() + "/Profile");
		} catch (Exception e) {
			request.setAttribute("error", "Error updating profile: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/jspfiles/profile.jsp").forward(request, response);
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		String username = (String) session.getAttribute("username");

		try {
			if (session == null || username == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not logged in");
				return;
			}

			boolean success = userDAO.deleteUser(username);
			if (success) {
				session.invalidate();
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("User deleted successfully");
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
			}
		} catch (SQLException | ClassNotFoundException e) {
			request.setAttribute("error", "Error deleting profile: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/jspfiles/profile.jsp").forward(request, response);
		}
	}

	private String validateProfileData(String fullName, String username, String email, String phone, String address) {
		if (validationUtil.isNullOrEmpty(fullName))
			return "Full name cannot be empty";
		if (!validationUtil.isAlphabetic(fullName))
			return "Full name can only contain letters";
		if (validationUtil.isNullOrEmpty(phone))
			return "Phone number cannot be empty";
		if (!validationUtil.isValidPhoneNumber(phone))
			return "Invalid phone number format";
		if (validationUtil.isNullOrEmpty(address))
			return "Address cannot be empty";
		if (validationUtil.isNullOrEmpty(email))
			return "Email cannot be empty";
		if (!validationUtil.isValidEmail(email))
			return "Invalid email format";
		if (validationUtil.isNullOrEmpty(username))
			return "Username cannot be empty";
		if (!validationUtil.isAlphanumeric(username))
			return "Username can only contain letters, numbers, and ._- characters";
		return null;
	}
}