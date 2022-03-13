package com.connecting.controller;

import com.connecting.entity.Treatment;
import com.connecting.services.TreatmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/connecting")
public class TreatmentController {

    private final TreatmentService service;

    public TreatmentController(TreatmentService service) {
        this.service = service;
    }

    @GetMapping("/treatments")
    public List<Treatment> getTreatments() {
        return service.getAllTreatments();
    }

    @GetMapping("/treatment/{name}")
    public Optional<Treatment> getTreatmentsByName(@PathVariable String name) {
        return service.getTreatmentBy(name);

    }

}
