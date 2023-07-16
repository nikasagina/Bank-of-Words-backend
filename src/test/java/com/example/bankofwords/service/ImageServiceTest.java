package com.example.bankofwords.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Test
    void whenGetImageRequestAndFileExists_returnsResponse() throws IOException {
        byte[] response = imageService.getImage("cat.jpg"); // cat.jpg must exist

        assertNotNull(response);
    }

    @Test
    void whenImageDoesNotExist_assertThrowsIOException() {
        assertThrows(IOException.class, () -> imageService.getImage(""));
    }

}
