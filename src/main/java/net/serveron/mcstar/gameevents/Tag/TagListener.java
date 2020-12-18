package net.serveron.mcstar.gameevents.Tag;

//import net.serveron.mcstar.teamevent.PlayTag.ActionBar;
import net.serveron.mcstar.gameevents.GameEvents;
import net.serveron.mcstar.gameevents.Timer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
//import org.bukkit.event.entity.PlayerDeathEvent;
//import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
        import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

        import java.util.ArrayList;
import java.util.List;

public class TagListener implements Listener {

    private final GameEvents plugin;
    private Timer actionBar;
    public Objective obj;
    private boolean listenable = false;

    //game information
    private TagInfo tagInfo;
    public List<Score> scoreList = new ArrayList<>();
    public List<String> playerList = new ArrayList<>();

    public TagListener(GameEvents plugin) {
        this.plugin = plugin;
    }

    //Team1 おにチーム　Team2 逃走チーム
    public void initListener(TagInfo tagInfo){
        if(!listenable){
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            listenable = true;
        }
        this.tagInfo = tagInfo;

        startTimer(tagInfo.gameTime);

        obj = plugin.mainScoreboard.getObjective("playtag");
        if(obj==null){
            obj = plugin.mainScoreboard.registerNewObjective("playtag","dummy","おに");
        }

        for(Player p : Bukkit.getOnlinePlayers()){
            if(tagInfo.taggerTeam.getName().equals(plugin.mainboardManager.getPlayerTeam(p).getName())){
                playerList.add(p.getName());
                p.teleport(tagInfo.taggerSpawn);
                p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
            } else {
                p.getInventory().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
            }
            Score score = obj.getScore(p.getName());
            score.setScore(100);
            scoreList.add(score);
            p.sendTitle(ChatColor.AQUA+"鬼ごっこ", "ゲームスタート",20,20,20);
            p.setGameMode(GameMode.ADVENTURE);
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player whoWasHit = (Player) e.getEntity();
            Player whoHit = (Player) e.getDamager();
            e.setDamage(0);
            if(playerList.contains(whoHit.getName())){
                setWhoWasHit(whoWasHit);
                setWhoHit(whoHit);
                Bukkit.broadcastMessage("鬼が"+whoHit.getName()+"から"+whoWasHit.getName()+"に変わりました。");
            }
        }
    }
    public void sendResult(){
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle(ChatColor.AQUA+"鬼ごっこ", "～ゲーム終了～",20,50,20);
        }
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        if(listenable){
            HandlerList.unregisterAll(this);
            listenable = false;
        }
    }

    public void deinitListener(){
        actionBar.deinitActionbar();
        actionBar = null;
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle("ありがとうございました", "お疲れさまでした",20,40,20);
        }
        plugin.mainScoreboard.getObjective("playtag").unregister();
        if(listenable){
            HandlerList.unregisterAll(this);
            listenable = false;
        }
    }

    private void startTimer(int time){
        //アクションバーの時間計測
        actionBar = new Timer(time){
            @Override
            public void triggerAction(){
                sendResult();
            }
        };
        actionBar.runTaskTimer(plugin,5,20);
    }

    private void setWhoHit(Player p){
        tagInfo.escapeTeam.addEntry(p.getName());
        playerList.remove(p.getName());
        p.getInventory().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
    }

    private void setWhoWasHit(Player p){
        tagInfo.taggerTeam.addEntry(p.getName());
        playerList.add(p.getName());

        p.teleport(tagInfo.taggerSpawn);
        p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        Score score = obj.getScore(p.getName());
        score.setScore(score.getScore()-1);
    }
}
