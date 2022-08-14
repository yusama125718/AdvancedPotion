package yusama125718.advancedpotion;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.io.File;
import java.io.IOError;
import java.util.List;

import static org.bukkit.Material.*;
import static yusama125718.advancedpotion.AdvancedPotion.*;
import static yusama125718.advancedpotion.Config.*;

public class Function {
    public static boolean reloadconfig(){
        Bukkit.broadcast("§9§l[AdvancedPotion] §rリロードします", "advpot.op");       //configロード
        potp.saveDefaultConfig();
        RoadFile();
        if (configfile.listFiles() != null){
            for (File file : configfile.listFiles()){
                if (getConfig(YamlConfiguration.loadConfiguration(file)) != null) recipe.add(getConfig(YamlConfiguration.loadConfiguration(file)));
            }
        }

        List<Integer> blaze = potp.getConfig().getIntegerList("BLAZE_POWDER");
        allowitem.put(Material.BLAZE_POWDER,blaze);
        List<Integer> fspider = potp.getConfig().getIntegerList("FERMENTED_SPIDER_EYE");
        allowitem.put(Material.FERMENTED_SPIDER_EYE,fspider);
        List<Integer> ghast = potp.getConfig().getIntegerList("GHAST_TEAR");
        allowitem.put(Material.GHAST_TEAR,ghast);
        List<Integer> melon = potp.getConfig().getIntegerList("GLISTERING_MELON_SLICE");
        allowitem.put(Material.GLISTERING_MELON_SLICE,melon);
        List<Integer> carrot = potp.getConfig().getIntegerList("GOLDEN_CARROT");
        allowitem.put(Material.GOLDEN_CARROT,carrot);
        List<Integer> gunpowder = potp.getConfig().getIntegerList("GUNPOWDER");
        allowitem.put(Material.GUNPOWDER,gunpowder);
        List<Integer> magma = potp.getConfig().getIntegerList("MAGMA_CREAM");
        allowitem.put(Material.MAGMA_CREAM,magma);
        List<Integer> phantom = potp.getConfig().getIntegerList("PHANTOM_MEMBRANE");
        allowitem.put(Material.PHANTOM_MEMBRANE,phantom);
        List<Integer> pufferfish = potp.getConfig().getIntegerList("PUFFERFISH");
        allowitem.put(Material.PUFFERFISH,pufferfish);
        List<Integer> rabbit = potp.getConfig().getIntegerList("RABBIT_FOOT");
        allowitem.put(Material.RABBIT_FOOT,rabbit);
        List<Integer> spider = potp.getConfig().getIntegerList("SPIDER_EYE");
        allowitem.put(Material.SPIDER_EYE,spider);
        List<Integer> sugar = potp.getConfig().getIntegerList("SUGAR");
        allowitem.put(Material.SUGAR,sugar);
        List<Integer> turtle = potp.getConfig().getIntegerList("TURTLE_HELMET");
        allowitem.put(Material.TURTLE_HELMET,turtle);
        recipeoperation = potp.getConfig().getBoolean("recipe");
        protectoperation = potp.getConfig().getBoolean("protect");
        Bukkit.broadcast("§9§l[AdvancedPotion] §rリロード完了", "advpot.op");
        return true;
    }

    public static boolean checkingredient(Material mate){
        return mate == BLAZE_POWDER || mate == FERMENTED_SPIDER_EYE || mate == GHAST_TEAR || mate == GLISTERING_MELON_SLICE || mate == GOLDEN_CARROT || mate == GUNPOWDER || mate == MAGMA_CREAM || mate == NETHER_WART || mate == PHANTOM_MEMBRANE || mate == PUFFERFISH || mate == RABBIT_FOOT || mate == SPIDER_EYE || mate == SUGAR || mate == TURTLE_HELMET;
    }

    public static boolean checkmaterial(String mate){
        try {
            ItemStack item = new ItemStack(POTION,1);
            ItemMeta meta = item.getItemMeta();
            PotionMeta potionMeta = (PotionMeta) meta;
            potionMeta.setBasePotionData(new PotionData(PotionType.valueOf(mate), false, false));
            return true;
        } catch (IOError error){
            return false;
        }
    }

    public static boolean checknull(YamlConfiguration config){
        if (config.getString("name") == null) {
            Bukkit.broadcast("name","advpot.op");
            return false;
        }
        if (config.getString("ingredient.material") == null) {
            Bukkit.broadcast("ingredient.material","advpot.op");
            return false;
        }
        if (!checkingredient(Material.getMaterial(config.getString("ingredient.material")))) {
            Bukkit.broadcast("ingredient.material.check","advpot.op");
            return false;
        }
        if (config.getString("ingredient.cmd") == null) {
            Bukkit.broadcast("ingredient.cmd","advpot.op");
            return false;
        }
        if (config.getString("ingredient.name") == null) {
            Bukkit.broadcast("ingredient.name","advpot.op");
            return false;
        }
        if (config.getString("material.material") == null) {
            Bukkit.broadcast("material.material","advpot.op");
            return false;
        }
        if (!checkmaterial(config.getString("material.material"))) {
            Bukkit.broadcast("material.material.check","advpot.op");
            return false;
        }
        if (config.getString("material.cmd") == null) {
            Bukkit.broadcast("material.cmd","advpot.op");
            return false;
        }
        if (config.getString("material.name") == null) {
            Bukkit.broadcast("material.name","advpot.op");
            return false;
        }
        if (config.getConfigurationSection("result") == null) {
            Bukkit.broadcast("result","advpot.op");
            return false;
        }
        if (config.getConfigurationSection("result.1st") == null){
            Bukkit.broadcast("result.1st","advpot.op");
            return false;
        }
        return true;
    }

    public static ItemStack getItem(Material mate,Integer amount,String name,Integer cmd){
        ItemStack item = new ItemStack(mate,amount);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        meta.setCustomModelData(cmd);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getItem(Material mate,Integer amount,String name,Integer cmd,PotionType type,boolean extended,boolean upgraded){
        ItemStack item = new ItemStack(mate,amount);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        meta.setCustomModelData(cmd);
        ((PotionMeta) meta).setBasePotionData(new PotionData(type,extended,upgraded));
        item.setItemMeta(meta);
        return item;
    }
}
