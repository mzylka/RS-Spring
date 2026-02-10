package com.rs.app.domain.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "youtube_channels")
public class YoutubeChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String url;
    private String channelName;

    @OneToOne
    @JoinColumn(name = "owner")
    private User owner;

    public Long getId() {
        return Id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    protected YoutubeChannel(){}

    public YoutubeChannel(String url, String channelName){
        this.url = url;
        this.channelName = channelName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        YoutubeChannel that = (YoutubeChannel) o;
        return Objects.equals(Id, that.Id) && Objects.equals(channelName, that.channelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, channelName);
    }

    @Override
    public String toString() {
        return "YoutubeChannel{" +
                "Id=" + Id +
                ", url='" + url + '\'' +
                ", channelName='" + channelName + '\'' +
                ", user=" + owner +
                '}';
    }
}
