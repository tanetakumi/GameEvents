package net.serveron.mcstar.gameevents.BreakBlockRun;

import net.serveron.mcstar.gameevents.GameEvents;
import org.bukkit.Location;
import org.bukkit.scoreboard.Team;

//import javax.xml.stream.Location;

public class BlockRunInfo {
    public GameEvents plugin;


    public Location stageLoc;
    public int stageSize = 0;
    public boolean stage = false;
    public Team team;

    public BlockRunInfo(GameEvents plugin){
        this.plugin = plugin;

    }

    public boolean constructStage(){
        if(stageLoc!=null && stageSize!=0){
            new BlockRunStudium().constructStudium(plugin,stageLoc,stageSize);
            stage = true;
            return true;
        } else {
            return false;
        }
    }

    public void deConstructStage(){
        if(stage){
            new BlockRunStudium().deConstructStudium(stageLoc,stageSize);
            stage = false;
        }
    }

    public void downsizeStage(){
        if(stage){
            new BlockRunStudium().downsizeStudium(stageLoc,stageSize);
        }
    }

    public boolean setInfo(String[] args){
        team = plugin.mainScoreboard.getTeam(args[2]);
        return team != null;
    }

    public boolean startable(){
        return team != null && stage && stageSize != 0 && stageLoc != null;
    }
}
