package com.pkk.wetravelserver.repository;

import com.pkk.wetravelserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);
    Boolean existsByUserName(String userName);
    Boolean existsByEmail(String email);
}
