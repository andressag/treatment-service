package com.connecting.integration;

import com.connecting.entity.Treatment;
import com.connecting.repository.TreatmentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class TreatmentRepositoryIntegrationTest {

  @Autowired private TreatmentRepository repository;

  @AfterEach
  void tearDown() {
    repository.deleteAll();
  }

  @Test
  void shouldFindTreatmentByNameIfNameExits() {

    final Treatment mockTreatment = Treatment.builder().name("mock test name").build();

    repository.saveAndFlush(mockTreatment);

    final Optional<Treatment> result = repository.findByName("mock test name");

    assertThat(result).isPresent();
    assertThat(result.get().getName()).isEqualTo(mockTreatment.getName());
  }

  @Test
  void shouldReturnEmptyWhenNameDoesNotExits() {
    Optional<Treatment> result = repository.findByName("any name");
    assertThat(result).isEmpty();
  }

}
