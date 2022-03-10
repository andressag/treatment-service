package com.connecting.services;

import com.connecting.entity.Treatment;
import com.connecting.repository.TreatmentRepository;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class DefaultTreatmentService {

    private final TreatmentRepository repository;

    public List<Treatment> getAllTreatments() {
        return availableTreatments();
    }

    public Treatment getTreatmentBy(String name) {
        final List<Treatment> treatments = availableTreatments();

        return treatments.stream()
                    .filter(treatment -> treatment.getName().equalsIgnoreCase(name))
                    .findAny().orElse(null);
    }

    private List<Treatment> availableTreatments() {
        final List<Treatment> treatments = new LinkedList<>();
        treatments.add(new Treatment("botox", 250, 30));
        treatments.add(new Treatment("feeler", 850, 60));
        return treatments;
    }
}
