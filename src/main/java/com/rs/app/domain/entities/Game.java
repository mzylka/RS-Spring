package com.rs.app.domain.entities;

import com.rs.app.domain.enums.PublicationStatus;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "games")
public class Game extends BasePublication {
    private String iconUrl;
    private String websiteUrl;
    private String xUrl;
    private String fbUrl;
    private String steamUrl;
    private Date releaseDate;
    private String appId;

    @OneToMany(mappedBy = "game")
    private List<BlogNews> blogNews;

    @OneToMany(mappedBy = "game")
    private List<Community> communities;

    @OneToMany(mappedBy = "game")
    private List<Tournament> tournaments;

    public Game() {}

    public Game(String title, String content, String websiteUrl, String xUrl, String fbUrl, String steamUrl, Date releaseDate, String appId, PublicationStatus status) {
        super(title, content, status);
        this.websiteUrl = websiteUrl;
        this.xUrl = xUrl;
        this.fbUrl = fbUrl;
        this.steamUrl = steamUrl;
        this.releaseDate = releaseDate;
        this.appId = appId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getxUrl() {
        return xUrl;
    }

    public void setxUrl(String xUrl) {
        this.xUrl = xUrl;
    }

    public String getFbUrl() {
        return fbUrl;
    }

    public void setFbUrl(String fbUrl) {
        this.fbUrl = fbUrl;
    }

    public String getSteamUrl() {
        return steamUrl;
    }

    public void setSteamUrl(String steamUrl) {
        this.steamUrl = steamUrl;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<BlogNews> getBlogNews() {
        return blogNews;
    }

    public void setBlogNews(List<BlogNews> blogNews) {
        this.blogNews = blogNews;
    }

    public List<Community> getCommunities() {
        return communities;
    }

    public void setCommunities(List<Community> communities) {
        this.communities = communities;
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) && Objects.equals(title, game.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", slug='" + slug + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", thumbnailMin='" + thumbnailMin + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", publicationStatus=" + status +
                ", iconUrl='" + iconUrl + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", xUrl='" + xUrl + '\'' +
                ", fbUrl='" + fbUrl + '\'' +
                ", steamUrl='" + steamUrl + '\'' +
                ", releaseDate=" + releaseDate +
                ", appId='" + appId + '\'' +
                ", blogNews=" + blogNews +
                ", communities=" + communities +
                '}';
    }
}
