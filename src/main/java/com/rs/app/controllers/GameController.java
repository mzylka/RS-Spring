package com.rs.app.controllers;

import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.dto.blognews.BlogNewsSimpleDTO;
import com.rs.app.dto.community.CommunitySimpleDTO;
import com.rs.app.dto.game.GameDTO;
import com.rs.app.dto.game.GameRequest;
import com.rs.app.dto.game.GameSimpleDTO;
import com.rs.app.services.BlogNewsService;
import com.rs.app.services.CommunityService;
import com.rs.app.services.GameService;
import com.rs.app.util.PaginationHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameService gameService;
    private final BlogNewsService blogNewsService;
    private final CommunityService communityService;
    private final PaginationHelper paginationHelper;

    public GameController(GameService gameService, BlogNewsService blogNewsService, CommunityService communityService, PaginationHelper paginationHelper) {
        this.gameService = gameService;
        this.blogNewsService = blogNewsService;
        this.communityService = communityService;
        this.paginationHelper = paginationHelper;
    }

    @GetMapping
    public ResponseEntity<Page<GameSimpleDTO>> getAllGames(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    )
    {
        Pageable pageable = paginationHelper.buildPageable(page, sortBy, direction, Set.of("title", "releaseDate"));
        Page<GameSimpleDTO> games = (q != null) ? gameService.getAll(pageable, q) : gameService.getAll(pageable);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public GameDTO getGame(@PathVariable Long id){
        return gameService.getOne(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MODERATOR_USER')")
    public ResponseEntity<GameDTO> saveGame(@RequestBody GameRequest gameRequest){
        GameDTO saved = gameService.save(gameRequest);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MODERATOR_USER')")
    public ResponseEntity<GameDTO> updateGame(@PathVariable Long id, @RequestBody GameDTO gameDTO){
        GameDTO saved = gameService.update(id, gameDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    public ResponseEntity<?> deleteGame(@PathVariable Long id){
        gameService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/blog-news")
    public ResponseEntity<Page<BlogNewsSimpleDTO>> getGameBlogNews(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    )
    {
        Pageable pageable = paginationHelper.buildPageable(page, sortBy, direction, Set.of("id", "title", "createdAt"), limit);
        Page<BlogNewsSimpleDTO> news = blogNewsService.getAllGameBlogNews(id, pageable, PublicationStatus.PUBLISHED);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @GetMapping("/{id}/communities")
    public ResponseEntity<Page<CommunitySimpleDTO>> geCommunities(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    )
    {
        Pageable pageable = paginationHelper.buildPageable(page, sortBy, direction, Set.of("id", "title", "createdAt"), limit);
        Page<CommunitySimpleDTO> communities = communityService.getAllPublishedByGame(id, pageable);
        return new ResponseEntity<>(communities, HttpStatus.OK);
    }

//    @GetMapping("/{id}/tournaments")
//    public ResponseEntity<Page<TournamentSimpleDTO>> getTournaments(
//            @PathVariable Long id,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int limit,
//            @RequestParam(defaultValue = "createdAt") String sortBy,
//            @RequestParam(defaultValue = "asc") String direction
//    )
//    {
//
//    }
}
