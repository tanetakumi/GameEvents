package net.serveron.mcstar.gameplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
//import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameCommand implements CommandExecutor, TabCompleter {

    public GameEvent plugin;

    public GameCommand(GameEvent plugin) {
        this.plugin = plugin;
        plugin.getCommand("ct").setExecutor(this);
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

        if(args.length > 0){
            if(player.hasPermission("ct")) {
                switch (args[0].toLowerCase()) {
                    case "gamestart":
                        if(args.length == 5){
                            String game = args[1];
                            int time = stringToInt(args[2]);
                            TeamInfo team1 = plugin.mainboardManager.extendTeamExists(args[3]);
                            TeamInfo team2 = plugin.mainboardManager.extendTeamExists(args[4]);
                            if(time != 0 && team1!=null && team2 != null){
                                if(!plugin.Start(game,time,team1,team2)){
                                    player.sendMessage("ゲーム名が違うか、すでにゲームが始まっています。");
                                }
                            }
                            else {
                                if(team1==null || team2 == null){
                                    player.sendMessage("カスタムチーム情報が登録されていません");
                                }
                                else {
                                    player.sendMessage("Usage:gamestart <game> <time> <team1> <team2>");
                                }

                            }
                        }
                        else{
                            player.sendMessage("Usage:gamestart <time> <team1> <team2>");
                        }
                        break;
                    case "gamestop":
                        plugin.Stop();
                        break;
                    case "javavsbe":
                        if(args.length == 3){
                            plugin.mainboardManager.JavaVsBE(args[1],args[2]);
                            Bukkit.broadcastMessage("すべてのプレイヤーをJava - 統合版に分けました。");
                        }
                        else {
                            player.sendMessage("引数の数が違います。");
                        }
                        break;
                    case "teaminfo"://これはargs[0]に当たる。
                        if(args.length>1){
                            if(args[1].equalsIgnoreCase("remove")){
                                if(args.length==3){
                                    if(plugin.mainboardManager.removeFromSpawnList(args[2])){
                                        player.sendMessage(args[2]+"を消去しました。");
                                    }
                                    else {
                                        player.sendMessage(args[2]+"は存在しません。");
                                    }
                                }
                                else{
                                    player.sendMessage("Usage:teaminfo remove <team(x,y,z)>");
                                }
                            }
                            else if(args[1].equalsIgnoreCase("spawn")){
                                if(args.length==4){
                                    if(plugin.mainboardManager.setRespaenable(args[2],args[3])){
                                        player.sendMessage(args[2]+"を"+args[3]+"にしました。");
                                    }
                                    else {
                                        player.sendMessage(args[2]+"は存在しません。");
                                    }
                                }
                                else{
                                    player.sendMessage("Usage:teaminfo spawn <team(x,y,z)> <on off>");
                                }
                            }
                            else if(args[1].equalsIgnoreCase("count")){
                                if(args.length==4){
                                    int i = stringToInt(args[3]);
                                    if(i>0){
                                        if(plugin.mainboardManager.setCount(args[2],i)){
                                            player.sendMessage(args[2]+"のカウントを"+i+"にしました");
                                        }
                                        else player.sendMessage("チームが存在しません");
                                    }
                                    else {
                                        player.sendMessage("Usage:teaminfo count <team> <number(>0)>");
                                    }
                                }
                                else{
                                    player.sendMessage("Usage:teaminfo count <team> <number>");
                                }
                            }
                            else if(args[1].equalsIgnoreCase("add")){
                                if(args.length == 6){
                                    try{
                                        int x = Integer.parseInt(args[3]);
                                        int y = Integer.parseInt(args[4]);
                                        int z = Integer.parseInt(args[5]);
                                        if(plugin.mainboardManager.addSpawnLocation(args[2],player.getWorld(),x,y,z)){
                                            Notify(args[2]+"チームのリスポーン位置を("+args[3]+","+args[4]+","+args[5]+")に固定しました");
                                        }
                                        else throw new Exception("チームが存在しません");
                                    }
                                    catch(Exception e){
                                        player.sendMessage(e.getMessage()+"\nteaminfo add <team> <x> <y> <z>");
                                        break;
                                    }
                                }
                                else {
                                    player.sendMessage("引数の数が違います。入力引数長："+args.length+"\nteaminfo add <team> <x> <y> <z>");
                                }
                            }
                            else if(args[1].equalsIgnoreCase("list")){
                                //player.sendMessage(plugin.tm.PlayerRespawnLocation(player).toString());
                                String message = "";
                                for(String text : plugin.mainboardManager.showSpawnList()){
                                    message += text + "\n";
                                }
                                player.sendMessage(message);
                            }
                            else if(args[1].equalsIgnoreCase("king")){
                                //player.sendMessage(plugin.tm.PlayerRespawnLocation(player).toString());
                                if(args.length == 4){
                                    if(plugin.mainboardManager.setKing(args[2],args[3])){
                                        Notify(args[2]+"チームの大将が"+args[3]+"に設定されました。");
                                    }
                                    else player.sendMessage("指定したプレイヤーが存在しません。");
                                }
                                else{
                                    player.sendMessage("Usage:teaminfo king <team> <player>");
                                }
                            }
                        }
                        else {
                            player.sendMessage("引数の数が違います。");
                        }
                        break;
                    default:
                        sender.sendMessage("コマンドの使い方が間違っています。");
                        break;
                }
            }
            else {
                sender.sendMessage("You don't have permisstion");
            }
        }
        else {
            sender.sendMessage("引数が間違っています");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String command, String[] args) {
        List<String> autoComplete = new ArrayList<>();
        if(sender.hasPermission("ct")){

            if (args.length == 1){//一段目
                autoComplete.addAll(Arrays.asList("javavsbe","teaminfo","gamestart","gamestop"));
            }
            else if(args.length >1){//二段目
                if(args[0].equalsIgnoreCase("gamestart")){
                    if(args.length == 2){
                        autoComplete.addAll(Arrays.asList("tag","teambattle"));
                    }
                    else if(args.length == 3){
                        autoComplete.addAll(Arrays.asList("600","900","1200","1800"));
                    }
                    else if(args.length == 4){
                        autoComplete.addAll(plugin.mainboardManager.getCurrentTeams());
                    }
                    else if(args.length == 5){
                        autoComplete.addAll(plugin.mainboardManager.getCurrentTeams());
                        autoComplete.remove(args[3]);
                    }
                }
                //java vs be コマンド
                else if (args[0].equalsIgnoreCase("javavsbe")){
                    if(args.length == 2){
                        autoComplete.addAll(plugin.mainboardManager.getCurrentTeams());
                    }
                    else if (args.length == 3){
                        autoComplete.addAll(plugin.mainboardManager.getCurrentTeams());
                        autoComplete.remove(args[1]);
                    }
                }
                //teamspawnpoint
                else if(args[0].equalsIgnoreCase("teaminfo")){
                    if(args.length == 2){
                        autoComplete.addAll(Arrays.asList("add","remove","list","spawn","king","count"));
                    }
                    else if(args.length == 3){
                        if(args[1].equalsIgnoreCase("add")){
                            autoComplete.addAll(plugin.mainboardManager.getCurrentTeams());
                        }
                        else if(args[1].equalsIgnoreCase("remove")){
                            autoComplete.addAll(plugin.mainboardManager.showSpawnTextList());
                        }
                        else if(args[1].equalsIgnoreCase("spawn")){
                            autoComplete.addAll(plugin.mainboardManager.showSpawnTextList());
                        }
                        else if(args[1].equalsIgnoreCase("king")){
                            autoComplete.addAll(plugin.mainboardManager.getCurrentTeams());
                        }
                        else if(args[1].equalsIgnoreCase("count")){
                            autoComplete.addAll(plugin.mainboardManager.showSpawnTextList());
                        }
                    }
                    else if(args.length == 4){
                        if(args[1].equalsIgnoreCase("spawn")){
                            autoComplete.addAll(Arrays.asList("on","off"));
                        }
                        else if(args[1].equalsIgnoreCase("king")){
                            for (Player player : Bukkit.getOnlinePlayers ()) {
                                autoComplete.add(player.getName());
                            }
                        }
                        else if(args[1].equalsIgnoreCase("count")){
                            autoComplete.addAll(Arrays.asList("10","20","30","40"));
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
}
