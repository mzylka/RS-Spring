package com.rs.app.controllers;

import com.rs.app.dto.blognews.BlogNewsDTO;
import com.rs.app.dto.blognews.BlogNewsRequest;
import com.rs.app.dto.comment.CommentDTO;
import com.rs.app.dto.comment.CommentRequest;
import com.rs.app.services.BlogNewsService;
import com.rs.app.util.PaginationHelper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/blog-news")
public class BlogNewsController {
    private final BlogNewsService blogNewsService;
    private final PaginationHelper paginationHelper;

    public BlogNewsController(BlogNewsService blogNewsService, PaginationHelper paginationHelper) {
        this.blogNewsService = blogNewsService;
        this.paginationHelper = paginationHelper;
    }

    @GetMapping
    public ResponseEntity<Page<BlogNewsDTO>> getAllBlogNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "published") String status
            )
    {

        Pageable pageable = paginationHelper.buildPageable(page, sortBy, direction, Set.of("title", "createdAt", "id"));
        Page<BlogNewsDTO> news;

        if ("all".equalsIgnoreCase(status)){
            news = blogNewsService.getAll(pageable, q);
        }
        else{
            news = blogNewsService.getAllPublished(pageable, q);
        }

        return ResponseEntity.ok(news);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogNewsDTO> getOneBlogNews(@PathVariable Long id){
        BlogNewsDTO oneNews = blogNewsService.getOne(id);
        return ResponseEntity.ok(oneNews);
    }

    @PostMapping
    public ResponseEntity<BlogNewsDTO> saveBlogNews(@Valid @RequestBody BlogNewsRequest blogNewsRequest, @AuthenticationPrincipal OAuth2User user){
//        Long userId = user.getAttribute("userId");
        BlogNewsDTO saved = blogNewsService.save(blogNewsRequest, user);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogNewsDTO> updateBlogNews(@PathVariable Long id, @Valid @RequestBody BlogNewsRequest blogNewsRequest){
        BlogNewsDTO saved = blogNewsService.update(blogNewsRequest, id);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlogNews(@PathVariable Long id){
        blogNewsService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long id){
        List<CommentDTO> comments = blogNewsService.getComments(id);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> saveBlogNewsComment(@PathVariable Long id,
                                                          @RequestBody CommentRequest commentRequest,
                                                          @AuthenticationPrincipal OAuth2User user)
    {
        CommentDTO commentDTO = blogNewsService.addComment(id, commentRequest, user.getAttribute("userId"));
        return ResponseEntity.ok(commentDTO);
    }
}
