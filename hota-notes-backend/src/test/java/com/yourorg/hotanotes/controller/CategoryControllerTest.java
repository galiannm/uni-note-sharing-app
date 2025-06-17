package com.yourorg.hotanotes.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void fullCrud_forCategories() throws Exception {
        // CREATE
        MvcResult createResult = mvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Work\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value("Work"))
            .andReturn();

        long id = mapper.readTree(createResult.getResponse()
                                               .getContentAsString())
                        .get("id").asLong();

        // READ
        mvc.perform(get("/api/categories/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Work"));

        // UPDATE
        mvc.perform(put("/api/categories/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Personal\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Personal"));

        // DELETE
        mvc.perform(delete("/api/categories/{id}", id))
            .andExpect(status().isNoContent());

        // VERIFY DELETE
        mvc.perform(get("/api/categories/{id}", id))
            .andExpect(status().isNotFound());
    }
}
