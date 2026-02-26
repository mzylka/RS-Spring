package com.rs.app.services;

import com.rs.app.domain.entities.Community;
import com.rs.app.domain.entities.Game;
import com.rs.app.domain.entities.User;
import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.dto.community.CommunityDTO;
import com.rs.app.dto.community.CommunityRequest;
import com.rs.app.dto.community.CommunitySimpleDTO;
import com.rs.app.mappers.CommunityMapper;
import com.rs.app.repositories.CommunityRepo;
import com.rs.app.repositories.GameRepo;
import com.rs.app.repositories.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service
public class CommunityService {
    private final CommunityRepo repo;
    private final CommunityMapper mapper;
    private final GameRepo gameRepo;
    private final UserRepo userRepo;

    public CommunityService(CommunityRepo communityRepo, CommunityMapper communityMapper, GameRepo gameRepo, UserRepo userRepo) {
        this.repo = communityRepo;
        this.mapper = communityMapper;
        this.gameRepo = gameRepo;
        this.userRepo = userRepo;
    }

    public Page<CommunitySimpleDTO> getAll(Pageable pageable){
        return repo.findAll(pageable).map(mapper::toSimpleDTO);
    }

    public Page<CommunitySimpleDTO> getAll(Pageable pageable, String q){
        return repo.findByTitleContainingIgnoreCase(q, pageable).map(mapper::toSimpleDTO);
    }

    public Page<CommunitySimpleDTO> getAllPublished(Pageable pageable){
        return repo.findByStatusIs(PublicationStatus.PUBLISHED, pageable).map(mapper::toSimpleDTO);
    }

    public Page<CommunitySimpleDTO> getAllPublished(Pageable pageable, String q){
        return repo.findByTitleContainingIgnoreCaseAndStatusIs(q, PublicationStatus.PUBLISHED, pageable).map(mapper::toSimpleDTO);
    }

    public CommunityDTO getOne(Long id){
        return repo.findById(id).map(mapper::toDto).orElseThrow(() -> new EntityNotFoundException("Community has not been found by ID"));
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    public CommunityDTO save(CommunityRequest communityRequest){
        Community communityEntity = mapper.fromRequest(communityRequest);

        if (communityRequest.gameId() != null){
            Game gameEntity = gameRepo.findById(communityRequest.gameId()).orElseThrow(() -> new EntityNotFoundException("Game has not been found by ID"));
            communityEntity.setGame(gameEntity);
        }

        if(communityRequest.ownerId() != null){
            User user = userRepo.findById(communityRequest.ownerId()).orElseThrow(() -> new EntityNotFoundException("User has not been found"));
            communityEntity.setOwner(user);
        }

        Community saved = repo.save(communityEntity);
        return mapper.toDto(saved);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(Long id){
        repo.deleteById(id);
    }

    public CommunityDTO update(Long id, CommunityRequest communityRequest){
        Community existingCommunity = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Community has not been found"));
        existingCommunity.setTitle(communityRequest.name());
        existingCommunity.setContent(communityRequest.description());

        if (communityRequest.gameId() != null) {
            Game gameEntity = gameRepo.findById(communityRequest.gameId()).orElseThrow(() -> new EntityNotFoundException("Game has not been found by ID"));
            existingCommunity.setGame(gameEntity);
        }

        if(communityRequest.ownerId() != null){
            User user = userRepo.findById(communityRequest.ownerId()).orElseThrow(() -> new EntityNotFoundException("User has not been found"));
            existingCommunity.setOwner(user);
        }

        Community updated =  repo.save(existingCommunity);
        return mapper.toDto(updated);
    }

    public Page<CommunitySimpleDTO> getAllPublishedByGame(Long gameId, Pageable pageable){
        return repo.findByGameIdAndStatusIs(gameId, PublicationStatus.PUBLISHED, pageable).map(mapper::toSimpleDTO);
    }

    public Page<CommunitySimpleDTO> getAllByGame(Long gameId, Pageable pageable){
        return repo.findByGameId(gameId, pageable).map(mapper::toSimpleDTO);
    }
}
