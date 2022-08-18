package yusama125718.advancedpotion;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static yusama125718.advancedpotion.AdvancedPotion.*;
import static yusama125718.advancedpotion.Data.*;
import static yusama125718.advancedpotion.Function.*;

public class Config {
    private static File folder = new File(potp.getDataFolder().getAbsolutePath() + File.separator + "recipes");
    private static File removefolder = new File(potp.getDataFolder().getAbsolutePath() + File.separator + "removefiles");

    public static void RoadFile(){
        if (potp.getDataFolder().listFiles() != null){
            for (File file : Objects.requireNonNull(potp.getDataFolder().listFiles())) {
                if (file.getName().equals("recipes")) {
                    configfile = file;
                    return;
                }
                if (file.getName().equals("removefiles")) {
                    configfile = file;
                    return;
                }
            }
        }
        if (folder.mkdir()) {
            Bukkit.broadcast("§9§l[AdvancedPotion] §rレシピフォルダを作成しました", "advpot.op");
            configfile = folder;
        } else {
            Bukkit.broadcast("§9§l[AdvancedPotion] §rレシピフォルダの作成に失敗しました", "advpot.op");
        }
        if (removefolder.mkdir()) {
            Bukkit.broadcast("§9§l[AdvancedPotion] §r削除フォルダを作成しました", "advpot.op");
            removefile = removefolder;
        } else {
            Bukkit.broadcast("§9§l[AdvancedPotion] §r削除フォルダの作成に失敗しました", "advpot.op");
        }
    }

    public static PotionRecipe getConfig(YamlConfiguration config){
        if (!checknull(config)) {
            Bukkit.broadcast("§9§l[AdvancedPotion] §r" + config.getName() + "の読み込みに失敗しました","advpot.op");
            return null;
        }
        PotionIngredient ingre = new PotionIngredient(Material.getMaterial(Objects.requireNonNull(config.getString("ingredient.material"))), config.getInt("ingredient.cmd"), config.getString("ingredient.name"));
        PotionMaterial potmaterial = new PotionMaterial(config.getString("material.material"),config.getInt("material.cmd"),config.getString("material.name"),config.getBoolean("material.extended"),config.getBoolean("material.upgraded"));
        List<PotionItem> item = new ArrayList<PotionItem>();
        for (int i = 1;i < 3;i++){
            if (config.getConfigurationSection("result."+ i) == null) return new PotionRecipe(config.getString("name"),ingre,potmaterial,item);
            item.add(new PotionItem(Material.getMaterial(Objects.requireNonNull(config.getString("result."+ i + ".material"))),config.getInt("result."+ i +".cmd"),config.getInt("result."+ i +".amount"),config.getString("result."+ i +".name")));
        }
        return new PotionRecipe(config.getString("name"),ingre,potmaterial,item);
    }
}
