package net.serveron.mcstar.gameevents.BreakBlockRun;

import net.serveron.mcstar.gameevents.GameEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PrepareBlockRun implements Listener {
    private GameEvent plugin;
    private String targetPlayer;

    public PrepareBlockRun(GameEvent plugin){
        this.plugin = plugin;
    }

    public void initListener(Player player){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        targetPlayer = player.getName();

        ItemStack item = new ItemStack(Material.STICK,1);
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(ChatColor.GOLD+"MagicStick");
        item.setItemMeta(im);
        player.getInventory().setItem(0,item);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        if(player.getName().equals(targetPlayer)){
            ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
            if(itemMeta!=null && itemMeta.hasDisplayName()){
                if(itemMeta.getDisplayName().equals(ChatColor.GOLD+"MagicStick")){
                    plugin.blockRun.locationDecision(e.getBlock().getLocation());
                    e.setCancelled(true);
                    player.sendMessage("開始位置をセットしました。ステージ作成コマンド\n"
                            +"　/cg blockrun construct <長さ>");
                }
            }
        }
    }

    public void deinitListener(){
        targetPlayer = null;
        HandlerList.unregisterAll(this);
    }
}
