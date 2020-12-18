package net.serveron.mcstar.gameevents.Tag;

import net.serveron.mcstar.gameevents.GameEvents;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PrepareTag implements Listener {
    private final GameEvents plugin;
    private String targetPlayer;

    public PrepareTag(GameEvents plugin){
        this.plugin = plugin;
    }

    public void initListener(Player player){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.targetPlayer = player.getName();

        ItemStack item = new ItemStack(Material.STICK,1);
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(ChatColor.GOLD+"おにのスポーン位置");
        item.setItemMeta(im);
        player.getInventory().setItem(0,item);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        if(player.getName().equals(targetPlayer)){
            ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
            if(itemMeta!=null && itemMeta.hasDisplayName()){
                if(itemMeta.getDisplayName().equals(ChatColor.GOLD+"おにのスポーン位置")){
                    e.setCancelled(true);
                    plugin.tag.tagInfo.taggerSpawn = e.getBlock().getLocation().clone().add(0,1,0);
                    player.sendMessage(ChatColor.GOLD+"おにのスポーン位置をセットしました");
                }
            }
        }
    }

    public void deinitListener(){
        targetPlayer = null;
        HandlerList.unregisterAll(this);
    }
}
