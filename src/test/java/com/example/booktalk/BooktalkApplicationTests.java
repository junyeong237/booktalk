package com.example.booktalk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BooktalkApplicationTests {

    @Test
    void tempTest() {

        int a = 1 + 2;
        assertEquals(3, a);

    }

}
