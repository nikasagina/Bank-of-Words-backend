package com.example.bankofwords.controller;

import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.utils.SecurityUtils;
import com.example.bankofwords.utils.AuthValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@org.springframework.stereotype.Controller
public class AuthController {
    private final UserDAO userDAO;
    private final AuthValidator authValidator;

    @Autowired
    public AuthController(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.authValidator = new AuthValidator(userDAO);
    }

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model) {

        if(!authValidator.validLogin(username, password)) {
            model.addAttribute("errors", true);
            return "login";
        }

        model.addAttribute("username", username);

        return "index";
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
        List<String> errors = authValidator.checkRegisterErrors(username, password, email);

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "register";
        }

        String hash = SecurityUtils.hashPassword(password);

        boolean success = userDAO.addUser(username, hash, email);

        if (!success) {
            model.addAttribute("error", "Failed to register user");
            return "register";
        }

        // Add the form data to the model for display on a confirmation page
        model.addAttribute("username", username);
        model.addAttribute("email", email);

        return "register-confirm";
    }
}