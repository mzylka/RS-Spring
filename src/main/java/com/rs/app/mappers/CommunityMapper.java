package com.rs.app.mappers;

import com.rs.app.domain.entities.Community;
import com.rs.app.dto.community.CommunityDTO;
import com.rs.app.dto.community.CommunityRequest;
import com.rs.app.dto.community.CommunitySimpleDTO;

public interface CommunityMapper{
    Community fromRequest(CommunityRequest communityRequest);
    CommunitySimpleDTO toSimpleDTO(Community community);
    CommunityDTO toDto(Community community);
}
