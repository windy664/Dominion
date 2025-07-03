package cn.lunadeer.dominion.uis.cuis;

import cn.lunadeer.dominion.commands.DominionCreateCommand;
import cn.lunadeer.dominion.configuration.Language;
import cn.lunadeer.dominion.uis.tuis.dominion.DominionList;
import cn.lunadeer.dominion.utils.scui.ChestButton;
import cn.lunadeer.dominion.utils.scui.ChestUserInterfaceManager;
import cn.lunadeer.dominion.utils.scui.ChestView;
import cn.lunadeer.dominion.utils.stui.inputter.InputterRunner;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class MainMenu {

    public static void show(Player player, Integer page) {
        ChestView view = ChestUserInterfaceManager.getInstance().getViewOf(player);

        view.setLayout(
                "#########",
                "##A#B#C##",
                "#########"
        );
        view.setButton('A', new ChestButton(Material.GRASS_BLOCK) {
            @Override
            public void onClick(ClickType type) {
                new InputterRunner(player, Language.createDominionInputterText.hint) {
                    @Override
                    public void run(String input) {
                        DominionCreateCommand.autoCreate(player, input);
                        DominionList.show(player, "1");
                    }
                };
            }
        }.setDisplayName("Create Dominion")
                .setLore("Create a new dominion around you."));

        view.open();
    }


}
