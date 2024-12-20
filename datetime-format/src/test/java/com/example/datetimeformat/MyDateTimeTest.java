package com.example.datetimeformat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


class MyDateTimeTest {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    @BeforeAll
    static void setUp() {
        JSON_MAPPER.registerModule(new JavaTimeModule());
    }

    @Test
    void testMyDateTime() throws JsonProcessingException {
        var myDateTime = new MyDateTime();
        myDateTime.setDateTime(LocalDateTime.of(2024, 12, 20, 15, 30, 45, 123_000_000));

        var actual = JSON_MAPPER.writeValueAsString(myDateTime);
        var expected = """
                {"dateTime":"2024:12:20##:15:30:45:123"}""";
        assertEquals(expected, actual);
    }
}