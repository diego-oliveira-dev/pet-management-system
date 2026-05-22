package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class ValidatorTest {

    @Test
    @DisplayName("The pipeline should return a String array that contains only numbers or sequences of it")
    void isCriteriaValid() {
        String criteria = "1 e 3";
        String[] fields = Arrays.stream(criteria
                        .trim()
                        .split(" "))
                .filter(c -> c.matches("(\\d+)"))
                .toArray(String[]::new);
        String[] expectedFields = {"1", "3"};
        Assertions.assertArrayEquals(expectedFields, fields);
    }
}