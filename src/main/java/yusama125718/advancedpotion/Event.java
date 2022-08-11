package yusama125718.advancedpotion;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static yusama125718.advancedpotion.AdvancedPotion.*;
import static yusama125718.advancedpotion.Data.*;

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

    @EventHandler
    public void BrewEvent(BrewEvent event) {
        if (recipeoperation){
            for (PotionRecipe pot : recipe) {      //Recipe確認処理
                ItemStack ingredient = event.getContents().getIngredient();
                if (ingredient == null) continue;
                if (ingredient.getType() != pot.ingredient.material || ingredient.getItemMeta().getCustomModelData() != pot.ingredient.cmd || !ingredient.getItemMeta().getDisplayName().equals(pot.ingredient.name)) continue;
                int create = 0;
                boolean finish = false;
                for (int i = 0; i <= Objects.requireNonNull(event.getContents().getHolder()).getInventory().getContents().length; i++){
                    ItemStack result = event.getResults().get(i);
                    ItemStack material = event.getContents().getHolder().getInventory().getContents()[i];
                    if (material == null) continue;
                    if (finish){
                        result.setItemMeta(material.getItemMeta());
                        result.setType(material.getType());
                        result.setAmount(material.getAmount());
                        continue;
                    }
                    if (material.getType() != pot.material.material || material.getItemMeta().getCustomModelData() != pot.material.cmd || !material.getItemMeta().getDisplayName().equals(pot.material.name)){
                        result.setItemMeta(material.getItemMeta());
                        result.setType(material.getType());
                        result.setAmount(material.getAmount());
                        continue;
                    }
                    result.setItemMeta(pot.result.get(i).getItemMeta());
                    result.setType(pot.result.get(i).material);
                    result.setAmount(pot.result.get(i).amount);
                    create++;
                    if (create <= pot.result.size()) finish = true;
                }
            }
        }
        if (!protectoperation) return;
        if (!allowitem.containsKey(Objects.requireNonNull(event.getContents().getIngredient()).getType())) return;      //Protect確認処理
        for (Integer number : allowitem.get(event.getContents().getIngredient().getType())) {
            if (!event.getContents().getIngredient().hasItemMeta()) return;
            if (event.getContents().getIngredient().getItemMeta().getCustomModelData() == number) return;
        }
        event.setCancelled(true);       //キャンセル処理
    }
}
