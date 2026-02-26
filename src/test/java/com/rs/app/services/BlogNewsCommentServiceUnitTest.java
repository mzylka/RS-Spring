package com.rs.app.services;

import com.rs.app.domain.entities.BlogNewsComment;
import com.rs.app.domain.entities.User;
import com.rs.app.dto.comment.CommentDTO;
import com.rs.app.dto.comment.CommentRequest;
import com.rs.app.dto.user.UserSimpleDTO;
import com.rs.app.mappers.BlogNewsCommentMapper;
import com.rs.app.repositories.BlogNewsCommentRepo;
import com.rs.app.repositories.BlogNewsRepo;
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
@DisplayName("BlogNewsComment Service Unit Test")
class BlogNewsCommentServiceUnitTest {
    @Mock
    private UserRepo userRepo;

    @Mock
    private BlogNewsRepo blogNewsRepo;

    @Mock
    private BlogNewsCommentRepo blogNewsCommentRepo;

    @Mock
    private BlogNewsCommentMapper mapper;

    @InjectMocks
    private BlogNewsCommentService blogNewsCommentService;

    private BlogNewsComment blogNewsComment;
    private CommentDTO commentDTO;
    private CommentRequest commentRequest;
    private UserSimpleDTO userSimpleDTO;
    private User user;

    @BeforeEach
    void setUp(){
        blogNewsComment = new BlogNewsComment("TestComment");
        userSimpleDTO = new UserSimpleDTO(1L, "Test User", "avatar");
        commentDTO = new CommentDTO(1L, "Test Comment", 0, userSimpleDTO);
        commentRequest = new CommentRequest("Test Comment");
        user = new User("12345", "Test User", "avatar");
    }

    @Test
    void getOneShouldThrowWhenCommentMissing(){
        when(blogNewsCommentRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> blogNewsCommentService.getOne(2L));
    }

    @Test
    void saveShouldThrowWhenUserMissing(){
        when(userRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> blogNewsCommentService.save(commentRequest, 2L, 1L));
    }

    @Test
    void saveShouldThrowWhenBlogNewsMissing(){
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(blogNewsRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> blogNewsCommentService.save(commentRequest, 1L, 2L));
    }

    @Test
    void updateShouldThrowWhenCommentMissing(){
        when(blogNewsCommentRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> blogNewsCommentService.update(commentRequest, 2L));
    }
}