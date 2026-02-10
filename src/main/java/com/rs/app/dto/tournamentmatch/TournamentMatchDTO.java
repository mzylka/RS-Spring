package com.rs.app.dto.tournamentmatch;

import java.time.LocalDateTime;

public record TournamentMatchDTO(
        Long id,
        String player1,
        String player2,
        String streamUrl,
        LocalDateTime startTime
) {
}
