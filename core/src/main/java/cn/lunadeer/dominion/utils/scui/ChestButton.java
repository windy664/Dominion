package cn.lunadeer.dominion.utils.scui;

import cn.lunadeer.dominion.utils.ColorParser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static cn.lunadeer.dominion.utils.Misc.setPlaceholder;

public abstract class ChestButton {

    private final ItemStack item;
    private String displayName;
    private List<String> lore;

    // Item of Material type
    public ChestButton(String displayName, Material material) {
        this.displayName = displayName;
        this.item = new ItemStack(material);
    }

//    // Item of PlayerHead type
//    public ChestButton(String displayName, String playerName) {
//        this.displayName = displayName;
//        this.item = new ItemStack(Material.PLAYER_HEAD);
//        Scheduler.runTaskAsync(() -> {
//            SkullMeta headMeta = (SkullMeta) item.getItemMeta();
//            headMeta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
//            item.setItemMeta(headMeta);
//        });
//    }

    public ChestButton setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ChestButton setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ChestButton setLore(String... lore) {
        this.lore = List.of(lore);
        return this;
    }

    public abstract void onClick(ClickType type);

    public ItemStack build(Player viewOwner) {
        ItemMeta meta = item.getItemMeta();
        if (displayName != null) {
            displayName = setPlaceholder(viewOwner, displayName);
            displayName = ColorParser.getBukkitType(displayName);
            // todo MiniMessage support
            meta.setDisplayName(displayName);
        }
        if (lore != null) {
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, setPlaceholder(viewOwner, lore.get(i)));
                lore.set(i, ColorParser.getBukkitType(lore.get(i)));
                // todo MiniMessage support
            }
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }
}
