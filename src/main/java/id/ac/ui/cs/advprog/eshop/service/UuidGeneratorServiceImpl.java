package id.ac.ui.cs.advprog.eshop.service;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UuidGeneratorServiceImpl implements IdGeneratorService {
    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}