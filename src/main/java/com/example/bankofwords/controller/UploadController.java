package com.example.bankofwords.controller;

import com.example.bankofwords.annotation.Secure;
import com.example.bankofwords.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/upload")
@Secure
public class UploadController {
    private final UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/word")
    public ResponseEntity<?> uploadWord(@RequestParam("tableId") Long tableId,
                                        @RequestParam("word") String word,
                                        @RequestParam("definition") String definition,
                                        @RequestParam(value = "image", required = false) MultipartFile image) {
        return uploadService.uploadWord(tableId, word, definition, image);
    }

    @PostMapping("/{wordId}/image")
    public ResponseEntity<?> addImageToWord(@RequestParam("tableId") Long tableId,
                                            @PathVariable("wordId") Long wordId,
                                            @RequestParam("image") MultipartFile image) {
        return uploadService.addImageToWord(tableId, wordId, image);
    }


    @PostMapping("/book")
    public ResponseEntity<?> uploadBook(@RequestParam("tableId") Long tableId,
                                        @RequestParam("file") MultipartFile file) {
        return uploadService.uploadBook(tableId, file);
    }
}
