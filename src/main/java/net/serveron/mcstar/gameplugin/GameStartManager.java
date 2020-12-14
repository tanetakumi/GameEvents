package net.serveron.mcstar.gameplugin;

import net.serveron.mcstar.gameplugin.BreakBlockRun.BlockRunListener;
import net.serveron.mcstar.gameplugin.Tag.TagListener;
import net.serveron.mcstar.gameplugin.TeamBattle.TeamBattleListener;

public class GameStartManager {
    TeamBattleListener teamBattleListener;
    TagListener tagListener;
    BlockRunListener blockRunListener;
    GameEvent plugin;
    public GameStartManager(GameEvent plugin){
        this.plugin = plugin;
    }

}
