package com.example.bankofwords.service;

import com.example.bankofwords.dao.*;
import com.example.bankofwords.objects.Table;
import com.example.bankofwords.objects.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;

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

    public Table create(String tableName) {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);

        if (tableDAO.existsTable(userId, tableName)) {
            return null;
        }

        return tableDAO.createTable(userId, tableName);
    }

    public void delete(long tableId) {
        tableDAO.deleteTable(tableId);
    }

    public List<Table> initialTables() {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);

        return userId != ADMIN_USER_ID ? tableDAO.getInitialTables() : null;
    }

    public List<Table> userTables() {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);

        return tableDAO.getUserTables(userId);
    }

    public List<Word> getWords(long tableId) {
        return wordDAO.getTableWords(tableId);
    }
}
