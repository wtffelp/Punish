package com.punish.Repository;

import java.util.List;
import java.util.Optional;

import org.jdbi.v3.core.Jdbi;

import com.punish.Config.Database;
import com.punish.Model.Tournament;

public class TournamentRepository {
    Jdbi jdbi = Database.getJdbi();
    public Tournament criarTournament(String name, String game){ 
        return jdbi.withHandle(handle -> {
            return handle.createUpdate("""
                INSERT INTO tournament (name, game) VALUES (:name, :game)
            """)
            .bind("name", name)
            .bind("game", game)
            .executeAndReturnGeneratedKeys("id")
            .mapToBean(Tournament.class)
            .findOne()
            .orElse(null);
        });
    }
    
    public List<Tournament> buscarTodosOsTorneios(){
        return jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM tournament")
            .mapToBean(Tournament.class)
            .list();
        });
    }

    public Tournament buscarPorId(Long id){
        Tournament tournament = jdbi.withHandle(handle -> {
            Optional<Tournament> result = handle.createQuery("SELECT * FROM tournament WHERE id = :id")
            .bind("id", id)
            .mapToBean(Tournament.class)
            .findOne();
            return result.orElse(null);
        });
        return tournament;
    }

    public List<Tournament> buscarPorNome(String name){
        return jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM tournament WHERE name")
                .bind("name", name)
                .mapToBean(Tournament.class)
                .list();
        });
    }

    public Tournament atualizarTournament(Long id, String name, String game){
        jdbi.withHandle(handle -> {
            return handle.createUpdate("UPDATE tournament SET name = :name, game = :game WHERE id = :id")
            .bind("name", name)
            .bind("game", game)
            .bind("id", id)
            .execute();
        });
        Tournament tournament = buscarPorId(id);
        return tournament;
    }

    public Tournament atualizarStatus(Long id, String status){
        jdbi.withHandle(handle -> {
            return handle.createUpdate("UPDATE tournament SET status = :status WHERE id = :id")
            .bind("status", status)
            .bind("id", id)
            .execute();
        });
        Tournament tournament = buscarPorId(id);
        return tournament;
    }

    public void deletar(Long id){
        jdbi.withHandle(handle -> {
            return handle.createUpdate("DELETE FROM tournament WHERE id = :id")
            .bind("id", id)
            .execute();
        });
    }
}
