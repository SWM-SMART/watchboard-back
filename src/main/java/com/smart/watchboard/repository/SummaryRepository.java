package com.smart.watchboard.repository;

import com.smart.watchboard.domain.Document;
import com.smart.watchboard.domain.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryRepository extends JpaRepository<Summary, Long> {
    Summary findByDocument(Document document);
}
