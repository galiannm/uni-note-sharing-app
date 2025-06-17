package com.yourorg.hotanotes.controller;

import static org.hamcrest.Matchers.nullValue;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void fullCrud_withoutCategory() throws Exception {
        // CREATE without categoryId and without tags
        NoteRequest req = new NoteRequest("NoCat", "Content", null, null);
        String jsonReq = mapper.writeValueAsString(req);

        var create = mockMvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReq))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.title").value("NoCat"))
            .andExpect(jsonPath("$.content").value("Content"))
            .andExpect(jsonPath("$.categoryId").value(nullValue()))
            .andExpect(jsonPath("$.categoryName").value(nullValue()))
            .andReturn();

        long id = mapper.readTree(create.getResponse().getContentAsString())
                          .get("id").asLong();

        // READ
        mockMvc.perform(get("/api/notes/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.categoryId").value(nullValue()))
            .andExpect(jsonPath("$.categoryName").value(nullValue()));

        // UPDATE (still no category, still no tags)
        NoteRequest upd = new NoteRequest("NoCat2", "New", null, null);
        mockMvc.perform(put("/api/notes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(upd)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("NoCat2"))
            .andExpect(jsonPath("$.categoryId").value(nullValue()));

        // DELETE
        mockMvc.perform(delete("/api/notes/{id}", id))
            .andExpect(status().isNoContent());

        // VERIFY DELETE
        mockMvc.perform(get("/api/notes/{id}", id))
            .andExpect(status().isNotFound());
    }

    @Test
    void fullCrud_withCategory() throws Exception {
        // 1) Create Category
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Work\"}"))
            .andExpect(status().isCreated());

        // 2) Create Note with categoryId=1 and no tags
        NoteRequest req = new NoteRequest("WithCat", "Has notebook", 1L, null);
        String jsonReq = mapper.writeValueAsString(req);

        var create = mockMvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReq))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.categoryId").value(1))
            .andExpect(jsonPath("$.categoryName").value("Work"))
            .andReturn();

        long id = mapper.readTree(create.getResponse().getContentAsString())
                          .get("id").asLong();

        // READ and verify category fields
        mockMvc.perform(get("/api/notes/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.categoryId").value(1))
            .andExpect(jsonPath("$.categoryName").value("Work"));

        // UPDATE category to null and keep no tags
        NoteRequest upd = new NoteRequest("WithCat", "Orphan now", null, null);
        mockMvc.perform(put("/api/notes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(upd)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.categoryId").value(nullValue()))
            .andExpect(jsonPath("$.categoryName").value(nullValue()));

        // Clean up
        mockMvc.perform(delete("/api/notes/{id}", id))
            .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/categories/{id}", 1L))
            .andExpect(status().isNoContent());
    }
}
