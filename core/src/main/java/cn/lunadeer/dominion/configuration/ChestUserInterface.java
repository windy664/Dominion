package cn.lunadeer.dominion.configuration;

import cn.lunadeer.dominion.uis.AllDominion;
import cn.lunadeer.dominion.uis.MainMenu;
import cn.lunadeer.dominion.utils.configuration.ConfigurationFile;
import cn.lunadeer.dominion.utils.configuration.Heads;

@Heads({
        "Brief Description:",
        "    This file defines the user interface for the chest GUI in Dominion.",
        "    Both name and lore support PlaceholderAPI and color codes.",
        "",
        "For list view layout:",
        "    You can change the number or position of the item symbol to control how",
        "    many and where items are displayed.",
        "",
        "For button material:",
        "    Use the material name in uppercase, e.g., STONE, DIAMOND_SWORD.",
        "    If you want to use player heads with textures, we support two formats:",
        "        1. PLAYER_HEAD;B64;{base64 texture value}",
        "        2. PLAYER_HEAD;URL;{texture image URL}",
})
public class ChestUserInterface extends ConfigurationFile {
    public static MainMenu.MainMenuCui mainMenuCui = new MainMenu.MainMenuCui();
    public static AllDominion.AllDominionCui allDominionCui = new AllDominion.AllDominionCui();
}
