package com.connecting.integration;

import com.connecting.Application;
import com.connecting.config.PostgresqlContainer;
import com.connecting.entity.Treatment;
import com.connecting.repository.TreatmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = Application.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TreatmentRestControllerIntegrationTest {

  @ClassRule
  public static PostgreSQLContainer postgreSQLContainer = PostgresqlContainer.getInstance();

  @Autowired private MockMvc mvc;

  @Autowired private TreatmentRepository repository;

  @BeforeAll
  void setUp() {
    final Treatment test1 = Treatment.builder().name("test1").duration(60).build();
    final Treatment test2 = Treatment.builder().name("test2").duration(60).build();
    repository.saveAll(Lists.list(test1, test2));
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
    mvc.perform(get("/api/connecting/treatment/test-1").contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  void whenPostNewTreatmentsThenSaveAndReturn() throws Exception {
    mvc.perform(
            post("/api/connecting/treatment/create")
                .content(asJsonString(Treatment.builder().name("test3").duration(30).build()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("test3")));
  }

  @Test
  void whenPostNewTreatmentsWithExistingNameThrowError() throws Exception {
    mvc.perform(
            post("/api/connecting/treatment/create")
                .content(asJsonString(Treatment.builder().name("test2").duration(30).build()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void whenPutTreatmentsWithExitingIdThenUpdateAndReturn() throws Exception {
    mvc.perform(
            put("/api/connecting/treatment/update")
                .content(
                    asJsonString(Treatment.builder().id(1L).name("test1").duration(15).build()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.duration", is(15)));
  }

  private String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
