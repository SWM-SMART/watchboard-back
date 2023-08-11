package com.smart.watchboard.repository;

import com.smart.watchboard.domain.Whiteboard;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WhiteboardRepository extends MongoRepository<Whiteboard, Long> {
    Optional<Whiteboard> findByDocumentId(long documentId);
}
