package com.punish.Service;

import java.util.List;

import com.punish.Model.Match;
import com.punish.Repository.MatchRepository;

public class MatchService {
    MatchRepository matchRepository = new MatchRepository();

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
        return matchRepository.buscarPorId(id);
    }
}