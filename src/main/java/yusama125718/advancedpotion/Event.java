package yusama125718.advancedpotion;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
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
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
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
        if (!event.getFuel().hasItemMeta()) return;
        if (!event.getFuel().getItemMeta().hasCustomModelData()) return;
        for (Integer number : allowitem.get(Material.BLAZE_POWDER)) {       //確認処理
            if (event.getFuel().getItemMeta().getCustomModelData() == number) return;
        }
        event.setCancelled(true);       //キャンセル処理
    }

    @EventHandler
    public void BrewRecipe(BrewEvent event) {      //Recipe用処理
        if (!recipeoperation) return;
        for (PotionRecipe pot : recipe) {
            ItemStack ingredient = event.getContents().getIngredient();
            if (ingredient == null) continue;
            if (!ingredient.hasItemMeta() || !ingredient.getItemMeta().hasCustomModelData()) return;
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
                    result.setItemMeta(material.getItemMeta());
                    result.setType(material.getType());
                    result.setAmount(material.getAmount());
                    continue;
                }
                if (material.getType() != Material.POTION || material.getItemMeta().getCustomModelData() != pot.material.cmd || !material.getItemMeta().getDisplayName().equals(pot.material.name)){
                    if (((PotionMeta) material.getItemMeta()).getBasePotionData().equals(new PotionData(PotionType.valueOf(pot.material.material), pot.material.extended, pot.material.upgraded))) {
                        result.setItemMeta(material.getItemMeta());
                        result.setType(material.getType());
                        result.setAmount(material.getAmount());
                        continue;
                    }
                }
                create++;
                if (create == pot.result.size()) finish = true;
            }
            if (!finish) {
                event.setCancelled(true);
                return;
            }
            for (int i = 0;create > 0;i++){
                ItemStack result = event.getResults().get(i);
                ItemStack material = event.getContents().getHolder().getInventory().getContents()[i];
                if (material == null) continue;
                result.setItemMeta(pot.result.get(create - 1).getItemMeta());
                result.setType(pot.result.get(create - 1).material);
                result.setAmount(pot.result.get(create - 1).amount);
                create--;
            }
        }
    }

    @EventHandler
    public void BrewProtect(BrewEvent event){             //Protect用処理
        if (!protectoperation) return;
        if (!allowitem.containsKey(Objects.requireNonNull(event.getContents().getIngredient()).getType())) return;
        if (!event.getContents().getIngredient().hasItemMeta()) return;
        if (!event.getContents().getIngredient().getItemMeta().hasCustomModelData()) return;
        for (Integer number : allowitem.get(event.getContents().getIngredient().getType())) {
            if (event.getContents().getIngredient().getItemMeta().getCustomModelData() == number) return;
        }
        for (PotionRecipe r : recipe){
            if (event.getContents().getIngredient().getItemMeta().getCustomModelData() == r.ingredient.cmd) return;
        }
        for (Integer number : allowitem.get(event.getContents().getIngredient().getType())) {
            if (event.getContents().getIngredient().getItemMeta().getCustomModelData() == number) return;
        }
        for (PotionRecipe r : recipe){
            if (event.getContents().getHolder().getInventory().getContents()[0] != null && event.getContents().getHolder().getInventory().getContents()[0].getType() == POTION) if (event.getContents().getHolder().getInventory().getContents()[0].getItemMeta().getCustomModelData() == r.ingredient.cmd) return;
            if (event.getContents().getHolder().getInventory().getContents()[1] != null && event.getContents().getHolder().getInventory().getContents()[1].getType() == POTION) if (event.getContents().getHolder().getInventory().getContents()[1].getItemMeta().getCustomModelData() == r.ingredient.cmd) return;
            if (event.getContents().getHolder().getInventory().getContents()[2] != null && event.getContents().getHolder().getInventory().getContents()[2].getType() == POTION) if (event.getContents().getHolder().getInventory().getContents()[2].getItemMeta().getCustomModelData() == r.ingredient.cmd) return;
        }
        event.setCancelled(true);       //キャンセル処理
    }

    @EventHandler
    public void AddGUIClick(InventoryClickEvent e) throws IOException {     //レシピ追加用
        if (!e.getView().getTitle().equals("[AdvPot]Add Recipe")) return;
        if (!e.getWhoClicked().hasPermission("advpot.op")) {
            e.setCancelled(true);
            return;
        }
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType() == BLACK_STAINED_GLASS_PANE || e.getCurrentItem().getType() == WHITE_STAINED_GLASS_PANE ){
            e.setCancelled(true);
            return;
        }
        if (e.getCurrentItem().getType() != RED_STAINED_GLASS_PANE || e.getRawSlot() != 49) return;
        Inventory inv = e.getInventory();
        if (inv.getItem(11) == null || inv.getItem(23) == null || inv.getItem(29) == null) {
            e.getWhoClicked().sendMessage("§9§l[AdvancedPotion] §cアイテムが不足しています");
            e.setCancelled(true);
            return;
        }
        if (!checkingredient(inv.getItem(11).getType())){
            e.getWhoClicked().sendMessage("§9§l[AdvancedPotion] §c材料(上)が不正です");
            e.setCancelled(true);
            return;
        }
        if (inv.getItem(29).getType() != POTION){
            e.getWhoClicked().sendMessage("§9§l[AdvancedPotion] §c材料(下)が不正です");
            e.setCancelled(true);
            return;
        }
        if (addname == null || !addname.containsKey(e.getWhoClicked())){
            e.setCancelled(true);
            return;
        }
        int cmd = 0;
        if (inv.getItem(11).getItemMeta().hasCustomModelData()) cmd = 0;
        else cmd = inv.getItem(11).getItemMeta().getCustomModelData();
        PotionIngredient ingre = new PotionIngredient(inv.getItem(11).getType(),cmd,inv.getItem(11).getItemMeta().displayName().toString());
        if (inv.getItem(29).getItemMeta().hasCustomModelData()) cmd = 0;
        else cmd = inv.getItem(29).getItemMeta().getCustomModelData();
        PotionData data = ((PotionMeta) inv.getItem(29).getItemMeta()).getBasePotionData();
        PotionMaterial mate = new PotionMaterial(data.getType().toString(),cmd,inv.getItem(29).getItemMeta().displayName().toString(),data.isExtended(), data.isUpgraded());
        List<PotionItem> addlist = new ArrayList<>();
        if (inv.getItem(23).getItemMeta().hasCustomModelData()) cmd = inv.getItem(23).getItemMeta().getCustomModelData();
        else cmd = 0;
        addlist.add(new PotionItem(inv.getItem(23).getType(),cmd,inv.getItem(23).getAmount(),inv.getItem(23).displayName().toString()));
        if (inv.getItem(24) != null){
            if (inv.getItem(24).getItemMeta().hasCustomModelData()) cmd = inv.getItem(24).getItemMeta().getCustomModelData();
            else cmd = 0;
            addlist.add(new PotionItem(inv.getItem(24).getType(),cmd,inv.getItem(24).getAmount(),inv.getItem(24).displayName().toString()));
        }
        if (inv.getItem(25) != null){
            if (inv.getItem(25).getItemMeta().hasCustomModelData()) cmd = inv.getItem(25).getItemMeta().getCustomModelData();
            else cmd = 0;
            addlist.add(new PotionItem(inv.getItem(25).getType(),cmd,inv.getItem(25).getAmount(),inv.getItem(25).displayName().toString()));
        }
        File folder = new File(configfile.getAbsolutePath() + File.separator + addname.get(e.getWhoClicked()) + ".yml");
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("name",addname.get(e.getWhoClicked()));
        yml.set("ingredient.material",ingre.material.toString());
        yml.set("ingredient.cmd",ingre.cmd);
        yml.set("ingredient.name",ingre.name);
        yml.set("material.material",mate.material.toString());
        yml.set("material.cmd",mate.cmd);
        yml.set("material.name",mate.name);
        yml.set("material.extended",mate.extended);
        yml.set("material.upgraded",mate.upgraded);
        for (int i = 1;i < addlist.size();i++){
            yml.set("result."+i+".material",addlist.get(i-1).material.toString());
            yml.set("result."+i+".cmd",addlist.get(i-1).cmd);
            yml.set("result."+i+".name",addlist.get(i-1).name);
            yml.set("result."+i+".amount",addlist.get(i-1).amount);
        }
        yml.save(folder);
        recipe.add(new PotionRecipe(addname.get(e.getWhoClicked()),ingre,mate,addlist));
        addname.remove(e.getWhoClicked());
        e.setCancelled(true);
        e.getInventory().close();
        e.getWhoClicked().sendMessage("§9§l[AdvancedPotion] §r追加しました");
    }
}
