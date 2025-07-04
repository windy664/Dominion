package cn.lunadeer.dominion.utils.scui.configuration;

import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;
import cn.lunadeer.dominion.utils.configuration.HandleManually;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

import static cn.lunadeer.dominion.utils.Misc.formatString;

public class ButtonConfiguration extends ConfigurationPart {


    @HandleManually
    public ButtonConfiguration(char symbol, Material material, String name, List<String> lore) {
        this.symbol = String.valueOf(symbol);
        this.name = name;
        this.lore = lore;
        this.material = material.name();
    }

//    @HandleManually
//    public ButtonConfiguration(char symbol, String headName, String name, List<String> lore) {
//        this.symbol = String.valueOf(symbol);
//        this.name = name;
//        this.lore = lore;
//        this.material = "HEAD(" + headName + ")";
//    }

    @HandleManually
    public Material getMaterial() {
        return Material.valueOf(material.toUpperCase());
    }

    @HandleManually
    public char getSymbol() {
        return symbol.charAt(0);
    }

    @HandleManually
    public List<String> getLore(Object... args) {
        List<String> formatStr = new ArrayList<>(lore);
        for (int i = 0; i < args.length; i++) {
            for (int j = 0; j < formatStr.size(); j++) {
                formatStr.set(j, formatStr.get(j).replace("{" + i + "}", args[i].toString()));
            }
        }
        return formatStr;
    }

    @HandleManually
    public String getName(Object... args) {
        return formatString(name, args);
    }

    public String symbol;
    public String name;
    public List<String> lore;
    public String material;
}
