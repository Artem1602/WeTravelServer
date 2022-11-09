package com.pkk.wetravelserver.repository;

import com.pkk.wetravelserver.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserData, Long> {
}
