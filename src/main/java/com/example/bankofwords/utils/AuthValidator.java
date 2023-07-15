package com.example.bankofwords.utils;

import com.example.bankofwords.dao.UserDAO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AuthValidator {
    private static final String USERNAME_ERROR_MESSAGE = "Username is not available";
    private static final String PASSWORD_ERROR_MESSAGE = "Password is not strong";
    private static final String EMAIL_ERROR_MESSAGE = "Already registered with this Email";

    private final UserDAO dao;
    private final SecurityUtils securityUtils;

    public AuthValidator(UserDAO dao, SecurityUtils securityUtils) {
        this.dao = dao;
        this.securityUtils = securityUtils;
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

        return Objects.equals(dao.getPassword(username), securityUtils.hashPassword(password));
    }

    public List<String> checkRegisterErrors(String username, String password, String email) {
        List<String> errors = new ArrayList<>();

        if (!isUsernameAvailable(username)) {
            errors.add(USERNAME_ERROR_MESSAGE);
        }

        if (!isPasswordStrong(password)) {
            errors.add(PASSWORD_ERROR_MESSAGE);
        }

        if (!isEmailAvailable(email)) {
            errors.add(EMAIL_ERROR_MESSAGE);
        }

        return errors;
    }

    public static String getRegisterErrorClass(String fieldName, List<String> errors) {
        if (Objects.equals(fieldName, "username")) {
            return errors.contains(USERNAME_ERROR_MESSAGE) ? USERNAME_ERROR_MESSAGE : null;
        } else if (Objects.equals(fieldName, "password")) {
            return errors.contains(PASSWORD_ERROR_MESSAGE)? PASSWORD_ERROR_MESSAGE : null;
        } else if (Objects.equals(fieldName, "email")) {
            return errors.contains(EMAIL_ERROR_MESSAGE)? EMAIL_ERROR_MESSAGE : null;
        }

        return "";
    }
}
