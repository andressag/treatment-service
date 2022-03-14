package com.connecting.integration;

import com.connecting.entity.Treatment;
import com.connecting.repository.TreatmentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class TreatmentRepositoryIntegrationTest {

  @Autowired private TreatmentRepository sut;

  @AfterEach
  void tearDown() {
    sut.deleteAll();
  }

  @Test
  void shouldFindTreatmentByNameIfNameExits() {

    final Treatment mockTreatment = Treatment.builder().name("mock test name").build();

    sut.saveAndFlush(mockTreatment);

    final Optional<Treatment> result = sut.findByName("mock test name");

    assertThat(result).isPresent();
    assertThat(result.get().getName()).isEqualTo(mockTreatment.getName());
  }

  @Test
  void shouldReturnEmptyWhenNameDoesNotExits() {
    Optional<Treatment> result = sut.findByName("any name");
    assertThat(result).isEmpty();
  }

  @Test
  void willUpdateTreatmentWithNewValues() {

    // saving treatment1 - id will be auto-populated
    final Treatment treatment1 = Treatment.builder().name("test name").duration(60).build();
    final Treatment treatment1Result = sut.saveAndFlush(treatment1);

    // using id from treatment1Result to change fields
    final Treatment toUpdate = Treatment.builder().id(treatment1Result.getId()).name("name updated").build();
    final Treatment updated = sut.saveAndFlush(toUpdate);

    final List<Treatment> all = sut.findAll();

    assertThat(all).hasSize(1);
    assertThat(updated.getId()).isEqualTo(treatment1Result.getId());
    assertThat(updated.getName()).isEqualTo("name updated");
    assertThat(updated.getDuration()).isZero();
  }

  @Test
  void updateOnlyHappenWhenTreatmentIsFetchAndModify() {

    final Treatment mockTreatment = Treatment.builder().name("mock test name").duration(60).build();
    final Treatment saved = sut.saveAndFlush(mockTreatment);
    final Optional<Treatment> result = sut.findById(saved.getId());
    assertThat(result).isPresent();
    final Treatment foundTreatment = result.get();

    // only name need to be updated
    foundTreatment.setName("mock name updated");

    final Treatment updated = sut.saveAndFlush(foundTreatment);

    final List<Treatment> all = sut.findAll();

    assertThat(all).hasSize(1);
    assertThat(updated.getName()).isEqualTo("mock name updated");
    assertThat(updated.getDuration()).isEqualTo(60);
  }
}
