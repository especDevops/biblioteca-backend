package com.biblioteca;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;  // Add this import

@SpringBootTest
@ActiveProfiles("test")
class BibliotecaApplicationTests {
    @Test
    void contextLoads() {
    }
}