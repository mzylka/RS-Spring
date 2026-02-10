package com.rs.app.controllers;

import com.rs.app.dto.community.CommunityDTO;
import com.rs.app.dto.community.CommunityRequest;
import com.rs.app.dto.community.CommunitySimpleDTO;
import com.rs.app.services.CommunityService;
import com.rs.app.util.PaginationHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("/communities")
public class CommunityController {
    private final CommunityService communityService;
    private final PaginationHelper paginationHelper;

    public CommunityController(CommunityService communityService, PaginationHelper paginationHelper) {
        this.communityService = communityService;
        this.paginationHelper = paginationHelper;
    }

    @GetMapping
    public ResponseEntity<Page<CommunitySimpleDTO>> getAllCommunities(@RequestParam(required = false) String q,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "name") String sortBy,
                                                                @RequestParam(defaultValue = "asc") String direction)
    {
        Pageable pageable = paginationHelper.buildPageable(page, sortBy, direction, Set.of("name", "createdAt"));
        Page<CommunitySimpleDTO> communities = (q != null) ? communityService.getAll(pageable, q) : communityService.getAll(pageable);
        return ResponseEntity.ok(communities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityDTO> getOneCommunity(@PathVariable Long id){
        CommunityDTO communityDTO = communityService.getOne(id);
        return ResponseEntity.ok(communityDTO);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    public ResponseEntity<CommunityDTO> saveCommunity(@RequestBody CommunityRequest communityRequest){
        CommunityDTO communityDTO = communityService.save(communityRequest);
        return new ResponseEntity<>(communityDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@ownerSecurity.isCommunityOwner(#id, user) || hasAuthority('ADMIN_USER')")
    public ResponseEntity<CommunityDTO> updateCommunity(@PathVariable Long id, @RequestBody CommunityRequest communityRequest, @AuthenticationPrincipal OAuth2User user){
        CommunityDTO communityDTO = communityService.update(id, communityRequest);
        return ResponseEntity.ok(communityDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    public ResponseEntity<?> deleteCommunity(@PathVariable Long id){
        communityService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
