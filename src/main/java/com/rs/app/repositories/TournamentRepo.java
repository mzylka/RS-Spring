package com.rs.app.repositories;

import com.rs.app.domain.entities.Tournament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TournamentRepo extends JpaRepository<Tournament, Long> {
    Page<Tournament> findByStartDateBeforeAndEndDateAfter(LocalDateTime now1, LocalDateTime now2, Pageable pageable);
    Page<Tournament> findByStartDateAfter(LocalDateTime now, Pageable pageable);
    Page<Tournament> findByEndDateBefore(LocalDateTime now, Pageable pageable);
    Page<Tournament> findByGameId(Long gameId, Pageable pageable);
    Page<Tournament> findByOwnerId(Long ownerId, Pageable pageable);
}
