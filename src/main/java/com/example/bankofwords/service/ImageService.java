package com.example.bankofwords.service;

import com.example.bankofwords.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ImageService {
    private final JwtUtil jwtUtil;

    @Autowired
    public ImageService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<byte[]> getImage(String authHeader, String filename) throws IOException {
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