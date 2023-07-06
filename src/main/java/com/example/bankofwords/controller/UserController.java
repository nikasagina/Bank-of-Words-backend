package com.example.bankofwords.controller;

import com.example.bankofwords.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInfo(@RequestHeader("Authorization") String authHeader) {
        return userService.getInfo(authHeader);
    }

    @GetMapping("/learning")
    public ResponseEntity<?> getAllLearningWords(@RequestHeader("Authorization") String authHeader) {
        return userService.getAllLearningWords(authHeader);
    }

    @GetMapping("/learned")
    public ResponseEntity<?> getAllLearnedWords(@RequestHeader("Authorization") String authHeader) {
        return userService.getAllLearnedWords(authHeader);
    }
}
