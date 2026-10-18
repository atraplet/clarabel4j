package com.ustermetrics.clarabel4j;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.val;
import org.junit.jupiter.api.Test;

class FileOutputTest {

    @Test
    void getNameReturnsName() {
        val name = "filename.txt";
        val output = new FileOutput(name);

        assertEquals(name, output.getName());
    }
}
