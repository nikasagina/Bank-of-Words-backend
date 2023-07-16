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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

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
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);

        User user = userService.getInfo(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("joinDate", user.getFormattedJoinDate());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/learning")
    public ResponseEntity<?> getAllLearningWords() {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        return ResponseEntity.ok(Map.of("learning_words", userService.getAllLearnedWords(userId)));
    }

    @GetMapping("/learned")
    public ResponseEntity<?> getAllLearnedWords() {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        return ResponseEntity.ok(Map.of("learned_words", userService.getAllLearnedWords(userId)));
    }
}
