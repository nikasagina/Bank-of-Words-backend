package com.example.bankofwords.controller;

import com.example.bankofwords.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/upload")
public class UploadController {
    private final UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/word")
    public ResponseEntity<?> uploadWord(@RequestHeader("Authorization") String authHeader,
                                        @RequestParam("word") String word,
                                        @RequestParam("definition") String definition,
                                        @RequestParam(value = "image", required = false) MultipartFile image) {
        return uploadService.uploadWord(authHeader, word, definition, image);
    }

    @PostMapping("/{word}/image")
    public ResponseEntity<?> addImageToWord(@RequestHeader("Authorization") String authHeader,
                                            @PathVariable("word") String word,
                                            @RequestParam("image") MultipartFile image) {
        return uploadService.addImageToWord(authHeader, word, image);
    }


    @PostMapping("/book")
    public ResponseEntity<?> uploadBook(@RequestHeader("Authorization") String authHeader,
                                        @RequestParam("file") MultipartFile file) {
        return uploadService.uploadBook(authHeader, file);
    }
}
