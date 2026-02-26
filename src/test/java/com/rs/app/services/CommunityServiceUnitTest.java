package com.rs.app.services;

import com.rs.app.domain.entities.Community;
import com.rs.app.domain.enums.PublicationStatus;
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

    @BeforeEach
    void setUp() {

    }

    @Test
    void shouldGetOneThrowWhenCommunityMissing(){
        when(communityRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> communityService.getOne(2L));
    }

    @Test
    void shouldSaveThrowWhenGameMissing(){
        CommunityRequest communityRequest = new CommunityRequest("Name", "test community", "websiteurl", "discordurl", true, 1L, 4L);
        Community community = new Community("Name", "test community", "websiteurl", "discordurl", PublicationStatus.PUBLISHED);

        when(communityMapper.fromRequest(communityRequest)).thenReturn(community);
        assertThrows(EntityNotFoundException.class, () -> communityService.save(communityRequest));
    }

    @Test
    void shouldSaveThrowWhenOwnerMissing(){
        CommunityRequest communityRequest = new CommunityRequest("Name", "test community", "websiteurl", "discordurl", true, 5L, null);
        Community community = new Community("Name", "test community", "websiteurl", "discordurl", PublicationStatus.PUBLISHED);

        when(communityMapper.fromRequest(communityRequest)).thenReturn(community);
        when(userRepo.findById(5L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> communityService.save(communityRequest));
    }

    @Test
    void shouldUpdateThrowWhenCommunityMissing(){
        when(communityRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> communityService.getOne(2L));
    }
}