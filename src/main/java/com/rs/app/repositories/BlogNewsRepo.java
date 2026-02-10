package com.rs.app.repositories;

import com.rs.app.domain.entities.BlogNews;
import com.rs.app.domain.enums.PublicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogNewsRepo extends JpaRepository<BlogNews, Long> {
    Page<BlogNews> findByGameId(Long gameId, Pageable pageable);
    Page<BlogNews> findByGameIdAndStatusIs(Long gameId, PublicationStatus status, Pageable pageable);
    Page<BlogNews> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<BlogNews> findByStatus(PublicationStatus status, Pageable pageable);
    Page<BlogNews> findByTitleContainingIgnoreCaseAndStatusIs(String title, PublicationStatus status, Pageable pageable);
    Page<BlogNews> findByAuthorId(Long authorId, Pageable pageable);
    Page<BlogNews> findByAuthorIdAndStatusIs(Long authorId, PublicationStatus status, Pageable pageable);
}
