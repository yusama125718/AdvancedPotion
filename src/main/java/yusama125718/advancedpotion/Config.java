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
    static File folder = new File(potp.getDataFolder().getAbsolutePath() + File.separator + "recipes");

    public static void RoadFile(){
        if (potp.getDataFolder().listFiles() != null){
            for (File file : Objects.requireNonNull(potp.getDataFolder().listFiles())) {
                if (file.getName().equals("recipes")) {
                    configfile = file;
                    return;
                }
            }
        }
        if (folder.mkdir()) {
            Bukkit.broadcast("§9§l[AdvancedPotion] §rフォルダを作成しました", "advpot.op");
            configfile = folder;
        } else {
            Bukkit.broadcast("§9§l[AdvancedPotion] §rフォルダの作成に失敗しました", "advpot.op");
        }
    }

    public static PotionRecipe getConfig(YamlConfiguration config){
        if (!checknull(config)) {
            Bukkit.broadcast("[AdvancedPotion]" + config.getCurrentPath() + "の読み込みに失敗しました","advpot.op");
            return null;
        }
        PotionIngredient ingre = new PotionIngredient(Material.getMaterial(Objects.requireNonNull(config.getString("ingredient.material"))), config.getInt("ingredient.cmd"), config.getString("ingredient.name"));
        PotionMaterial potmaterial = new PotionMaterial(Material.getMaterial(Objects.requireNonNull(config.getString("material.material"))),config.getInt("material.cmd"),config.getString("material.name"));
        List<PotionItem> item = new ArrayList<PotionItem>();
        for (int i = 1;i < 3;i++){
            if (config.getConfigurationSection("result."+ i +"st") == null) return new PotionRecipe(config.getString("name"),ingre,potmaterial,item);
            item.add(new PotionItem(Material.getMaterial(Objects.requireNonNull(config.getString("result."+ i +"st.material"))),config.getInt("result."+ i +"st.cmd"),config.getInt("result."+ i +"st.amount"),config.getString("result."+ i +"st.name")));
        }
        return new PotionRecipe(config.getString("name"),ingre,potmaterial,item);
    }
}
