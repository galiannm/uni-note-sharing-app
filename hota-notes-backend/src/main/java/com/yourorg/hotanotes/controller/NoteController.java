package com.yourorg.hotanotes.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.yourorg.hotanotes.dto.NoteRequest;
import com.yourorg.hotanotes.dto.NoteResponse;
import com.yourorg.hotanotes.model.Note;
import com.yourorg.hotanotes.service.NoteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    // GET /api/notes
    @GetMapping
    public List<NoteResponse> all() {
        return service.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    // GET /api/notes/{id}
    @GetMapping("/{id}")
    public NoteResponse one(@PathVariable Long id) {
        return toDto(service.findById(id));
    }

    // POST /api/notes  → create
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse create(@Valid @RequestBody NoteRequest req) {
        // build entity from request
        Note toSave = Note.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .build();

        // save & get back fully populated entity
        Note saved = service.save(toSave);

        return toDto(saved);
    }

    // PUT /api/notes/{id}  → update
    @PutMapping("/{id}")
    public NoteResponse update(
        @PathVariable Long id,
        @Valid @RequestBody NoteRequest req
    ) {
        Note updatedData = Note.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .build();

        Note updated = service.update(id, updatedData);
        return toDto(updated);
    }

    // DELETE /api/notes/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // helper to map your JPA entity → DTO
    private NoteResponse toDto(Note note) {
        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }
}
