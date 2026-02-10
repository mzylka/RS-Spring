package com.rs.app.controllers;

import com.rs.app.dto.tournamentmatch.TournamentMatchDTO;
import com.rs.app.dto.tournamentmatch.TournamentMatchRequest;
import com.rs.app.services.TournamentMatchService;
import com.rs.app.util.PaginationHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/tournament-matches")
public class TournamentMatchController {
    private final TournamentMatchService tournamentMatchService;
    private final PaginationHelper paginationHelper;

    public TournamentMatchController(TournamentMatchService tournamentMatchService, PaginationHelper paginationHelper) {
        this.tournamentMatchService = tournamentMatchService;
        this.paginationHelper = paginationHelper;
    }

    @GetMapping
    public ResponseEntity<Page<TournamentMatchDTO>> getAllTournamentMatches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    )
    {
        Pageable pageable = paginationHelper.buildPageable(page, sortBy, direction, Set.of("id", "startDate"));
        Page<TournamentMatchDTO> tournamentMatches = tournamentMatchService.getAll(pageable);
        return ResponseEntity.ok(tournamentMatches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentMatchDTO> getOneTournamentMatch(@PathVariable Long id){
        TournamentMatchDTO match = tournamentMatchService.getOne(id);
        return ResponseEntity.ok(match);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TournamentMatchDTO> updateTournamentMatch(@RequestBody TournamentMatchRequest tournamentMatchRequest, @PathVariable Long id){
        TournamentMatchDTO match = tournamentMatchService.update(tournamentMatchRequest, id);
        return ResponseEntity.ok(match);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTournamentMatch(@PathVariable Long id){
        tournamentMatchService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
