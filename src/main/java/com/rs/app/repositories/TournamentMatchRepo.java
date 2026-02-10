package com.rs.app.repositories;

import com.rs.app.domain.entities.TournamentMatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TournamentMatchRepo extends JpaRepository<TournamentMatch, Long> {
    Page<TournamentMatch> findByTournamentId(Long id, Pageable pageable);
    Optional<TournamentMatch> findByIdAndTournamentId(Long id, Long tournamentId);
    Page<TournamentMatch> findByTournamentIdAndStartTimeAfter(Long id, LocalDateTime after, Pageable pageable);
    Page<TournamentMatch> findByTournamentIdAndStartTimeBefore(Long id, LocalDateTime before, Pageable pageable);
}
