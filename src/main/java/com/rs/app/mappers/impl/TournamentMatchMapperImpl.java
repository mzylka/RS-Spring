package com.rs.app.mappers.impl;

import com.rs.app.domain.entities.TournamentMatch;
import com.rs.app.dto.tournamentmatch.TournamentMatchDTO;
import com.rs.app.dto.tournamentmatch.TournamentMatchRequest;

import com.rs.app.mappers.TournamentMatchMapper;
import org.springframework.stereotype.Component;

@Component
public class TournamentMatchMapperImpl implements TournamentMatchMapper {

    @Override
    public TournamentMatch fromRequest(TournamentMatchRequest request) {
        return new TournamentMatch(
                request.player1(),
                request.player2(),
                request.streamUrl(),
                request.startTime()
        );
    }

    @Override
    public TournamentMatchDTO toDto(TournamentMatch tournamentMatch) {
        return new TournamentMatchDTO(
                tournamentMatch.getId(),
                tournamentMatch.getPlayer1(),
                tournamentMatch.getPlayer2(),
                tournamentMatch.getStreamUrl(),
                tournamentMatch.getStartTime()
        );
    }
}
