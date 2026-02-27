package com.rs.app.domain.entities;

import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.util.Slugify;
import jakarta.persistence.*;

import java.time.Instant;

@MappedSuperclass
public class BasePublication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected String title;
    protected String slug;
    protected String thumbnail;
    protected String thumbnailMin;

    @Column(columnDefinition = "TEXT", nullable = false)
    protected String content;

    protected Instant createdAt = Instant.now();
    protected Instant updatedAt;

    @Enumerated(EnumType.STRING)
    protected PublicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    protected User author;

    protected BasePublication(){}

    protected BasePublication(String title, String content, PublicationStatus status){
        this.title = title;
        this.slug = Slugify.slug(title);
        this.content = content;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.slug = Slugify.slug(title);
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailMin() {
        return thumbnailMin;
    }

    public void setThumbnailMin(String thumbnailMin) {
        this.thumbnailMin = thumbnailMin;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @PostUpdate
    public void update() {
        this.updatedAt = Instant.now();
    }

    public PublicationStatus getStatus() {
        return status;
    }

    public void setStatus(PublicationStatus status) {
        this.status = status;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

}
