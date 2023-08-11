package com.smart.watchboard.repository;

import com.smart.watchboard.domain.Whiteboard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WhiteboardRepository extends MongoRepository<Whiteboard, Long> {
    Whiteboard findByDocumentId(long documentId);
}
