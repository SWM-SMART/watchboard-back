package com.smart.watchboard.repository;

import com.smart.watchboard.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
    File findByDocument(Long documentId);
}
