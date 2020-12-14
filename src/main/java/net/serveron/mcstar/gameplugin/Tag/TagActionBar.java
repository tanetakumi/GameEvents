package net.serveron.mcstar.gameplugin.Tag;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
//import net.serveron.mcstar.teamevent.TeamBattle.TeamBattleListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TagActionBar extends BukkitRunnable {
    private int time;
    private final TagListener event;

    public TagActionBar(TagListener event, int time){
        this.event = event;
        this.time = time;
    }
    @Override
    public void run(){
        time--;
        if(time>0) {
            int minute = time/60;
            int second = time%60;
            TextComponent component = new TextComponent();
            component.setText("残り時間 "+minute+"分"+second+"秒");

            for(Player p : Bukkit.getOnlinePlayers()){
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR,component);
            }
        }
        else {
            this.cancel();
            event.timerFinish();
        }
    }
}