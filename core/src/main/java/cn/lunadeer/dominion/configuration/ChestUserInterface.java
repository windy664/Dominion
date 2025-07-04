package cn.lunadeer.dominion.configuration;

import cn.lunadeer.dominion.uis.AllDominion;
import cn.lunadeer.dominion.uis.MainMenu;
import cn.lunadeer.dominion.utils.configuration.ConfigurationFile;
import cn.lunadeer.dominion.utils.configuration.PostProcess;

public class ChestUserInterface extends ConfigurationFile {
    public static MainMenu.MainMenuCui mainMenuCui = new MainMenu.MainMenuCui();
    public static AllDominion.AllDominionCui allDominionCui = new AllDominion.AllDominionCui();


    @PostProcess
    public void postProcess() {
        // todo download head data
    }
}
