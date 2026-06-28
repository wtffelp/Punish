package com.punish;

import com.punish.Controller.DashboardController;
import com.punish.Controller.MatchController;
import com.punish.Controller.PlayerController;
import com.punish.Controller.TournamentController;

import io.javalin.Javalin;

public class App 
{
    public static void main( String[] args )
    {
        Javalin javalin = Javalin.create( config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> {
                    // rule.allowHost("https://punish.vercel.app")
                    rule.anyHost();
                });
            });
        }).start(7000);

        new DashboardController().dashboardRoutes(javalin);
        new MatchController().matchRoutes(javalin);
        new PlayerController().playerRoutes(javalin);
        new TournamentController().tournamentRoutes(javalin);

    }
}
