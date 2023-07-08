package com.example.bankofwords.service;

import com.example.bankofwords.dao.*;
import com.example.bankofwords.parser.PdfParser;
import com.example.bankofwords.utils.JwtUtil;
import com.example.bankofwords.utils.WordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TableService {

    private final UserDAO userDAO;
    private final JwtUtil jwtUtil;
    private final TableDAO tableDAO;

    @Autowired
    public TableService(UserDAO userDAO, JwtUtil jwtUtil, TableDAO tableDAO) {
        this.userDAO = userDAO;
        this.jwtUtil = jwtUtil;
        this.tableDAO = tableDAO;
    }

    public ResponseEntity<?> create(String authHeader, String tableName) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);
            tableDAO.createTable(userId, tableName);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<?> delete(String authHeader, String tableName) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);
            long tableId = tableDAO.getTableId(userId, tableName);
            tableDAO.deleteTable(tableId);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<?> initialTables(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            response.put("tables", tableDAO.getInitialTables());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<?> userTables(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);
            response.put("tables", tableDAO.getUserTables(userId));

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
