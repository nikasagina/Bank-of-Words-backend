package com.example.bankofwords.controller;

import com.example.bankofwords.annotation.Secure;
import com.example.bankofwords.objects.User;
import com.example.bankofwords.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, Object> response = new HashMap<>();

        User user = userService.getInfo();

        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("joinDate", user.getFormattedJoinDate());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/learning")
    public ResponseEntity<?> getAllLearningWords() {
        return ResponseEntity.ok(Map.of("learning_words", userService.getAllLearnedWords()));
    }

    @GetMapping("/learned")
    public ResponseEntity<?> getAllLearnedWords() {
        return ResponseEntity.ok(Map.of("learned_words", userService.getAllLearnedWords()));
    }
}
