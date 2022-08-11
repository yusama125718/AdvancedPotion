package yusama125718.advancedpotion;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static org.bukkit.Material.*;

public class Data {

    public static class PotionRecipe{
        public String name;
        public PotionIngredient ingredient;
        public PotionMaterial material;
        public List<PotionItem> result;
        public PotionRecipe(String recipename, PotionIngredient ingre,PotionMaterial mate,List<PotionItem> res){
            name = recipename;
            ingredient = ingre;
            material = mate;
            result = res;
        }
    }

    public static class PotionIngredient{
        public Material material;
        public int cmd;
        public String name;
        public PotionIngredient(Material mate,int cm,String display){
            material = mate;
            cmd = cm;
            name = display;
        }
        public ItemStack getitem(){
            ItemStack item = new ItemStack(material,1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.setCustomModelData(cmd);
            item.setItemMeta(meta);
            return item;
        }
    }

    public static class PotionItem{
        public Material material;
        public int amount;
        public int cmd;
        public String name;
        public PotionItem(Material mate,int cm,int amo,String display){
            material = mate;
            cmd = cm;
            amount = amo;
            name = display;
        }
        public ItemStack getitem(){
            ItemStack item = new ItemStack(material,amount);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.setCustomModelData(cmd);
            item.setItemMeta(meta);
            return item;
        }
        public ItemMeta getItemMeta(){
            ItemStack item = new ItemStack(material,amount);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.setCustomModelData(cmd);
            return meta;
        }
    }

    public static class PotionMaterial{
        public Material material;
        public int cmd;
        public String name;
        public PotionMaterial(Material mate,int cm,String display){
            material = mate;
            cmd = cm;
            name = display;
        }
        public ItemStack getitem(){
            ItemStack item = new ItemStack(material,1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.setCustomModelData(cmd);
            item.setItemMeta(meta);
            return item;
        }
    }
}
