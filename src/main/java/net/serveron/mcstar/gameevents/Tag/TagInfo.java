package net.serveron.mcstar.gameevents.Tag;

import net.serveron.mcstar.gameevents.GameEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class TagInfo {
    public GameEvent plugin;
    public Location taggerSpawn;
    public int gameTime = 0;
    public Team taggerTeam;
    public Team escapeTeam;

    public TagInfo(GameEvent plugin){
        this.plugin = plugin;
    }

    public boolean startable(){
        if(taggerSpawn!=null && taggerTeam!=null && escapeTeam!=null && gameTime!=0 ){
            return true;
        } else {
            return false;
        }
    }
    public boolean setInfo(String[] args){
        gameTime = stringToInt(args[2]);
        taggerTeam = plugin.mainScoreboard.getTeam(args[3]);
        escapeTeam = plugin.mainScoreboard.getTeam(args[4]);
        if(gameTime!=0 && taggerTeam != null && escapeTeam != null){
            return true;
        } else {
            return false;
        }
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
