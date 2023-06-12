package com.example.bankofwords.utils;

import com.example.bankofwords.dao.UserDAO;

public class AuthValidator {
    private final UserDAO dao;

    public AuthValidator(UserDAO dao) {
        this.dao = dao;
    }

    public boolean isUsernameAvailable(String username) {
        // Implement the logic to check if the username is available
        // Return true if the username is available, false otherwise
        return dao.checkAvailability("username", username);
    }

    public boolean isPasswordStrong(String password) {
        // Implement the logic to check if the password is strong
        // Return true if the password is strong, false otherwise
        return password.length() >= 5;
    }

    public boolean isEmailAvailable(String email) {
        // Implement the logic to check if the email exists
        // Return true if the email is available, false otherwise
        return dao.checkAvailability("email", email);
    }

}
