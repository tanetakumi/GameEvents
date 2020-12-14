package net.serveron.mcstar.gameplugin.BreakBlockRun;

import net.serveron.mcstar.gameplugin.GameEvent;

public class BreakBlockRun {
    private GameEvent plugin;
    public BreakBlockRun(GameEvent plugin){
        this.plugin = plugin;
        BlockRunListener blockRunListener = new BlockRunListener(plugin);
        //BlockRunListener blockRunListener = new BlockRunListener(plugin);
    }
    public void onPrepare(){

    }
    public void onStart(){

    }
    public void onFinish(){

    }
}
