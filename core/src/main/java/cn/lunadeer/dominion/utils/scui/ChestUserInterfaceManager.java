package cn.lunadeer.dominion.utils.scui;

import cn.lunadeer.dominion.utils.XLogger;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChestUserInterfaceManager implements Listener {

    private static ChestUserInterfaceManager instance;

    private final Map<UUID, ChestView> views = new HashMap<>();

    private static final NamespacedKey tag = new NamespacedKey("dominion", "chest_view");
    private static final NamespacedKey id = new NamespacedKey("dominion", "view_id");

    public static ItemStack PLACE_HOLDER_ITEM;


    public static ChestUserInterfaceManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ChestUserInterface has not been initialized. Please call ChestUserInterface.init(plugin) first.");
        }
        return instance;
    }

    public ChestUserInterfaceManager(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        instance = this;
        PLACE_HOLDER_ITEM = attachTag(null, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        ItemMeta meta = PLACE_HOLDER_ITEM.getItemMeta();
        meta.setDisplayName(" ");
        PLACE_HOLDER_ITEM.setItemMeta(meta);
    }

    public void registerView(ChestView view) {
        if (view == null || view.getViewId() == null) {
            XLogger.error("Cannot register ChestView with null ID");
            return;
        }
        views.put(view.getViewId(), view);
    }

    public @NotNull ChestView getViewOf(Player viewOwner) {
        UUID viewId = viewOwner.getUniqueId();
        if (views.containsKey(viewId)) {
            if (views.get(viewId) instanceof ChestListView) {
                return new ChestView(viewOwner);
            } else {
                return views.get(viewId).clearButtons().clearLayout();
            }
        } else {
            return new ChestView(viewOwner);
        }
    }

    public @NotNull ChestListView getListViewOf(Player viewOwner) {
        return new ChestListView(viewOwner);
    }

    @EventHandler
    public void unregisterView(PlayerQuitEvent view) {
        UUID viewId = view.getPlayer().getUniqueId();
        if (views.containsKey(viewId)) {
            XLogger.debug("Unregistering ChestView for player: " + view.getPlayer().getName());
            views.remove(viewId);
        }
    }

    @EventHandler
    public void playerClickItem(InventoryClickEvent event) {
        try {
            if (event.getClickedInventory() == null || event.getCurrentItem() == null) return;

            ItemStack item = event.getCurrentItem();
            if (!hasTag(item)) return;

            event.setCancelled(true);

            UUID viewId = getViewId(item);
            if (viewId == null) return;
            ChestView view = views.get(viewId);
            if (view == null) {
                XLogger.error("ChestView not found for ID: " + viewId);
                return;
            }
            view.handleClick(event.getSlot(), event.getClick());
        } catch (Exception e) {
            XLogger.error(e);
            event.setCancelled(true);   // safeguard against any exceptions during click handling
        }
    }


    public static ItemStack attachTag(@Nullable UUID viewId, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(tag, PersistentDataType.STRING, "chest_view");
            if (viewId != null)
                meta.getPersistentDataContainer().set(id, PersistentDataType.STRING, viewId.toString());
            item.setItemMeta(meta);
        } else {
            throw new IllegalStateException("ItemMeta cannot be null for cui item: " + item);
        }
        return item;
    }

    public static boolean hasTag(ItemStack item) {
        if (item == null || item.getItemMeta() == null) {
            XLogger.debug("Item is not a valid CUI item: " + item);
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer().has(tag, org.bukkit.persistence.PersistentDataType.STRING);
    }

    public static @Nullable UUID getViewId(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.getPersistentDataContainer().has(id, PersistentDataType.STRING)) {
            String idString = meta.getPersistentDataContainer().get(id, PersistentDataType.STRING);
            if (idString == null || idString.isEmpty()) {
                XLogger.debug("View ID is null or empty for item: " + item);
                return null;
            }
            XLogger.debug("View ID: " + idString);
            return UUID.fromString(idString);
        } else {
            XLogger.debug("ItemMeta cannot be null for cui item: " + item);
            return null;
        }
    }

}
