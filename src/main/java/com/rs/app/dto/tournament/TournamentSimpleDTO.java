package com.rs.app.dto.tournament;

import java.time.LocalDate;

public record TournamentSimpleDTO(
        String title,
        String thumbnail,
        String thumbnailMin,
        LocalDate startDate,
        LocalDate endDate
) {
}
