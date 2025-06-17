package com.yourorg.hotanotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yourorg.hotanotes.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> { }
