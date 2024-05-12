package com.logistn.IdentityService.repository;

import com.logistn.IdentityService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String s);

    Optional<User> findByUsername(String username);
}
