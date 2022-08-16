package yusama125718.advancedpotion;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Integer.parseInt;
import static yusama125718.advancedpotion.AdvancedPotion.*;
import static yusama125718.advancedpotion.GUI.*;

public class Command implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {   //コマンド処理
        if (!sender.hasPermission("advpot.op")) {         //permission確認
            sender.sendMessage("§c[PotionProtect] You don't have Permission");
            return true;
        }

        switch (args.length) {
            case 1:
                switch (args[0]) {
                    case "help":        //help
                        if (sender.hasPermission("advpot.op")) {
                            sender.sendMessage("§9§l[AdvancedPotion] §7/advpot protect on/off §r保護をon/offします");
                            sender.sendMessage("§9§l[AdvancedPotion] §7/advpot protect add [数字] §r手に持っているアイテムの指定したカスタムモデルデータのポーションへの使用を許可します");
                            sender.sendMessage("§9§l[AdvancedPotion] §7/advpot protect delete [数字] §r手に持っているアイテムの指定したカスタムモデルデータのポーションへの使用を禁止します");
                            sender.sendMessage("§9§l[AdvancedPotion] §7/advpot protect check §r手に持っているアイテムの許可されているカスタムモデルデータを確認します");
                            sender.sendMessage("§9§l[AdvancedPotion] §7/advpot recipe on/off §rレシピをon/offします");
                        }
                        sender.sendMessage("§9§l[AdvancedPotion] §7/advpot recipe §rレシピを確認します");
                        return true;

                    case "recipe":
                        RecipeList((Player) sender,1);
                        return true;

                    default:
                        sender.sendMessage("§9§l[AdvancedPotion] §7/advpot help §rでhelpを表示");
                        return true;
                }

            case 2:
                switch (args[0]){
                    case "protect":
                        if (!sender.hasPermission("advpot.op")) {         //permission確認
                            sender.sendMessage("§9§l[AdvancedPotion] §7/advpot help §rでhelpを表示");
                            return true;
                        }
                        switch (args[1]){

                            case "on":      //保護on
                                if (protectoperation) {
                                    sender.sendMessage("§9§l[AdvancedPotion] §cすでに有効になっています");
                                    return true;
                                }
                                protectoperation = true;
                                potp.getConfig().set("protect", protectoperation);
                                potp.saveConfig();
                                sender.sendMessage("§9§l[AdvancedPotion] §r保護を有効化しました");
                                return true;

                            case "off":     //保護off
                                if (!protectoperation) {
                                    sender.sendMessage("§9§l[AdvancedPotion] §cすでに無効になっています");
                                    return true;
                                }
                                protectoperation = false;
                                potp.getConfig().set("protect", protectoperation);
                                potp.saveConfig();
                                sender.sendMessage("§9§l[AdvancedPotion] §r保護を無効化しました");
                                return true;

                            case "check":       //登録確認
                                Material inhand = ((Player) sender).getInventory().getItemInMainHand().getType();
                                if (!allowitem.containsKey(inhand)) {
                                    sender.sendMessage("§9§l[AdvancedPotion] §cそのアイテムは使えません");
                                    return true;
                                }
                                sender.sendMessage("===== 許可されているカスタムモデルデータ =====");
                                for (Integer number : allowitem.get(inhand)) {
                                    sender.sendMessage(String.valueOf(number));
                                }
                                return true;

                            default:
                                sender.sendMessage("§9§l[AdvancedPotion] §7/advpot help §rでhelpを表示");
                                return true;
                        }

                    case "recipe":
                        switch (args[1]){
                            case "on":      //レシピon
                                if (recipeoperation) {
                                    sender.sendMessage("§9§l[AdvancedPotion] §cすでに有効になっています");
                                    return true;
                                }
                                recipeoperation = true;
                                potp.getConfig().set("recipe", recipeoperation);
                                potp.saveConfig();
                                sender.sendMessage("§9§l[AdvancedPotion] §rレシピを有効化しました");
                                return true;

                            case "off":     //レシピoff
                                if (!recipeoperation) {
                                    sender.sendMessage("§9§l[AdvancedPotion] §cすでに無効になっています");
                                    return true;
                                }
                                recipeoperation = false;
                                potp.getConfig().set("recipe", recipeoperation);
                                potp.saveConfig();
                                sender.sendMessage("§9§l[AdvancedPotion] §rレシピを無効化しました");
                                return true;
                        }

                    default:
                        sender.sendMessage("§9§l[AdvancedPotion] §7/advpot help §rでhelpを表示");
                        return true;
                }

            case 3:
                switch (args[0]){
                    case "protect":
                        if (!sender.hasPermission("advpot.op")) {         //permission確認
                            sender.sendMessage("§9§l[AdvancedPotion] §7/advpot help §rでhelpを表示");
                            return true;
                        }
                        if (args[1].equals("add")){     //保護追加処理
                            Material inhand = ((Player) sender).getInventory().getItemInMainHand().getType();
                            if (!allowitem.containsKey(inhand)) {
                                sender.sendMessage("§9§l[AdvancedPotion] §cそのアイテムは使えません");
                                return true;
                            }
                            boolean isNumeric = args[1].matches("-?\\d+");
                            if (!isNumeric) {
                                sender.sendMessage("§9§l[AdvancedPotion] §c無効な数字です");
                                return true;
                            }
                            if (args[1].length() >= 10) {
                                sender.sendMessage("§9§l[AdvancedPotion] §c無効な数字です");
                                return true;
                            }
                            int addnumber = parseInt(args[1]);
                            if (addnumber < 0) {
                                sender.sendMessage("§9§l[AdvancedPotion] §c無効な数字です");
                                return true;
                            }
                            if (allowitem.get(inhand).contains(addnumber)) {
                                sender.sendMessage("§9§l[AdvancedPotion] §cその番号はすでに登録されています");
                                return true;
                            }
                            List<Integer> addlist = allowitem.get(inhand);
                            addlist.add(addnumber);
                            allowitem.replace(inhand, addlist);
                            potp.getConfig().set(inhand.toString(), allowitem.get(inhand));
                            potp.saveConfig();
                            sender.sendMessage("§9§l[AdvancedPotion] §r追加しました");
                            return true;
                        }

                        else if (args[1].equals("delete")){    //削除処理
                            Material inhand = ((Player) sender).getInventory().getItemInMainHand().getType();
                            if (!allowitem.containsKey(inhand)) {
                                sender.sendMessage("§9§l[AdvancedPotion] §cそのアイテムは使えません");
                                return true;
                            }
                            boolean isNumeric = args[1].matches("-?\\d+");
                            if (!isNumeric) {
                                sender.sendMessage("§9§l[AdvancedPotion] §c無効な数字です");
                                return true;
                            }
                            if (args[1].length() >= 10) {
                                sender.sendMessage("§9§l[AdvancedPotion] §c無効な数字です");
                                return true;
                            }
                            int removenumber = parseInt(args[1]);
                            if (removenumber < 0) {
                                sender.sendMessage("§9§l[AdvancedPotion] §c無効な数字です");
                                return true;
                            }
                            if (!allowitem.get(inhand).contains(removenumber)) {
                                sender.sendMessage("§9§l[AdvancedPotion] §cその番号は存在しません");
                                return true;
                            }
                            List<Integer> removelist = allowitem.get(inhand);
                            for (int i = 0;i < removelist.size() - 1;i++) {
                                if (removelist.get(i) == removenumber) {
                                    removelist.remove(i);
                                    break;
                                }
                            }
                            allowitem.replace(inhand, removelist);
                            potp.getConfig().set(inhand.toString(), allowitem.get(inhand));
                            potp.saveConfig();
                            sender.sendMessage("§9§l[AdvancedPotion] §r削除しました");
                            return true;
                        }

                    case "recipe":
                        switch (args[1]){
                            case "add":
                                addname.put((Player) sender,args[2]);
                                addrecipeGUI((Player) sender);
                                return true;

                            case "remove":
                                boolean check = false;
                                Data.PotionRecipe target;
                                for (Data.PotionRecipe data : recipe) {
                                    if (data.name == args[2]) {
                                        check = true;
                                        target = data;
                                        break;
                                    }
                                }
                                if (!check){
                                    sender.sendMessage("§9§l[AdvancedPotion] §cその名前のレシピは存在しません");
                                    return true;
                                }
                                for (File file : configfile){
                                    if (!file.getName().equals(args[2])) continue;
                                    try{
                                        Files.move(removefile.getAbsoluteFile().toPath(), file.getAbsoluteFile().toPath());
                                    }catch(IOException e){
                                        System.out.println(e);
                                        sender.sendMessage("§9§l[AdvancedPotion] §cファイルの移動に失敗しました");
                                        return true;
                                    }
                                    recipe.remove(target);
                                    sender.sendMessage("§9§l[AdvancedPotion] §r削除しました(ファイルは専用ディレクトリで保管されています)");
                                }
                                return true;

                            default:
                                sender.sendMessage("§9§l[AdvancedPotion] §7/advpot help §rでhelpを表示");
                                return true;
                        }

                    default:
                        sender.sendMessage("§9§l[AdvancedPotion] §7/advpot help §rでhelpを表示");
                        return true;
                }

            default:
                sender.sendMessage("§9§l[AdvancedPotion] §7/advpot help §rでhelpを表示");
                return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!sender.hasPermission("advpot.op")) return null;      //permission確認

        if(command.getName().equalsIgnoreCase("advpot")){
            if (args.length == 1){
                if (args[0].length() == 0)
                {
                    return Arrays.asList("add","check","delete","help","off","on");
                }

                else {
                    if ("on".startsWith(args[0])&&"off".startsWith(args[0]))
                    {
                        return Arrays.asList("off","on");
                    }
                    else if ("add".startsWith(args[0]))
                    {
                        return Collections.singletonList("add");
                    }
                    else if ("check".startsWith(args[0]))
                    {
                        return Collections.singletonList("check");
                    }
                    else if ("delete".startsWith(args[0]))
                    {
                        return Collections.singletonList("delete");
                    }
                    else if ("help".startsWith(args[0]))
                    {
                        return Collections.singletonList("help");
                    }
                    else if ("on".startsWith(args[0]))
                    {
                        return Collections.singletonList("on");
                    }
                    else if ("off".startsWith(args[0]))
                    {
                        return Collections.singletonList("off");
                    }
                }
            }
            else if (args.length == 2){
                if (args[0].equals("add")||args[0].equals("delete")){
                    return Collections.singletonList("[カスタムモデルデータ]");
                }
            }
        }
        return null;
    }
}
