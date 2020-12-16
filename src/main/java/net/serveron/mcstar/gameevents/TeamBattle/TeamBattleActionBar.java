package net.serveron.mcstar.gameevents.TeamBattle;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

//import java.awt.*;


public class TeamBattleActionBar extends BukkitRunnable {
    private int time;
    private final TeamBattleListener event;

    public TeamBattleActionBar(TeamBattleListener event, int time){
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
//Integer.toString(minute)+"分"+Integer.toString(second)+"秒"