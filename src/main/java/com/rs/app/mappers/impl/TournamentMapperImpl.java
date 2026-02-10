package com.rs.app.mappers.impl;

import com.rs.app.domain.entities.Tournament;
import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.dto.tournament.TournamentDTO;
import com.rs.app.dto.tournament.TournamentRequest;
import com.rs.app.dto.tournament.TournamentSimpleDTO;
import com.rs.app.dto.tournamentmatch.TournamentMatchDTO;
import com.rs.app.mappers.TournamentMapper;
import com.rs.app.mappers.TournamentMatchMapper;
import com.rs.app.mappers.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TournamentMapperImpl implements TournamentMapper {
    private final UserMapper userMapper;
    private final TournamentMatchMapper tournamentMatchMapper;

    public TournamentMapperImpl(UserMapper userMapper, TournamentMatchMapper tournamentMatchMapper) {
        this.userMapper = userMapper;
        this.tournamentMatchMapper = tournamentMatchMapper;
    }

    @Override
    public Tournament fromRequest(TournamentRequest tournamentRequest) {
        PublicationStatus status = PublicationStatus.PUBLISHED;
        return new Tournament(
                tournamentRequest.title(),
                tournamentRequest.format(),
                tournamentRequest.content(),
                tournamentRequest.ladderUrl(),
                tournamentRequest.challongeUrl(),
                tournamentRequest.streamUrl(),
                tournamentRequest.vodsUrl(),
                tournamentRequest.startDate(),
                tournamentRequest.endDate(),
                status
        );
    }

    @Override
    public TournamentDTO toDto(Tournament element) {
        List<TournamentMatchDTO> matches = element.getMatches().stream().map(tournamentMatchMapper::toDto).toList();
        return new TournamentDTO(
                element.getTitle(),
                element.getThumbnail(),
                element.getThumbnailMin(),
                element.getFormat(),
                element.getContent(),
                element.getLadderUrl(),
                element.getChallongeUrl(),
                element.getVodsUrl(),
                element.getStartDate(),
                element.getEndDate(),
                userMapper.toSimpleDto(element.getOwner()),
                matches
        );
    }

    @Override
    public TournamentSimpleDTO toSimpleDto(Tournament tournament) {
        return new TournamentSimpleDTO(
                tournament.getTitle(),
                tournament.getThumbnail(),
                tournament.getThumbnailMin(),
                tournament.getStartDate(),
                tournament.getEndDate()
        );
    }
}
