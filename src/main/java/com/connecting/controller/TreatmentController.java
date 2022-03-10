package com.connecting.controller;

import com.connecting.entity.Treatment;
import com.connecting.services.DefaultTreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/connecting/treatments")
public class TreatmentController {

    private final DefaultTreatmentService service;

    @Autowired
    public TreatmentController(DefaultTreatmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Treatment> getAllTreatments() {
        return service.getAllTreatments();
    }


}
