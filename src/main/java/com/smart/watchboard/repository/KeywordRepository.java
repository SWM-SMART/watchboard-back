package com.smart.watchboard.repository;

import com.smart.watchboard.domain.Document;
import com.smart.watchboard.domain.Keyword;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface KeywordRepository extends MongoRepository<Keyword, ObjectId> {
    Optional<Keyword> findByDocument(Document document);
    Optional<Keyword> findByDocumentId(Long documentId);
}
