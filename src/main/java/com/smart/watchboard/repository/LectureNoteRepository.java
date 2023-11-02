package com.smart.watchboard.repository;

import com.smart.watchboard.domain.LectureNote;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LectureNoteRepository extends MongoRepository<LectureNote, Long> {
    LectureNote findByDocumentId(Long documentId);
}
