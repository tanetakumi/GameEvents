package net.serveron.mcstar.gameevents.TeamBattle;

import net.serveron.mcstar.gameevents.GameEvents;
import org.bukkit.Location;
import org.bukkit.scoreboard.Team;

public class TeamBattleInfo {
    public GameEvents plugin;


    public Location stageLoc;
    public int stageSize = 0;
    public boolean stage = false;
    public Team team;

    public TeamBattleInfo(GameEvents plugin){
        this.plugin = plugin;

    }
}
