package com.example.bankofwords.service;

import com.example.bankofwords.dao.TableDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.objects.Table;
import com.example.bankofwords.objects.Word;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private TableDAO tableDAO;

    @Mock
    private WordDAO wordDAO;


    @Test
    public void createTable_shouldReturnNullIfTableExists() {
        // given
        String tableName = "MyTable";
        long userId = 1L;
        when(tableDAO.existsTable(userId, tableName)).thenReturn(true);

        // when
        Table result = tableService.create(tableName, userId);

        // then
        assertNull(result);
    }

    @Test
    public void createTable_shouldCreateNewTable() {
        // given
        long userId = 1L;
        Table table = new Table(1L, userId, "tableName");
        when(tableDAO.existsTable(userId, table.getName())).thenReturn(false);
        when(tableDAO.createTable(userId, table.getName())).thenReturn(table);

        // when
        Table result = tableService.create(table.getName(), userId);

        // then
        assertNotNull(result);
    }

    @Test
    public void deleteTable_shouldCallDeleteTableMethod() {
        // given
        long tableId = 1L;

        // when
        tableService.delete(tableId);

        // then
        verify(tableDAO).deleteTable(tableId);
    }

    @Test
    public void initialTables_shouldReturnNullForAdminUser() {
        // given
        long userId = 1L;

        // when
        List<Table> result = tableService.initialTables(userId);

        // then
        assertNull(result);
    }

    @Test
    public void initialTables_shouldReturnInitialTablesForNonAdminUser() {
        // given
        long userId = 2L;
        Table table1 = new Table(1L, userId, "tableName1");
        Table table2 = new Table(1L, userId, "tableName2");
        List<Table> expected = List.of(table1, table2);

        when(tableDAO.getInitialTables()).thenReturn(expected);

        // when
        List<Table> result = tableService.initialTables(userId);

        // then
        assertEquals(expected, result);
    }

    @Test
    public void userTables_shouldReturnUserTables() {
        // given
        long userId = 1L;
        Table table1 = new Table(1L, userId, "tableName1");
        Table table2 = new Table(1L, userId, "tableName2");
        List<Table> expected = List.of(table1, table2);
        when(tableDAO.getUserTables(userId)).thenReturn(expected);

        // when
        List<Table> result = tableService.userTables(userId);

        // then
        assertEquals(expected, result);
    }

    @Test
    public void getWords_shouldReturnTableWords() {
        // given
        long tableId = 1L;
        List<Word> expected = List.of(new Word(1L, "word", "definition", tableId));
        when(wordDAO.getTableWords(tableId)).thenReturn(expected);

        // when
        List<Word> result = tableService.getWords(tableId);

        // then
        assertEquals(expected, result);
    }
}
