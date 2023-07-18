package com.example.bankofwords.service;

import com.example.bankofwords.dao.ImageDAO;
import com.example.bankofwords.dao.TableDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.objects.Table;
import com.example.bankofwords.objects.Word;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
@Slf4j
public class TransferService {
    private final WordDAO wordDAO;
    private final ImageDAO imageDAO;
    private final TableDAO tableDAO;

    @Autowired
    public TransferService(WordDAO wordDAO, ImageDAO imageDAO, TableDAO tableDAO) {
        this.wordDAO = wordDAO;
        this.imageDAO = imageDAO;
        this.tableDAO = tableDAO;
    }


    public Map<String, Object> importTable(MultipartFile file, long userId) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
        Map<String, Object> json = gson.fromJson(reader, type);

        String tableName = (String) json.get("tableName");
        Table mockTable = new Table(0, 0, tableName);

        if (tableDAO.getUserTables(userId).contains(mockTable) || tableDAO.getInitialTables().contains(mockTable)){
            log.info("Importing table failed for userId: {}, table name: {}", userId, tableName);
            return Map.of("error", "You already have a table with the same name as the imported table");
        }

        Table table = tableDAO.createTable(userId, tableName);
        long tableId = table.getTableId();
        List<Map<String, String>> words = (List<Map<String, String>>) json.get("words");

        log.info("Importing new table: {}", tableId);
        for (Map<String, String> wordMap : words) {
            String word = wordMap.get("word");
            String definition = wordMap.get("definition");
            String imageUrl = wordMap.get("imageUrl");

            wordDAO.addWord(tableId, word, definition);
            long wordId = wordDAO.getWordId(word, definition, userId);

            if (!Objects.equals(imageUrl, ""))
                imageDAO.addImage(wordId, imageUrl);
        }
        log.info("Importing table: {} has finished", tableId);
        return Map.of("table", table);
    }

    public String exportTable(long tableId) {
        Map<String, Object> response = new HashMap<>();

        Table table = tableDAO.getTable(tableId);
        List<Word> wordList = wordDAO.getTableWords(tableId);
        List<Map<String, String>> words = wordList.stream()
                .map(word -> Map.of("word", word.getWord(),
                        "definition", word.getDefinition(),
                        "imageUrl", imageDAO.getImageUrl(word.getId())))
                .toList();
        response.put("tableName", table.getName());
        response.put("words", words);

        try {
            // Convert the response to JSON
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.info("Error while processing JSON to export tableId: {}", table);
            return "";
        }
    }
}
