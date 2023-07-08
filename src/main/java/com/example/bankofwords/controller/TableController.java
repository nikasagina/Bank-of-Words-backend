package com.example.bankofwords.controller;

import com.example.bankofwords.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/table")
public class TableController {
    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String authHeader,
                                    @RequestParam("tableName") String tableName) {
        return tableService.create(authHeader, tableName);
    }

    @DeleteMapping("/delete/{tableId}")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String authHeader,
                                    @PathVariable("tableId") long tableId) {
        return tableService.delete(authHeader, tableId);
    }

    @GetMapping("/initial")
    public ResponseEntity<?> initialTables(@RequestHeader("Authorization") String authHeader) {
        return tableService.initialTables(authHeader);
    }


    @GetMapping("/user")
    public ResponseEntity<?> userTables(@RequestHeader("Authorization") String authHeader) {
        return tableService.userTables(authHeader);
    }


    @GetMapping("/words/{tableId}")
    public ResponseEntity<?> words(@RequestHeader("Authorization") String authHeader,
                                   @PathVariable("tableId") long tableId) {
        return tableService.getWords(authHeader, tableId);
    }
}
