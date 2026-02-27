package com.rs.app.controllers;

import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.dto.blognews.BlogNewsDTO;
import com.rs.app.dto.game.GameSimpleDTO;
import com.rs.app.dto.user.UserSimpleDTO;
import com.rs.app.services.BlogNewsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlogNewsController.class)
class BlogNewsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BlogNewsService blogNewsService;

    @Test
    void shouldGetOneReturnBlogNewsDTO() throws Exception {
        BlogNewsDTO blogNewsDTO = new BlogNewsDTO(
                "",
                "",
                "",
                "",
                "",
                Instant.now(),
                PublicationStatus.PUBLISHED,
                new GameSimpleDTO(1L, "name", "name", "s", "smin", "ico"),
                new UserSimpleDTO(1L, "name", "avatar"));
        when(blogNewsService.getOne(1L)).thenReturn(blogNewsDTO);

        mockMvc.perform(get("/blog-news/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blogNewsDTO))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("title"));

        verify(blogNewsService).getOne(1L);
    }
}