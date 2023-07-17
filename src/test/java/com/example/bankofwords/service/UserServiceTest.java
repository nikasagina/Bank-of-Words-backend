package com.example.bankofwords.service;

import com.example.bankofwords.dao.TableDAO;
import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.objects.Table;
import com.example.bankofwords.objects.User;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private WordDAO wordDAO;


    @Test
    public void getInfo_shouldReturnUserInfo() {
        // given
        long userId = 1L;
        User user = new User( "John", "john.doe@example.com", LocalDateTime.now());

        when(userDAO.getUserById(userId)).thenReturn(user);

        // when
        User result = userService.getInfo(userId);

        // then
        assertEquals(user, result);
    }

    @Test
    public void getAllLearningWords_shouldReturnList() {
        // given
        long userId = 1L;
        List<Word> expectedWords = List.of(
                new Word(1L, "example", "a thing characteristic of its kind or illustrating a general rule", userId),
                new Word(2L, "test", "a procedure intended to establish the quality, performance, or reliability of something", userId)
        );

        when(wordDAO.getAllLearningWords(userId)).thenReturn(expectedWords);

        // when
        List<Word> result = userService.getAllLearningWords(userId);

        // then
        assertEquals(expectedWords, result);
    }

    @Test
    public void getAllLearnedWords_shouldReturnList() {
        // given
        long userId = 1L;
        List<Word> expectedWords = List.of(
                new Word(3L, "book", "a written or printed work consisting of pages glued or sewn together along one side and bound in covers", userId),
                new Word(4L, "computer", "an electronic device that can store, retrieve, and process data", userId)
        );

        when(wordDAO.getAllLearnedWords(userId)).thenReturn(expectedWords);

        // when
        List<Word> result = userService.getAllLearnedWords(userId);

        // then
        assertEquals(expectedWords, result);
    }
}
