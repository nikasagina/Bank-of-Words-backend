package com.example.bankofwords.controller;

import com.example.bankofwords.annotation.Secure;
import com.example.bankofwords.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@Secure
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInfo() {
        return userService.getInfo();
    }

    @GetMapping("/learning")
    public ResponseEntity<?> getAllLearningWords() {
        return userService.getAllLearningWords();
    }

    @GetMapping("/learned")
    public ResponseEntity<?> getAllLearnedWords() {
        return userService.getAllLearnedWords();
    }
}
