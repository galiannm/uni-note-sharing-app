package com.yourorg.hotanotes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yourorg.hotanotes.exception.ResourceNotFoundException;
import com.yourorg.hotanotes.model.Category;
import com.yourorg.hotanotes.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public List<Category> findAll() {
        return repo.findAll();
    }

    public Category findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id" + id));
    }

    public Category save(Category category) {
        return repo.save(category);
    }

    public Category update(Long id, Category updated) {
        Category existing = findById(id);
        existing.setName(updated.getName());
        return repo.save(existing); 
    }

    public void delete(Long id){
        Category existing = findById(id); 
        repo.delete(existing);
    }
}
