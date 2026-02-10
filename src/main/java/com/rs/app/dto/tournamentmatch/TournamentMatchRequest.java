package com.rs.app.dto.tournamentmatch;

import java.time.LocalDateTime;

public record TournamentMatchRequest(
        String player1,
        String player2,
        String streamUrl,
        LocalDateTime startTime
) {
}
