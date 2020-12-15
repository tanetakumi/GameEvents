package net.serveron.mcstar.gameplugin.BreakBlockRun;

import net.serveron.mcstar.gameplugin.GameEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BlockRun {
    private GameEvent plugin;
    private BlockRunListener blockRunListener;
    private PrepareBlockRun prepareBlockRun;
    private BlockRunStudium blockRunStudium;

    public Location loc;

    public Progress progress = Progress.None;

    public enum Progress{
        None,
        PrepareListener,
        LocationDecision,
        Construction,
        Ready,
        Start,
    }

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

    public boolean locationDecision(Location loc){
        if(progress == Progress.PrepareListener){
            progress = Progress.LocationDecision;
            this.loc = loc;
            return true;
        }
        else return false;
    }

    public boolean construction(){
        if(progress == Progress.LocationDecision){
            blockRunStudium = new BlockRunStudium(plugin);
            blockRunStudium.constructStudium(loc,20);
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
            prepareBlockRun.deinitListener();
            prepareBlockRun = null;
            blockRunListener = new BlockRunListener(plugin);
            blockRunListener.register(loc);
            return true;
        }
        else return false;
    }

    //--------------------------------------------------------------
    public void onStart(){
        blockRunListener.initListener();
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
