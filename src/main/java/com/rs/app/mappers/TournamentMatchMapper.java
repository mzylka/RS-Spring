package com.rs.app.mappers;

import com.rs.app.domain.entities.TournamentMatch;
import com.rs.app.dto.tournamentmatch.TournamentMatchDTO;
import com.rs.app.dto.tournamentmatch.TournamentMatchRequest;

public interface TournamentMatchMapper {
    TournamentMatch fromRequest(TournamentMatchRequest request);
    TournamentMatchDTO toDto(TournamentMatch tournamentMatch);
}
