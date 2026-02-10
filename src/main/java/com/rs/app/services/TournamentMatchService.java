package com.rs.app.services;

import com.rs.app.domain.entities.TournamentMatch;
import com.rs.app.dto.tournamentmatch.TournamentMatchDTO;
import com.rs.app.dto.tournamentmatch.TournamentMatchRequest;
import com.rs.app.mappers.TournamentMatchMapper;
import com.rs.app.repositories.TournamentMatchRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TournamentMatchService {
    private final TournamentMatchRepo repo;
    private final TournamentMatchMapper tournamentMatchMapper;

    public TournamentMatchService(TournamentMatchRepo repo, TournamentMatchMapper tournamentMatchMapper) {
        this.repo = repo;
        this.tournamentMatchMapper = tournamentMatchMapper;
    }

    public TournamentMatchDTO getOne(Long id){
        TournamentMatch tournamentMatch = repo.findById(id).orElseThrow(()-> new EntityNotFoundException("Tournament Match hasn't been found"));
        return tournamentMatchMapper.toDto(tournamentMatch);
    }

    public Page<TournamentMatchDTO> getAll(Pageable pageable){
        return repo.findAll(pageable).map(tournamentMatchMapper::toDto);
    }

    public TournamentMatchDTO update(TournamentMatchRequest tournamentMatchRequest, Long id){
        TournamentMatch tournamentMatch = repo.findById(id).orElseThrow(()-> new EntityNotFoundException("Tournament Match hasn't been found"));
        tournamentMatch.setPlayer1(tournamentMatchRequest.player1());
        tournamentMatch.setPlayer2(tournamentMatchRequest.player2());
        tournamentMatch.setStartTime(tournamentMatchRequest.startTime());
        tournamentMatch.setStreamUrl(tournamentMatchRequest.streamUrl());

        TournamentMatch updated = repo.save(tournamentMatch);
        return tournamentMatchMapper.toDto(updated);
    }

    public void delete(Long id){
        repo.deleteById(id);
    }
}
