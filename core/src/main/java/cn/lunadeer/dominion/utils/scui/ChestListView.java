package cn.lunadeer.dominion.utils.scui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

public class ChestListView extends ChestView {

    public ChestListView(@NotNull Player viewOwner) {
        super(viewOwner);
    }

    protected void refresh(@NotNull InventoryView view){
        // generate the buttons
        super.refresh(view);
    }

}
