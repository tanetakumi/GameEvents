package net.serveron.mcstar.gameevents.Tag;

import org.bukkit.Location;
import org.bukkit.scoreboard.Team;

public class TagInfo {
    public Location taggerSpawn;
    public int gameTime = 0;
    public Team taggerTeam;
    public Team escapeTeam;

    public boolean startable(){
        if(taggerSpawn!=null && gameTime!=0 && taggerTeam!=null
            && escapeTeam !=null){
            return true;
        }
        else return false;
    }

    public String getInfo(){
        String text = "おにリス:("+taggerSpawn.getBlockX()+","+taggerSpawn.getBlockY()+","
                + taggerSpawn.getBlockZ()+")";
        text += "時間:"+gameTime;
        return text;
    }
}
