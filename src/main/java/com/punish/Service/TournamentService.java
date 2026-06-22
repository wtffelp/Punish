package com.punish.Service;

import java.util.List;

import com.punish.Model.Tournament;
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
        return tournamentRepository.buscarPorId(id);
    }

    public List<Tournament> buscarPorNome(String name){
        return tournamentRepository.buscarPorNome(name);
    }

    public Tournament atualizarTournament(Long id, String name, String game){
        return tournamentRepository.atualizarTournament(id, name, game);
    }
    
    public Tournament atualizarStatus(Long id, String status){
        return tournamentRepository.atualizarStatus(id, status);
    }

    public void deletar(Long id){
        tournamentRepository.deletar(id);
    }
}
