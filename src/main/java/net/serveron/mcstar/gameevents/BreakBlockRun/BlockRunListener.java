package net.serveron.mcstar.gameevents.BreakBlockRun;

import net.serveron.mcstar.gameevents.GameEvents;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

import java.util.ArrayList;
import java.util.List;


public class BlockRunListener implements Listener {
    private final GameEvents plugin;
    private final List<String> entryPlayer= new ArrayList<>();
    private boolean listenable = false;
    private BlockRunInfo gameInfo;

    public BlockRunListener(GameEvents plugin){
        this.plugin = plugin;
    }

    public void registerPlayer(BlockRunInfo gameInfo){
        this.gameInfo = gameInfo;
        Location startPos = gameInfo.stageLoc.clone().add((double)gameInfo.stageSize/2,6,(double)gameInfo.stageSize/2);
        for(Player p : Bukkit.getOnlinePlayers()){
            if(gameInfo.team.hasEntry(p.getName())){
                entryPlayer.add(p.getName());
                p.teleport(startPos);
            }
        }
        Bukkit.broadcastMessage(entryPlayer.size()+"人参加");
    }


    //ゲーム----------------------------------------------------------------------------------------------
    public void initListener(){
        if(!listenable){
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            for(Player p : Bukkit.getOnlinePlayers()){
                p.sendTitle(ChatColor.AQUA+"ブロックランバトルロワイヤル", "ゲーム開始",20,50,20);
            }
            gameInfo.downsizeStage();
            listenable = true;
        }
    }

    @EventHandler
    public void playerJump(PlayerStatisticIncrementEvent e) {
        if(e.getStatistic()== Statistic.JUMP){
            Block block = e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
            if(block.getType() == Material.BEDROCK){
                block.setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        String player_name = e.getEntity().getPlayer().getName();

        if(entryPlayer.contains(player_name)) {
            entryPlayer.remove(player_name);
            if(entryPlayer.size()==1){
                sendResult(entryPlayer.get(0));
            }
        }
    }
    public void sendResult(String player_name){
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle("優勝 "+player_name, "～ゲーム終了～",20,50,20);
        }
        if(listenable){
            HandlerList.unregisterAll(this);
            listenable = false;
        }
    }
    public void deinitListener(){

        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle("ありがとうございました。", "お疲れさまでした",20,40,20);
        }
        if(listenable){
            HandlerList.unregisterAll(this);
            listenable = false;
        }
    }
}
