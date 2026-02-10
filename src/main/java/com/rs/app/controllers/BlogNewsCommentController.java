package com.rs.app.controllers;

import com.rs.app.dto.comment.CommentDTO;
import com.rs.app.dto.comment.CommentRequest;
import com.rs.app.services.BlogNewsCommentService;
import com.rs.app.util.PaginationHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/blog-news-comments")
public class BlogNewsCommentController {
    private final BlogNewsCommentService blogNewsCommentService;
    private final PaginationHelper paginationHelper;

    public BlogNewsCommentController(BlogNewsCommentService blogNewsCommentService, PaginationHelper paginationHelper) {
        this.blogNewsCommentService = blogNewsCommentService;
        this.paginationHelper = paginationHelper;
    }

    @GetMapping
    public ResponseEntity<Page<CommentDTO>> getAllBlogNewsComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ){
        Pageable pageable = paginationHelper.buildPageable(page, sortBy, direction, Set.of("createdAt", "id"));
        Page<CommentDTO> comments = (userId == null) ? blogNewsCommentService.getAll(pageable) : blogNewsCommentService.getAll(pageable, userId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getOneComment(@PathVariable Long id){
        CommentDTO comment = blogNewsCommentService.getOne(id);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@RequestBody CommentRequest commentRequest, @PathVariable Long id){
        CommentDTO comment = blogNewsCommentService.update(commentRequest, id);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id){
        blogNewsCommentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
