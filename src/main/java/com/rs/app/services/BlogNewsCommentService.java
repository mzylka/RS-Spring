package com.rs.app.services;

import com.rs.app.domain.entities.BlogNews;
import com.rs.app.domain.entities.BlogNewsComment;
import com.rs.app.domain.entities.User;
import com.rs.app.dto.comment.CommentDTO;
import com.rs.app.dto.comment.CommentRequest;
import com.rs.app.mappers.BlogNewsCommentMapper;
import com.rs.app.repositories.BlogNewsRepo;
import com.rs.app.repositories.BlogNewsCommentRepo;
import com.rs.app.repositories.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service
public class BlogNewsCommentService {
    private final BlogNewsCommentRepo repo;
    private final UserRepo userRepo;
    private final BlogNewsRepo blogNewsRepo;
    private final BlogNewsCommentMapper blogNewsCommentMapper;

    public BlogNewsCommentService(BlogNewsCommentRepo repo, UserRepo userRepo, BlogNewsRepo blogNewsRepo, BlogNewsCommentMapper blogNewsCommentMapper) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.blogNewsRepo = blogNewsRepo;
        this.blogNewsCommentMapper = blogNewsCommentMapper;
    }

    public Page<CommentDTO> getAll(Pageable pageable){
        return repo.findAll(pageable).map(blogNewsCommentMapper::toDto);
    }

    public Page<CommentDTO> getAll(Pageable pageable, Long userId){
        return repo.findByUserId(userId, pageable).map(blogNewsCommentMapper::toDto);
    }

    public CommentDTO getOne(Long id){
        BlogNewsComment comment = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("The Comment has not been found"));
        return blogNewsCommentMapper.toDto(comment);
    }

    @PreAuthorize("hasAuthority('NORMAL')")
    public CommentDTO save(CommentRequest commentRequest, Long userId, Long blogNewsId){
        User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User has not been found"));
        BlogNews blogNews = blogNewsRepo.findById(blogNewsId).orElseThrow(() -> new EntityNotFoundException("Blog news has not been found"));
        BlogNewsComment comment = new BlogNewsComment(commentRequest.content());
        comment.setUser(user);
        comment.setBlogNews(blogNews);
        return blogNewsCommentMapper.toDto(repo.save(comment));
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    public void delete(Long id){
        repo.deleteById(id);
    }

    @PreAuthorize("@permissionService.isCommentOwner(authentication, #id)")
    public CommentDTO update(CommentRequest commentRequest, Long id){
        BlogNewsComment comment = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("The comment has not been found"));
        comment.setContent(commentRequest.content());
        return blogNewsCommentMapper.toDto(repo.save(comment));
    }
}
