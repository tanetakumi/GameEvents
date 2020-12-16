package net.serveron.mcstar.gameevents.BreakBlockRun;

import net.serveron.mcstar.gameevents.GameEvent;
import net.serveron.mcstar.gameevents.ProgressClass.Progress;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BlockRun {
    //Class
    private GameEvent plugin;
    private BlockRunListener blockRunListener;
    private PrepareBlockRun prepareBlockRun;
    private BlockRunStudium blockRunStudium;

    //Progress
    public Progress progress = Progress.None;

    //GameInfo
    public Location loc;
    public int size;
    public int time;



    public BlockRun(GameEvent plugin){
        this.plugin = plugin;
    }

    public boolean prepare(Player player){
        if(progress == Progress.None){
            prepareBlockRun = new PrepareBlockRun(plugin);
            prepareBlockRun.initListener(player);
            progress = Progress.PrepareListener;
            return true;
        }
        else return false;
    }

    public void locationDecision(Location loc){
        if(progress == Progress.PrepareListener){
            progress = Progress.LocationDecision;
            this.loc = loc;
        }
    }

    public boolean construction(int size){
        if(progress == Progress.LocationDecision){
            this.size = size;
            blockRunStudium = new BlockRunStudium(plugin);
            blockRunStudium.constructStudium(loc, size);
            progress = Progress.Construction;
            return true;
        }
        else return false;
    }

    public boolean deConstruction(){
        if(progress != Progress.Start && progress != Progress.Ready){
            if(blockRunStudium!=null && blockRunStudium.construction){
                blockRunStudium.deConstructStudium();
                progress = Progress.PrepareListener;
                return true;
            }
            else return false;
        }
        else return false;
    }

    public boolean ready(){
        if(progress == Progress.Construction){
            if(prepareBlockRun!=null){
                prepareBlockRun.deinitListener();
                prepareBlockRun = null;
            }
            blockRunListener = new BlockRunListener(plugin);
            blockRunListener.register(loc);
            progress = Progress.Ready;
            return true;
        }
        else return false;
    }

    //--------------------------------------------------------------
    public boolean onStart(){
        if(progress == Progress.Ready){
            blockRunListener.initListener();
            return true;
        }
        else return false;
    }
    //--------------------------------------------------------------
    public void onFinish(){
        if(blockRunStudium!=null && blockRunStudium.construction){
            blockRunStudium.deConstructStudium();
            blockRunStudium = null;
        }
        if(blockRunListener!=null) {
            blockRunListener.deinitListener();
            blockRunListener = null;
        }
        if(prepareBlockRun!=null){
            prepareBlockRun.deinitListener();
            prepareBlockRun = null;
        }
        progress = Progress.None;
    }
}
