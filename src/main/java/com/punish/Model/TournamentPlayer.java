package com.punish.Model;

public class TournamentPlayer {
    private Long id;
    private Long fk_tournament_id;
    private Long fk_player_id;
    private Integer seed;

    public TournamentPlayer () {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFk_tournament_id() { return fk_tournament_id; }
    public void setFk_tournament_id(Long fk_tournament_id) { this.fk_tournament_id = fk_tournament_id; }

    public Long getFk_player_id() { return fk_player_id; }
    public void setFk_player_id(Long fk_player_id) { this.fk_player_id = fk_player_id; }

    public Integer getSeed() { return seed; }
    public void setSeed(Integer seed) { this.seed = seed; }
}
