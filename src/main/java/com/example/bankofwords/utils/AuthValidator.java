package com.example.bankofwords.utils;

import com.example.bankofwords.dao.UserDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthValidator {
    private final UserDAO dao;

    public AuthValidator(UserDAO dao) {
        this.dao = dao;
    }

    private boolean isUsernameAvailable(String username) {
        return dao.checkAvailability("username", username);
    }

    private boolean isPasswordStrong(String password) {
        return password.length() >= 5;
    }

    private boolean isEmailAvailable(String email) {
        return dao.checkAvailability("email", email);
    }

    public Boolean validLogin(String username, String password) {
        if (dao.checkAvailability("username", username)) {
            return false;
        }

        return Objects.equals(dao.getPassword(username), SecurityUtils.hashPassword(password));
    }

    public List<String> checkRegisterErrors(String username, String password, String email) {
        List<String> errors = new ArrayList<>();

        if (!isUsernameAvailable(username)) {
            errors.add("Username is not available");
        }

        if (!isPasswordStrong(password)) {
            errors.add("Password is not strong");
        }

        if (!isEmailAvailable(email)) {
            errors.add("Already registered with this Email");
        }

        return errors;
    }
}
