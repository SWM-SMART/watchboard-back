package com.smart.watchboard.repository;

import com.smart.watchboard.common.enums.SocialType;
import com.smart.watchboard.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Optional<User> findById(Optional<Long> userId);
    User getById(Optional<Long> userId);
}
