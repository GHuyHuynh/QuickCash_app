package com.example.quick_cash.Credentials;

/**
 * Helper class for sign up and login page
 */
public class HelperClass {

    String username;
    String email;
    String password;

    /**
     * Empty constructor, used for pre-construction before fields are nown
     */
    public HelperClass() {}

    /**
     *
     * @param username -> username of the user
     * @param email -> email of the user
     * @param password -> password of the user
     */
    public HelperClass(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * @return -> returns the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username -> sets the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Simple getters and setters
     * @return
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
