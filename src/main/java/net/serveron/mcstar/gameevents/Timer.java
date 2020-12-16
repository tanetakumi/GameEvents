package net.serveron.mcstar.gameevents;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.serveron.mcstar.gameevents.Tag.TagListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class Timer extends BukkitRunnable {
    private int time;
    public Timer(int time){
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
            deinitActionbar();
            triggerAction();
        }
    }
    public void deinitActionbar(){
        this.cancel();
    }
    public void triggerAction(){

    }

}
