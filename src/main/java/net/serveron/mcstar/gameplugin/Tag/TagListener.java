package net.serveron.mcstar.gameplugin.Tag;

import net.serveron.mcstar.gameplugin.TeamInfo;
//import net.serveron.mcstar.teamevent.PlayTag.ActionBar;
import net.serveron.mcstar.gameplugin.GameEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
//import org.bukkit.event.entity.PlayerDeathEvent;
//import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.ArrayList;
import java.util.List;

public class TagListener implements Listener {

    private final GameEvent plugin;

    private TagActionBar actionBar;
    private BukkitTask gameTask;

    public Objective obj;
    private TeamInfo team1;
    private TeamInfo team2;
    public List<Score> scoreList = new ArrayList<>();
    public List<Player> playerList = new ArrayList<>();

    public TagListener(GameEvent plugin) {
        this.plugin = plugin;
    }

    //Team1 おにチーム　Team2 逃走チーム
    public void initListener(int time, TeamInfo team1, TeamInfo team2){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.team1 = team1;
        this.team2 = team2;
        //アクションバーの時間計測
        actionBar = new TagActionBar(this,time);
        gameTask = actionBar.runTaskTimer(plugin, 5,20);

        obj = plugin.mainScoreboard.getObjective("playtag");
        if(obj==null){
            obj = plugin.mainScoreboard.registerNewObjective("playtag","dummy","おに");
        }

        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle(ChatColor.AQUA+"鬼ごっこ", "ゲームスタート",20,20,20);
            p.setGameMode(GameMode.ADVENTURE);
            if(team1.teamName.equals(plugin.mainboardManager.getPlayerTeam(p).getName())){
                playerList.add(p);
            }
            Score score = obj.getScore(p.getName());
            score.setScore(100);
            scoreList.add(score);
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player whoWasHit = (Player) e.getEntity();
            Player whoHit = (Player) e.getDamager();
            e.setDamage(0);
            if(playerList.contains(whoHit)){
                whoWasHit.teleport(team1.location);
                playerList.remove(whoHit);
                playerList.add(whoWasHit);
                Score score = obj.getScore(whoWasHit.getName());
                score.setScore(score.getScore()-1);
                Bukkit.broadcastMessage("鬼が"+whoHit.getName()+"から"+whoWasHit+"に変わりました。");
            }
        }
    }

    public void timerFinish(){
        sendResult();
    }
    public void sendResult(){
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle(ChatColor.AQUA+"鬼ごっこ", "～ゲーム終了～",20,50,20);
        }
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        gameTask.cancel();
        gameTask = null;
    }
    public void deinitListener(){
        if(gameTask!=null){
            gameTask.cancel();
            gameTask = null;
        }
        plugin.mainScoreboard.getObjective("playtag").unregister();
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle("ありがとうございました。", "お疲れさまでした",20,40,20);
        }
        HandlerList.unregisterAll(this);
    }
}
