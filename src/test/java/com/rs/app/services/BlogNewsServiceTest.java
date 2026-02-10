package com.rs.app.services;

import com.rs.app.domain.entities.BlogNews;
import com.rs.app.domain.entities.Game;
import com.rs.app.domain.entities.User;
import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.dto.blognews.BlogNewsDTO;
import com.rs.app.dto.blognews.BlogNewsRequest;
import com.rs.app.dto.game.GameSimpleDTO;
import com.rs.app.mappers.BlogNewsCommentMapper;
import com.rs.app.mappers.BlogNewsMapper;
import com.rs.app.repositories.BlogNewsRepo;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BlogNews Service Tests")
class BlogNewsServiceTest {
    @Mock
    private BlogNewsRepo blogNewsRepo;
    @Mock
    private GameRepo gameRepo;
    @Mock
    private BlogNewsMapper mapper;
    @Mock
    private BlogNewsCommentMapper blogNewsCommentMapper;
    @Mock
    private UserRepo userRepo;
    @Mock
    private OAuth2User oAuth2User;

    @InjectMocks
    private BlogNewsService blogNewsService;

    private BlogNewsRequest blogNewsRequest;
    private BlogNewsRequest blogNewsRequestWithGame;
    private BlogNewsDTO blogNewsDTO;
    private BlogNewsDTO blogNewsDTOwithGame;
    private BlogNews blogNews;
    private BlogNews blogNewsWithGame;
    private Game game;
    private GameSimpleDTO gameSimpleDTO;
    private Pageable pageable = PageRequest.of(0,20);
    private User user;

    @BeforeEach
    void setUp(){
        this.game = new Game(
                "Game",
                "game content",
                "https://www",
                "https://x",
                "https://fb",
                "https://steam",
                Date.valueOf("2022-03-03"),
                "333",
                PublicationStatus.PUBLISHED
        );
        this.gameSimpleDTO = new GameSimpleDTO(1L, "Game", "game", "thumbnailUrl", "thumbnailMinUrl","iconUrl");
        this.blogNewsRequest = new BlogNewsRequest("Title", "content", false, null);
        this.blogNewsRequestWithGame = new BlogNewsRequest("Title2", "content2", false, 1L);
        this.blogNews = new BlogNews("Title", "content", PublicationStatus.PUBLISHED);
        this.blogNewsWithGame = new BlogNews("Title2", "content2", PublicationStatus.DRAFT);
        this.blogNewsWithGame.setGame(game);
        this.blogNewsDTO = new BlogNewsDTO("Title", "title", "content", null, null, null, PublicationStatus.PUBLISHED, null, null);
        this.blogNewsDTOwithGame = new BlogNewsDTO("Title2", "title2", "content2", null, null, null, PublicationStatus.DRAFT, gameSimpleDTO, null);
        this.user = new User("discordId", "discordname", "avatarurl");
    }

    @Test
    @DisplayName("Should return Page with one BlogNewsDTO")
    void getAllShouldReturnPageWithOneBlogNewsDTO() {
        //Given
        Page<BlogNews> page = new PageImpl<>(List.of(blogNews));
        when(blogNewsRepo.findAll(pageable)).thenReturn(page);
        when(mapper.toDto(blogNews)).thenReturn(blogNewsDTO);

        //When
        final Page<BlogNewsDTO> result = blogNewsService.getAll(pageable, null);
        List<BlogNewsDTO> blogNewsDTOList = result.toList();

        //Then
        assertNotNull(result);
        assertFalse(blogNewsDTOList.isEmpty());
        assertEquals("Title", blogNewsDTOList.get(0).title());
        verify(this.blogNewsRepo).findAll(pageable);
        verify(mapper).toDto(blogNews);
    }

    @Test
    @DisplayName("Should return Page with two BlogNewsDTO")
    void getAllShouldReturnPageWithTwoBlogNewsDTO() {
        //Given
        Page<BlogNews> page = new PageImpl<>(List.of(blogNews, blogNewsWithGame));
        when(blogNewsRepo.findAll(pageable)).thenReturn(page);
        when(mapper.toDto(blogNews)).thenReturn(blogNewsDTO, blogNewsDTOwithGame);

        //When
        final Page<BlogNewsDTO> result = blogNewsService.getAll(pageable, null);
        List<BlogNewsDTO> blogNewsDTOList = result.toList();

        //Then
        assertNotNull(result);
        assertFalse(blogNewsDTOList.isEmpty());
        assertEquals("Title", blogNewsDTOList.get(0).title());
        assertEquals("Title2", blogNewsDTOList.get(1).title());
        verify(this.blogNewsRepo).findAll(pageable);
        verify(mapper, times(2)).toDto(blogNews);
    }

    @Test
    @DisplayName("Should find BlogNews with the title as such the search parameter")
    void GetAllShouldReturnPageWithBlogNewsDTOThatHasTitleLikeSearchParam(){
        //Given
        Page<BlogNews> page = new PageImpl<>(List.of(blogNews, blogNewsWithGame));
        final String searchParam = "title";
        when(blogNewsRepo.findByTitleContainingIgnoreCase(searchParam, pageable)).thenReturn(page);
        when(mapper.toDto(blogNews)).thenReturn(blogNewsDTO, blogNewsDTOwithGame);

        //When
        final Page<BlogNewsDTO> result = blogNewsService.getAll(pageable, searchParam);
        List<BlogNewsDTO> blogNewsDTOList = result.toList();

        //Then
        assertNotNull(result);
        assertFalse(blogNewsDTOList.isEmpty());
        assertEquals("Title", blogNewsDTOList.get(0).title());
        assertEquals("Title2", blogNewsDTOList.get(1).title());
        verify(this.blogNewsRepo).findByTitleContainingIgnoreCase(searchParam, pageable);
        verify(blogNewsRepo, never()).findAll();
        verify(mapper, times(2)).toDto(blogNews);
    }

    @Test
    @DisplayName("Should not find any BlogNews, cause the search param doesn't correspond to any title")
    void GetAllShouldReturnEmptyPage(){
        //Given
        Page<BlogNews> page = new PageImpl<>(List.of());
        final String searchParam = "empty";
        when(blogNewsRepo.findByTitleContainingIgnoreCase(searchParam, pageable)).thenReturn(page);

        //When
        final Page<BlogNewsDTO> result = blogNewsService.getAll(pageable, searchParam);
        List<BlogNewsDTO> blogNewsDTOList = result.toList();

        //Then
        assertNotNull(result);
        assertTrue(result.stream().findAny().isEmpty());
        assertTrue(blogNewsDTOList.isEmpty());
        verify(this.blogNewsRepo).findByTitleContainingIgnoreCase(searchParam, pageable);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should find published BlogNews")
    void GetAllPublishedShouldReturnPublishedBlogNews(){
        //Given
        Page<BlogNews> page = new PageImpl<>(List.of(blogNews));
        when(blogNewsRepo.findByStatus(PublicationStatus.PUBLISHED, pageable)).thenReturn(page);
        when(mapper.toDto(blogNews)).thenReturn(blogNewsDTO);

        //When
        final Page<BlogNewsDTO> result = blogNewsService.getAllPublished(pageable, null);
        List<BlogNewsDTO> blogNewsDTOList = result.toList();

        //Then
        assertNotNull(result);
        assertFalse(result.stream().findAny().isEmpty());
        assertFalse(blogNewsDTOList.isEmpty());
        verify(this.blogNewsRepo).findByStatus(PublicationStatus.PUBLISHED, pageable);
        verify(mapper).toDto(blogNews);
    }

    @Test
    @DisplayName("Should find published BlogNews with the same title such as search parameter")
    void GetAllPublishedShouldReturnPublishedBlogNewsWithSearchParam(){
        //Given
        String searchParam = "title";
        Page<BlogNews> page = new PageImpl<>(List.of(blogNews));
        when(blogNewsRepo.findByTitleContainingIgnoreCaseAndStatusIs(searchParam, PublicationStatus.PUBLISHED, pageable)).thenReturn(page);
        when(mapper.toDto(blogNews)).thenReturn(blogNewsDTO);

        //When
        final Page<BlogNewsDTO> result = blogNewsService.getAllPublished(pageable, searchParam);
        List<BlogNewsDTO> blogNewsDTOList = result.toList();

        //Then
        assertNotNull(result);
        assertFalse(result.stream().findAny().isEmpty());
        assertFalse(blogNewsDTOList.isEmpty());
        assertEquals("Title", blogNewsDTOList.get(0).title());
        verify(this.blogNewsRepo).findByTitleContainingIgnoreCaseAndStatusIs(searchParam, PublicationStatus.PUBLISHED, pageable);
        verify(mapper).toDto(blogNews);
    }

    @Test
    @DisplayName("Should return Blog news")
    void GetOneShouldReturnBlogNews(){
        final Long newsId = 1L;
        //Given
        when(blogNewsRepo.findById(newsId)).thenReturn(Optional.ofNullable(blogNews));
        when(mapper.toDto(blogNews)).thenReturn(blogNewsDTO);

        //When
        final BlogNewsDTO result = blogNewsService.getOne(newsId);

        //Then
        assertNotNull(result);
        assertEquals("Title", result.title());
        verify(blogNewsRepo).findById(newsId);
        verify(mapper).toDto(blogNews);
    }

    @Test
    @DisplayName("Should return Exception")
    void GetOneShouldReturnException(){
        final Long newsId = 3L;
        //Given
        when(blogNewsRepo.findById(newsId)).thenReturn(Optional.empty());

        //When
        final EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> blogNewsService.getOne(newsId)
        );

        //Then
        assertEquals("Blog has not been found by ID", exception.getMessage());
        verify(blogNewsRepo, times(1)).findById(newsId);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should save BlogNews without Game reference")
    void ShouldSaveBlogNewsWithoutGame(){
        //Given
        Long userId = 1L;
        when(oAuth2User.getAttribute("userId")).thenReturn(userId);
        when(mapper.fromRequest(blogNewsRequest)).thenReturn(blogNews);
        when(userRepo.getReferenceById(userId)).thenReturn(user);
        when(blogNewsRepo.save(blogNews)).thenReturn(blogNews);
        when(mapper.toDto(blogNews)).thenReturn(blogNewsDTO);

        //When
        final BlogNewsDTO result = blogNewsService.save(blogNewsRequest, oAuth2User);

        //Then
        assertNotNull(result);
        assertEquals(blogNewsDTO, result);

        verify(mapper).fromRequest(blogNewsRequest);
        verify(userRepo).getReferenceById(userId);
        verify(blogNewsRepo).save(blogNews);
        verify(mapper).toDto(blogNews);

        assertEquals(user, blogNews.getAuthor());
    }

    @Test
    @DisplayName("Should save BlogNews with Game reference")
    void ShouldSaveBlogNewsWithGame(){
        //Given
        Long userId = 1L;
        when(oAuth2User.getAttribute("userId")).thenReturn(userId);
        when(mapper.fromRequest(blogNewsRequestWithGame)).thenReturn(blogNewsWithGame);
        when(userRepo.getReferenceById(userId)).thenReturn(user);
        when(blogNewsRepo.save(blogNews)).thenReturn(blogNews);
        when(mapper.toDto(blogNews)).thenReturn(blogNewsDTO);

        //When
        final BlogNewsDTO result = blogNewsService.save(blogNewsRequest, oAuth2User);

        //Then
        assertNotNull(result);
        assertEquals(blogNewsDTO, result);

        verify(mapper).fromRequest(blogNewsRequest);
        verify(userRepo).getReferenceById(userId);
        verify(blogNewsRepo).save(blogNews);
        verify(mapper).toDto(blogNews);

        assertEquals(user, blogNews.getAuthor());
    }
}