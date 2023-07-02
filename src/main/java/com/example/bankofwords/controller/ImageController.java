package com.example.bankofwords.controller;


import com.example.bankofwords.singletons.FlashcardAnswers;
import com.example.bankofwords.singletons.UniqueIdGenerator;
import com.example.bankofwords.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("api")
public class ImageController {

    private final JwtUtil jwtUtil;
    private final ResourceLoader resourceLoader;

    @Autowired
    public ImageController(JwtUtil jwtUtil, ResourceLoader resourceLoader) {
        this.jwtUtil = jwtUtil;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/get/image")
    public ResponseEntity<Resource> image(@RequestHeader("Authorization") String authHeader, @RequestParam("id") long flashcard_id) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            String imageName = FlashcardAnswers.getInstance().getAnswer(flashcard_id) + ".jpg";
            Resource resource = resourceLoader.getResource("classpath:static/images/" + imageName);

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageName + "\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
