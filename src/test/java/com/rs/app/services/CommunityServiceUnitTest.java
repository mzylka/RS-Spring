package com.rs.app.services;

import com.rs.app.domain.entities.Community;
import com.rs.app.domain.entities.Game;
import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.dto.community.CommunityDTO;
import com.rs.app.dto.community.CommunityRequest;
import com.rs.app.mappers.CommunityMapper;
import com.rs.app.repositories.CommunityRepo;
import com.rs.app.repositories.GameRepo;
import com.rs.app.repositories.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommunityService Unit Test")
class CommunityServiceUnitTest {
    @Mock
    private CommunityRepo communityRepo;

    @Mock
    private GameRepo gameRepo;

    @Mock
    private CommunityMapper communityMapper;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private CommunityService communityService;

    private CommunityRequest communityRequest;
    private CommunityRequest communityRequestNoOwner;
    private CommunityRequest communityRequestNoGame;
    private CommunityRequest communityRequestNoOwnerAndGame;
    private Community community;

    @BeforeEach
    void setUp() {
        communityRequest = new CommunityRequest("thumbnailUrl","thumbnailMinUrl","Name", "test community", "websiteurl", "discordurl", true, 5L, 1L);
        communityRequestNoOwner = new CommunityRequest("thumbnailUrl","thumbnailMinUrl","Name", "test community", "websiteurl", "discordurl", true, null, 1L);
        communityRequestNoGame = new CommunityRequest("thumbnailUrl","thumbnailMinUrl","Name", "test community", "websiteurl", "discordurl", true, 5L, null);
        communityRequestNoOwnerAndGame = new CommunityRequest("thumbnailUrl","thumbnailMinUrl","Name", "test community", "websiteurl", "discordurl", true, null, null);
        community = new Community("Name", "test community", "websiteurl", "discordurl", PublicationStatus.PUBLISHED);
    }

    @Test
    void shouldGetOneThrowWhenCommunityMissing(){
        when(communityRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> communityService.getOne(2L));
    }

    @Test
    void shouldSaveThrowWhenGameMissing(){
        when(communityMapper.fromRequest(communityRequest)).thenReturn(community);
        assertThrows(EntityNotFoundException.class, () -> communityService.save(communityRequest));
    }

    @Test
    void shouldSaveThrowWhenOwnerMissing(){
        when(communityMapper.fromRequest(communityRequest)).thenReturn(community);
        when(gameRepo.findById(1L)).thenReturn(Optional.of(new Game()));
        when(userRepo.findById(5L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> communityService.save(communityRequest));
    }

    @Test
    void shouldSaveReturnSavedCommunityAsDTO(){
        CommunityDTO communityDTO = new CommunityDTO(1L, "Name", "name", "test community", "", "", null, null, PublicationStatus.PUBLISHED, null);

        when(communityMapper.fromRequest(communityRequestNoOwnerAndGame)).thenReturn(community);
        when(communityRepo.save(community)).thenReturn(community);
        when(communityMapper.toDto(community)).thenReturn(communityDTO);

        CommunityDTO dto = communityService.save(communityRequestNoOwnerAndGame);

        assertEquals(dto.title(), communityDTO.title());
    }

    @Test
    void shouldUpdateThrowWhenCommunityMissing(){
        when(communityRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> communityService.getOne(2L));
    }

    @Test
    void shouldUpdateReturnUpdatedCommunityAsDTO(){
        CommunityRequest communityRequestUpdated = new CommunityRequest("thumbnailUrl","thumbnailMinUrl","Name Updated", "test community", "", "", true, null, null);
        Community updatedCommunity = new Community("Name updated", "test community", "", "", PublicationStatus.PUBLISHED);
        CommunityDTO communityDTO = new CommunityDTO(1L, "Name Updated", "name-updated", "test community", "", "", null, null, PublicationStatus.PUBLISHED, null);

        when(communityRepo.findById(1L)).thenReturn(Optional.of(community));
        when(communityRepo.save(community)).thenReturn(updatedCommunity);
        when(communityMapper.toDto(updatedCommunity)).thenReturn(communityDTO);

        CommunityDTO dto = communityService.update(1L, communityRequestUpdated);

        assertEquals(community.getTitle(), communityRequestUpdated.name());
        assertEquals(dto.title(), communityDTO.title());
        assertEquals(dto.slug(), community.getSlug());
    }
}