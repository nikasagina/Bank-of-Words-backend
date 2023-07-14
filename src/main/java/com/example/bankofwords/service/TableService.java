package com.example.bankofwords.service;

import com.example.bankofwords.dao.*;
import com.example.bankofwords.objects.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TableService {
    private final static long ADMIN_USER_ID = 1L;

    private final TableDAO tableDAO;
    private final WordDAO wordDAO;

    @Autowired
    public TableService(TableDAO tableDAO, WordDAO wordDAO) {
        this.tableDAO = tableDAO;
        this.wordDAO = wordDAO;
    }

    public ResponseEntity<?> create(String tableName) {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        Map<String, Object> response = new HashMap<>();

        if (tableDAO.existsTable(userId, tableName)) {
            response.put("success", false);
        } else {
            response.put("table", tableDAO.createTable(userId, tableName));
            response.put("success", true);
        }

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> delete(long tableId) {
        Map<String, Object> response = new HashMap<>();

        tableDAO.deleteTable(tableId);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> initialTables() {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        Map<String, Object> response = new HashMap<>();

        response.put("tables", userId != ADMIN_USER_ID ? tableDAO.getInitialTables() : "");

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> userTables() {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        Map<String, Object> response = new HashMap<>();

        response.put("tables", tableDAO.getUserTables(userId));

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getWords(long tableId) {
        Map<String, Object> response = new HashMap<>();

        List<Word> wordList = wordDAO.getTableWords(tableId);
        response.put("words", wordList);

        return ResponseEntity.ok(response);
    }
}
