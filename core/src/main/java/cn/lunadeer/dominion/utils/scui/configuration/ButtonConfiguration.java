package cn.lunadeer.dominion.utils.scui.configuration;

import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;
import cn.lunadeer.dominion.utils.configuration.HandleManually;
import org.bukkit.Material;

import java.net.URL;
import java.util.List;

public class ButtonConfiguration extends ConfigurationPart {
    /**
     * Constructs a ButtonConfiguration with a material type.
     *
     * @param symbol   The character symbol representing the button.
     * @param material The material type of the button.
     * @param name     The display name of the button.
     * @param lore     The lore (description) of the button.
     */
    @HandleManually
    public ButtonConfiguration(char symbol, Material material, String name, List<String> lore) {
        this.symbol = String.valueOf(symbol);
        this.name = name;
        this.lore = lore;
        this.material = material.name();
    }

    /**
     * Constructs a ButtonConfiguration with player head texture in specific texture format.
     *
     * @param symbol     The character symbol representing the button.
     * @param textureB64 The base64-encoded profile texture value.
     * @param name       The display name of the button.
     * @param lore       The lore (description) of the button.
     */
    @HandleManually
    public ButtonConfiguration(char symbol, String textureB64, String name, List<String> lore) {
        this.symbol = String.valueOf(symbol);
        this.name = name;
        this.lore = lore;
        this.material = "PLAYER_HEAD;B64;" + textureB64;
    }

    /**
     * Constructs a ButtonConfiguration with player head texture in specific texture format.
     *
     * @param symbol  The character symbol representing the button.
     * @param skinUrl The URL of the skin image.
     * @param name    The display name of the button.
     * @param lore    The lore (description) of the button.
     */
    @HandleManually
    public ButtonConfiguration(char symbol, URL skinUrl, String name, List<String> lore) {
        this.symbol = String.valueOf(symbol);
        this.name = name;
        this.lore = lore;
        this.material = "PLAYER_HEAD;URL;" + skinUrl.toString();
    }

    @HandleManually
    public char getSymbol() {
        return symbol.charAt(0);
    }

    public String symbol;
    public String name;
    public List<String> lore;
    public String material;
}
