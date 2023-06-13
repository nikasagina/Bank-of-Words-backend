package com.example.bankofwords.controller;

import com.example.bankofwords.dao.UserDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class DashboardController {

    private final UserDAO userDAO;

    @Autowired
    public DashboardController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");

        if (username == null)
            return "redirect:/login";

        model.addAttribute("username", username);
        return "dashboard";
    }

}
