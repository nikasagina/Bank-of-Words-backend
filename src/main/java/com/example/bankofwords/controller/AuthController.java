package com.example.bankofwords.controller;

import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.utils.SecurityUtils;
import com.example.bankofwords.utils.AuthValidator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;


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
    public String home(HttpSession session) {
        if (session.getAttribute("username") != null){
            return "redirect:/dashboard";
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("username") != null){
            return "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              HttpSession session,
                              Model model) {
        if(!authValidator.validLogin(username, password)) {
            model.addAttribute("errors", true);
            return new RedirectView("/login");
        }

        session.setAttribute("username", username);

        return new RedirectView("/dashboard");  // Redirect to the dashboard page
    }

    @GetMapping("/register")
    public String register(HttpSession session) {
        if (session.getAttribute("username") != null){
            return "redirect:/dashboard";
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("email") String email,
                               Model model) {
        List<String> errors = authValidator.checkRegisterErrors(username, password, email);

        System.out.println(authValidator.getRegisterErrorClass("username", errors));
        if (!errors.isEmpty()) {
            model.addAttribute("usernameErrorClass", authValidator.getRegisterErrorClass("username", errors));
            model.addAttribute("passwordErrorClass", authValidator.getRegisterErrorClass("password", errors));
            model.addAttribute("emailErrorClass", authValidator.getRegisterErrorClass("email", errors));
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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session.getAttribute("username") != null){
            session.removeAttribute("username");
        }

        return "redirect:/";
    }
}