package com.theYarnestShop.model;

/**
 * Represents a user entity in the system with all relevant user information.
 * This model class is used to transfer user data between different layers of the application.
 */
public class UserModel {
    // Field declarations with comments
    private Integer user_id;        // Unique identifier for the user
    private String full_name;       // User's full name
    private String user_name;       // Unique username for login/identification
    private String email;           // User's email address
    private String phone;           // User's phone number
    private String address;         // User's physical address
    private String password;        // Encrypted password
    private String image_path;      // Path to user's profile image

    /**
     * Default constructor. Creates an empty UserModel instance.
     * Typically used when constructing user objects from data sources.
     */
    public UserModel() {
        super();
        // Initialization of fields will be done through setters
    }

    /**
     * Constructor for basic authentication purposes.
     * @param user_name The username for login
     * @param password The user's password (should be encrypted)
     */
    public UserModel(String user_name, String password) {
        super();
        this.user_name = user_name;
        this.password = password;
    }

    /**
     * Constructor for new user registration.
     * @param full_name User's full name
     * @param user_name Unique username
     * @param email User's email address
     * @param phone User's phone number
     * @param address User's physical address
     * @param password Encrypted password
     * @param image_path Path to profile image
     */
    public UserModel(String full_name, String user_name, String email, 
                    String phone, String address, String password, 
                    String image_path) {
        super();
        this.full_name = full_name;
        this.user_name = user_name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.image_path = image_path;
    }

    /**
     * Complete constructor with all fields including user_id.
     * Typically used when retrieving existing user from database.
     * @param user_id Unique database identifier
     * @param full_name User's full name
     * @param user_name Unique username
     * @param email User's email address
     * @param phone User's phone number
     * @param address User's physical address
     * @param password Encrypted password
     * @param image_path Path to profile image
     */
    public UserModel(Integer user_id, String full_name, String user_name, 
                    String email, String phone, String address,
                    String password, String image_path) {
        super();
        this.user_id = user_id;
        this.full_name = full_name;
        this.user_name = user_name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.image_path = image_path;
    }

    /**
     * Alternate complete constructor with different parameter order.
     * Provided for flexibility in object creation.
     * @param user_name Unique username
     * @param user_id Unique database identifier
     * @param full_name User's full name
     * @param email User's email address
     * @param phone User's phone number
     * @param address User's physical address
     * @param password Encrypted password
     * @param image_path Path to profile image
     */
    public UserModel(String user_name, Integer user_id, String full_name, 
                    String email, String phone, String address, 
                    String password, String image_path) {
        super();
        this.user_id = user_id;
        this.full_name = full_name;
        this.user_name = user_name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.image_path = image_path;
    }

    // ========== GETTER AND SETTER METHODS ========== //

    /**
     * @return The unique user identifier
     */
    public Integer getUser_id() {
        return user_id;
    }

    /**
     * @param user_id The unique user identifier to set
     */
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    /**
     * @return The username
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * @return The user's full name
     */
    public String getFull_name() {
        return full_name;
    }

    /**
     * @param full_name The full name to set
     */
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    /**
     * @param user_name The username to set
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone The phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return The physical address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address The physical address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return The encrypted password (Note: should never return plain text)
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password The password to set (should be encrypted before setting)
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return The path to the profile image
     */
    public String getImage_path() {
        return image_path;
    }

    /**
     * @param image_path The profile image path to set
     */
    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}