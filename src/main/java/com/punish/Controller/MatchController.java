package com.punish.Controller;

import java.util.List;
import java.util.Map;

import com.punish.Model.Match;
import com.punish.Service.MatchService;

import io.javalin.Javalin;

public class MatchController {
    MatchService matchService = new MatchService();

    public void matchRoutes(Javalin app){
        app.get("/tournaments/{id}/matches", ctx -> {
            Long tournament_id = Long.parseLong(ctx.pathParam("id"));
            List<Match> m = matchService.buscarPorTournament(tournament_id);
            ctx.json(m);
        });

        app.get("/matches/{id}", ctx -> {
            Long match_id = Long.parseLong(ctx.pathParam("id"));
            Match m = matchService.buscarPorId(match_id);
            ctx.json(m);
        });

        app.patch("/matches/{id}/result", ctx -> {
            Long match_id = Long.parseLong(ctx.pathParam("id"));
            Map<String, Integer> body = ctx.bodyAsClass(Map.class);
            Long winner = body.get("fk_winner_id").longValue();
            Match matchAtualizada = matchService.registrarResultado(match_id, winner, body.get("score_player1"), body.get("score_player2"));
            ctx.json(matchAtualizada);
        });
    }
}
