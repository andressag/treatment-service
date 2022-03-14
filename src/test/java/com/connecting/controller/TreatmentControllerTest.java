package com.connecting.controller;

import com.connecting.entity.Treatment;
import com.connecting.services.TreatmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreatmentControllerTest {

    @Mock
    TreatmentService service;

    @InjectMocks
    TreatmentController su;

    @Test
    void shouldReturnAllTreatment() {
        final Treatment treatment = Treatment.builder().name("test 1").build();

        when(service.getAllTreatments()).thenReturn(Collections.singletonList(treatment));

        final List<Treatment> result = su.getTreatments();

        assertThat(result).hasSize(1).contains(treatment);
    }

    @Test
    void shouldReturnTreatmentWhenExists() {
        final Treatment treatment = Treatment.builder().name("test 1").build();

        when(service.getTreatmentBy("test 1")).thenReturn(ofNullable(treatment));

        final Optional<Treatment> result = su.getTreatmentsByName("test 1");

        assertThat(result).contains(treatment);
    }

    @Test
    void shouldSaveNewTreatmentWhenNameIsNotDuplicated() {
        final Treatment treatment = Treatment.builder().name("test 1").build();

        su.createTreatment(treatment);

        ArgumentCaptor<Treatment> captor = ArgumentCaptor.forClass(Treatment.class);

        verify(service).createTreatment(captor.capture());

        final Treatment captured = captor.getValue();

        assertThat(captured).isEqualTo(treatment);
    }
}