package com.connecting.services;

import com.connecting.entity.Treatment;
import com.connecting.repository.TreatmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

@Service
public class TreatmentService {

  Logger logger = LoggerFactory.getLogger("jsonLogger");

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

  public Treatment createTreatment(Treatment treatment) {
    logger.info("my message");
    final Optional<Treatment> result = repository.findByName(treatment.getName());
    if (result.isPresent()) {
      throw new PersistenceException("Treatment name: " + treatment.getName() + " already exits.");
    }
    return repository.saveAndFlush(treatment);
  }

  public Optional<Treatment> updateTreatment(Treatment treatment) {
    final Optional<Treatment> result = repository.findById(treatment.getId());
    if (result.isPresent()) {
      final Treatment toUpdate = result.get();
      toUpdate.setName(treatment.getName());
      toUpdate.setDuration(treatment.getDuration());
      return Optional.of(repository.saveAndFlush(treatment));
    }
    return Optional.empty();
  }
}
