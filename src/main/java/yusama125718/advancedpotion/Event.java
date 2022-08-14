package yusama125718.advancedpotion;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.Objects;

import static org.bukkit.Material.*;
import static yusama125718.advancedpotion.AdvancedPotion.*;
import static yusama125718.advancedpotion.Data.*;
import static yusama125718.advancedpotion.Function.checkingredient;

public class Event implements Listener{
    public Event(AdvancedPotion plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void BrewingStandFuelEvent(BrewingStandFuelEvent event) {
        if (!protectoperation) return;
        if (!allowitem.containsKey(event.getFuel().getType())) return;
        for (Integer number : allowitem.get(Material.BLAZE_POWDER)) {       //確認処理
            if (!event.getFuel().hasItemMeta()) return;
            if (event.getFuel().getItemMeta().getCustomModelData() == number) return;
        }
        event.setCancelled(true);       //キャンセル処理
    }

//    @EventHandler
//    public void BrewRecipe(BrewEvent event) {
//        if (!recipeoperation) return;
//        for (PotionRecipe pot : recipe) {      //Recipe確認処理
//            ItemStack ingredient = event.getContents().getIngredient();
//            if (ingredient == null) continue;
//            if (ingredient.getType() != pot.ingredient.material || ingredient.getItemMeta().getCustomModelData() != pot.ingredient.cmd || !ingredient.getItemMeta().getDisplayName().equals(pot.ingredient.name)) continue;
//            int create = 0;
//            boolean finish = false;
//            for (int i = 0; i <= event.getResults().size() - 1; i++){
//                if (event.getContents().getHolder().getInventory().getContents()[i] == null) continue;
//                ItemStack result = event.getResults().get(i);
//                ItemStack material = event.getContents().getHolder().getInventory().getContents()[i];
//                if (material == null) continue;
//                if (finish){
//                    result.setItemMeta(material.getItemMeta());
//                    result.setType(material.getType());
//                    result.setAmount(material.getAmount());
//                    continue;
//                }
//                if (material.getType() != Material.POTION || material.getItemMeta().getCustomModelData() != pot.material.cmd || !material.getItemMeta().getDisplayName().equals(pot.material.name)){
//                    if (((PotionMeta) material.getItemMeta()).getBasePotionData().equals(new PotionData(PotionType.valueOf(pot.material.material), pot.material.extended, pot.material.upgraded))) {
//                        result.setItemMeta(material.getItemMeta());
//                        result.setType(material.getType());
//                        result.setAmount(material.getAmount());
//                        continue;
//                    }
//                }
//                result.setItemMeta(pot.result.get(create).getItemMeta());
//                result.setType(pot.result.get(create).material);
//                result.setAmount(pot.result.get(create).amount);
//                create++;
//                if (create >= pot.result.size()) finish = true;
//            }
//        }
//    }

    @EventHandler
    public void BrewRecipe(BrewEvent event) {      //Recipe用処理
        if (!recipeoperation) return;
        for (PotionRecipe pot : recipe) {
            ItemStack ingredient = event.getContents().getIngredient();
            if (ingredient == null) continue;
            if (ingredient.getType() != pot.ingredient.material || ingredient.getItemMeta().getCustomModelData() != pot.ingredient.cmd || !ingredient.getItemMeta().getDisplayName().equals(pot.ingredient.name)) continue;
            int create = 0;
            boolean finish = false;
            if (event.getResults().size() < pot.result.size()){
                System.out.println("aaaaaaaaaa");
                event.setCancelled(true);
                return;
            }
            for (int i = 0; i <= event.getResults().size() - 1; i++){
                if (event.getContents().getHolder().getInventory().getContents()[i] == null) continue;
                ItemStack result = event.getResults().get(i);
                ItemStack material = event.getContents().getHolder().getInventory().getContents()[i];
                if (material == null) continue;
                if (finish){
                    System.out.println("iiiiiiiiiiii");
                    result.setItemMeta(material.getItemMeta());
                    result.setType(material.getType());
                    result.setAmount(material.getAmount());
                    continue;
                }
                if (material.getType() != Material.POTION || material.getItemMeta().getCustomModelData() != pot.material.cmd || !material.getItemMeta().getDisplayName().equals(pot.material.name)){
                    if (((PotionMeta) material.getItemMeta()).getBasePotionData().equals(new PotionData(PotionType.valueOf(pot.material.material), pot.material.extended, pot.material.upgraded))) {
                        System.out.println("uuuuuuuuuu");
                        result.setItemMeta(material.getItemMeta());
                        result.setType(material.getType());
                        result.setAmount(material.getAmount());
                        continue;
                    }
                }
                create++;
                if (create == pot.result.size()) finish = true;
            }
            if (!finish) return;
            for (int i = 0;create == 0;i++){
                ItemStack result = event.getResults().get(i);
                ItemStack material = event.getContents().getHolder().getInventory().getContents()[i];
                if (material == null) continue;
                result.setItemMeta(pot.result.get(create).getItemMeta());
                result.setType(pot.result.get(create).material);
                result.setAmount(pot.result.get(create).amount);
                create--;
            }
        }
    }

    @EventHandler
    public void BrewProtect(BrewEvent event){             //Protect用処理
        if (!protectoperation) return;
        if (!allowitem.containsKey(Objects.requireNonNull(event.getContents().getIngredient()).getType())) return;
        for (Integer number : allowitem.get(event.getContents().getIngredient().getType())) {
            if (!event.getContents().getIngredient().hasItemMeta()) return;
            if (event.getContents().getIngredient().getItemMeta().getCustomModelData() == number) return;
        }
        event.setCancelled(true);       //キャンセル処理
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent e){
        if (e.getView().getTitle().equals("[AdvPot]Add Recipe")){
            if (e.getWhoClicked().hasPermission("advpot.op")) return;
            if (e.getCurrentItem() == null) return;
            if (e.getCurrentItem().getType() == BLACK_STAINED_GLASS_PANE || e.getCurrentItem().getType() == WHITE_STAINED_GLASS_PANE ){
                e.setCancelled(true);
                return;
            }
            if (e.getCurrentItem().getType() != RED_STAINED_GLASS_PANE || e.getRawSlot() != 41) return;
            Inventory inv = e.getInventory();
            if (inv.getItem(12) == null || inv.getItem(24) == null || inv.getItem(30) == null) {
                e.getWhoClicked().sendMessage("§9§l[AdvancedPotion] §cアイテムが不足しています");
                e.setCancelled(true);
                return;
            }
            if (!checkingredient(inv.getItem(12).getType())){
                e.getWhoClicked().sendMessage("§9§l[AdvancedPotion] §c材料(上)が不正です");
                e.setCancelled(true);
                return;
            }
            if (inv.getItem(30).getType() != POTION){
                e.getWhoClicked().sendMessage("§9§l[AdvancedPotion] §c材料(下)が不正です");
                e.setCancelled(true);
                return;
            }
            //ここからまだ製作中
        }
    }
}
