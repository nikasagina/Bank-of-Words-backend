package com.example.bankofwords.controller;

import com.example.bankofwords.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/transfer")
public class TransferController {
    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/import")
    public ResponseEntity<?> importTable(@RequestHeader("Authorization") String authHeader,
                                         @RequestParam("file") MultipartFile file) throws IOException {
        return transferService.importTable(authHeader, file);

    }

    @GetMapping("/export/{tableId}")
    public ResponseEntity<?> exportTable(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable("tableId") long tableId) throws IOException {
        return transferService.exportTable(authHeader, tableId);
    }
}
