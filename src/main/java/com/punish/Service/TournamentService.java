package com.punish.Service;

import java.util.List;

import com.punish.Model.Tournament;
import com.punish.Model.Enums.TournamentStatus;
import com.punish.Repository.TournamentRepository;

public class TournamentService {
    TournamentRepository tournamentRepository = new TournamentRepository();

    public Tournament criarTournament(String name, String game){
        return tournamentRepository.criarTournament(name, game);
    }

    public List<Tournament> buscarTodosOsTorneios(){
        return tournamentRepository.buscarTodosOsTorneios();
    }

    public Tournament buscarPorId(Long id){
        Tournament t = tournamentRepository.buscarPorId(id);
        if (t == null) throw new RuntimeException("Torneio não encontrado");
        return t;
    }

    public List<Tournament> buscarPorNome(String name){
        return tournamentRepository.buscarPorNome(name);
    }

    public Tournament atualizarTournament(Long id, String name, String game){
        Tournament t = buscarPorId(id);
        if (t.getStatus() == TournamentStatus.FINISHED) {
            throw new RuntimeException("Torneio ja foi encerrado");
        }
        return tournamentRepository.atualizarTournament(id, name, game);
    }
    
    public Tournament atualizarStatus(Long id, String status){
        buscarPorId(id);
        return tournamentRepository.atualizarStatus(id, status);
    }

    public void start(Long id) {
        Tournament t = buscarPorId(id);
        if (t.getStatus() != TournamentStatus.CREATED) {
            throw new RuntimeException("Torneio não pode ser iniciado");
        }
        tournamentRepository.atualizarStatus(id, "STARTED");
    }

    public void finalizar(Long id) {
        Tournament t = buscarPorId(id);
        if (t.getStatus() != TournamentStatus.CREATED) {
            throw new RuntimeException("Torneio não pode ser iniciado");
        }
        tournamentRepository.atualizarStatus(id, "F");
    }

    public void deletar(Long id){
        buscarPorId(id);
        tournamentRepository.deletar(id);
    }
}
