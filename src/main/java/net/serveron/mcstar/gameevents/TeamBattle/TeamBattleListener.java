package net.serveron.mcstar.gameevents.TeamBattle;

import net.serveron.mcstar.gameevents.TeamInfo;
import net.serveron.mcstar.gameevents.GameEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

public class TeamBattleListener implements Listener {

    private final GameEvent plugin;
    private TeamInfo team1;
    private TeamInfo team2;

    private TeamBattleActionBar actionBar;
    private BukkitTask gameTask;

    public Objective obj;

    public Score score1;
    public Score score2;

    public TeamBattleListener(GameEvent plugin) {
        this.plugin = plugin;
    }

    public void initListener(int time, TeamInfo team1, TeamInfo team2){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.team1 = team1;
        this.team2 = team2;
        //アクションバーの時間計測
        actionBar = new TeamBattleActionBar(this,time);
        gameTask = actionBar.runTaskTimer(plugin, 5,20);

        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle(ChatColor.AQUA+"ゲームスタート", "～"+ team1.teamName+"チーム vs "+team2.teamName+"チーム～",20,20,20);
        }

        obj = plugin.mainScoreboard.getObjective("vsnakomugi");
        if(obj==null){
            obj = plugin.mainScoreboard.registerNewObjective("vsnakomugi","dummy","なこむぎ マイクラ対戦");
        }
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        score1 = obj.getScore(team1.team.getColor() + team1.teamName+" リスポーンリミット");
        score1.setScore(team1.count);
        score2 = obj.getScore(team2.team.getColor() + team2.teamName+" リスポーンリミット");
        score2.setScore(team2.count);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        String team_name = plugin.mainboardManager.getPlayerTeam(e.getPlayer()).getName();
        //チームリスポーン
        for (TeamInfo et : plugin.teamInfoList) {
            if(team_name.equals(et.teamName)){
                if(et.respawnAble && et.location!=null){
                    e.setRespawnLocation(et.location);
                    break;
                }
            }
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player player = e.getEntity().getPlayer();

        if(team1.King == player){
            sendResult(team2);
        }
        else if(team2.King == player){
            sendResult(team1);
        }
        else{
            String team_name = plugin.mainboardManager.getPlayerTeam(player).getName();
            if(team2.teamName.equals(team_name)){
                int count = team2.downCount();
                score2.setScore(count);
                if(count==0){
                    sendResult(team1);
                }
            }
            else if(team1.teamName.equals(team_name)){
                int count = team1.downCount();
                score1.setScore(count);
                if(count==0){
                    sendResult(team2);
                }
            }
        }
    }

    public void timerFinish(){
        if(team1.count>team2.count){
            sendResult(team1);
        }
        else if(team1.count<team2.count){
            sendResult(team2);
        }
    }
    public void sendResult(TeamInfo win_team){
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle(win_team.team.getColor()+win_team.teamName+"チームの勝ち", "～ゲーム終了～",20,50,20);
        }
        gameTask.cancel();
        gameTask = null;
    }
    public void deinitListener(){
        if(gameTask!=null)gameTask.cancel();
        plugin.mainScoreboard.getObjective("vsnakomugi").unregister();

        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle("ありがとうございました。", "お疲れさまでした",20,40,20);
        }

        HandlerList.unregisterAll(this);
    }
}
