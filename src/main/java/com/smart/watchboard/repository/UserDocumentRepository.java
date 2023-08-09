package com.smart.watchboard.repository;

import com.smart.watchboard.domain.UserDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDocumentRepository extends JpaRepository<UserDocument, Long> {

}
