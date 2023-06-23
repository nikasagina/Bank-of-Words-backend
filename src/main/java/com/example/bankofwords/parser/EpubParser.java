package com.example.bankofwords.parser;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public class EpubParser {
    public List<String> parseEpub(MultipartFile file) throws IOException {
        // Implement EPUB parsing logic here
        return null;
    }
}