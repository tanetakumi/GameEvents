package net.serveron.mcstar.gameplugin.BreakBlockRun;

import net.serveron.mcstar.gameplugin.GameEvent;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class PrepareBreakBlockRun implements Listener {
    private GameEvent plugin;
    //public Location location;

    public PrepareBreakBlockRun(GameEvent plugin){
        this.plugin = plugin;
    }
    public void initListener(){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void deinitListener(){
        HandlerList.unregisterAll(this);
    }
}
