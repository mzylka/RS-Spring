package com.rs.app.controllers;

import com.rs.app.domain.entities.User;
import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.dto.blognews.BlogNewsSimpleDTO;
import com.rs.app.dto.user.UserDTO;
import com.rs.app.mappers.UserMapper;
import com.rs.app.services.BlogNewsService;
import com.rs.app.services.UserService;
import com.rs.app.util.PaginationHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final BlogNewsService blogNewsService;
    private final UserMapper userMapper;
    private final PaginationHelper paginationHelper;

    public UserController(UserService userService, BlogNewsService blogNewsService, UserMapper userMapper, PaginationHelper paginationHelper) {
        this.userService = userService;
        this.blogNewsService = blogNewsService;
        this.userMapper = userMapper;
        this.paginationHelper = paginationHelper;
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('NORMAL_USER')")
    public ResponseEntity<Object> me(@AuthenticationPrincipal OAuth2User user){
        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Map<String, Object> userInfo = Map.of(
                "id", user.getAttribute("userId"),
                "discordId", user.getAttribute("id"),
                "discordName", user.getAttribute("global_name"),
                "avatar", user.getAttribute("avatar")
        );

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String direction,
        @RequestParam(required = false) String q
        )
    {
        Pageable pageable = paginationHelper.buildPageable(page, sortBy, direction, Set.of("discordName", "createdAt", "id"));
        Page<UserDTO> users = (q != null) ? userService.getAll(pageable, q) : userService.getAll(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getOneUser(@PathVariable Long id){
        User user = userService.getOne(id);
        UserDTO userDTO = userMapper.toDto(user);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/discord-name/{discordName}")
    public ResponseEntity<UserDTO> getOneUserByName(@PathVariable String discordName){
        User user = userService.getOne(discordName);
        UserDTO userDTO = userMapper.toDto(user);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/discord-id/{discordId}")
    public ResponseEntity<UserDTO> getOneUserByDiscordId(@PathVariable String discordId){
        User user = userService.getOneByDiscordId(discordId);
        UserDTO userDTO = userMapper.toDto(user);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/{id}/blog-news")
    public ResponseEntity<Page<BlogNewsSimpleDTO>> getAllUserBlogNews(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "published") String status
    )
    {
        Pageable pageable = paginationHelper.buildPageable(page, sortBy, direction, Set.of("title", "createdAt", "id"));
        Page<BlogNewsSimpleDTO> news = blogNewsService.getAllUserBlogNews(id, pageable);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/me/blog-news")
    public ResponseEntity<Page<BlogNewsSimpleDTO>> getUserBlogNews(
            @AuthenticationPrincipal OAuth2User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "all") String status
    )
    {
        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Pageable pageable = paginationHelper.buildPageable(page, sortBy, direction, Set.of("title", "createdAt", "id"));
        Long userId = user.getAttribute("userId");
        Page<BlogNewsSimpleDTO> news = blogNewsService.getAllGameBlogNews(userId, pageable, PublicationStatus.PUBLISHED);
        return ResponseEntity.ok(news);
    }
}
