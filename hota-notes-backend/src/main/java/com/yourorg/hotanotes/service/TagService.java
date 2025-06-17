package com.yourorg.hotanotes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yourorg.hotanotes.exception.ResourceNotFoundException;
import com.yourorg.hotanotes.model.Tag;
import com.yourorg.hotanotes.repository.TagRepository;

@Service
public class TagService {
    private final TagRepository repo;

    public TagService(TagRepository repo) {
        this.repo = repo;
    }

    public List<Tag> findAll() {
        return repo.findAll();
    }

    public Tag findById(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id " + id));
    }

    public Tag save(Tag tag) {
        return repo.save(tag);
    }

    public Tag update(Long id, Tag updated) {
        Tag existing = findById(id);
        existing.setName(updated.getName());
        return repo.save(existing);
    }

    public void delete(Long id) {
        Tag existing = findById(id);
        repo.delete(existing);
    }
}
