package com.rs.app.services;

import com.rs.app.domain.entities.Game;
import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.dto.game.GameDTO;
import com.rs.app.dto.game.GameRequest;
import com.rs.app.dto.game.GameSimpleDTO;
import com.rs.app.mappers.CommunityMapper;
import com.rs.app.mappers.GameMapper;
import com.rs.app.repositories.CommunityRepo;
import com.rs.app.repositories.GameRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;


@Service
public class GameService {
    private final GameRepo repo;
    private final CommunityRepo communityRepo;
    private final GameMapper mapper;
    private final CommunityMapper communityMapper;

    public GameService(GameRepo repo, CommunityRepo communityRepo, GameMapper mapper, CommunityMapper communityMapper) {
        this.repo = repo;
        this.communityRepo = communityRepo;
        this.mapper = mapper;
        this.communityMapper = communityMapper;
    }

    public Page<GameSimpleDTO> getAll(Pageable pageable){
        return repo.findAll(pageable).map(mapper::toSimpleDto);
    }

    public Page<GameSimpleDTO> getAll(Pageable pageable, String q){
        return repo.findByTitleContainingIgnoreCase(q, pageable).map(mapper::toSimpleDto);
    }

    public Page<GameSimpleDTO> getAllPublished(Pageable pageable){
        return  repo.findByStatusIs(PublicationStatus.PUBLISHED, pageable).map(mapper::toSimpleDto);
    }

    public Page<GameSimpleDTO> getAllPublished(Pageable pageable, String q){
        return repo.findByTitleContainingIgnoreCaseAndStatusIs(q, PublicationStatus.PUBLISHED, pageable).map(mapper::toSimpleDto);
    }

    @PostAuthorize("")
    public GameDTO getOne(Long id){
        return repo.findById(id).map(mapper::toDto).orElseThrow(() -> new EntityNotFoundException("Game has not been found by ID"));
    }

    public GameDTO save(GameRequest gameRequest){
        Game gameEntity = mapper.fromRequest(gameRequest);
        Game saved = repo.save(gameEntity);
        return mapper.toDto(saved);
    }

    public GameDTO update(Long id, GameDTO gameDTO){
        Game existingGame = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Game has not been found by ID"));
        existingGame.setTitle(gameDTO.title());
        existingGame.setContent(gameDTO.content());
        existingGame.setWebsiteUrl(gameDTO.websiteUrl());
        existingGame.setxUrl(gameDTO.xUrl());
        existingGame.setFbUrl(gameDTO.fbUrl());
        existingGame.setReleaseDate(gameDTO.releaseDate());
        existingGame.setAppId(gameDTO.steamAppId());
        return mapper.toDto(repo.save(existingGame));
    }

    public void delete(Long id){
        repo.deleteById(id);
    }

}
