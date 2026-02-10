package com.rs.app.domain.entities;

import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.util.Slugify;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "blog_news", indexes = @Index(columnList = "status"))
public class BlogNews extends BasePublication {

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "blogNews", cascade = CascadeType.REMOVE)
    private List<BlogNewsComment> comments;

    public BlogNews(){}

    public BlogNews(String title, String content, PublicationStatus status) {
        super(title, content, status);
        this.title = title;
        this.slug = Slugify.slug(title);
        this.content = content;
        this.status = status;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<BlogNewsComment> getComments() {
        return comments;
    }

    public void setComments(List<BlogNewsComment> comments) {
        this.comments = comments;
    }

    public void addComment(BlogNewsComment comment){
        comments.add(comment);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BlogNews blogNews = (BlogNews) o;
        return Objects.equals(id, blogNews.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BlogNews{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", game=" + game +
                ", author=" + author +
                ", comments=" + comments +
                ", status=" + status +
                '}';
    }
}
