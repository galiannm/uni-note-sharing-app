package com.yourorg.hotanotes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yourorg.hotanotes.exception.ResourceNotFoundException;
import com.yourorg.hotanotes.model.Note;
import com.yourorg.hotanotes.repository.NoteRepository;

@Service
public class NoteService {
    private final NoteRepository repo;

    public NoteService(NoteRepository repo) {
        this.repo = repo;
    }

    public List<Note> findAll() {
        return repo.findAll();
    }

    public Note findById(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new ResourceNotFoundException("Note not found with id" + id));
    }

    public Note save(Note note) {
        return repo.save(note);
    }

    public Note update(Long id, Note updatedData) {
        Note existing = findById(id);
        existing.setTitle(updatedData.getTitle());
        existing.setContent(updatedData.getContent());
        return repo.save(existing); // updatedAt will change via @PreUpdate
    }

    public void delete(Long id){
        Note existing = findById(id); // throw 404 if absent
        repo.delete(existing);
    }
}
