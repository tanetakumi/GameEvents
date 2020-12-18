package net.serveron.mcstar.gameevents.Command;

import net.serveron.mcstar.gameevents.BreakBlockRun.BlockRun;
import net.serveron.mcstar.gameevents.GameEvents;
import net.serveron.mcstar.gameevents.Tag.Tag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
//import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameCommand implements CommandExecutor, TabCompleter {

    public GameEvents plugin;

    public GameCommand(GameEvents plugin) {
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



                    case "tag":
                        if (plugin.currentGame().equals("none")) {
                            if (args.length == 2 && args[1].equalsIgnoreCase("prepare")) {
                                plugin.tag = new Tag(plugin);
                                plugin.tag.prepare(player);
                                player.sendMessage("鬼ごっこの準備をします。最初のコマンド↓\n"
                                                +"/cg tag set <時間(秒)> <鬼チーム> <逃走チーム>");
                            } else {
                                player.sendMessage("コマンド /cg tag prepare");
                            }
                        } else if(plugin.currentGame().equals("tag")){
                            switch (args[1].toLowerCase()) {
                                case "set":
                                    if(args.length==5){
                                        if(plugin.tag.tagInfo.setInfo(args)){
                                            player.sendMessage("次に鬼のリス位置を決めます。’おにのスポーン位置’の棒を持ちブロックを壊してください。");
                                        } else {
                                            player.sendMessage("コマンドエラー");
                                        }
                                    } else {
                                        player.sendMessage("引数の数が違います。");
                                    }
                                    break;
                                case "ready":
                                    if(plugin.tag.ready()){
                                        player.sendMessage("準備完了");
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
                        } else {
                            player.sendMessage("現在ほかのゲームが開始されています。");
                        }

                    case "blockrun":
                        if (plugin.currentGame().equals("none")) {
                            if (args.length == 2 && args[1].equalsIgnoreCase("prepare")) {
                                plugin.blockRun = new BlockRun(plugin);
                                plugin.blockRun.prepare(player);
                                player.sendMessage("ブロックランの準備をします。最初のコマンド↓\n"
                                        +"/cg blockrun set <参加者のチーム>");
                            } else {
                                player.sendMessage("コマンド /cg blockrun prepare");
                            }
                        } else if(plugin.currentGame().equals("blockrun")){
                            switch (args[1].toLowerCase()) {
                                case "set":
                                    if(args.length == 3){
                                        if(plugin.blockRun.blockRunInfo.setInfo(args)){
                                            player.sendMessage("次にステージの建設位置を棒で選択して下さい。\n" +
                                                    "コマンド /cg blockrun construct <大きさ>　で建設できます。");
                                        } else {
                                            player.sendMessage("コマンド /cg blockrun set <参加者のチーム>");
                                        }
                                    } else {
                                        player.sendMessage("引数の数が違います。");
                                    }
                                    break;
                                case "construct":
                                    if (args.length == 3) {
                                        int r = stringToInt(args[2]);
                                        if (r != 0) {
                                            if (plugin.blockRun.construction(r)) {
                                                player.sendMessage("ステージを作成しました");
                                            } else {
                                                player.sendMessage("ステージを作成できませんでした");
                                            }
                                        } else {
                                            player.sendMessage("数値を入力してください");
                                        }
                                    } else {
                                        player.sendMessage("コマンド /cg blockrun construct <大きさ>");
                                    }
                                    break;
                                case "deconstruct":
                                    if (plugin.blockRun.deConstruction()) {
                                        player.sendMessage("ステージを解体しました");
                                    } else {
                                        player.sendMessage("ステージを解体できませんでした");
                                    }
                                    break;
                                case "ready":
                                    if(plugin.blockRun.ready()){
                                        player.sendMessage("準備完了");
                                    } else {
                                        player.sendMessage("準備が整っていません。");
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
                        } else {
                            player.sendMessage("現在ほかのゲームが開始されています。");
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
                        autoComplete.addAll(Arrays.asList("prepare","set","ready","start","finish"));
                    }
                    else if(args[1].equalsIgnoreCase("set")){
                        if(args.length == 3){
                            autoComplete.addAll(Arrays.asList("600","900","1200","1800"));
                        } else if(args.length == 4){
                            autoComplete.addAll(plugin.mainboardManager.getCurrentTeams());
                        } else if(args.length == 5){
                            autoComplete.addAll(plugin.mainboardManager.getCurrentTeams());
                            autoComplete.remove(args[3]);
                        }
                    }
                } else if(args[0].equalsIgnoreCase("blockrun")){
                    if(args.length == 2){
                        autoComplete.addAll(Arrays.asList("prepare","set","construct","deconstruct","ready","start","finish"));
                    } else if(args.length == 3){
                        if(args[1].equalsIgnoreCase("set")){
                            autoComplete.addAll(plugin.mainboardManager.getCurrentTeams());
                        } else if(args[1].equalsIgnoreCase("construct")){
                            autoComplete.addAll(Arrays.asList("30","35","40"));
                        }
                    }
                }
            }
        }
        //文字比較と削除-----------------------------------------------------
        //Collections.sort(autoComplete);
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
