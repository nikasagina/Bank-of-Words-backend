package com.example.bankofwords.controller;

import com.example.bankofwords.annotation.Secure;
import com.example.bankofwords.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.io.IOException;

@RestController
@RequestMapping("api")
@Secure
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/image/{filename}")
    public ResponseEntity<byte[]> image(@PathVariable("filename") String filename) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.set("Content-Disposition",
                "attachment; filename=\"" + UriUtils.encodePathSegment(filename, "UTF-8") + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageService.getImage(filename));
    }
}