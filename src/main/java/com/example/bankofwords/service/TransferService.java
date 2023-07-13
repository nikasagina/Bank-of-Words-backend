package com.example.bankofwords.service;

import com.example.bankofwords.dao.*;
import com.example.bankofwords.objects.Table;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class TransferService {
    private final WordDAO wordDAO;
    private final UserDAO userDAO;
    private final JwtUtil jwtUtil;
    private final ImageDAO imageDAO;
    private final TableDAO tableDAO;

    @Autowired
    public TransferService(WordDAO wordDAO, UserDAO userDAO, StatisticsDAO statisticsDAO, ImageDAO imageDAO,
                           JwtUtil jwtUtil, TableDAO tableDAO) {
        this.wordDAO = wordDAO;
        this.userDAO = userDAO;
        this.imageDAO = imageDAO;
        this.jwtUtil = jwtUtil;
        this.tableDAO = tableDAO;
    }


    public ResponseEntity<?> importTable(String authHeader, MultipartFile file) throws IOException {

        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
            Map<String, Object> json = gson.fromJson(reader, type);

            Map<String, Object> response = new HashMap<>();
            long userId = userDAO.getUserID(username);
            String tableName = (String) json.get("tableName");
            Table mockTable = new Table(0, 0, tableName);

            if (tableDAO.getUserTables(userId).contains(mockTable) || tableDAO.getInitialTables().contains(mockTable)){
                response.put("error", "You already have a table with the same name as the imported table");
                return ResponseEntity.ok(response);
            }

            Table table = tableDAO.createTable(userId, tableName);
            long tableId = table.getTableId();
            List<Map<String, String>> words = (List<Map<String, String>>) json.get("words");

            for (Map<String, String> wordMap : words) {
                String word = wordMap.get("word");
                String definition = wordMap.get("definition");
                String imageUrl = wordMap.get("imageUrl");

                wordDAO.addWord(tableId, word, definition);
                long wordId = wordDAO.getWordId(word, definition, userId);

                if (!Objects.equals(imageUrl, ""))
                    imageDAO.addImage(wordId, imageUrl);
            }

            response.put("message", "Table imported successfully.");

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<Resource> exportTable(String authHeader, String tableName) throws IOException {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);
            long tableId = tableDAO.getTableId(userId, tableName);
            Table table = tableDAO.getTable(tableId);
            List<Word> wordList = wordDAO.getTableWords(tableId);
            List<Map<String, String>> words = wordList.stream()
                    .map(word -> Map.of("word", word.getWord(),
                            "definition", word.getDefinition(),
                            "imageUrl", imageDAO.getImageUrl(word.getId())))
                    .toList();
            response.put("tableName", table.getName());
            response.put("words", words);

            // Convert the response to JSON
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(response);

            // Write the JSON data to a file
            File file = new File(table.getName() + ".json");
            FileUtils.writeStringToFile(file, json, Charset.defaultCharset());

            // Create a Resource object from the file
            Resource resource = new FileSystemResource(file);

            // Set the content type and attachment header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentDisposition(ContentDisposition.attachment().filename(file.getName()).build());

            // Return the file as a downloadable resource
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
