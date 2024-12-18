package org.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MyStringBuilderTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "Hello", "12345", "null"})
    void testAppendString(String input) {
        MyStringBuilder builder = new MyStringBuilder();
        builder.append(input);
        assertEquals(input, builder.toString());
    }

    @ParameterizedTest
    @CsvSource({
            "Hello, olleH",
            "Java, avaJ",
            "12345, 54321",
            "'', ''"
    })
    void testReverse(String input, String expected) {
        MyStringBuilder builder = new MyStringBuilder();
        builder.append(input);
        builder.reverse();
        assertEquals(expected, builder.toString());
    }

    @ParameterizedTest
    @CsvSource({
            "'', ''",
            "'A', 'A'",
            "'AB', 'AB'"
    })
    void testAppendChar(String initial, String expected) {
        MyStringBuilder builder = new MyStringBuilder();
        for (char c : initial.toCharArray()) {
            builder.append(c);
        }
        assertEquals(expected, builder.toString());
    }

    @ParameterizedTest
    @CsvSource({
            "Hello, Hello World, Hello, ''",
            "A,B,A,''",
            "12345, 67890, 12345, ''"
    })
    void testUndoSequence(String first, String second, String expectedAfterUndo, String expectedFinal) {
        MyStringBuilder builder = new MyStringBuilder();
        builder.append(first);
        builder.append(second);
        builder.undo();
        assertEquals(expectedAfterUndo, builder.toString());

        builder.undo();
        assertEquals(expectedFinal, builder.toString());
    }

}