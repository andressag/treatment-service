package com.connecting.services;

import com.connecting.entity.Treatment;
import com.connecting.repository.TreatmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreatmentServiceTest {

  @Mock TreatmentRepository repository;

  // System under test
  TreatmentService sut;

  @BeforeEach
  void setUp() {
    sut = new TreatmentService(repository);
  }

  @Test
  void shouldReturnAllTreatments() {

    final Treatment mockTest1 = Treatment.builder().name("mock test1").build();
    final Treatment mockTest2 = Treatment.builder().name("mock test2").build();
    when(repository.findAll()).thenReturn(list(mockTest1, mockTest2));

    final List<Treatment> results = sut.getAllTreatments();

    assertThat(results).hasSize(2).contains(mockTest1, mockTest2);
  }

  @Test
  void shouldReturnTreatmentsWhenNameIsFound() {

    final Treatment mockTest1 = Treatment.builder().name("mock test1").build();
    when(repository.findByName("mock name")).thenReturn(ofNullable(mockTest1));

    final Optional<Treatment> result = sut.getTreatmentBy("mock name");

    assertThat(result).isPresent().contains(mockTest1);
  }

  @Test
  void shouldCreateTreatmentWhenUniqueName() {

    final Treatment mockTest1 = Treatment.builder().name("mock test1").build();

    sut.createTreatment(mockTest1);

    ArgumentCaptor<Treatment> captor = ArgumentCaptor.forClass(Treatment.class);

    verify(repository).saveAndFlush(captor.capture());

    final Treatment captured = captor.getValue();

    assertThat(captured).isEqualTo(mockTest1);

  }

  @Test
  void shouldThrowExceptionWhenAddingAnExitingTreatmentName() {

    final Treatment treatment = Treatment.builder().name("mock test1").build();
    when(repository.findByName("mock test1")).thenReturn(Optional.ofNullable(treatment));

    assertThatThrownBy(() -> sut.createTreatment(treatment))
            .isInstanceOf(PersistenceException.class)
            .hasMessageContaining("Treatment name: " + treatment.getName() + " already exits.");

  }
}
