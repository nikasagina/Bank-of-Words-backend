package com.example.bankofwords.parser;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public class MobiParser {
    public List<String> parseMobi(MultipartFile file) throws IOException {
        // Implement MOBI parsing logic here
        return null;
    }
}