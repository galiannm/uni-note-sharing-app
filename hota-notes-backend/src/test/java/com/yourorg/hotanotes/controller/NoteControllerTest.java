package com.yourorg.hotanotes.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourorg.hotanotes.dto.NoteRequest;

@SpringBootTest
@AutoConfigureMockMvc
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void fullCrud_with_NoteResponseDto() throws Exception {
        // 1) CREATE
        NoteRequest req = new NoteRequest("Test", "DTO test");
        String createJson = mapper.writeValueAsString(req);

        var createResult = mockMvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.title").value("Test"))
            .andExpect(jsonPath("$.content").value("DTO test"))
            .andExpect(jsonPath("$.createdAt").isNotEmpty())
            .andReturn();

        // extract the generated id
        JsonNode created = mapper.readTree(createResult.getResponse().getContentAsString());
        long id = created.get("id").asLong();

        // 2) READ (GET by id)
        mockMvc.perform(get("/api/notes/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.title").value("Test"));

        // 3) UPDATE
        NoteRequest updateReq = new NoteRequest("Updated", "Still DTO");
        String updateJson = mapper.writeValueAsString(updateReq);
        mockMvc.perform(put("/api/notes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Updated"))
            .andExpect(jsonPath("$.content").value("Still DTO"));

        // 4) DELETE
        mockMvc.perform(delete("/api/notes/{id}", id))
            .andExpect(status().isNoContent());

        // 5) VERIFY DELETE (404 on fetch)
        mockMvc.perform(get("/api/notes/{id}", id))
            .andExpect(status().isNotFound());
    }
}
