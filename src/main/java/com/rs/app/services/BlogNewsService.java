package com.rs.app.services;

import com.rs.app.domain.entities.BlogNews;
import com.rs.app.domain.entities.BlogNewsComment;
import com.rs.app.domain.entities.Game;
import com.rs.app.domain.entities.User;
import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.dto.blognews.BlogNewsDTO;
import com.rs.app.dto.blognews.BlogNewsRequest;
import com.rs.app.dto.blognews.BlogNewsSimpleDTO;
import com.rs.app.dto.comment.CommentDTO;
import com.rs.app.dto.comment.CommentRequest;
import com.rs.app.mappers.BlogNewsMapper;
import com.rs.app.mappers.BlogNewsCommentMapper;
import com.rs.app.repositories.BlogNewsRepo;
import com.rs.app.repositories.GameRepo;
import com.rs.app.repositories.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlogNewsService {
    private final BlogNewsRepo repo;
    private final GameRepo gameRepo;
    private final BlogNewsMapper mapper;
    private final BlogNewsCommentMapper blogNewsCommentMapper;
    private final UserRepo userRepo;
    private final Logger log = LoggerFactory.getLogger(BlogNewsService.class);

    BlogNewsService(BlogNewsRepo repo, GameRepo gameRepo, BlogNewsMapper mapper, BlogNewsCommentMapper blogNewsCommentMapper, UserRepo userRepo) {
        this.repo = repo;
        this.gameRepo = gameRepo;
        this.mapper = mapper;
        this.blogNewsCommentMapper = blogNewsCommentMapper;
        this.userRepo = userRepo;
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    public Page<BlogNewsDTO> getAll(Pageable pageable, String search) {
        if (search == null){
            return repo.findAll(pageable).map(mapper::toDto);
        }
        return repo.findByTitleContainingIgnoreCase(search, pageable).map(mapper::toDto);
    }

    public Page<BlogNewsDTO> getAllPublished(Pageable pageable, String search) {
        if (search == null){
            return repo.findByStatus(PublicationStatus.PUBLISHED, pageable).map(mapper::toDto);
        }
        return repo.findByTitleContainingIgnoreCaseAndStatusIs(search, PublicationStatus.PUBLISHED, pageable).map(mapper::toDto);
    }

    @PostAuthorize("@permissionService.isAuthorOrMod(authentication, returnObject)")
    public BlogNewsDTO getOne(Long id){
        BlogNews blogNews = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Blog has not been found by ID"));
        return mapper.toDto(blogNews);
    }

    @PreAuthorize("hasAuthority('REDACTOR')")
    public BlogNewsDTO save(BlogNewsRequest blogNewsRequest, OAuth2User user) {
        Long userId = user.getAttribute("userId");
        if(userId == null){
            throw new IllegalStateException("User ID not found in authentication");
        }
        BlogNews blogNewsEntity = mapper.fromRequest(blogNewsRequest);
        if (blogNewsRequest.gameId() != null) {
            Game gameEntity = gameRepo.getReferenceById(blogNewsRequest.gameId());
            blogNewsEntity.setGame(gameEntity);
        }

        User author = userRepo.getReferenceById(userId);
        blogNewsEntity.setAuthor(author);
        BlogNews saved = repo.save(blogNewsEntity);
        return mapper.toDto(saved);
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @PreAuthorize("@permissionService.canUpdateBlog(authentication, #id) || hasAuthority('MODERATOR')")
    @Transactional
    public BlogNewsDTO update(BlogNewsRequest blogNewsRequest, Long id) {
        BlogNews existingBlogNews = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Blog Entity not found"));
        existingBlogNews.setTitle(blogNewsRequest.title());
        existingBlogNews.setContent(blogNewsRequest.content());

        if (blogNewsRequest.gameId() != null) {
            Game gameEntity = gameRepo.getReferenceById(blogNewsRequest.gameId());
            existingBlogNews.setGame(gameEntity);
        }
        BlogNews updated = repo.save(existingBlogNews);
        return mapper.toDto(updated);
    }

    public List<CommentDTO> getComments(Long id) {
        BlogNews blogNews = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Blog Entity not found"));
        return blogNews.getComments().stream().map(blogNewsCommentMapper::toDto).toList();
    }

    @PreAuthorize("hasAuthority('NORMAL')")
    @Transactional
    public CommentDTO addComment(Long id, CommentRequest commentRequest, Long userId) {
        BlogNews blogNews = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Blog Entity not found"));
        User author = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User Entity not found"));
        BlogNewsComment comment = blogNewsCommentMapper.fromRequest(commentRequest);
        comment.setUser(author);
        blogNews.addComment(comment);
        repo.save(blogNews);
        return blogNewsCommentMapper.toDto(comment);
    }

    public Page<BlogNewsSimpleDTO> getAllUserBlogNews(Long userId, Pageable pageable) {
        return repo.findByAuthorIdAndStatusIs(userId, PublicationStatus.PUBLISHED, pageable).map(mapper::toSimpleDto);
    }

    public Page<BlogNewsSimpleDTO> getAllGameBlogNews(Long gameId, Pageable pageable, PublicationStatus status) {
        return repo.findByGameIdAndStatusIs(gameId, status, pageable).map(mapper::toSimpleDto);
    }
}