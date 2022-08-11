package yusama125718.advancedpotion;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public final class AdvancedPotion extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {

    public static JavaPlugin potp;
    public static HashMap<Material,List<Integer>> allowitem = new HashMap<>();
    public static boolean recipeoperation;
    public static boolean protectoperation;
    public static ArrayList<Data.PotionRecipe> recipe = new ArrayList<>();
    public static File configfile;

    @Override
    public void onEnable() {    //起動処理
        getCommand("advpot").setExecutor(new Command());
        new Event(this);
        potp = this;
        getServer().getPluginManager().registerEvents(this, this);      //Event用
    }
}
