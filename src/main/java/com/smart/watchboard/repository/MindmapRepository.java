package com.smart.watchboard.repository;

import com.smart.watchboard.domain.Mindmap;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MindmapRepository extends MongoRepository<Mindmap, ObjectId> {
    Optional<Mindmap> findByDocumentId(Long documentId);
    void deleteByDocumentId(Long documentId);
}
