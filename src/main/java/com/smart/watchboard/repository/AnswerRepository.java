package com.smart.watchboard.repository;

import com.smart.watchboard.domain.Answer;
import com.smart.watchboard.domain.Keyword;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AnswerRepository extends MongoRepository<Answer, Long> {
    Optional<Answer> findByDocumentIdAndKeyword(Long documentId, String keyword);
}
