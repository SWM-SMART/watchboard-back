package com.smart.watchboard.repository;

import com.smart.watchboard.domain.Document;
import com.smart.watchboard.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByUserAndIsDeletedFalse(User user);

    Document findByDocumentId(Long documentId);
}
