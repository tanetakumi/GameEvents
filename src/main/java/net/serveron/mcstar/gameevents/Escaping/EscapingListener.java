package net.serveron.mcstar.gameevents.Escaping;

import net.serveron.mcstar.gameevents.GameEvents;
import net.serveron.mcstar.gameevents.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class EscapingListener implements Listener {
    public GameEvents plugin;

    private Timer actionBar;
    public Objective obj;
    private boolean listenable = false;

    private Score score;

    private EscapingInfo escapingInfo;
    public List<String> taggerList = new ArrayList<>();
    public List<String> escapeList = new ArrayList<>();

    public EscapingListener(GameEvents plugin) {
        this.plugin = plugin;
    }

    //Team1 おにチーム　Team2 逃走チーム
    public void initListener(EscapingInfo escapingInfo){
        this.escapingInfo = escapingInfo;

        if(!listenable){
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            listenable = true;
        }
        startTimer(escapingInfo.gameTime);

        String tagger = escapingInfo.taggerTeam.getName();
        String escape = escapingInfo.escapeTeam.getName();
        for(Player p : Bukkit.getOnlinePlayers()){
            Team team = plugin.mainScoreboard.getEntryTeam(p.getName());
            if(team!=null){
                if(team.getName().equals(tagger)){
                    taggerList.add(p.getName());
                    p.teleport(escapingInfo.taggerSpawn);
                }
                else if(team.getName().equals(escape)){
                    p.getInventory().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
                    escapeList.add(p.getName());
                }
            }

            p.sendTitle(ChatColor.AQUA+"逃走中", "ゲームスタート",20,20,20);
        }

        obj = plugin.mainScoreboard.getObjective("escaping");
        if(obj==null){
            obj = plugin.mainScoreboard.registerNewObjective("escaping","dummy","逃走中");
        }
        score = obj.getScore("残りの逃走者");
        score.setScore(escapeList.size());
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        e.getPlayer().teleport(escapingInfo.captureSpawn);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player whoWasHit = (Player) e.getEntity();
            Player whoHit = (Player) e.getDamager();
            e.setDamage(0);
            if(taggerList.contains(whoHit.getName()) && escapeList.contains(whoWasHit.getName())){
                escapeList.remove(whoWasHit.getName());
                score.setScore(escapeList.size());
                Bukkit.broadcastMessage(whoWasHit.getName()+"がハンターにつかまりました");
                if(escapeList.size()==0){
                    sendResult();
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        String player_name = e.getPlayer().getName();
        if(escapeList.contains(player_name)){
            escapeList.remove(player_name);
            score.setScore(escapeList.size());
        }
    }

    public void sendResult(){

        if(escapeList.size()==0){
            for(Player p : Bukkit.getOnlinePlayers()){
                p.sendTitle(ChatColor.AQUA+"ゲーム終了", "～全員がつかまりました～",20,50,20);
            }
        } else {
            for(Player p : Bukkit.getOnlinePlayers()){
                p.sendTitle(ChatColor.AQUA+"ゲーム終了", "",20,50,20);
            }
            for(String player : escapeList){
                score = obj.getScore(player);
                score.setScore(100);
                obj.setDisplayName("優勝プレイヤー");
            }
            //obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        if(listenable){
            HandlerList.unregisterAll(this);
            listenable = false;
        }
    }

    public void deinitListener(){
        if(actionBar!=null){
            actionBar.deinitActionbar();
            actionBar = null;
        }
        if(obj!=null){
            obj.unregister();
            obj = null;
        }
        if(listenable){
            HandlerList.unregisterAll(this);
            listenable = false;
        }
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle("ありがとうございました", "お疲れさまでした",20,40,20);
        }
    }

    private void startTimer(int time){
        //アクションバーの時間計測
        actionBar = new Timer(time){
            @Override
            public void triggerAction(){
                sendResult();
            }
        };
        actionBar.runTaskTimer(plugin,5,20);
    }
/*
    private void setWhoHit(Player p){
        escapingInfo.escapeTeam.addEntry(p.getName());
        playerList.remove(p.getName());
        p.getInventory().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
    }

    private void setWhoWasHit(Player p){
        escapingInfo.taggerTeam.addEntry(p.getName());
        playerList.add(p.getName());

        p.teleport(escapingInfo.taggerSpawn);
        p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        Score score = obj.getScore(p.getName());
        score.setScore(score.get
    }

 */
}
