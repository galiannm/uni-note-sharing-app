package com.yourorg.hotanotes.controller;

import java.util.List;
import java.util.Set;
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
import com.yourorg.hotanotes.model.Category;
import com.yourorg.hotanotes.model.Note;
import com.yourorg.hotanotes.model.Tag;
import com.yourorg.hotanotes.service.CategoryService;
import com.yourorg.hotanotes.service.NoteService;
import com.yourorg.hotanotes.service.TagService;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final CategoryService categoryService;
    private final TagService tagService;

    public NoteController(NoteService noteService,
                          CategoryService categoryService,
                          TagService tagService) {
        this.noteService = noteService;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    @GetMapping
    public List<NoteResponse> all() {
        return noteService.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public NoteResponse one(@PathVariable Long id) {
        Note note = noteService.findById(id);
        return toDto(note);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse create(@RequestBody NoteRequest req) {
        Note saved = noteService.save(toEntity(req));
        return toDto(saved);
    }

    @PutMapping("/{id}")
    public NoteResponse update(@PathVariable Long id,
                               @RequestBody NoteRequest req) {
        Note existing = noteService.findById(id);

        existing.setTitle(req.getTitle());
        existing.setContent(req.getContent());

        if (req.getCategoryId() != null) {
            Category cat = categoryService.findById(req.getCategoryId());
            existing.setCategory(cat);
        } else {
            existing.setCategory(null);
        }

        if (req.getTagIds() != null) {
            Set<Tag> tags = req.getTagIds().stream()
                    .map(tagService::findById)
                    .collect(Collectors.toSet());
            existing.setTags(tags);
        } else {
            existing.getTags().clear();
        }

        Note updated = noteService.save(existing);
        return toDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        noteService.delete(id);
    }

    private Note toEntity(NoteRequest req) {
        Note note = new Note();
        note.setTitle(req.getTitle());
        note.setContent(req.getContent());

        if (req.getCategoryId() != null) {
            Category cat = categoryService.findById(req.getCategoryId());
            note.setCategory(cat);
        }

        if (req.getTagIds() != null) {
            Set<Tag> tags = req.getTagIds().stream()
                    .map(tagService::findById)
                    .collect(Collectors.toSet());
            note.setTags(tags);
        }

        return note;
    }

    private NoteResponse toDto(Note note) {
        NoteResponse dto = new NoteResponse();
        dto.setId(note.getId());
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        dto.setCreatedAt(note.getCreatedAt());
        dto.setUpdatedAt(note.getUpdatedAt());

        if (note.getCategory() != null) {
            dto.setCategoryId(note.getCategory().getId());
            dto.setCategoryName(note.getCategory().getName());
        }

        if (note.getTags() != null && !note.getTags().isEmpty()) {
            dto.setTagIds(
                note.getTags().stream()
                    .map(Tag::getId)
                    .collect(Collectors.toSet())
            );
            dto.setTagNames(
                note.getTags().stream()
                    .map(Tag::getName)
                    .collect(Collectors.toSet())
            );
        }

        return dto;
    }
}
