package com.yourorg.hotanotes.dto;

import java.time.Instant;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteResponse {
   private Long id;
   private String title;
   private String content;
   private Instant createdAt;
   private Instant updatedAt; 

   private Long categoryId;
   private String categoryName;

   private Set<Long> tagIds;
   private Set<String> tagNames;
}
