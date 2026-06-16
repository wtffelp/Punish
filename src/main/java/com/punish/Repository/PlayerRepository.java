package com.punish.Repository;

import java.util.List;
import java.util.Optional;

import org.jdbi.v3.core.Jdbi;

import com.punish.Config.Database;
import com.punish.Model.Tournament;

public class PlayerRepository {
    // - criar(String nickname) → Player
    // - buscarPorId(long id) → Player (ou null)
    // - buscarPorNickname(String nickname) → Player (ou null)
    // - buscarTodos() → List<Player>
    Jdbi jdbi = Database.getJdbi();
    public Tournament criarPlayer(String nickname){
        return jdbi.withHandle(handle -> {
            return handle.createUpdate("INSERT INTO player (nicknack) VALUES (nickname)")
            .bind("nickname", nickname)
            .executeAndReturnGeneratedKeys("id")
            .mapToBean(Tournament.class)
            .findOne()
            .orElse(null);
        });
    }

    public Tournament buscarPorId(Long id){
        Tournament tournament = jdbi.withHandle(handle -> {
            Optional<Tournament> result = handle.createQuery("SELECT * FROM player WHERE id = :id")
            .bind("id", id)
            .mapToBean(Tournament.class)
            .findOne();
            return result.orElse(null);
        });
        return tournament;
    }

    public List<Tournament> buscarPorNickname(String nickname){
        return jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM player WHERE nickname = :nickname")
                .bind("nickname", nickname)
                .mapToBean(Tournament.class)
                .list();
        });
    }

    public List<Tournament> buscarTodosOsPlayers(){
        return jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM player")
            .mapToBean(Tournament.class)
            .list();
        });
    }
}
