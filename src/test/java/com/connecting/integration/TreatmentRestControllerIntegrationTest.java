package com.connecting.integration;

import com.connecting.Application;
import com.connecting.entity.Treatment;
import com.connecting.repository.TreatmentRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class TreatmentRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TreatmentRepository repository;

    @BeforeEach
    void setUp() {
        final Treatment test1 = Treatment.builder().id(1L).name("test1").duration(60).build();
        final Treatment test2 = Treatment.builder().id(2L).name("test2").duration(60).build();
        repository.saveAll(Lists.list(test1, test2));
    }

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    void whenRequestedGetTreatmentsReturnAllTreatments() throws Exception {
        mvc.perform(get("/api/connecting/treatments").contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].name", is("test1")))
                .andExpect(jsonPath("$[1].name", is("test2")));
    }

    @Test
    void whenGetTreatmentsByNameExitsThenReturnTreatment() throws Exception {
        mvc.perform(get("/api/connecting/treatment/test1").contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("test1")));
    }

    @Test
    void whenGetTreatmentsByNameDoesNotExistsThenReturnEmptyTreatment() throws Exception {
        mvc.perform(get("/api/connecting/treatment/test5").contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$").doesNotExist());
    }
}
