package com.rs.app.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tournament_matches")
public class TournamentMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String player1;
    private String player2;
    private String streamUrl;
    private LocalDateTime startTime;

    public TournamentMatch(){}

    public TournamentMatch(String player1, String player2, String streamUrl, LocalDateTime startTime){
        this.player1 = player1;
        this.player2 = player2;
        this.streamUrl = streamUrl;
        this.startTime = startTime;
    }

    @ManyToOne
    private Tournament tournament;

    public Long getId() {
        return id;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TournamentMatch tournamentMatch = (TournamentMatch) o;
        return Objects.equals(id, tournamentMatch.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", player1='" + player1 + '\'' +
                ", player2='" + player2 + '\'' +
                ", streamUrl='" + streamUrl + '\'' +
                ", start=" + startTime +
                ", tournament=" + tournament +
                '}';
    }
}
