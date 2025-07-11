package cn.lunadeer.dominion.uis.dominion.manage.member;

import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.dtos.MemberDTO;
import cn.lunadeer.dominion.api.dtos.flag.Flags;
import cn.lunadeer.dominion.api.dtos.flag.PriFlag;
import cn.lunadeer.dominion.commands.MemberCommand;
import cn.lunadeer.dominion.configuration.ChestUserInterface;
import cn.lunadeer.dominion.configuration.Language;
import cn.lunadeer.dominion.misc.CommandArguments;
import cn.lunadeer.dominion.uis.AbstractUI;
import cn.lunadeer.dominion.uis.MainMenu;
import cn.lunadeer.dominion.uis.dominion.DominionList;
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
import cn.lunadeer.dominion.utils.stui.components.buttons.FunctionalButton;
import cn.lunadeer.dominion.utils.stui.components.buttons.ListViewButton;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

import static cn.lunadeer.dominion.Dominion.defaultPermission;
import static cn.lunadeer.dominion.misc.Converts.*;
import static cn.lunadeer.dominion.utils.Misc.*;


public class MemberSetting extends AbstractUI {

    public static void show(CommandSender sender, String dominionName, String playerName, String pageStr) {
        new MemberSetting().displayByPreference(sender, dominionName, playerName, pageStr);
    }

    public static SecondaryCommand setting = new SecondaryCommand("member_setting", List.of(
            new CommandArguments.RequiredDominionArgument(),
            new CommandArguments.RequiredPlayerArgument()
    )) {
        @Override
        public void executeHandler(CommandSender sender) {
            show(sender, getArgumentValue(0), getArgumentValue(1), "1");
        }
    }.needPermission(defaultPermission).register();

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ TUI ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static class MemberSettingTuiText extends ConfigurationPart {
        public String title = "{0} Member Setting";
        public String description = "Set member's privilege of dominion.";
        public String button = "SETTING";
    }

    public static ListViewButton button(CommandSender sender, String dominionName, String playerName) {
        return (ListViewButton) new ListViewButton(Language.memberSettingTuiText.button) {
            @Override
            public void function(String pageStr) {
                show(sender, dominionName, playerName, pageStr);
            }
        }.needPermission(defaultPermission).setHoverText(Language.memberSettingTuiText.description);
    }

    @Override
    protected void showTUI(CommandSender sender, String... args) throws Exception {
        String dominionName = args[0];
        String playerName = args[1];
        String pageStr = args.length > 2 ? args[2] : "1";
        DominionDTO dominion = toDominionDTO(dominionName);
        MemberDTO member = toMemberDTO(dominion, playerName);
        int page = toIntegrity(pageStr);
        ListView view = ListView.create(10, button(sender, dominionName, playerName));
        view.title(formatString(Language.memberSettingTuiText.title, playerName));
        view.navigator(
                Line.create()
                        .append(MainMenu.button(sender).build())
                        .append(DominionList.button(sender).build())
                        .append(DominionManage.button(sender, dominionName).build())
                        .append(MemberList.button(sender, dominionName).build())
                        .append(Language.memberSettingTuiText.button)
        );
        view.add(Line.create().append(SelectTemplate.button(sender, dominionName, playerName).build()));
        if (member.getFlagValue(Flags.ADMIN)) {
            view.add(createOption(sender, Flags.ADMIN, true, playerName, dominion.getName(), page));
            view.add(createOption(sender, Flags.GLOW, member.getFlagValue(Flags.GLOW), playerName, dominion.getName(), page));
        } else {
            for (PriFlag flag : Flags.getAllPriFlagsEnable()) {
                view.add(createOption(sender, flag, member.getFlagValue(flag), playerName, dominion.getName(), page));
            }
        }
        view.showOn(sender, page);
    }

    private static Line createOption(CommandSender sender, PriFlag flag, boolean value, String player_name, String dominion_name, int page) {
        if (value) {
            return Line.create()
                    .append(new FunctionalButton("☑") {
                        @Override
                        public void function() {
                            MemberCommand.setMemberPrivilege(sender, dominion_name, player_name, flag.getFlagName(), "false", String.valueOf(page));
                        }
                    }.needPermission(defaultPermission).green().build())
                    .append(Component.text(flag.getDisplayName()).hoverEvent(Component.text(flag.getDescription()))
                    );
        } else {
            return Line.create()
                    .append(new FunctionalButton("☐") {
                        @Override
                        public void function() {
                            MemberCommand.setMemberPrivilege(sender, dominion_name, player_name, flag.getFlagName(), "true", String.valueOf(page));
                        }
                    }.needPermission(defaultPermission).red().build())
                    .append(Component.text(flag.getDisplayName()).hoverEvent(Component.text(flag.getDescription()))
                    );
        }
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ TUI ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ CUI ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static class MemberSettingCui extends ConfigurationPart {
        public String title = "§6✦ §f§lMember {0} Settings §6✦";
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
                        "member list."
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
    protected void showCUI(Player player, String... args) throws Exception {
        DominionDTO dominion = toDominionDTO(args[0]);
        MemberDTO member = toMemberDTO(dominion, args[1]);
        ChestListView view = ChestUserInterfaceManager.getInstance().getListViewOf(player);
        view.setTitle(formatString(ChestUserInterface.memberSettingCui.title, args[1]));
        view.applyListConfiguration(ChestUserInterface.memberSettingCui.listConfiguration, toIntegrity(args[2]));

        view.setButton(ChestUserInterface.memberSettingCui.backButton.getSymbol(),
                new ChestButton(ChestUserInterface.memberSettingCui.backButton) {
                    @Override
                    public void onClick(ClickType type) {
                        MemberList.show(player, dominion.getName(), "1");
                    }
                }
        );

        for (int i = 0; i < Flags.getAllPriFlagsEnable().size(); i++) {
            PriFlag flag = Flags.getAllPriFlagsEnable().get(i);
            Integer page = (int) Math.ceil((double) (i + 1) / view.getPageSize());
            String flagState = member.getFlagValue(flag) ? ChestUserInterface.memberSettingCui.flagItemStateTrue : ChestUserInterface.memberSettingCui.flagItemStateFalse;
            String flagName = formatString(ChestUserInterface.memberSettingCui.flagItemName, flag.getDisplayName());
            List<String> descriptions = foldLore2Line(flag.getDescription(), 30);
            List<String> flagLore = formatStringList(ChestUserInterface.memberSettingCui.flagItemLore, flagState, descriptions.get(0), descriptions.get(1));
            ButtonConfiguration btnConfig = ButtonConfiguration.createMaterial(
                    ChestUserInterface.memberSettingCui.listConfiguration.itemSymbol.charAt(0),
                    flag.getMaterial(),
                    flagName,
                    flagLore
            );
            view.addItem(new ChestButton(btnConfig) {
                @Override
                public void onClick(ClickType type) {
                    boolean newValue = !member.getFlagValue(flag);
                    MemberCommand.setMemberPrivilege(player, dominion.getName(), args[1], flag.getFlagName(), String.valueOf(newValue), String.valueOf(page));
                }
            });
        }

        view.open();
    }
}
