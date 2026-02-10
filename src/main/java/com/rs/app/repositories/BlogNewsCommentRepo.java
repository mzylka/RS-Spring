package com.rs.app.repositories;

import com.rs.app.domain.entities.BlogNewsComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogNewsCommentRepo extends JpaRepository<BlogNewsComment, Long> {
    Page<BlogNewsComment> findByUserId(Long userId, Pageable pageable);
}
