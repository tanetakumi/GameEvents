package net.serveron.mcstar.gameevents.Tag;

import net.serveron.mcstar.gameevents.GameEvents;
import org.bukkit.entity.Player;

public class Tag {
    //Class
    private final GameEvents plugin;
    private TagListener tagListener;
    private PrepareTag prepareTag;

    //Progress
    public int progress = 0;

    //GameInfo
    public TagInfo tagInfo;

    public Tag(GameEvents plugin){
        this.plugin = plugin;
        tagInfo = new TagInfo(plugin);
    }

    public boolean prepare(Player player){
        if(progress == 0){
            prepareTag = new PrepareTag(plugin);
            prepareTag.initListener(player);
            progress = 1;
            return true;
        } else {
            return false;
        }
    }

    public boolean ready(){
        if(progress == 1){
            if(tagInfo.startable()){
                if(tagListener!=null){
                    tagListener.deinitListener();
                    tagListener = null;
                }
                tagListener = new TagListener(plugin);
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
            tagListener.initListener(tagInfo);
            return true;
        } else {
            return false;
        }
    }
    //--------------------------------------------------------------
    public void onFinish(){
        if(tagListener!=null) {
            tagListener.deinitListener();
            tagListener = null;
        }
        if(prepareTag!=null) {
            prepareTag.deinitListener();
            prepareTag = null;
        }
        if(tagInfo!=null){
            tagInfo = null;
        }
        progress = 0;
    }
}
