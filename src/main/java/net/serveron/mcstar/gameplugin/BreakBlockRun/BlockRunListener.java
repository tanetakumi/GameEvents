package net.serveron.mcstar.gameplugin.BreakBlockRun;

import net.serveron.mcstar.gameplugin.GameEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;


public class BlockRunListener implements Listener {
    private final GameEvent plugin;
    private PrepareBreakBlockRun prepareBreakBlockRun;

    public BlockRunListener(GameEvent plugin){
        this.plugin = plugin;
        prepareBreakBlockRun = new PrepareBreakBlockRun(plugin);
    }
    public void ready(){
        prepareBreakBlockRun.deinitListener();
        prepareBreakBlockRun = null;
    }
    //ゲーム----------------------------------------------------------------------------------------------
    public void initListener(){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void playerJump(PlayerStatisticIncrementEvent e) {
        if(e.getStatistic()== Statistic.JUMP){
            Block block = e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
            if(block.getType() == Material.SMOOTH_QUARTZ){
                block.setType(Material.AIR);
            }
        }
    }

    public void deinitListener(){
        HandlerList.unregisterAll(this);
    }
}
