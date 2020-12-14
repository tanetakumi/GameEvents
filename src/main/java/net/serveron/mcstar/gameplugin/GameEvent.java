package net.serveron.mcstar.gameplugin;

//import net.serveron.mcstar.gameplugin.Milk.MilkListener;
import net.serveron.mcstar.gameplugin.BreakBlockRun.BlockRunListener;
import net.serveron.mcstar.gameplugin.Tag.TagListener;
import net.serveron.mcstar.gameplugin.TeamBattle.TeamBattleListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;

public final class GameEvent extends JavaPlugin {

    public MainboardManager mainboardManager;
    public Scoreboard mainScoreboard;
    public List<TeamInfo> teamInfoList = new ArrayList<>();

    TeamBattleListener teamBattleListener;
    TagListener tagListener;
    BlockRunListener blockRunListener;
    //MilkListener milkListener;
    String prepareGame = "";
    boolean listenable = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        mainScoreboard = manager.getMainScoreboard();
        mainboardManager = new MainboardManager(this);

        new GameCommand(this);
        //milkListener = new MilkListener(this);
    }
    public boolean prepareForGame(String game_name){
        if(game_name == "blockrun"){
            blockRunListener = new BlockRunListener(this);
            return true;
        } else {
            return false;
        }
    }
    public boolean hasPreparedForGame(String game_name){
        if(game_name == "blockrun"){

            return true;
        } else {
            return false;
        }
    }
    public boolean Start(String game_name, int time, TeamInfo team1, TeamInfo team2) {
        if(!listenable){
            if(game_name.equalsIgnoreCase("tag")){
                tagListener = new TagListener(this);
                tagListener.initListener(time,team1,team2);
                listenable =true;
                return true;
            }
            else if(game_name.equalsIgnoreCase("teambattle")){
                teamBattleListener = new TeamBattleListener(this);
                teamBattleListener.initListener(time,team1,team2);
                listenable =true;
                return true;
            }
            else if(game_name.equalsIgnoreCase("blockrun")){
                if(prepareGame =="blockrun"){

                    listenable =true;
                    return true;
                } else {
                    return false;
                }
            }
            else return false;
        }
        else return false;
    }

    public void Stop() {
        if(listenable){
            if(teamBattleListener!=null){
                teamBattleListener.deinitListener();
                teamBattleListener = null;
            }
            if(tagListener!=null){
                tagListener.deinitListener();
                tagListener = null;
            }
            listenable =false;
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(teamBattleListener!=null)teamBattleListener.deinitListener();
        if(tagListener!=null)tagListener.deinitListener();
        //if(milkListener!=null)milkListener.deinitListener();
    }
}
