package com.rs.app.repositories;

import com.rs.app.domain.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
//    Set<Role> findByUserDiscordId(String discordId);
//    Set<Role> findByUserId(Long userId);
}
