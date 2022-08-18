package yusama125718.advancedpotion;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.Potion;

import static com.sun.java.accessibility.util.EventID.CONTAINER;
import static org.bukkit.event.inventory.InventoryType.CHEST;
import static yusama125718.advancedpotion.AdvancedPotion.recipe;
import static yusama125718.advancedpotion.Function.*;

public class GUI {
    public static void RecipeList(Player player,Integer page){
        Inventory inv = Bukkit.createInventory(null,54,Component.text("[AdvPot]Recipe List" + page));
        for (int i = 51;i < 54;i++){
            inv.setItem(i,getItem(Material.BLUE_STAINED_GLASS_PANE,1,"次のページへ",1));
            inv.setItem(i - 3,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",1));
            inv.setItem(i - 6,getItem(Material.RED_STAINED_GLASS_PANE,1,"前のページへ",1));
        }
        for (int i = 0;i < recipe.size() - 1;i++){
            if (i == 44 || recipe.size() == i + 44 * (page - 1)){
                player.openInventory(inv);
                return;
            }
            Data.PotionRecipe list;
            if (page == 1){
                list = recipe.get(i);
            }else{
                list = recipe.get(i + 45 * (page - 1));
            }
            inv.setItem(i + 1,getItem(list.result.get(0).material,list.result.get(0).amount,list.result.get(0).name,list.result.get(0).cmd));
        }
        player.openInventory(inv);
    }

    public static void addrecipeGUI(Player player){
        Inventory inv = Bukkit.createInventory(null,54, Component.text("[AdvPot]Add Recipe"));
        for (int i = 0;i < 54;i++) if (i != 11 && i != 23 && i != 24 && i != 25 && i != 29) inv.setItem(i,getItem(Material.WHITE_STAINED_GLASS_PANE,1,"",0));
        inv.setItem(20,getItem(Material.BLACK_STAINED_GLASS_PANE,1,"上に材料(上)を置く",0));
        inv.setItem(32,getItem(Material.BLACK_STAINED_GLASS_PANE,1,"上に完成品(1)を置く",0));
        inv.setItem(33,getItem(Material.BLACK_STAINED_GLASS_PANE,1,"上に完成品(2)を置く",0));
        inv.setItem(34,getItem(Material.BLACK_STAINED_GLASS_PANE,1,"上に完成品(3)を置く",0));
        inv.setItem(38,getItem(Material.BLACK_STAINED_GLASS_PANE,1,"上に材料(下)を置く",0));
        inv.setItem(49,getItem(Material.RED_STAINED_GLASS_PANE,1,"追加する",0));
        player.openInventory(inv);
    }
}
