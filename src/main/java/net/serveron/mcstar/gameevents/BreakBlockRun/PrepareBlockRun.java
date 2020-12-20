package net.serveron.mcstar.gameevents.BreakBlockRun;

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

public class PrepareBlockRun implements Listener {
    private final GameEvents plugin;
    private Player targetPlayer;

    public PrepareBlockRun(GameEvents plugin){
        this.plugin = plugin;
    }

    public void initListener(Player player){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        targetPlayer = player;

        ItemStack item = new ItemStack(Material.STICK,1);
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(ChatColor.GOLD+"ステージ場所");
        item.setItemMeta(im);
        player.getInventory().setItem(0,item);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        if(player.getName().equals(targetPlayer.getName())){
            ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
            if(itemMeta!=null && itemMeta.hasDisplayName()){
                if(itemMeta.getDisplayName().equals(ChatColor.GOLD+"ステージ場所")){
                    plugin.blockRun.locationDecision(e.getBlock().getLocation());
                    e.setCancelled(true);
                    player.sendMessage("開始位置をセットしました。ステージ作成コマンド\n"
                            +"　/cg blockrun construct <長さ>");
                }
            }
        }
    }

    public void deinitListener(){
        targetPlayer.getInventory().clear(0);
        targetPlayer = null;
        HandlerList.unregisterAll(this);
    }
}
