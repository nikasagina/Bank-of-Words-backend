package com.example.bankofwords.controller;

import com.example.bankofwords.annotation.Secure;
import com.example.bankofwords.objects.Table;
import com.example.bankofwords.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
        Table table = tableService.create(tableName);
        if (table == null)
            return ResponseEntity.ok(Map.of("error", "You already have the table with the same name"));
        return ResponseEntity.ok(Map.of("table", table));
    }

    @DeleteMapping("/delete/{tableId}")
    public void delete(@PathVariable("tableId") long tableId) {
        tableService.delete(tableId);
    }

    @GetMapping("/initial")
    public ResponseEntity<?> initialTables() {
        return ResponseEntity.ok(Map.of("tables", tableService.initialTables()));
    }


    @GetMapping("/user")
    public ResponseEntity<?> userTables() {
        return ResponseEntity.ok(Map.of("tables", tableService.userTables()));
    }


    @GetMapping("/words/{tableId}")
    public ResponseEntity<?> words(@PathVariable("tableId") long tableId) {
        return ResponseEntity.ok(Map.of("words", tableService.getWords(tableId)));
    }
}
