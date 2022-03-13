package com.connecting.services;

import com.connecting.entity.Treatment;
import com.connecting.repository.TreatmentRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TreatmentService {

    private final TreatmentRepository repository;

    public TreatmentService(TreatmentRepository repository) {
        this.repository = repository;
    }

    public List<Treatment> getAllTreatments() {
        return repository.findAll();
    }

    public Optional<Treatment> getTreatmentBy(String name) {
        return repository.findByName(name);
    }
}
