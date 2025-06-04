package cn.lunadeer.dominion.utils.scui;

import cn.lunadeer.dominion.utils.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public abstract class ChestButton {

    private final ItemStack item;
    private String displayName;
    private List<String> lore;

    // Item of Material type
    public ChestButton(Material material) {
        this.item = new ItemStack(material);
    }

    // Item of PlayerHead type
    public ChestButton(String playerName) {
        item = new ItemStack(Material.PLAYER_HEAD);
        Scheduler.runTaskAsync(() -> {
            SkullMeta headMeta = (SkullMeta) item.getItemMeta();
            headMeta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
            item.setItemMeta(headMeta);
        });
    }

    public ChestButton setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ChestButton setLore(String... lore) {
        this.lore = List.of(lore);
        return this;
    }

    public abstract void onClick(ClickType type);

    public ItemStack build() {
        if (displayName != null) {
            item.getItemMeta().setDisplayName(displayName);
        }
        if (lore != null) {
            item.getItemMeta().setLore(lore);
        }
        return item;
    }
}
