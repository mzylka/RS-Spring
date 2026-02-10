package com.rs.app.dto.tournament;

import com.rs.app.dto.tournamentmatch.TournamentMatchDTO;
import com.rs.app.dto.user.UserSimpleDTO;

import java.time.LocalDate;
import java.util.List;

public record TournamentDTO(
        String title,
        String thumbnail,
        String thumbnailMin,
        String format,
        String content,
        String ladderUrl,
        String challongeUrl,
        String vodsUrl,
        LocalDate startDate,
        LocalDate endDate,
        UserSimpleDTO creator,
        List<TournamentMatchDTO> matches
) {

}
