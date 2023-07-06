package com.example.bankofwords.controller;


import com.example.bankofwords.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("api")
public class ImageController {
    private final JwtUtil jwtUtil;

    @Autowired
    public ImageController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<byte[]> image(@RequestHeader("Authorization") String authHeader, @PathVariable("filename") String filename) throws IOException {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {

            byte[] imageBytes = Files.readAllBytes(Path.of("src/main/resources/static/images/" + filename));

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