package com.smart.watchboard.repository;

import com.smart.watchboard.domain.Whiteboard;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WhiteboardRepository extends MongoRepository<Whiteboard, ObjectId> {
    Optional<Whiteboard> findByDocumentId(long documentId);
}
