package cn.lunadeer.dominion.utils.scui;

import cn.lunadeer.dominion.utils.ColorParser;
import cn.lunadeer.dominion.utils.scui.configuration.ButtonConfiguration;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static cn.lunadeer.dominion.utils.Misc.formatString;
import static cn.lunadeer.dominion.utils.Misc.setPlaceholder;

public abstract class ChestButton {

    private final ItemStack item;
    private String displayName;
    private List<String> lore;

    public ChestButton(ButtonConfiguration config) {
        if (config.material.contains(";")) {
            String[] parts = config.material.split(";");
            if (parts.length == 3 && parts[0].equalsIgnoreCase("PLAYER_HEAD")) {
                // Handle PLAYER_HEAD with B64 or URL
                this.item = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) this.item.getItemMeta();
                String textureValue;
                if (parts[1].equalsIgnoreCase("B64")) {
                    textureValue = parts[2];
                } else if (parts[1].equalsIgnoreCase("URL")) {
                    textureValue = "{\"textures\":{\"SKIN\":{\"url\":\"" + parts[2] + "\"}}}";
                    textureValue = Base64.getEncoder().encodeToString(textureValue.getBytes());
                } else {
                    throw new IllegalArgumentException("Invalid PLAYER_HEAD texture type: " + parts[1] +
                            ". Expected 'B64' or 'URL'.");
                }
                PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), null);
                profile.setProperty(new ProfileProperty("textures", textureValue));
                meta.setPlayerProfile(profile);
                this.item.setItemMeta(meta);
            } else {
                throw new IllegalArgumentException("Invalid material type: " + config.material);
            }
        } else {
            this.item = new ItemStack(Material.valueOf(config.material));
        }
        this.displayName = config.name;
        this.lore = config.lore;
    }

    /**
     * Sets the display name of the button with formatted arguments.
     * The arguments are applied to placeholders in the display name.
     *
     * @param args The arguments to format the display name.
     * @return The current instance of `ChestButton` for method chaining.
     */
    public ChestButton setDisplayNameArgs(Object... args) {
        this.displayName = formatString(displayName, args);
        return this;
    }

    /**
     * Sets the lore of the button with formatted arguments.
     * Each argument replaces corresponding placeholders in the lore strings.
     *
     * @param args The arguments to format the lore.
     * @return The current instance of `ChestButton` for method chaining.
     */
    public ChestButton setLoreArgs(Object... args) {
        for (int i = 0; i < args.length; i++) {
            for (int j = 0; j < lore.size(); j++) {
                lore.set(j, lore.get(j).replace("{" + i + "}", args[i].toString()));
            }
        }
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
