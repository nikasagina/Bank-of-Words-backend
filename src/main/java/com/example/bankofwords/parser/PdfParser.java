package com.example.bankofwords.parser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Optional;

@Component
public class PdfParser {
    private static final Pattern WORD_PATTERN = Pattern.compile("\\b\\w+\\b");

    public List<String> parsePdf(MultipartFile file) throws IOException {
        List<String> words = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String text = pdfTextStripper.getText(document);
            Arrays.stream(text.split("\\s+"))
                    .map(word -> WORD_PATTERN.matcher(word).results().map(m -> m.group().toLowerCase()).findFirst())
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(words::add);
        }
        return words;
    }
}