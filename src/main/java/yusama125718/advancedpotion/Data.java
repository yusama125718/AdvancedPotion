package yusama125718.advancedpotion;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

import static org.bukkit.Material.POTION;

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
            meta.displayName(Component.text(name));
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
            meta.displayName((Component) meta);
            meta.setCustomModelData(cmd);
            item.setItemMeta(meta);
            return item;
        }
        public ItemMeta getItemMeta(){
            ItemStack item = new ItemStack(material,amount);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(name));
            meta.setCustomModelData(cmd);
            return meta;
        }
    }

    public static class PotionMaterial{
        public String material;
        public int cmd;
        public String name;
        public boolean extended;
        public boolean upgraded;
        public PotionMaterial(String mate,int cm,String display,boolean ext,boolean upg){
            material = mate;
            cmd = cm;
            name = display;
            extended = ext;
            upgraded = upg;
        }
        public ItemStack getitem(){
            ItemStack item = new ItemStack(POTION,1);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(name));
            PotionMeta potionMeta = (PotionMeta) meta;
            potionMeta.setBasePotionData(new PotionData(PotionType.valueOf(material), extended, upgraded));
            item.setItemMeta(potionMeta);
            meta.setCustomModelData(cmd);
            item.setItemMeta(meta);
            return item;
        }
    }
}
