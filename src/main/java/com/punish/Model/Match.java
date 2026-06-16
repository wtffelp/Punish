package com.punish.Model;

public class Match {
    private Long id;
    private Long fk_tournament_id;
    private Long fk_player1_id;
    private Long fk_player2_id;
    private Long fk_winner_id;
    private Integer score_player1;
    private Integer score_player2;
    private String bracket_type;
    private int round_number;
    private Integer match_number;
    private Long fk_next_match_win_id;
    private Long fk_next_match_lose_id;
    private String status;

    public Match () {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFk_tournament_id() { return fk_tournament_id; }
    public void setFk_tournament_id(Long fk_tournament_id) { this.fk_tournament_id = fk_tournament_id; }

    public Long getFk_player1_id() { return fk_player1_id; }
    public void setFk_player1_id(Long fk_player1_id) { this.fk_player1_id = fk_player1_id; }

    public Long getFk_player2_id() { return fk_player2_id; }
    public void setFk_player2_id(Long fk_player2_id) { this.fk_player2_id = fk_player2_id; }

    public Long getFk_winner_id() { return fk_winner_id; }
    public void setFk_winner_id(Long fk_winner_id) { this.fk_winner_id = fk_winner_id; }

    public Integer getScore_player1() { return score_player1; }
    public void setScore_player1(Integer score_player1) { this.score_player1 = score_player1; }

    public Integer getScore_player2() { return score_player2; }
    public void setScore_player2(Integer score_player2) { this.score_player2 = score_player2; }

    public String getBracket_type() { return bracket_type; }
    public void setBracket_type(String bracket_type) { this.bracket_type = bracket_type; }

    public int getRound_number() { return round_number; }
    public void setRound_number(int round_number) { this.round_number = round_number; }

    public Integer getMatch_number() { return match_number; }
    public void setMatch_number(Integer match_number) { this.match_number = match_number; }

    public Long getFk_next_match_win_id() { return fk_next_match_win_id; }
    public void setFk_next_match_win_id(Long fk_next_match_win_id) { this.fk_next_match_win_id = fk_next_match_win_id; }

    public Long getfk_next_match_lose_id() { return fk_next_match_lose_id; }
    public void setfk_next_match_lose_id(Long fk_next_match_lose_id) { this.fk_next_match_lose_id = fk_next_match_lose_id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}
