package cn.lunadeer.dominion.utils.scui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static cn.lunadeer.dominion.utils.scui.ChestUserInterface.attachTag;
import static cn.lunadeer.dominion.utils.scui.ChestUserInterface.hasTag;

public class ChestView {

    private final Player viewOwner;
    private String title;
    private final Map<Integer, ChestButton> buttons = new HashMap<>();

    public ChestView(@NotNull Player viewOwner) {
        this.viewOwner = viewOwner;
        this.title = "Default Title"; // Set a default title
        ChestUserInterface.getInstance().registerView(this);
    }

    public ChestView setTitle(@NotNull String title) {
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        this.title = title;
        return this;
    }

    public ChestView clearButtons() {
        buttons.clear();
        return this;
    }

    public ChestView setButton(int slot, @NotNull ChestButton button) {
        if (slot < 0 || slot >= 54) {
            throw new IllegalArgumentException("Slot must be between 0 and 53.");
        }
        buttons.put(slot, button);
        return this;
    }

    /**
     * Sets a button at the specified row and column in the chest view.
     *
     * @param row    Row index (0-5)
     * @param column Column index (0-8)
     * @param button The ChestButton to set at the specified position.
     * @return The current ChestView instance for method chaining.
     */
    public ChestView setButton(int row, int column, @NotNull ChestButton button) {
        if (row < 0 || row >= 6 || column < 0 || column >= 9) {
            throw new IllegalArgumentException("Row must be between 0 and 5 and column must be between 0 and 8.");
        }
        return setButton(row * 9 + column, button);
    }

    public ChestView setButtons(@NotNull Map<Integer, ChestButton> buttons) {
        for (Map.Entry<Integer, ChestButton> entry : buttons.entrySet()) {
            setButton(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public void open() {
        ItemStack firstItem = viewOwner.getOpenInventory().getItem(0);
        if (hasTag(firstItem)) {
            refresh(viewOwner.getOpenInventory());
        } else {
            create();
        }
    }

    private void create() {
        Inventory view = Bukkit.createInventory(viewOwner, 54, "");
        InventoryView inventoryView = viewOwner.openInventory(view);
        if (inventoryView == null) {
            throw new IllegalStateException("Failed to open inventory for player: " + viewOwner.getName());
        }
        refresh(inventoryView);
    }

    protected void refresh(@NotNull InventoryView view) {
        view.setTitle(title);
        for (Map.Entry<Integer, ChestButton> entry : buttons.entrySet()) {
            int slot = entry.getKey();
            ChestButton button = entry.getValue();
            if (button != null) {
                ItemStack item = attachTag(viewOwner.getUniqueId(), button.build());
                view.setItem(slot, item);
            } else {
                view.setItem(slot, null);
            }
        }
        // Fill empty slots with placeholder items
        for (int i = 0; i < 54; i++) {
            if (view.getItem(i) != null) continue; // Skip if item is already set
            view.setItem(i, ChestUserInterface.PLACE_HOLDER_ITEM);
        }
    }

    public UUID getViewId() {
        return viewOwner.getUniqueId();
    }

    public void handleClick(Integer slot, ClickType type) {
        ChestButton button = buttons.get(slot);
        if (button != null) {
            button.onClick(type);
        } else {
            viewOwner.sendMessage("§cNo action assigned to this slot.");
        }
    }


}
