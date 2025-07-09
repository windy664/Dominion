package cn.lunadeer.dominion.uis;

import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.cache.CacheManager;
import cn.lunadeer.dominion.configuration.ChestUserInterface;
import cn.lunadeer.dominion.configuration.Language;
import cn.lunadeer.dominion.misc.CommandArguments;
import cn.lunadeer.dominion.uis.dominion.DominionManage;
import cn.lunadeer.dominion.utils.command.SecondaryCommand;
import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;
import cn.lunadeer.dominion.utils.scui.ChestButton;
import cn.lunadeer.dominion.utils.scui.ChestListView;
import cn.lunadeer.dominion.utils.scui.ChestUserInterfaceManager;
import cn.lunadeer.dominion.utils.scui.configuration.ButtonConfiguration;
import cn.lunadeer.dominion.utils.scui.configuration.ListViewConfiguration;
import cn.lunadeer.dominion.utils.stui.ListView;
import cn.lunadeer.dominion.utils.stui.components.Line;
import cn.lunadeer.dominion.utils.stui.components.buttons.ListViewButton;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

import static cn.lunadeer.dominion.Dominion.adminPermission;
import static cn.lunadeer.dominion.misc.Converts.toIntegrity;
import static cn.lunadeer.dominion.uis.dominion.DominionList.BuildTreeLines;

public class AllDominion extends AbstractUI {

    public static void show(CommandSender sender, String pageStr) {
        new AllDominion().displayByPreference(sender, pageStr);
    }

    public static SecondaryCommand listAll = new SecondaryCommand("list_all", List.of(
            new CommandArguments.OptionalPageArgument()
    )) {
        @Override
        public void executeHandler(CommandSender sender) {
            show(sender, getArgumentValue(0));
        }
    }.needPermission(adminPermission).register();

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ TUI ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static class AllDominionTuiText extends ConfigurationPart {
        public String title = "All Dominions";
        public String description = "List all dominions.";
        public String button = "LIST ALL";
    }

    public static ListViewButton button(CommandSender sender) {
        return (ListViewButton) new ListViewButton(Language.allDominionTuiText.button) {
            @Override
            public void function(String pageStr) {
                show(sender, pageStr);
            }
        }.needPermission(adminPermission);
    }

    @Override
    protected void showTUI(CommandSender sender, String... args) {
        int page = toIntegrity(args[0], 1);
        ListView view = ListView.create(10, button(sender));

        view.title(Language.allDominionTuiText.title);
        view.navigator(Line.create()
                .append(MainMenu.button(sender).build())
                .append(Language.allDominionTuiText.button));
        view.addLines(BuildTreeLines(sender, CacheManager.instance.getCache().getDominionCache().getAllDominionNodes(), 0));
        view.showOn(sender, page);
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ TUI ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ CUI ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static class AllDominionCui extends ConfigurationPart {
        public String title = "All Dominions";
        public ListViewConfiguration listConfiguration = new ListViewConfiguration(
                'i',
                List.of(
                        "<########",
                        "#iiiiiii#",
                        "#iiiiiii#",
                        "#iiiiiii#",
                        "p#######n"
                )
        );

        public ButtonConfiguration dominionItemButton = ButtonConfiguration.createMaterial(
                'i', Material.PAPER, "Name: {0}",
                List.of(
                        "Owner: {0}",
                        "Click to manage ",
                        "this dominion."
                )
        );

        public ButtonConfiguration backButton = ButtonConfiguration.createMaterial(
                '<', Material.RED_STAINED_GLASS_PANE,
                "Back",
                List.of(
                        "Go back to the",
                        "dominion list."
                )
        );
    }

    @Override
    protected void showCUI(Player player, String... args) {
        ChestListView view = ChestUserInterfaceManager.getInstance().getListViewOf(player);
        view.setTitle(ChestUserInterface.allDominionCui.title);
        view.applyListConfiguration(ChestUserInterface.allDominionCui.listConfiguration, toIntegrity(args[0]));

        List<DominionDTO> dominions = CacheManager.instance.getCache().getDominionCache().getAllDominions();
        for (DominionDTO dominion : dominions) {
            ChestButton btn = new ChestButton(ChestUserInterface.allDominionCui.dominionItemButton) {
                @Override
                public void onClick(ClickType type) {
                    DominionManage.show(player, dominion.getName(), "1");
                }
            };
            btn = btn.setDisplayNameArgs(dominion.getName());
            btn = btn.setLoreArgs(List.of(dominion.getOwnerDTO().getLastKnownName()));
            view = view.addItem(btn);
        }

        view.setButton(ChestUserInterface.allDominionCui.backButton.getSymbol(),
                new ChestButton(ChestUserInterface.allDominionCui.backButton) {
                    @Override
                    public void onClick(ClickType type) {
                        MainMenu.show(player, "1");
                    }
                }
        );

        view.open();
    }
}
