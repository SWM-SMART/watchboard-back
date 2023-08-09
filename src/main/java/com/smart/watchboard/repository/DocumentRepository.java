package com.smart.watchboard.repository;

import com.smart.watchboard.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT d FROM Document d JOIN UserDocument ud ON d.documentId = ud.document.documentId WHERE ud.user.id = :userId AND d.isDeleted = false")
    List<Document> findDocumentsByUserId(@Param("userId") Long userId);
}
