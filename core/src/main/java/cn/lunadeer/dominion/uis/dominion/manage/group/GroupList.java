package cn.lunadeer.dominion.uis.dominion.manage.group;

import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.dtos.GroupDTO;
import cn.lunadeer.dominion.api.dtos.MemberDTO;
import cn.lunadeer.dominion.api.dtos.PlayerDTO;
import cn.lunadeer.dominion.commands.GroupCommand;
import cn.lunadeer.dominion.configuration.ChestUserInterface;
import cn.lunadeer.dominion.configuration.Language;
import cn.lunadeer.dominion.doos.GroupDOO;
import cn.lunadeer.dominion.inputters.CreateGroupInputter;
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
import cn.lunadeer.dominion.utils.stui.components.buttons.Button;
import cn.lunadeer.dominion.utils.stui.components.buttons.FunctionalButton;
import cn.lunadeer.dominion.utils.stui.components.buttons.ListViewButton;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

import static cn.lunadeer.dominion.Dominion.defaultPermission;
import static cn.lunadeer.dominion.doos.MemberDOO.selectByDominionId;
import static cn.lunadeer.dominion.misc.Asserts.assertDominionAdmin;
import static cn.lunadeer.dominion.misc.Converts.*;
import static cn.lunadeer.dominion.utils.Misc.formatString;

public class GroupList extends AbstractUI {

    public static void show(CommandSender sender, String dominionName, String pageStr) {
        new GroupList().displayByPreference(sender, dominionName, pageStr);
    }

    public static SecondaryCommand list = new SecondaryCommand("group_list", List.of(
            new CommandArguments.RequiredDominionArgument(),
            new CommandArguments.OptionalPageArgument()
    )) {
        @Override
        public void executeHandler(CommandSender sender) {
            show(sender, getArgumentValue(0), getArgumentValue(1));
        }
    }.needPermission(defaultPermission).register();

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ TUI ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static class GroupListTuiText extends ConfigurationPart {
        public String title = "{0} Group List";
        public String description = "List of groups of this dominion.";
        public String button = "GROUPS";
        public String deleteButton = "DELETE";
        public String deleteDescription = "Delete this group, all members will be move out of this group.";
        public String removeMemberDescription = "Remove {0} from group {1} to default group.";
    }

    public static ListViewButton button(CommandSender sender, String dominionName) {
        return (ListViewButton) new ListViewButton(Language.groupListTuiText.button) {
            @Override
            public void function(String page) {
                show(sender, dominionName, page);
            }
        }.needPermission(defaultPermission);
    }

    @Override
    protected void showTUI(CommandSender sender, String... args) throws Exception {
        String dominionName = args[0];
        String pageStr = args[1];
        DominionDTO dominion = toDominionDTO(dominionName);
        assertDominionAdmin(sender, dominion);
        int page = toIntegrity(pageStr);

        List<GroupDOO> groups = GroupDOO.selectByDominionId(dominion.getId());

        ListView view = ListView.create(10, button(sender, dominionName));
        view.title(formatString(Language.groupListTuiText.title, dominion.getName()));
        view.navigator(
                Line.create()
                        .append(MainMenu.button(sender).build())
                        .append(DominionList.button(sender).build())
                        .append(DominionManage.button(sender, dominionName).build())
                        .append(Language.groupListTuiText.button)
        );
        view.add(new Line()
                .append(CreateGroupInputter.createTuiButtonOn(sender, dominionName).needPermission(defaultPermission).build())
        );

        // get data from database directly because cache update may not be in time
        List<MemberDTO> members = new ArrayList<>(selectByDominionId(dominion.getId()));
        for (GroupDTO group : groups) {
            Line line = new Line();
            Button deleteGroup = new FunctionalButton(Language.groupListTuiText.deleteButton) {
                @Override
                public void function() {
                    GroupCommand.deleteGroup(sender, dominionName, group.getNamePlain(), pageStr);
                }
            }.needPermission(defaultPermission).red().setHoverText(Language.groupListTuiText.deleteDescription);
            Button setting = GroupSetting.button(sender, dominionName, group.getNamePlain());
            Button addMember = SelectMember.button(sender, dominionName, group.getNamePlain(), pageStr);
            line.append(deleteGroup.build()).append(setting.build()).append(group.getNameColoredComponent()).append(addMember.build());
            view.add(line);
            for (MemberDTO member : members) {
                if (!member.getGroupId().equals(group.getId())) {
                    continue;
                }
                PlayerDTO p = toPlayerDTO(member.getPlayerUUID());
                Button remove = new FunctionalButton("-") {
                    @Override
                    public void function() {
                        GroupCommand.removeMember(sender, dominionName, group.getNamePlain(), p.getLastKnownName(), pageStr);
                    }
                }.needPermission(defaultPermission).red().setHoverText(Language.groupListTuiText.removeMemberDescription);
                Line playerLine = new Line().setDivider("");
                playerLine.append(Component.text("        "));
                playerLine.append(remove.build()).append(" |  " + p.getLastKnownName());
                view.add(playerLine);
            }
            view.add(new Line().append(""));
        }

        view.showOn(sender, page);
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ TUI ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ CUI ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static class GroupListCui extends ConfigurationPart {
        public String title = "Group List of {0}";
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
                        "&fGo back to the",
                        "&fdominion manage."
                )
        );

        public ButtonConfiguration newGroupButton = ButtonConfiguration.createMaterial(
                'i', Material.GREEN_DYE,
                "&a&lNew Group",
                List.of(
                        "&fCreate a new group",
                        "&ffor this dominion."
                )
        );

        public ButtonConfiguration groupItemButton = ButtonConfiguration.createMaterial(
                'i', Material.CHEST,
                "&fGroup: &9{0}",
                List.of(
                        "&fMembers: &a{0}",
                        "",
                        "&fClick to manage edit",
                        "&fthis group's flags."
                )
        );
    }

    @Override
    protected void showCUI(Player player, String... args) throws Exception {
        String dominionName = args[0];
        DominionDTO dominion = toDominionDTO(dominionName);
        assertDominionAdmin(player, dominion);
        ChestListView view = ChestUserInterfaceManager.getInstance().getListViewOf(player);
        view.setTitle(formatString(ChestUserInterface.groupListCui.title, dominion.getName()));
        view.applyListConfiguration(ChestUserInterface.groupListCui.listConfiguration, toIntegrity(args[1]));

        view.setButton(ChestUserInterface.groupListCui.backButton.getSymbol(),
                new ChestButton(ChestUserInterface.groupListCui.backButton) {
                    @Override
                    public void onClick(ClickType type) {
                        DominionManage.show(player, dominion.getName(), "1");
                    }
                }
        );

        view.addItem(new ChestButton(ChestUserInterface.groupListCui.newGroupButton) {
            @Override
            public void onClick(ClickType type) {
                CreateGroupInputter.createOn(player, dominion.getName());
                view.close();
            }
        });

        for (GroupDTO group : dominion.getGroups()) {
            ChestButton groupChest = new ChestButton(ChestUserInterface.groupListCui.groupItemButton) {
                @Override
                public void onClick(ClickType type) {
                    GroupManage.show(player, dominion.getName(), group.getNamePlain(), "1");
                }
            }.setDisplayNameArgs(group.getNameColoredBukkit()).setLoreArgs(group.getMembers().size());
            view.addItem(groupChest);
        }

        view.open();
    }

}
