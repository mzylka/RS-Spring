package com.rs.app.domain.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String discordName;
    private String discordId;
    private String steamUrl;
    private String avatarUrl;

    private Instant createdAt = Instant.now();
    private Instant lastLogged = Instant.now();

    private Instant bannedTo;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<BlogNews> blogNews;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Community> community;

    @OneToOne(fetch = FetchType.LAZY)
    private YoutubeChannel youtubeChannel;

    @OneToOne(fetch = FetchType.LAZY)
    private TwitchStream twitchStream;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Tournament> tournaments;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "liked")
    private Set<BlogNewsComment> likes;

    protected User(){}

    public User(String discordId, String discordName, String avatarUrl){
        this.discordId = discordId;
        this.discordName = discordName;
        this.avatarUrl = avatarUrl;
    }

    public Long getId() {
        return id;
    }

    public String getDiscordName() {
        return discordName;
    }

    public void setDiscordName(String discordName) {
        this.discordName = discordName;
    }

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }

    public String getSteamUrl() {
        return steamUrl;
    }

    public void setSteamUrl(String steamUrl) {
        this.steamUrl = steamUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public List<BlogNews> getBlogNews() {
        return blogNews;
    }

    public void setBlogNews(List<BlogNews> blogNews) {
        this.blogNews = blogNews;
    }

    public List<Community> getCommunity() {
        return community;
    }

    public void setCommunity(List<Community> community) {
        this.community = community;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Instant getBannedTo() {
        return bannedTo;
    }

    public void setBannedTo(Instant bannedTo) {
        this.bannedTo = bannedTo;
    }

    public void ban(int days){
        bannedTo = Instant.now().plus(days, ChronoUnit.DAYS);
    }

    public TwitchStream getTwitchStream() {
        return twitchStream;
    }

    public void setTwitchStream(TwitchStream twitchStream) {
        this.twitchStream = twitchStream;
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    public YoutubeChannel getYoutubeChannel() {
        return youtubeChannel;
    }

    public void setYoutubeChannel(YoutubeChannel youtubeChannel) {
        this.youtubeChannel = youtubeChannel;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastLogged() {
        return lastLogged;
    }

    public void setLastLogged(Instant lastLogged) {
        this.lastLogged = lastLogged;
    }

    public Set<BlogNewsComment> getLikes() {
        return likes;
    }

    public void setLikes(Set<BlogNewsComment> likes) {
        this.likes = likes;
    }

    public boolean isBanned(){
        if (bannedTo == null){
            return false;
        }
        else {
            return bannedTo.isAfter(Instant.now());
        }
    }

    public boolean hasRole(String roleName){
        for (Role r: roles){
            if(Objects.equals(r.getName(), roleName)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, discordId);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", discordName='" + discordName + '\'' +
                ", discordId='" + discordId + '\'' +
                ", steamUrl='" + steamUrl + '\'' +
                ", createdAt=" + createdAt +
                ", lastLogged=" + lastLogged +
                ", bannedTo=" + bannedTo +
                ", community=" + community +
                ", youtubeChannel=" + youtubeChannel +
                ", twitchStream=" + twitchStream +
                ", roles=" + roles +
                ", likes=" + likes +
                '}';
    }
}
