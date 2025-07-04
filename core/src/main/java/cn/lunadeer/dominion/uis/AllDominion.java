package cn.lunadeer.dominion.uis;

import cn.lunadeer.dominion.cache.CacheManager;
import cn.lunadeer.dominion.configuration.ChestUserInterface;
import cn.lunadeer.dominion.configuration.Language;
import cn.lunadeer.dominion.misc.CommandArguments;
import cn.lunadeer.dominion.utils.Notification;
import cn.lunadeer.dominion.utils.command.SecondaryCommand;
import cn.lunadeer.dominion.utils.configuration.Comments;
import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;
import cn.lunadeer.dominion.utils.scui.ChestListView;
import cn.lunadeer.dominion.utils.scui.ChestUserInterfaceManager;
import cn.lunadeer.dominion.utils.stui.ListView;
import cn.lunadeer.dominion.utils.stui.components.Line;
import cn.lunadeer.dominion.utils.stui.components.buttons.ListViewButton;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        try {
            int page = toIntegrity(args[0]);
            ListView view = ListView.create(10, button(sender));

            view.title(Language.allDominionTuiText.title);
            view.navigator(Line.create()
                    .append(MainMenu.button(sender).build())
                    .append(Language.allDominionTuiText.button));
            view.addLines(BuildTreeLines(sender, CacheManager.instance.getCache().getDominionCache().getAllDominionNodes(), 0));
            view.showOn(sender, page);
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ TUI ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ CUI ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static class AllDominionCui extends ConfigurationPart {
        public String title = "All Dominions of Server";
        @Comments({
                "This is a list view, so 'i'(item), 'p'(previous page), 'n'(next page) are necessary.",
                "These three symbols are unchangeable. But you can change the position of them.",
        })
        public List<String> layout = List.of(
                "#########",
                "#iiiiiii#",
                "#iiiiiii#",
                "#iiiiiii#",
                "#p#####n#"
        );
    }

    @Override
    protected void showCUI(Player player, String... args) {
        ChestListView view = ChestUserInterfaceManager.getInstance().getListViewOf(player);
        view.setTitle(ChestUserInterface.allDominionCui.title);
        view.setLayout(ChestUserInterface.allDominionCui.layout);
    }
}
