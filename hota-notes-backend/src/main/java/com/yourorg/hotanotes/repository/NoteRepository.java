package com.yourorg.hotanotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yourorg.hotanotes.model.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    // out-of-the-box: findAll(), findById(), save(), deleteById(), etc.
}
