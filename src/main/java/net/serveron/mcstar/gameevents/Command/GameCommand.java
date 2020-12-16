package net.serveron.mcstar.gameevents.Command;

import net.serveron.mcstar.gameevents.BreakBlockRun.BlockRun;
import net.serveron.mcstar.gameevents.GameEvent;
import net.serveron.mcstar.gameevents.Tag.Tag;
import net.serveron.mcstar.gameevents.TeamInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
//import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameCommand implements CommandExecutor, TabCompleter {

    public GameEvent plugin;

    public GameCommand(GameEvent plugin) {
        this.plugin = plugin;
        plugin.getCommand("cg").setExecutor(this);
    }

    private void Notify(String text) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if(p.hasPermission("op")) {
                p.sendMessage(ChatColor.AQUA + text);
            }
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You cannot use commands with the console.");
            return true;
        }
        Player player = (Player) sender;

        if(args.length > 1){
            if(player.hasPermission("cg")) {
                switch (args[0].toLowerCase()) {
                    case "teambattle":
                        /*
                        if (args[1].equalsIgnoreCase("ready")) {
                            if (args.length == 3) {
                                int time = stringToInt(args[2]);
                                if (time != 0) {
                                    //
                                } else {
                                    player.sendMessage("数値を入力してください");
                                }
                            } else {
                                player.sendMessage("Usage:teambattle ready <time>");
                            }
                        } else if (args[1].equalsIgnoreCase("start")) {
                            if (args.length == 4) {
                                TeamInfo team1 = plugin.mainboardManager.extendTeamExists(args[2]);
                                TeamInfo team2 = plugin.mainboardManager.extendTeamExists(args[3]);
                                if (team1 != null && team2 != null) {

                                    if(!plugin.Start(team1,team2)){
                                        player.sendMessage("ゲーム名が違うか、すでにゲームが始まっています。");
                                    }


                                } else {
                                    player.sendMessage("カスタムチーム情報が登録されていません");
                                }
                            } else {
                                player.sendMessage("Usage:teambattle start <team1> <team2>");
                            }
                        } else if (args[1].equalsIgnoreCase("stop")) {
                            plugin.Stop();
                            break;
                        }*/
                        break;
                    case "tag"://args[0]
                        if (plugin.tag == null) {
                            if (args[1].equalsIgnoreCase("prepare") && args.length == 4) {//args[1]
                                List<Team> teamList = argsToTeamList(2,args);
                                if(teamList!=null && teamList.size()==2){
                                    plugin.tag = new Tag(plugin);
                                    if(plugin.tag.prepare(player,teamList)){
                                        player.sendMessage("準備を開始します");
                                    } else {
                                        plugin.tag = null;
                                        player.sendMessage("エラーが起きました");
                                    }
                                } else {
                                    player.sendMessage("チームデータがありません");
                                }
                            } else {
                                player.sendMessage("Usage: prepare <tagger team> <escape team>");
                            }
                        } else {
                            switch (args[1].toLowerCase()) {
                                case "ready":
                                    if (args.length == 3) {
                                        int time = stringToInt(args[2]);
                                        if (time != 0) {
                                            plugin.tag.tagInfo.gameTime = time;
                                            if(plugin.blockRun.ready()){
                                                player.sendMessage("準備ができました");
                                            } else {
                                                player.sendMessage("準備が完了していません");
                                            }
                                        } else {
                                            player.sendMessage("数値を入力してください");
                                        }
                                    } else {
                                        player.sendMessage("Usage: construct <length>");
                                    }
                                    break;
                                case "start":
                                    if (plugin.tag.onStart()) {
                                        player.sendMessage("ゲームを開始しました");
                                    } else {
                                        player.sendMessage("ゲームを開始できませんでした");
                                    }
                                    break;
                                case "finish":
                                    plugin.tag.onFinish();
                                    plugin.tag = null;
                                    player.sendMessage("ゲームを終了しました");
                                    break;
                                default:
                                    break;
                            }
                        }

                    case "blockrun":
                        if (plugin.blockRun == null) {
                            if (args[1].equalsIgnoreCase("prepare")) {
                                plugin.blockRun = new BlockRun(plugin);
                                plugin.blockRun.prepare(player);
                            }
                        } else {
                            switch (args[1].toLowerCase()) {
                                case "construct":
                                    if (args.length == 3) {
                                        int r = stringToInt(args[2]);
                                        if (r != 0) {
                                            if (plugin.blockRun.construction(r)) {
                                                player.sendMessage("ゲーム場を作成しました");
                                            } else {
                                                player.sendMessage("ゲーム場を作成できませんでした");
                                            }
                                        } else {
                                            player.sendMessage("数値を入力してください");
                                        }
                                    } else {
                                        player.sendMessage("Usage: construct <length>");
                                    }
                                    break;
                                case "deconstruct":
                                    if (plugin.blockRun.deConstruction()) {
                                        player.sendMessage("ゲーム場を解体しました");
                                    } else {
                                        player.sendMessage("ゲーム場を解体できませんでした");
                                    }
                                    break;
                                case "ready":
                                    if (args.length == 3) {
                                        int time = stringToInt(args[2]);
                                        if (time != 0) {
                                            plugin.blockRun.ready();
                                            player.sendMessage("ゲーム時間を" + time + "秒に設定しました。");
                                        } else {
                                            player.sendMessage("数値を入力してください");
                                        }
                                    } else {
                                        player.sendMessage("Usage: construct <length>");
                                    }
                                    break;
                                case "start":
                                    if (plugin.blockRun.onStart()) {
                                        player.sendMessage("ゲームを開始しました");
                                    } else {
                                        player.sendMessage("ゲームを開始できませんでした");
                                    }
                                    break;
                                case "finish":
                                    plugin.blockRun.onFinish();
                                    plugin.blockRun = null;
                                    player.sendMessage("ゲームを終了しました");
                                    break;
                                default:
                                    break;
                            }
                        }
                }
            } else {
                player.sendMessage("You don't have permisstion");
            }
        } else {
            player.sendMessage("引数が間違っています");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String command, String[] args) {
        List<String> autoComplete = new ArrayList<>();
        if(sender.hasPermission("cg")){

            if (args.length == 1){//一段目
                autoComplete.addAll(Arrays.asList("tag","teambattle","blockrun"));
            }
            else if(args.length >1){//二段目
                if(args[0].equalsIgnoreCase("tag") || args[0].equalsIgnoreCase("teambattle") ){
                    if(args.length == 2){
                        autoComplete.addAll(Arrays.asList("ready","start","stop"));
                    }
                    else if(args.length == 3){
                        if(args[1].equalsIgnoreCase("ready")){
                            autoComplete.addAll(Arrays.asList("600","900","1200","1800"));
                        }
                        else if(args[1].equalsIgnoreCase("start")){
                            autoComplete.addAll(plugin.mainboardManager.getCurrentTeams());
                        }
                    }
                    else if(args.length == 4){
                        if(args[1].equalsIgnoreCase("start")){
                            autoComplete.addAll(plugin.mainboardManager.getCurrentTeams());
                            autoComplete.remove(args[2]);
                        }

                    }
                } else if(args[0].equalsIgnoreCase("blockrun")){
                    if(args.length == 2){
                        autoComplete.addAll(Arrays.asList("prepare","construct","deconstruct","ready","start","finish"));
                    }
                    else if(args.length == 3){
                        if(args[1].equalsIgnoreCase("ready")){
                            autoComplete.addAll(Arrays.asList("600","900","1200","1800"));
                        } else if(args[1].equalsIgnoreCase("construct")){
                            autoComplete.addAll(Arrays.asList("20","25","30"));
                        }
                    }
                }
            }
        }
        //文字比較と削除-----------------------------------------------------
        Collections.sort(autoComplete);
        autoComplete.removeIf(str -> !str.startsWith(args[args.length - 1]));
        //------------------------------------------------------
        return autoComplete;
    }

    private int stringToInt(String str){
        int x = 0;
        try{
            x = Integer.parseInt(str);
        }
        catch(Exception ignored){
        }
        return x;
    }

    private List<Team> argsToTeamList(int startNumber,String[] args){
        List<Team> teamList = new ArrayList<>();
        if(args.length <= startNumber){
            return null;
        }
        else {
            for(int i=startNumber;i< args.length;i++){
                Team team = plugin.mainScoreboard.getTeam(args[i]);
                if(team==null){
                    return null;
                } else {
                    teamList.add(team);
                }
            }
            return teamList;
        }
    }
}
