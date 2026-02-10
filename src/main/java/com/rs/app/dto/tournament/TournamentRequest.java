package com.rs.app.dto.tournament;

import java.time.LocalDate;

public record TournamentRequest(
        String title,
        String thumbnail,
        String thumbnailMin,
        String format,
        String content,
        String ladderUrl,
        String challongeUrl,
        String vodsUrl,
        String streamUrl,
        LocalDate startDate,
        LocalDate endDate,
        Long gameId
) {}