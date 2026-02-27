package com.rs.app.dto.tournament;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;

public record TournamentRequest(
        @NotBlank String title,
        @NotBlank String thumbnail,
        @NotBlank String thumbnailMin,
        @NotBlank String format,
        @NotBlank String content,
        String ladderUrl,
        String challongeUrl,
        String vodsUrl,
        String streamUrl,
        LocalDate startDate,
        LocalDate endDate,
        Long gameId
) {}