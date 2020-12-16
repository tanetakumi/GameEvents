package net.serveron.mcstar.gameevents.Tag;

import net.serveron.mcstar.gameevents.GameEvent;
import net.serveron.mcstar.gameevents.ProgressClass.Progress;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class Tag {
    //Class
    private GameEvent plugin;
    private TagListener tagListener;
    private PrepareTag prepareTag;

    //Progress
    public Progress progress = Progress.None;

    //GameInfo
    public TagInfo tagInfo = new TagInfo();

    public Tag(GameEvent plugin){
        this.plugin = plugin;
    }

    public boolean prepare(Player player, List<Team> teamList){
        if(progress == Progress.None){
            prepareTag = new PrepareTag(plugin);
            prepareTag.initListener(player);
            tagInfo.taggerTeam = teamList.get(0);
            tagInfo.escapeTeam = teamList.get(1);
            progress = Progress.Construction;
            return true;
        } else {
            return false;
        }
    }

    public boolean ready(){
        if(progress == Progress.Construction){
            if(tagInfo.startable()){
                if(tagListener!=null){
                    tagListener.deinitListener();
                    tagListener = null;
                }
                tagListener = new TagListener(plugin);
                progress = Progress.Ready;
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
        if(progress == Progress.Ready){
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
        progress = Progress.None;
    }
}
