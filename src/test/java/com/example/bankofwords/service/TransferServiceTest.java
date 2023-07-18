package com.example.bankofwords.service;
import static org.junit.jupiter.api.Assertions.*;


import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.example.bankofwords.dao.*;
import com.example.bankofwords.objects.Table;
import com.example.bankofwords.objects.Word;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @InjectMocks
    private TransferService transferService;

    @Mock
    private WordDAO wordDAO;

    @Mock
    private ImageDAO imageDAO;

    @Mock
    private TableDAO tableDAO;


    @Test
    void testImportTable() throws IOException {
        // Prepare test data
        String tableName = "testTable";
        String jsonContent = "{\"tableName\":\"" + tableName + "\", \"words\": [{\"word\": \"test\", \"definition\": \"test definition\", \"imageUrl\": \"test.jpg\"}]}";
        MultipartFile file = new MockMultipartFile("file", "file.json", "application/json", jsonContent.getBytes());

        when(tableDAO.getUserTables(anyLong())).thenReturn(new ArrayList<>());
        when(tableDAO.getInitialTables()).thenReturn(new ArrayList<>());
        when(tableDAO.createTable(anyLong(), any())).thenReturn(new Table(1L, 1L, tableName));
        when(wordDAO.getWordId(any(), any(), anyLong())).thenReturn(1L);

        // Call the method under test
        Map<String, Object> result = transferService.importTable(file, 1L);

        // Assertions
        assertTrue(result.containsKey("table"));
        assertEquals(tableName, ((Table) result.get("table")).getName());
    }

    @Test
    void testImportTableErrors() throws IOException {
        // Prepare test data
        String tableName = "testTable";
        String jsonContent = "{\"tableName\":\"" + tableName + "\", \"words\": [{\"word\": \"test\", \"definition\": \"test definition\", \"imageUrl\": \"test.jpg\"}]}";
        MultipartFile file = new MockMultipartFile("file", "file.json", "application/json", jsonContent.getBytes());

        when(tableDAO.getUserTables(1L)).thenReturn(List.of(new Table(1L, 1L, tableName)));

        // Call the method under test
        Map<String, Object> result = transferService.importTable(file, 1L);

        // Assertions
        assertTrue(result.containsKey("error"));
    }

    @Test
    void testExportTable() throws JsonProcessingException {
        // Prepare test data
        long tableId = 1L;
        String tableName = "testTable";
        String wordName = "test";
        String wordDefinition = "test definition";
        String imageUrl = "test.jpg";

        Table table = new Table(tableId, 1L, tableName);
        Word word = new Word(1L, wordName, wordDefinition, tableId);
        List<Word> wordList = Collections.singletonList(word);

        when(tableDAO.getTable(anyLong())).thenReturn(table);
        when(wordDAO.getTableWords(anyLong())).thenReturn(wordList);
        when(imageDAO.getImageUrl(anyLong())).thenReturn(imageUrl);

        // Call the method under test
        String result = transferService.exportTable(tableId);

        // Assertions
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> resultMap = mapper.readValue(result, Map.class);

        assertEquals(tableName, resultMap.get("tableName"));
        List<Map<String, String>> words = (List<Map<String, String>>) resultMap.get("words");
        assertEquals(wordName, words.get(0).get("word"));
        assertEquals(wordDefinition, words.get(0).get("definition"));
        assertEquals(imageUrl, words.get(0).get("imageUrl"));
    }
}
