package cn.lunadeer.dominion.configuration;

import cn.lunadeer.dominion.uis.AllDominion;
import cn.lunadeer.dominion.uis.MainMenu;
import cn.lunadeer.dominion.utils.configuration.ConfigurationFile;
import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;
import cn.lunadeer.dominion.utils.configuration.HandleManually;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ChestUserInterface extends ConfigurationFile {

    public static class ButtonConfiguration extends ConfigurationPart {

        @HandleManually
        public ButtonConfiguration(char symbol, Material material, String name, List<String> lore) {
            this.symbol = String.valueOf(symbol);
            this.name = name;
            this.lore = lore;
            this.material = material.name();
        }

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

        public String symbol;
        public String name;
        public List<String> lore;
        public String material;
    }

    public static MainMenu.MainMenuCui mainMenuCui = new MainMenu.MainMenuCui();
    public static AllDominion.AllDominionCui allDominionCui = new AllDominion.AllDominionCui();
}
