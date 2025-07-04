package cn.lunadeer.dominion.utils.scui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChestListView extends ChestView {

    public ChestListView(@NotNull Player viewOwner) {
        super(viewOwner);
    }

    private int pageSize = 45; // Default page size for a chest view
    private int currentPage = 1;
    private int itemCounter = 0;
    private int itemSymbolPosition = 0;

    @Override
    public ChestView setLayout(@NotNull String layout) {
        super.setLayout(layout);
        if (!this.getLayout().contains("i") || !this.getLayout().contains("p") || !this.getLayout().contains("n")) {
            throw new IllegalArgumentException("ChestListView requires layout to contain 'i', 'p', and 'n' for item, preview, and next buttons.");
        }
        this.pageSize = (int) this.getLayout().chars().filter(ch -> ch == 'i').count();
        return this;
    }

    public ChestView setCurrentPage(int page) {
        if (page < 1) {
            throw new IllegalArgumentException("Page number must be greater than 0.");
        }
        this.currentPage = page;
        return this;
    }

    public ChestView addItem(@NotNull ChestButton item) {
        // skip item not in the current page
        if (itemCounter < (currentPage - 1) * pageSize || itemCounter >= currentPage * pageSize) {
            itemCounter++;
            return this;
        }
        // find next available itemSymbolPosition
        while (itemSymbolPosition < this.getLayout().length() && this.getLayout().charAt(itemSymbolPosition) != 'i') {
            itemSymbolPosition++;
        }
        // setButton(int slot, @NotNull ChestButton button) from the first available itemSymbolPosition
        if (itemSymbolPosition >= this.getLayout().length()) {
            throw new IllegalStateException("No available item slot in the current layout.");
        }
        this.setButton(itemSymbolPosition, item);
        itemCounter++;
        return this;
    }

}
