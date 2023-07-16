package com.example.bankofwords.controller;

import com.example.bankofwords.annotation.Secure;
import com.example.bankofwords.objects.Table;
import com.example.bankofwords.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

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
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        Table table = tableService.create(tableName, userId);
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
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        return ResponseEntity.ok(Map.of("tables", tableService.initialTables(userId)));
    }


    @GetMapping("/user")
    public ResponseEntity<?> userTables() {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        return ResponseEntity.ok(Map.of("tables", tableService.userTables(userId)));
    }


    @GetMapping("/words/{tableId}")
    public ResponseEntity<?> words(@PathVariable("tableId") long tableId) {
        return ResponseEntity.ok(Map.of("words", tableService.getWords(tableId)));
    }
}
