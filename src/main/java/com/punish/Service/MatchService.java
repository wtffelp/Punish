package com.punish.Service;

import java.util.List;

import com.punish.Model.Match;
import com.punish.Repository.MatchRepository;
import com.punish.Repository.TournamentRepository;

public class MatchService {
    MatchRepository matchRepository = new MatchRepository();
    TournamentRepository tournamentRepository = new TournamentRepository();

    public Match buscarPorId(Long id){
        Match m = matchRepository.buscarPorId(id);
        if (m == null) {
            throw new RuntimeException("Partida não encontrada");
        }
        return m;
    }

    public List<Match> buscarPorTournament(long fk_tournament_id){
        return matchRepository.buscarPorTournament(fk_tournament_id);
    }

    public Match registrarResultado(Long id, Long fk_winner_id, Integer score_player1, Integer score_player2){
        Match m = matchRepository.buscarPorId(id);
        if (m == null) throw new RuntimeException("Partida não encontrada");
        if (!"READY".equals(m.getStatus()) && !"IN_PROGRESS".equals(m.getStatus())){
            throw new RuntimeException("Partida não está em andamento");
        }
        if (fk_winner_id == null) {
            throw new RuntimeException("Vencedor não informado");
        }
        if (!fk_winner_id.equals(m.getFk_player1_id()) && !fk_winner_id.equals(m.getFk_player2_id())) {
            throw new RuntimeException("Vencedor inválido");
        }
        matchRepository.atualizarVencedor(id, fk_winner_id, score_player1, score_player2);
        Match next_match = matchRepository.buscarPorId(m.getFk_next_match_win_id());
        if (next_match == null) {
            tournamentRepository.atualizarCampeao(id, fk_winner_id);
            tournamentRepository.atualizarStatus(id, "FINISHED");
            return matchRepository.buscarPorId(id);
        }
        if (next_match.getFk_player1_id() == null) {
            matchRepository.atualizarPlayer1(m.getFk_next_match_win_id(), fk_winner_id);
        } else if (next_match.getFk_player2_id() == null) {
            matchRepository.atualizarPlayer2(m.getFk_next_match_win_id(), fk_winner_id);
        } else {
            throw new RuntimeException("Não existe vaga nessa partida");
        }
        return matchRepository.buscarPorId(id);
    }
}