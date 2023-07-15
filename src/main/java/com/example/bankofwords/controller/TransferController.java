package com.example.bankofwords.controller;

import com.example.bankofwords.annotation.Secure;
import com.example.bankofwords.service.TransferService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

@RestController
@RequestMapping("api/transfer")
@Secure
public class TransferController {
    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/import")
    public ResponseEntity<?> importTable(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(transferService.importTable(file));

    }

    @GetMapping("/export/{tableId}")
    public ResponseEntity<?> exportTable(@PathVariable("tableId") long tableId) throws IOException {
        String response = transferService.exportTable(tableId);

        // Write the JSON data to a file
        File file = new File("table" + tableId + ".json");
        FileUtils.writeStringToFile(file, response, Charset.defaultCharset());

        // Create a Resource object from the file
        Resource resource = new FileSystemResource(file);

        // Set the content type and attachment header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDisposition(ContentDisposition.attachment().filename(file.getName()).build());

        // Return the file as a downloadable resource
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .body(resource);
    }
}
