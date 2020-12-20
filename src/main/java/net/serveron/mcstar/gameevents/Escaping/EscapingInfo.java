package net.serveron.mcstar.gameevents.Escaping;

import net.serveron.mcstar.gameevents.GameEvents;
import org.bukkit.Location;
import org.bukkit.scoreboard.Team;

public class EscapingInfo {
    public GameEvents plugin;
    public Location taggerSpawn;
    public Location captureSpawn;
    public int gameTime = 0;
    public Team taggerTeam;
    public Team escapeTeam;

    public EscapingInfo(GameEvents plugin){
        this.plugin = plugin;
    }

    public boolean startable(){
        return captureSpawn != null && taggerSpawn != null && taggerTeam != null && escapeTeam != null && gameTime != 0;
    }
    public boolean setInfo(String[] args){
        gameTime = stringToInt(args[2]);
        taggerTeam = plugin.mainScoreboard.getTeam(args[3]);
        escapeTeam = plugin.mainScoreboard.getTeam(args[4]);
        return gameTime != 0 && taggerTeam != null && escapeTeam != null;
    }

    private int stringToInt(String str){
        int x = 0;
        try{
            x = Integer.parseInt(str);
        }
        catch(Exception ignored){
        }
        return x;
    }
}
