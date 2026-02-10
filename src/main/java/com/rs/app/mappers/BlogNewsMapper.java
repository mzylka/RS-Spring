package com.rs.app.mappers;

import com.rs.app.domain.entities.BlogNews;
import com.rs.app.dto.blognews.BlogNewsDTO;
import com.rs.app.dto.blognews.BlogNewsRequest;
import com.rs.app.dto.blognews.BlogNewsSimpleDTO;

public interface BlogNewsMapper{
    BlogNews fromRequest(BlogNewsRequest blogNewsRequest);
    BlogNewsDTO toDto(BlogNews blogNews);
    BlogNewsSimpleDTO toSimpleDto(BlogNews blogNews);
}
