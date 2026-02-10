package com.rs.app.domain.entities;

import com.rs.app.domain.enums.PublicationStatus;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Community extends BasePublication {
    private String websiteUrl;
    private String discordUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    private Game game;

    public Community(){}

    public Community(String name, String content, String websiteUrl, String discordUrl, PublicationStatus status) {
        super(name, content, status);
        this.websiteUrl = websiteUrl;
        this.discordUrl = discordUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getDiscordUrl() {
        return discordUrl;
    }

    public void setDiscordUrl(String discordUrl) {
        this.discordUrl = discordUrl;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Community community = (Community) o;
        return Objects.equals(id, community.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Community{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", discordUrl='" + discordUrl + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", owner=" + owner +
                ", game=" + game +
                ", author=" + author +
                '}';
    }


}
