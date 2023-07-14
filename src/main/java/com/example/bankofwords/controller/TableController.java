package com.example.bankofwords.controller;

import com.example.bankofwords.annotation.Secure;
import com.example.bankofwords.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/table")
@Secure
public class TableController {
    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam("tableName") String tableName) {
        return tableService.create(tableName);
    }

    @DeleteMapping("/delete/{tableId}")
    public ResponseEntity<?> delete(@PathVariable("tableId") long tableId) {
        return tableService.delete(tableId);
    }

    @GetMapping("/initial")
    public ResponseEntity<?> initialTables() {
        return tableService.initialTables();
    }


    @GetMapping("/user")
    public ResponseEntity<?> userTables() {
        return tableService.userTables();
    }


    @GetMapping("/words/{tableId}")
    public ResponseEntity<?> words(@PathVariable("tableId") long tableId) {
        return tableService.getWords(tableId);
    }
}
