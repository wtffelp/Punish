package com.punish.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.punish.Model.Match;
import com.punish.Model.Player;
import com.punish.Repository.MatchRepository;

public class BracketService {

    MatchRepository matchRepository = new MatchRepository();
    
    private int calcularTamanhoBracket(int numJogadores){
        int tamanho = 1;
        while (tamanho < numJogadores) {
            tamanho = tamanho * 2;
        }
        return tamanho;
    }
    public List<Match> gerarBracket(Long tournament_id, List<Player> players){
        Collections.shuffle(players);
        int tamanho_bracket = calcularTamanhoBracket(players.size());
        int byes = tamanho_bracket - players.size();
        List<Player> slots = new ArrayList<>();
        slots.addAll(players);
        int numRodadas = (int) (Math.log(tamanho_bracket) / Math.log(2));
        List<Match> matches = new ArrayList<>();
        for(int i = 0; i < byes; i++){
            slots.add(null);
        }
        for(int r = 1; r <= numRodadas; r++){
            int divisor = (int) Math.pow(2, r);
            int partidas_nessa_rodada = tamanho_bracket/divisor;
            for(int p = 0; p < partidas_nessa_rodada; p++){
                Match match = new Match();
                match.setFk_tournament_id(tournament_id);
                match.setRound_number(r);
                match.setMatch_number(p);
                match.setBracket_type("WINNERS");
                match.setStatus("WAITING");
                Match matchSalvo = matchRepository.criar(match);
                matches.add(matchSalvo);
            }
        }
        for (Match match : matches) {
            Match prox = matches.stream()
                         .filter(m -> m.getRound_number() == match.getRound_number() + 1 && m.getMatch_number() == match.getMatch_number()/2)
                         .findFirst()
                         .orElse(null);
            if (prox != null) {
                match.setFk_next_match_win_id(prox.getId());
                matchRepository.atualizarNextMatchWin(match.getFk_next_match_win_id(), match.getId());
            }
        }

        List<Match> primeiraRodada = matches.stream()
                                     .filter(m -> m.getRound_number() == 1)
                                     .toList();
        int indice = 0;

        for (Match match : primeiraRodada) {
            Player p1 = slots.get(indice++);
            Player p2 = slots.get(indice++);

            if (p1 != null && p2 != null) {
                matchRepository.atualizarPlayer1(match.getId(), p1.getId());
                matchRepository.atualizarPlayer2(match.getId(), p2.getId());
                matchRepository.atualizarStatus("READY", match.getId());
            } else if (p1 == null){
                matchRepository.atualizarPlayer2(match.getId(), p2.getId());
                matchRepository.atualizarVencedor(match.getId(), p2.getId(), 0, 0);
                matchRepository.atualizarStatus("FINISHED", match.getId());
                Match prox = matches.stream()
                             .filter(m -> m.getId().equals(match.getFk_next_match_win_id()))
                             .findFirst()
                             .orElse(null);
                if (prox.getFk_player1_id() == null) {
                    matchRepository.atualizarPlayer2(prox.getId(), p2.getId());
                } else {
                    matchRepository.atualizarPlayer1(prox.getId(), p2.getId());
                }
            } else if (p2 == null){
                matchRepository.atualizarPlayer1(match.getId(), p1.getId());
                matchRepository.atualizarVencedor(match.getId(), p1.getId(), 0,0);
                matchRepository.atualizarStatus("FINISHED", match.getId());
                Match prox = matches.stream()
                             .filter(m -> m.getId().equals(match.getFk_next_match_win_id()))
                             .findFirst()
                             .orElse(null);
                if (prox.getFk_player1_id() == null) {
                    matchRepository.atualizarPlayer1(prox.getId(), p1.getId());
                } else {
                    matchRepository.atualizarPlayer2(prox.getId(), p1.getId());
                }
            }
        }
        return matches;
    }
}
