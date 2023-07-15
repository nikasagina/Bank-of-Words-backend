package com.example.bankofwords.service;


import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ImageService {

    public byte[] getImage(String filename) throws IOException {
        return Files.readAllBytes(Path.of("src/main/resources/static/images/" + filename));
    }
}