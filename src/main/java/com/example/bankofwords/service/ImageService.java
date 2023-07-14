package com.example.bankofwords.service;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ImageService {

    public ResponseEntity<byte[]> getImage(String filename) throws IOException {
        byte[] imageBytes = Files.readAllBytes(Path.of("src/main/resources/static/images/" + filename));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.set("Content-Disposition",
                "attachment; filename=\"" + UriUtils.encodePathSegment(filename, "UTF-8") + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageBytes);
    }
}