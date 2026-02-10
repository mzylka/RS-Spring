package com.rs.app.services;

import com.rs.app.domain.entities.Game;
import com.rs.app.domain.entities.Tournament;
import com.rs.app.domain.entities.TournamentMatch;
import com.rs.app.domain.entities.User;
import com.rs.app.dto.tournament.TournamentDTO;
import com.rs.app.dto.tournament.TournamentRequest;
import com.rs.app.dto.tournament.TournamentSimpleDTO;
import com.rs.app.dto.tournamentmatch.TournamentMatchDTO;
import com.rs.app.dto.tournamentmatch.TournamentMatchRequest;
import com.rs.app.mappers.TournamentMapper;
import com.rs.app.mappers.TournamentMatchMapper;
import com.rs.app.repositories.GameRepo;
import com.rs.app.repositories.TournamentMatchRepo;
import com.rs.app.repositories.TournamentRepo;
import com.rs.app.repositories.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TournamentService {
    private final TournamentRepo repo;
    private final TournamentMapper mapper;
    private final TournamentMatchRepo tournamentMatchRepo;
    private final TournamentMatchMapper tournamentMatchMapper;
    private final GameRepo gameRepo;
    private final UserRepo userRepo;

    public TournamentService(TournamentRepo repo, TournamentMapper mapper, TournamentMatchRepo tournamentMatchRepo, TournamentMatchMapper tournamentMatchMapper, GameRepo gameRepo, UserRepo userRepo) {
        this.repo = repo;
        this.mapper = mapper;
        this.tournamentMatchRepo = tournamentMatchRepo;
        this.tournamentMatchMapper = tournamentMatchMapper;
        this.gameRepo = gameRepo;
        this.userRepo = userRepo;
    }

    @PostAuthorize("")
    public TournamentDTO getOne(Long id){
        Tournament tournament = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Tournament hasn't been found"));
        return mapper.toDto(tournament);
    }

    public Page<TournamentSimpleDTO> getAll(Pageable pageable){
        return repo.findAll(pageable).map(mapper::toSimpleDto);
    }

    public TournamentDTO save(TournamentRequest tournamentRequest, Long ownerId){
        Tournament tournament = mapper.fromRequest(tournamentRequest);
        if (tournamentRequest.gameId() != null){
            Game game = gameRepo.findById(tournamentRequest.gameId()).orElseThrow(() -> new EntityNotFoundException("Game hasn't been found"));
            tournament.setGame(game);
        }
        User owner = userRepo.findById(ownerId).orElseThrow(()-> new EntityNotFoundException("Owner hasn't been found"));
        tournament.setOwner(owner);
        Tournament saved = repo.save(tournament);
        return  mapper.toDto(saved);
    }

    public TournamentDTO update(TournamentRequest tournamentRequest, Long id){
        Tournament tournament = repo.findById(id).orElseThrow(()-> new EntityNotFoundException("Tournament hasn't been found"));
        if (tournamentRequest.gameId() != null){
            Game game = gameRepo.findById(tournamentRequest.gameId()).orElseThrow(() -> new EntityNotFoundException("Game hasn't been found"));
            tournament.setGame(game);
        }
        tournament.setTitle(tournamentRequest.title());
        tournament.setThumbnail(tournamentRequest.thumbnail());
        tournament.setThumbnailMin(tournamentRequest.thumbnailMin());
        tournament.setContent(tournamentRequest.content());
        tournament.setFormat(tournamentRequest.format());
        tournament.setLadderUrl(tournamentRequest.ladderUrl());
        tournament.setStreamUrl(tournamentRequest.streamUrl());
        tournament.setVodsUrl(tournamentRequest.vodsUrl());
        tournament.setStartDate(tournamentRequest.startDate());
        tournament.setEndDate(tournamentRequest.endDate());

        Tournament saved = repo.save(tournament);
        return mapper.toDto(saved);
    }

    public void delete(Long id){
        repo.deleteById(id);
    }

    public Page<TournamentSimpleDTO> getAllIncoming(Pageable pageable){
        Page<Tournament> tournaments = repo.findByStartDateAfter(LocalDateTime.now(), pageable);
        return tournaments.map(mapper::toSimpleDto);
    }

    public Page<TournamentSimpleDTO> getAllActive(Pageable pageable){
        Page<Tournament> tournaments = repo.findByStartDateBeforeAndEndDateAfter(LocalDateTime.now(), LocalDateTime.now(), pageable);
        return tournaments.map(mapper::toSimpleDto);
    }

    public Page<TournamentSimpleDTO> getAllFinished(Pageable pageable){
        Page<Tournament> tournaments = repo.findByEndDateBefore(LocalDateTime.now(), pageable);
        return tournaments.map(mapper::toSimpleDto);
    }

    public Page<TournamentMatchDTO> getTournamentAllMatches(Long id, Pageable pageable){
        return tournamentMatchRepo.findByTournamentId(id, pageable).map(tournamentMatchMapper::toDto);
    }

    public Page<TournamentMatchDTO> getTournamentIncomingMatches(Long id, Pageable pageable){
        Page<TournamentMatch> tournamentMatches = tournamentMatchRepo.findByTournamentIdAndStartTimeBefore(id, LocalDateTime.now(), pageable);
        return tournamentMatches.map(tournamentMatchMapper::toDto);
    }

    public Page<TournamentMatchDTO> getTournamentPastMatches(Long id, Pageable pageable){
        Page<TournamentMatch> tournamentMatches = tournamentMatchRepo.findByTournamentIdAndStartTimeAfter(id, LocalDateTime.now(), pageable);
        return tournamentMatches.map(tournamentMatchMapper::toDto);
    }

    public TournamentDTO saveNewMatch(TournamentMatchRequest tournamentMatchRequest, Long id){
        Tournament tournament = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Tournament hasn't been found"));
        TournamentMatch tournamentMatch = tournamentMatchMapper.fromRequest(tournamentMatchRequest);
        tournament.getMatches().add(tournamentMatch);
        Tournament saved = repo.save(tournament);
        return mapper.toDto(saved);
    }

    public TournamentMatchDTO getTournamentMatch(Long id, Long matchId){
        TournamentMatch tournamentMatch = tournamentMatchRepo.findByIdAndTournamentId(matchId, id).orElseThrow(() -> new EntityNotFoundException("Match hasn't been found for the tournament"));
        return tournamentMatchMapper.toDto(tournamentMatch);
    }

    public Page<TournamentSimpleDTO> getAllTournamentsByGame(Long gameId, Pageable pageable){
        return repo.findByGameId(gameId, pageable).map(mapper::toSimpleDto);
    }

    public Page<TournamentSimpleDTO> getAllTournamentsByOwner(Long ownerId, Pageable pageable){
        return repo.findByOwnerId(ownerId, pageable).map(mapper::toSimpleDto);
    }
}
