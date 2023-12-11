package com.example.EgarProject.repos;

import com.example.EgarProject.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepo extends JpaRepository<Document,Long> {
    Optional<Document> findByAuthor(String author);
}
