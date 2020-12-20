package net.serveron.mcstar.gameevents.Escaping;


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

public class PrepareEscaping implements Listener {
    private GameEvents plugin;
    private Player targetPlayer;
    public PrepareEscaping(GameEvents plugin){
        this.plugin = plugin;
    }

    public void initListener(Player player){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.targetPlayer = player;

        setStick(player,0,"ハンター初期位置");
        setStick(player,1,"捕獲者の牢獄位置");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        if(player.getName().equals(targetPlayer.getName())){
            ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
            if(itemMeta!=null && itemMeta.hasDisplayName()){
                if(itemMeta.getDisplayName().equals(ChatColor.GOLD+"ハンター初期位置")){
                    e.setCancelled(true);
                    plugin.escaping.escapingInfo.taggerSpawn = e.getBlock().getLocation().clone().add(0,1,0);
                    player.sendMessage(ChatColor.GOLD+"ハンター初期位置をセットしました");
                } else if(itemMeta.getDisplayName().equals(ChatColor.GOLD+"捕獲者の牢獄位置")){
                    e.setCancelled(true);
                    plugin.escaping.escapingInfo.captureSpawn = e.getBlock().getLocation().clone().add(0,1,0);
                    player.sendMessage(ChatColor.GOLD+"捕獲者の牢獄位置をセットしました");
                }
            }
        }
    }

    public void deinitListener(){
        targetPlayer.getInventory().clear(0);
        targetPlayer.getInventory().clear(1);
        targetPlayer = null;
        HandlerList.unregisterAll(this);
    }

    private void setStick(Player player,int slot,String displayName){
        ItemStack item = new ItemStack(Material.STICK,1);
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(ChatColor.GOLD+displayName);
        item.setItemMeta(im);
        player.getInventory().setItem(slot,item);
    }
}
