package com.example.bankofwords.controller;

import com.example.bankofwords.annotation.Secure;
import com.example.bankofwords.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
        return transferService.importTable(file);

    }

    @GetMapping("/export/{tableId}")
    public ResponseEntity<?> exportTable(@PathVariable("tableId") long tableId) throws IOException {
        return transferService.exportTable(tableId);
    }
}
