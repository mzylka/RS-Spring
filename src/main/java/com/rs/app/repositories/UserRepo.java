package com.rs.app.repositories;

import com.rs.app.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByDiscordId(String discordId);
    Optional<User> findByDiscordName(String discordName);

    @Query("select u from User u join fetch u.roles r where u.discordId=?1")
    Optional<User> findByDiscordIdWithRoles(String discordId);
    Page<User> findByDiscordNameContainingIgnoreCase(String discordName, Pageable pageable);
}
