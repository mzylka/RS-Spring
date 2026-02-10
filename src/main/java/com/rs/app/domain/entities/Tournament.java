package com.rs.app.domain.entities;

import com.rs.app.domain.enums.PublicationStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
public class Tournament extends BasePublication{
    private String format;
    private String ladderUrl;
    private String challongeUrl;
    private String streamUrl;
    private String vodsUrl;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<TournamentMatch> tournamentMatches;

    public Tournament(String title, String format, String content, String ladderUrl, String challongeUrl, String streamUrl, String vodsUrl, LocalDate startDate, LocalDate endDate, PublicationStatus status) {
        super(title, content, status);
        this.format = format;
        this.ladderUrl = ladderUrl;
        this.challongeUrl = challongeUrl;
        this.streamUrl = streamUrl;
        this.vodsUrl = vodsUrl;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Tournament(){}

    public String getFormat() {
        return format;
    }

    public void setFormat(String type) {
        this.format = type;
    }

    public String getLadderUrl() {
        return ladderUrl;
    }

    public void setLadderUrl(String ladderUrl) {
        this.ladderUrl = ladderUrl;
    }

    public String getChallongeUrl() {
        return challongeUrl;
    }

    public void setChallongeUrl(String challongeUrl) {
        this.challongeUrl = challongeUrl;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getVodsUrl() {
        return vodsUrl;
    }

    public void setVodsUrl(String vodsUrl) {
        this.vodsUrl = vodsUrl;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<TournamentMatch> getMatches() {
        return tournamentMatches;
    }

    public void setMatches(List<TournamentMatch> tournamentMatches) {
        this.tournamentMatches = tournamentMatches;
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
        Tournament that = (Tournament) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", format='" + format + '\'' +
                ", content='" + content + '\'' +
                ", ladderUrl='" + ladderUrl + '\'' +
                ", challongeUrl='" + challongeUrl + '\'' +
                ", streamUrl='" + streamUrl + '\'' +
                ", vodsUrl='" + vodsUrl + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", creator=" + owner +
                '}';
    }
}
