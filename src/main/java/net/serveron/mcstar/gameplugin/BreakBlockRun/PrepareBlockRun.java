package net.serveron.mcstar.gameplugin.BreakBlockRun;

import net.serveron.mcstar.gameplugin.GameEvent;
//import org.bukkit.Location;
import net.serveron.mcstar.gameplugin.TeamInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PrepareBlockRun implements Listener {
    private GameEvent plugin;
    private String targetPlayer;
    //public Location location;

    public PrepareBlockRun(GameEvent plugin){
        this.plugin = plugin;
    }

    public void initListener(Player player){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        targetPlayer = player.getName();

        ItemStack item = new ItemStack(Material.STICK,1);
        ItemMeta im = item.getItemMeta();
        im.setDisplayName("MagicStick");
        player.getInventory().setItem(0,item);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        if(player.getName().equals(targetPlayer)){
            if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("MagicStick")){
                //plugin.breakRun.loc = e.getBlock().getLocation();
                e.setCancelled(true);
                player.sendMessage("開始位置をセットしました。");
            }
        }
    }

    public void deinitListener(){
        targetPlayer = null;
        HandlerList.unregisterAll(this);
    }


}
