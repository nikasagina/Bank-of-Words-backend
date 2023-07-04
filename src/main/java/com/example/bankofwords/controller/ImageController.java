package com.example.bankofwords.controller;


import com.example.bankofwords.dao.ImageDAO;
import com.example.bankofwords.singletons.FlashcardAnswers;
import com.example.bankofwords.singletons.UniqueIdGenerator;
import com.example.bankofwords.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

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
    public ImageController(JwtUtil jwtUtil, ResourceLoader resourceLoader, ImageDAO imageDAO) {
        this.jwtUtil = jwtUtil;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/get/image")
    public ResponseEntity<byte[]> image(@RequestHeader("Authorization") String authHeader, @RequestParam("filename") String filename) throws IOException {
        // Authenticate the user using the provided JWT token
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {

            Resource imageResource = resourceLoader.getResource("classpath:static/images/" + filename);


            byte[] imageBytes = Files.readAllBytes(imageResource.getFile().toPath());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.set("Content-Disposition",
                    "attachment; filename=\"" + UriUtils.encodePathSegment(filename, "UTF-8") + "\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageBytes);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}