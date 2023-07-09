package com.example.bankofwords.controller;

import com.example.bankofwords.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/image/{filename}")
    public ResponseEntity<byte[]> image(@RequestHeader("Authorization") String authHeader, @PathVariable("filename") String filename) throws IOException {
        return imageService.getImage(authHeader, filename);
    }
}