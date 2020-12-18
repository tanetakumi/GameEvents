package net.serveron.mcstar.gameevents;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class MainboardManager {
    public GameEvents plugin;

    public MainboardManager(GameEvents plugin){
        this.plugin = plugin;
    }

    public List<String> getCurrentTeams() {
        List<String> list = new ArrayList<>();
        for (Team team : plugin.mainScoreboard.getTeams()) {
            list.add(team.getName());
        }
        return list;
    }
    public TeamInfo extendTeamExists(String team_name) {
        for (TeamInfo et : plugin.teamInfoList) {
            if(team_name.equals(et.teamName)){
                return et;
            }
        }
        return null;
    }
    public boolean setKing(String team_name,String player_name){
        for(TeamInfo et : plugin.teamInfoList){
            if(team_name.equals(et.teamName)){
                for(Player player : Bukkit.getOnlinePlayers()){
                    if(player_name.equals(player.getName())){
                        et.King = player;
                        return true;
                    }
                }

            }
        }
        return false;
    }
    public Team getPlayerTeam(Player player){
        return plugin.mainScoreboard.getEntryTeam(player.getName());
    }

    public boolean addSpawnLocation(String team_name, World world, int x, int y, int z){
        if(getCurrentTeams().contains(team_name)){
            Team team = plugin.mainScoreboard.getTeam(team_name);
            TeamInfo et = new TeamInfo(team);
            et.location = new Location(world,x,y,z);
            plugin.teamInfoList.add(et);
            return true;
        }
        else {
            return false;
        }
    }
    public List<String> showSpawnList(){
        List<String> et_string = new ArrayList<>();
        for(TeamInfo et : plugin.teamInfoList){
            et_string.add(et.ToTextForList());
        }
        return et_string;
    }
    public List<String> showSpawnTextList(){
        List<String> et_string = new ArrayList<>();
        for(TeamInfo et : plugin.teamInfoList){
            et_string.add(et.ToText());
        }
        return et_string;
    }

    public boolean removeFromSpawnList(String et_text){
        //List<String> tsc_string = new ArrayList<>();
        for(TeamInfo et : plugin.teamInfoList){
            if(et_text.equals(et.ToText())){
                plugin.teamInfoList.remove(et);
                return true;
            }
        }
        return false;
    }
    public boolean setRespaenable(String et_text,String On_Off){
        if(On_Off.equalsIgnoreCase("on")){
            for(TeamInfo et : plugin.teamInfoList){
                if(et_text.equals(et.ToText())){
                    et.respawnAble = true;
                    return true;
                }
            }
        }
        else if(On_Off.equalsIgnoreCase("off")){
            for(TeamInfo et : plugin.teamInfoList){
                if(et_text.equals(et.ToText())){
                    et.respawnAble = false;
                    return true;
                }
            }
        }
        return false;
    }
    public boolean setCount(String et_text,int num){
        for(TeamInfo et : plugin.teamInfoList){
            if(et_text.equals(et.ToText())){
                et.count = num;
                return true;
            }
        }
        return false;
    }
    public void JavaVsBE(String team1,String team2){
        if(!getCurrentTeams().contains(team1)){
            plugin.mainScoreboard.registerNewTeam(team1);
        }
        if(!getCurrentTeams().contains(team2)){
            plugin.mainScoreboard.registerNewTeam(team2);
        }
        Team team_java = plugin.mainScoreboard.getTeam(team1);
        Team team_be = plugin.mainScoreboard.getTeam(team2);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if(p.getName().indexOf("_")==0){
                team_be.addEntry(p.getName());
            }
            else {
                team_java.addEntry(p.getName());
            }
        }
    }

    public void randomTeam(String team1,String team2){
        if(!getCurrentTeams().contains(team1)){
            plugin.mainScoreboard.registerNewTeam(team1);
        }
        if(!getCurrentTeams().contains(team2)){
            plugin.mainScoreboard.registerNewTeam(team2);
        }
        Team team_1 = plugin.mainScoreboard.getTeam(team1);
        Team team_2 = plugin.mainScoreboard.getTeam(team2);

        int i = Bukkit.getOnlinePlayers().size();

        for (Player p : Bukkit.getOnlinePlayers()) {
        }
    }
}
