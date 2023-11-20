package com.smart.watchboard.repository;


import com.smart.watchboard.domain.Document;
import com.smart.watchboard.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    Note findByDocument(Document document);
}
