package com.rs.app.domain.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "twitch_streams")
public class TwitchStream {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String twitchName;

    @OneToOne
    private User owner;

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTwitchName() {
        return twitchName;
    }

    public void setTwitchName(String twitchName) {
        this.twitchName = twitchName;
    }

    public User getUser() {
        return owner;
    }

    public void setUser(User owner) {
        this.owner = owner;
    }

    public TwitchStream(String url, User owner) {
        this.url = url;
        this.owner = owner;
    }

    public TwitchStream(){}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TwitchStream that = (TwitchStream) o;
        return Objects.equals(id, that.id) && Objects.equals(twitchName, that.twitchName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, twitchName);
    }

    @Override
    public String toString() {
        return "TwitchStream{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", twitchName='" + twitchName + '\'' +
                ", user=" + owner +
                '}';
    }
}
