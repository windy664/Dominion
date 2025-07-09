package cn.lunadeer.dominion.uis.dominion.manage;

import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.dtos.flag.Flags;
import cn.lunadeer.dominion.api.dtos.flag.PriFlag;
import cn.lunadeer.dominion.commands.DominionFlagCommand;
import cn.lunadeer.dominion.configuration.ChestUserInterface;
import cn.lunadeer.dominion.configuration.Language;
import cn.lunadeer.dominion.uis.AbstractUI;
import cn.lunadeer.dominion.uis.MainMenu;
import cn.lunadeer.dominion.uis.dominion.DominionList;
import cn.lunadeer.dominion.uis.dominion.DominionManage;
import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;
import cn.lunadeer.dominion.utils.scui.ChestButton;
import cn.lunadeer.dominion.utils.scui.ChestListView;
import cn.lunadeer.dominion.utils.scui.ChestUserInterfaceManager;
import cn.lunadeer.dominion.utils.scui.configuration.ButtonConfiguration;
import cn.lunadeer.dominion.utils.scui.configuration.ListViewConfiguration;
import cn.lunadeer.dominion.utils.stui.ListView;
import cn.lunadeer.dominion.utils.stui.components.Line;
import cn.lunadeer.dominion.utils.stui.components.buttons.FunctionalButton;
import cn.lunadeer.dominion.utils.stui.components.buttons.ListViewButton;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

import static cn.lunadeer.dominion.Dominion.defaultPermission;
import static cn.lunadeer.dominion.misc.Asserts.assertDominionAdmin;
import static cn.lunadeer.dominion.misc.Converts.toDominionDTO;
import static cn.lunadeer.dominion.misc.Converts.toIntegrity;
import static cn.lunadeer.dominion.utils.Misc.*;

public class GuestSetting extends AbstractUI {

    public static void show(CommandSender sender, String dominionName, String pageStr) {
        new GuestSetting().displayByPreference(sender, dominionName, pageStr);
    }

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ TUI ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static class GuestSettingTuiText extends ConfigurationPart {
        public String title = "{0} Guest Setting";
        public String button = "GUEST SET";
        public String description = "Set guest behavior of dominion.";
    }

    public static ListViewButton button(CommandSender sender, String dominionName) {
        return (ListViewButton) new ListViewButton(Language.guestSettingTuiText.button) {
            @Override
            public void function(String pageStr) {
                show(sender, dominionName, pageStr);
            }
        }.needPermission(defaultPermission);
    }

    @Override
    protected void showTUI(CommandSender sender, String... args) {
        String dominionName = args[0];
        DominionDTO dominion = toDominionDTO(dominionName);
        assertDominionAdmin(sender, dominion);
        int page = toIntegrity(args[1]);

        ListView view = ListView.create(10, button(sender, dominionName));
        view.title(formatString(Language.guestSettingTuiText.title, dominion.getName()))
                .navigator(Line.create()
                        .append(MainMenu.button(sender).build())
                        .append(DominionList.button(sender).build())
                        .append(DominionManage.button(sender, dominionName).build())
                        .append(Language.guestSettingTuiText.button));
        for (PriFlag flag : Flags.getAllPriFlagsEnable()) {
            if (flag.equals(Flags.ADMIN)) continue; // Skip admin flag this only for group or member
            if (dominion.getGuestFlagValue(flag)) {
                view.add(Line.create()
                        .append(new FunctionalButton("☑") {
                            @Override
                            public void function() {
                                DominionFlagCommand.setGuest(sender, dominionName, flag.getFlagName(), "false", String.valueOf(page));
                            }
                        }.green().build())
                        .append(Component.text(flag.getDisplayName()).hoverEvent(Component.text(flag.getDescription())))
                );
            } else {
                view.add(Line.create()
                        .append(new FunctionalButton("☐") {
                            @Override
                            public void function() {
                                DominionFlagCommand.setGuest(sender, dominionName, flag.getFlagName(), "true", String.valueOf(page));
                            }
                        }.red().build())
                        .append(Component.text(flag.getDisplayName()).hoverEvent(Component.text(flag.getDescription())))
                );
            }
        }
        view.showOn(sender, page);
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ TUI ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ CUI ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static class GuestSettingCui extends ConfigurationPart {
        public String title = "Guest Privilege of {0}";
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

        public ButtonConfiguration backButton = ButtonConfiguration.createMaterial(
                '<', Material.RED_STAINED_GLASS_PANE,
                "Back",
                List.of(
                        "Go back to the",
                        "dominion manage."
                )
        );

        public String flagItemName = "&7Flag: &9{0}";
        public String flagItemStateTrue = "&a&l[ENABLED]";
        public String flagItemStateFalse = "&c&l[DISABLED]";
        public List<String> flagItemLore = List.of(
                "&7State: {0}",
                "&7Des: &f{1}",
                "       &f{2}",
                "",
                "&7Click to toggle this flag."
        );
    }

    @Override
    protected void showCUI(Player player, String... args) {
        DominionDTO dominion = toDominionDTO(args[0]);
        assertDominionAdmin(player, dominion);

        ChestListView view = ChestUserInterfaceManager.getInstance().getListViewOf(player);
        view.setTitle(formatString(ChestUserInterface.guestSettingCui.title, dominion.getName()));
        view.applyListConfiguration(ChestUserInterface.guestSettingCui.listConfiguration, toIntegrity(args[1]));

        view.setButton(ChestUserInterface.guestSettingCui.backButton.getSymbol(),
                new ChestButton(ChestUserInterface.guestSettingCui.backButton) {
                    @Override
                    public void onClick(ClickType type) {
                        DominionManage.show(player, dominion.getName(), "1");
                    }
                }
        );

        for (int i = 0; i < Flags.getAllPriFlagsEnable().size(); i++) {
            PriFlag flag = Flags.getAllPriFlagsEnable().get(i);
            if (flag.equals(Flags.ADMIN)) continue; // Skip admin flag this only for group or member
            Integer page = (int) Math.ceil((double) i / view.getPageSize());
            String flagState = dominion.getGuestFlagValue(flag) ? ChestUserInterface.guestSettingCui.flagItemStateTrue : ChestUserInterface.guestSettingCui.flagItemStateFalse;
            String flagName = formatString(ChestUserInterface.guestSettingCui.flagItemName, flag.getDisplayName());
            List<String> descriptions = foldLore2Line(flag.getDescription(), 30);
            List<String> flagLore = formatStringList(ChestUserInterface.guestSettingCui.flagItemLore, flagState, descriptions.get(0), descriptions.get(1));
            ButtonConfiguration btnConfig = ButtonConfiguration.createMaterial(
                    ChestUserInterface.guestSettingCui.listConfiguration.itemSymbol.charAt(0),
                    flag.getMaterial(),
                    flagName,
                    flagLore
            );
            view.addItem(new ChestButton(btnConfig) {
                @Override
                public void onClick(ClickType type) {
                    DominionFlagCommand.setGuest(player, dominion.getName(), flag.getFlagName(), String.valueOf(!dominion.getGuestFlagValue(flag)), page.toString());
                }
            });
        }

        view.open();
    }
}
