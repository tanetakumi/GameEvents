package net.serveron.mcstar.gameevents.BreakBlockRun;

import net.serveron.mcstar.gameevents.GameEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BlockRun {
    //Class
    private GameEvent plugin;
    private BlockRunListener blockRunListener;
    private PrepareBlockRun prepareBlockRun;

    //Progress
    public int progress = 0;

    //GameInfo
    public BlockRunInfo blockRunInfo;


    public BlockRun(GameEvent plugin){
        this.plugin = plugin;
    }

    public boolean prepare(Player player){
        if(progress == 0){
            prepareBlockRun = new PrepareBlockRun(plugin);
            prepareBlockRun.initListener(player);
            progress = 1;
            return true;
        }
        else return false;
    }

    public void locationDecision(Location loc){
        if(progress == 1 || progress == 2){
            blockRunInfo.stageLoc = loc;
            progress = 2;
        }
    }

    public boolean construction(int size){
        if(progress == 2){
            blockRunInfo.stageSize = size;
            if(blockRunInfo.constructStage()){
                progress = 3;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean deConstruction(){
        if(progress == 3){
            blockRunInfo.deConstructStage();
            progress = 2;
            return true;
        } else {
            return false;
        }
    }

    public boolean ready(){
        if(progress == 3){
            if(blockRunInfo.startable()){
                prepareBlockRun.deinitListener();
                prepareBlockRun = null;
                blockRunListener = new BlockRunListener(plugin);
                blockRunListener.registerPlayer(blockRunInfo);
                progress = 4;
                return true;
            } else{
                return false;
            }
        }
        else return false;
    }

    //--------------------------------------------------------------
    public boolean onStart(){
        if(progress == 4){
            blockRunListener.initListener();
            return true;
        }
        else return false;
    }
    //--------------------------------------------------------------
    public void onFinish(){
        if(blockRunListener!=null) {
            blockRunListener.deinitListener();
            blockRunListener = null;
        }
        if(prepareBlockRun!=null){
            prepareBlockRun.deinitListener();
            prepareBlockRun = null;
        }
        if(blockRunInfo!=null){
            blockRunInfo.deConstructStage();
            prepareBlockRun = null;
        }
        progress = 0;
    }
}
