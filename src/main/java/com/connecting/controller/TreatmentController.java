package com.connecting.controller;

import com.connecting.entity.Treatment;
import com.connecting.services.TreatmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
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

  @PostMapping("/treatment/create")
  public ResponseEntity<Object> createTreatment(@RequestBody Treatment treatment) {
    try {
      final Treatment result = service.createTreatment(treatment);
      return ResponseEntity.ok(result);
    } catch (PersistenceException e) {
      return ResponseEntity.badRequest()
          .body("Could not create treatment, name: " + treatment.getName() + " is not unique");
    }
  }

  @PutMapping("/treatment/update")
  public ResponseEntity<Object> updateTreatment(@RequestBody Treatment treatment) {
      final Optional<Treatment> result = service.updateTreatment(treatment);
    return result.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest()
            .body("Could not find treatment name: " + treatment.getName() + " with id: " + treatment.getId()));

  }
}
