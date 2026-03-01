package id.ac.ui.cs.advprog.eshop.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UuidGeneratorServiceImplTest {

    @Test
    void testGenerateId() {
        UuidGeneratorServiceImpl generator = new UuidGeneratorServiceImpl();
        String generatedId = generator.generateId();
        assertNotNull(generatedId);
        assertFalse(generatedId.isEmpty());
    }
}