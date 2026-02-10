package com.rs.app.controllers;

import com.rs.app.dto.tournament.TournamentDTO;
import com.rs.app.dto.tournament.TournamentRequest;
import com.rs.app.dto.tournament.TournamentSimpleDTO;
import com.rs.app.dto.tournamentmatch.TournamentMatchDTO;
import com.rs.app.dto.tournamentmatch.TournamentMatchRequest;
import com.rs.app.services.TournamentService;
import com.rs.app.util.PaginationHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/tournaments")
public class TournamentController {
    private final TournamentService tournamentService;
    private final PaginationHelper paginationHelper;

    public TournamentController(TournamentService tournamentService, PaginationHelper paginationHelper) {
        this.tournamentService = tournamentService;
        this.paginationHelper = paginationHelper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentDTO> getOneTournament(@PathVariable Long id){
        return ResponseEntity.ok(tournamentService.getOne(id));
    }

    @GetMapping
    public ResponseEntity<Page<TournamentSimpleDTO>> getAllTournaments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String status
            )
    {
        Pageable pageable = paginationHelper.buildPageable(page, sortBy, direction, Set.of("title", "createdAt", "id", "startDate", "endDate"));

        Page<TournamentSimpleDTO> tournaments;
        if(Objects.equals(status, "active")){
            tournaments = tournamentService.getAllActive(pageable);
        }
        else if(Objects.equals(status, "incoming")){
            tournaments = tournamentService.getAllIncoming(pageable);
        }
        else if(Objects.equals(status, "finished")){
            tournaments = tournamentService.getAllFinished(pageable);
        }
        else {
            tournaments = tournamentService.getAll(pageable);
        }

        return ResponseEntity.ok(tournaments);
    }

    @PostMapping
    public ResponseEntity<TournamentDTO> saveTournament(@RequestBody TournamentRequest tournamentRequest, @AuthenticationPrincipal OAuth2User user){
        TournamentDTO tournament = tournamentService.save(tournamentRequest, user.getAttribute("userId"));
        return new ResponseEntity<>(tournament, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TournamentDTO> updateTournament(@RequestBody TournamentRequest tournamentRequest, @RequestParam Long id, @AuthenticationPrincipal OAuth2User user){
        TournamentDTO tournament = tournamentService.update(tournamentRequest, id);
        return ResponseEntity.ok(tournament);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTournament(@RequestParam Long id){
        tournamentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/matches")
    public ResponseEntity<Page<TournamentMatchDTO>> getTournamentMatches(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "startTime") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "incoming") String status
    )
    {
        Pageable pageable = paginationHelper.buildPageable(page, sortBy, direction, Set.of("id", "startTime"));
        Page<TournamentMatchDTO> matches;
        Set<String> types = Set.of("incoming", "past", "all");

        if(!types.contains(status)){
            throw new IllegalArgumentException("Illegal status argument!");
        }

        if (Objects.equals(status, "incoming")){
            matches = tournamentService.getTournamentIncomingMatches(id, pageable);
        }
        else if (Objects.equals(status, "past")) {
            matches = tournamentService.getTournamentPastMatches(id, pageable);
        }
        else {
            matches = tournamentService.getTournamentAllMatches(id, pageable);
        }

        return ResponseEntity.ok(matches);
    }

    @GetMapping("/{id}/matches/{matchId}")
    public ResponseEntity<TournamentMatchDTO> getTournamentMatch(@PathVariable Long id, @PathVariable Long matchId){
        TournamentMatchDTO tournamentMatch = tournamentService.getTournamentMatch(id, matchId);
        return ResponseEntity.ok(tournamentMatch);
    }

    @PostMapping("/{id}/matches")
    public ResponseEntity<TournamentDTO> saveTournamentMatch(@RequestBody TournamentMatchRequest tournamentMatchRequest, @PathVariable Long id){
        TournamentDTO tournament = tournamentService.saveNewMatch(tournamentMatchRequest, id);
        return new ResponseEntity<>(tournament, HttpStatus.CREATED);
    }
}
