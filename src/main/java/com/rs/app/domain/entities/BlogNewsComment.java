package com.rs.app.domain.entities;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "comments_blog_news")
public class BlogNewsComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private Instant createdAt = Instant.now();
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "blog_news_id")
    private BlogNews blogNews;

    @PreUpdate
    public void onUpdate(){
        updatedAt = Instant.now();
    }

    @ManyToMany
    @JoinTable(
            name = "comment_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> liked;

    public BlogNewsComment(String content) {
        this.content = content;
    }

    public BlogNewsComment(){}

    public Long getId() {
        return id;
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

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BlogNews getBlogNews() {
        return blogNews;
    }

    public void setBlogNews(BlogNews blogNews) {
        this.blogNews = blogNews;
    }

    public Set<User> getLiked() {
        return liked;
    }

    public void setLiked(Set<User> liked) {
        this.liked = liked;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BlogNewsComment that = (BlogNewsComment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CommentBlogNews{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", user=" + user +
                ", blogNews=" + blogNews +
                ", liked=" + liked +
                '}';
    }
}
