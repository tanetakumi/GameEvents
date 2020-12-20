package net.serveron.mcstar.gameevents.Escaping;

import net.serveron.mcstar.gameevents.GameEvents;
//import net.serveron.mcstar.gameevents.Tag.PrepareTag;
//import net.serveron.mcstar.gameevents.Tag.TagInfo;
//import net.serveron.mcstar.gameevents.Tag.TagListener;
import org.bukkit.entity.Player;

public class Escaping {
    //Class
    private GameEvents plugin;
    private EscapingListener escapingListener;
    private PrepareEscaping prepareEscaping;


    //Progress
    public int progress = 0;

    //GameInfo
    public EscapingInfo escapingInfo;


    public Escaping(GameEvents plugin){
        this.plugin = plugin;
        escapingInfo = new EscapingInfo(plugin);
    }

    public boolean prepare(Player player){
        if(progress == 0){
            prepareEscaping = new PrepareEscaping(plugin);
            prepareEscaping.initListener(player);
            progress = 1;
            return true;
        } else {
            return false;
        }
    }

    public boolean ready(){
        if(progress == 1){
            if(escapingInfo.startable()){
                if(escapingListener!=null){
                    escapingListener.deinitListener();
                    escapingListener = null;
                }
                escapingListener = new EscapingListener(plugin);
                progress = 2;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //--------------------------------------------------------------
    public boolean onStart(){
        if(progress == 2){
            escapingListener.initListener(escapingInfo);
            return true;
        } else {
            return false;
        }
    }
    //--------------------------------------------------------------
    public void onFinish(){
        if(escapingListener!=null) {
            escapingListener.deinitListener();
            escapingListener = null;
        }
        if(prepareEscaping!=null) {
            prepareEscaping.deinitListener();
            prepareEscaping = null;
        }
        progress = 0;
    }
}
