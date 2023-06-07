package com.example.bankofwords.controller;

import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.utils.SecurityUtils;
import com.example.bankofwords.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {
    private final UserDAO userDAO;
    private final UserValidator userValidator;

    @Autowired
    public Controller(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.userValidator = new UserValidator(userDAO);
    }

    @RequestMapping("/")
    public String home(Principal principal) {
        if (principal != null){
            return "index";
        } else {
            return "login";
        }
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("email") String email,
                               Model model) {
        List<String> errors = new ArrayList<>();

        // Check if the username is available
        if (!userValidator.isUsernameAvailable(username)) {
            errors.add("Username is not available");
        }

        // Check if the password is strong
        if (!userValidator.isPasswordStrong(password)) {
            errors.add("Password is not strong");
        }

        // Check if the email exists
        if (!userValidator.isEmailAvailable(email)) {
            errors.add("Already registered with this Email");
        }

        // If there are errors, display them on the registration page
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "register";
        }

        // All validations passed, process the form data (e.g., save it to a database)
        String hash = SecurityUtils.hashPassword(password);

        boolean success = userDAO.addUser(username, hash, email);

        // Check if the user addition was successful
        if (!success) {
            model.addAttribute("error", "Failed to register user");
            return "register";
        }

        // Add the form data to the model for display on a confirmation page
        model.addAttribute("username", username);
        model.addAttribute("email", email);

        // Return the name of the confirmation page
        return "register-confirm";
    }


}