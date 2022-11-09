package com.pkk.wetravelserver.repository;

import com.pkk.wetravelserver.model.ERole;
import com.pkk.wetravelserver.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
