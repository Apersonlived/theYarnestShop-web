package com.theYarnestShop.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.theYarnestShop.config.DatabaseConfig;
import com.theYarnestShop.model.UserModel;

/**
 * Service class handling user registration operations and database
 * interactions. Provides methods to check user existence and register new users
 * in the system.
 */
public class RegisterService {
	private Connection databaseConn; // Database connection object

	/**
	 * Default constructor that initializes the database connection.
	 * 
	 * @throws SQLException           if database connection fails
	 * @throws ClassNotFoundException if database driver class is not found
	 */
	public RegisterService() {
		try {
			this.databaseConn = DatabaseConfig.getDatabaseConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			System.err.println("There was an error with your database connection. Error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * Checks if a username already exists in the database.
	 * 
	 * @param user_name The username to check
	 * @return true if username exists, false otherwise (or if connection fails)
	 * @throws SQLException if database query fails
	 */
	public boolean isUsernameExists(String user_name) {
		if (databaseConn == null) {
			System.err.println("Database connection not available for username check");
			return false;
		}

		String sql = "SELECT COUNT(*) FROM users WHERE user_name = ?";

		try (PreparedStatement stmt = databaseConn.prepareStatement(sql)) {
			stmt.setString(1, user_name);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0; // Returns true if count > 0
				}
			}
		} catch (SQLException e) {
			System.err.println("Error checking username: " + e.getMessage());
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Checks if an email address already exists in the database.
	 * 
	 * @param email The email to check
	 * @return true if email exists, false otherwise (or if connection fails)
	 * @throws SQLException if database query fails
	 */
	public boolean isEmailExists(String email) {
		if (databaseConn == null) {
			System.err.println("Database connection not available for email check");
			return false;
		}

		String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

		try (PreparedStatement stmt = databaseConn.prepareStatement(sql)) {
			stmt.setString(1, email);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			System.err.println("Error checking email: " + e.getMessage());
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Checks if a phone number already exists in the database.
	 * 
	 * @param phone The phone number to check
	 * @return true if phone exists, false otherwise (or if connection fails)
	 * @throws SQLException if database query fails
	 */
	public boolean isPhoneExists(String phone) {
		if (databaseConn == null) {
			System.err.println("Database connection not available for phone check");
			return false;
		}

		String sql = "SELECT COUNT(*) FROM users WHERE phone = ?";

		try (PreparedStatement stmt = databaseConn.prepareStatement(sql)) {
			stmt.setString(1, phone);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			System.err.println("Error checking phone: " + e.getMessage());
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Registers a new user in the database after performing validation checks.
	 * 
	 * @param user The UserModel object containing user details
	 * @return true if registration succeeded, false if failed or if user exists
	 * @throws SQLException          if database operation fails
	 * @throws IllegalStateException if database connection is not available
	 */
	public Boolean addUser(UserModel user) {
		if (databaseConn == null) {
			System.err.println("Database connection not available for user registration");
			return false;
		}

		System.out.println("Attempting to register user: " + user.getUser_name() + ", " + user.getEmail());

		// Validate user doesn't already exist
		if (isUsernameExists(user.getUser_name())) {
			System.err.println("Registration failed - Username exists: " + user.getUser_name());
			return false;
		}
		if (isEmailExists(user.getEmail())) {
			System.err.println("Registration failed - Email exists: " + user.getEmail());
			return false;
		}
		if (isPhoneExists(user.getPhone())) {
			System.err.println("Registration failed - Phone exists: " + user.getPhone());
			return false;
		}

		String insertQuery = "INSERT INTO users (full_name, user_name, email, phone, address, password, image_path) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement insertStmt = databaseConn.prepareStatement(insertQuery,
				Statement.RETURN_GENERATED_KEYS)) {

			// Set all parameters for the prepared statement
			insertStmt.setString(1, user.getFull_name());
			insertStmt.setString(2, user.getUser_name());
			insertStmt.setString(3, user.getEmail());
			insertStmt.setString(4, user.getPhone());
			insertStmt.setString(5, user.getAddress());
			insertStmt.setString(6, user.getPassword());
			insertStmt.setString(7, user.getImage_path());

			int rowsAffected = insertStmt.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Registration successful! Rows affected: " + rowsAffected);

				// Retrieve auto-generated user ID
				try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						user.setUser_id(generatedKeys.getInt(1));
						System.out.println("Generated user ID: " + user.getUser_id());
					}
				}
				return true;
			}

			System.out.println("Registration failed - No rows affected");
			return false;

		} catch (SQLException e) {
			System.err.println("Database error during registration: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}