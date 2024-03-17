package dev.integration;

import dev.webserver.Sample;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void sampleTestAgainstWebServerObject() {
        assertEquals(new Sample("frank"), new Sample("frank"));
    }

}