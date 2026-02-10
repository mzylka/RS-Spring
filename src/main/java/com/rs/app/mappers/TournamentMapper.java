package com.rs.app.mappers;

import com.rs.app.domain.entities.Tournament;
import com.rs.app.dto.tournament.TournamentDTO;
import com.rs.app.dto.tournament.TournamentRequest;
import com.rs.app.dto.tournament.TournamentSimpleDTO;

public interface TournamentMapper{
    Tournament fromRequest(TournamentRequest tournamentRequest);
    TournamentDTO toDto(Tournament tournament);
    TournamentSimpleDTO toSimpleDto(Tournament tournament);
}
