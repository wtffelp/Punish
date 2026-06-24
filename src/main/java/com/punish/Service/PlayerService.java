package com.punish.Service;

import java.util.List;

import com.punish.Model.Player;
import com.punish.Model.Tournament;
import com.punish.Model.TournamentPlayer;
import com.punish.Model.Enums.TournamentStatus;
import com.punish.Repository.PlayerRepository;
import com.punish.Repository.TournamentPlayerRepository;

public class PlayerService {
    private TournamentService tournamentService = new TournamentService();
    private TournamentPlayerRepository tournamentPlayerRepository = new TournamentPlayerRepository();
    PlayerRepository playerRepository = new PlayerRepository();

    public Player criarPlayer(String nickname){
        return playerRepository.criarPlayer(nickname);
    }

    public Player buscarPorId(Long id){
        Player p = playerRepository.buscarPorId(id);
        if (p == null) throw new RuntimeException("Player não encontrado");
        return p;
    }

    public List<Player> buscarPorNickname(String nickname){
        return playerRepository.buscarPorNickname(nickname);
    }

    public List<Player> buscarTodosOsPlayers(){
        return playerRepository.buscarTodosOsPlayers();
    }

    public void adicionarAoTournament(Long tournament_id, Long player_id){
        Tournament t = tournamentService.buscarPorId(tournament_id);
        if (t.getStatus() != TournamentStatus.CREATED) {
            throw new RuntimeException("Torneio não está aberto para inscrição");
        }
        buscarPorId(player_id);
        if (tournamentPlayerRepository.existe(tournament_id, player_id)){
            throw new RuntimeException("Jogador já está no torneio");
        }
        if (tournamentPlayerRepository.contarPorTournament(tournament_id) >= 16) {
            throw new RuntimeException("Torneio cheio");
        }
        tournamentPlayerRepository.criarTournamentPlayer(tournament_id, player_id);
    }

    public void removerPlayersDoTournament(Long tournament_id, Long player_id){
        Tournament t = tournamentService.buscarPorId(tournament_id);
        if (t.getStatus() != TournamentStatus.CREATED) {
            throw new RuntimeException("Não é possivel remover players");
        }
        tournamentPlayerRepository.deletarTournamentPlayer(tournament_id, player_id);
    }

    public List<Player> buscarPlayersDoTournament(Long tournament_id){
        return tournamentPlayerRepository.buscarPlayerDoTournament (tournament_id);
    }
}
