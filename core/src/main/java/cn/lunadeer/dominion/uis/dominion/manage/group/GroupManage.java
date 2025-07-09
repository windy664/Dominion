package cn.lunadeer.dominion.uis.dominion.manage.group;

import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.dtos.GroupDTO;
import cn.lunadeer.dominion.api.dtos.MemberDTO;
import cn.lunadeer.dominion.commands.GroupCommand;
import cn.lunadeer.dominion.configuration.ChestUserInterface;
import cn.lunadeer.dominion.inputters.RenameGroupInputter;
import cn.lunadeer.dominion.uis.AbstractUI;
import cn.lunadeer.dominion.utils.Notification;
import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;
import cn.lunadeer.dominion.utils.scui.ChestButton;
import cn.lunadeer.dominion.utils.scui.ChestListView;
import cn.lunadeer.dominion.utils.scui.ChestUserInterfaceManager;
import cn.lunadeer.dominion.utils.scui.configuration.ButtonConfiguration;
import cn.lunadeer.dominion.utils.scui.configuration.ListViewConfiguration;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

import static cn.lunadeer.dominion.misc.Converts.*;
import static cn.lunadeer.dominion.utils.Misc.formatString;

public class GroupManage extends AbstractUI {

    public static void show(CommandSender sender, String dominionName, String groupName, String pageStr) {
        new GroupManage().displayByPreference(sender, dominionName, groupName, pageStr);
    }

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ TUI ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    @Override
    protected void showTUI(CommandSender sender, String... args) {
        throw new UnsupportedOperationException("GroupManage does not support TUI.");
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ TUI ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ CUI ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static class GroupManageCUI extends ConfigurationPart {
        public String title = "Group {0} Management";
        public ListViewConfiguration listConfiguration = new ListViewConfiguration(
                'i',
                List.of(
                        "<##P#R#D#",
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
                        "&fgroup list."
                )
        );

        public ButtonConfiguration groupFlagsButton = ButtonConfiguration.createMaterial(
                'P', Material.OAK_SIGN,
                "Group Flags",
                List.of(
                        "&fClick to setting",
                        "&fthe group flags."
                )
        );

        public ButtonConfiguration renameGroupButton = ButtonConfiguration.createMaterial(
                'R', Material.PAPER,
                "Rename Group",
                List.of(
                        "&fClick to rename",
                        "&fthe group."
                )
        );

        public ButtonConfiguration deleteGroupButton = ButtonConfiguration.createMaterial(
                'D', Material.RED_DYE,
                "Delete Group",
                List.of(
                        "&cClick to delete",
                        "&cthis group."
                )
        );

        public ButtonConfiguration addMemberButton = ButtonConfiguration.createMaterial(
                'i', Material.GREEN_DYE,
                "&a&lAdd",
                List.of(
                        "&fClick to select a",
                        "&fmember added to",
                        "&fthe group."
                )
        );

        public List<String> playerHeadItemLore = List.of(
                "&cClick to remove",
                "&cout of this group."
        );
    }

    @Override
    protected void showCUI(Player player, String... args) {
        try {
            DominionDTO dominion = toDominionDTO(args[0]);
            GroupDTO group = toGroupDTO(dominion, args[1]);
            ChestListView view = ChestUserInterfaceManager.getInstance().getListViewOf(player);
            view.setTitle(formatString(ChestUserInterface.groupManageCUI.title, group.getNameColoredBukkit()));
            view.applyListConfiguration(ChestUserInterface.groupManageCUI.listConfiguration, toIntegrity(args[2]));

            view.setButton(ChestUserInterface.groupManageCUI.backButton.getSymbol(),
                    new ChestButton(ChestUserInterface.groupManageCUI.backButton) {
                        @Override
                        public void onClick(ClickType type) {
                            GroupList.show(player, args[0], "1");
                        }
                    }
            );

            view.setButton(ChestUserInterface.groupManageCUI.groupFlagsButton.getSymbol(),
                    new ChestButton(ChestUserInterface.groupManageCUI.groupFlagsButton) {
                        @Override
                        public void onClick(ClickType type) {
                            GroupSetting.show(player, args[0], args[1], "1");
                        }
                    }
            );

            view.setButton(ChestUserInterface.groupManageCUI.renameGroupButton.getSymbol(),
                    new ChestButton(ChestUserInterface.groupManageCUI.renameGroupButton) {
                        @Override
                        public void onClick(ClickType type) {
                            RenameGroupInputter.createOn(player, args[0], args[1]);
                            view.close();
                        }
                    }
            );

            view.setButton(ChestUserInterface.groupManageCUI.deleteGroupButton.getSymbol(),
                    new ChestButton(ChestUserInterface.groupManageCUI.deleteGroupButton) {
                        @Override
                        public void onClick(ClickType type) {
                            GroupCommand.deleteGroup(player, args[0], args[1], "1");
                        }
                    }
            );

            view.addItem(new ChestButton(ChestUserInterface.groupManageCUI.addMemberButton) {
                @Override
                public void onClick(ClickType type) {
                    SelectMember.show(player, dominion.getName(), group.getNamePlain(), "1", "1");
                }
            });

            for (int i = 0; i < group.getMembers().size(); i++) {
                MemberDTO m = group.getMembers().get(i);
                Integer page = (int) Math.ceil((double) (i + 1) / view.getPageSize());
                ButtonConfiguration item = ButtonConfiguration.createHeadByName(
                        ChestUserInterface.groupManageCUI.listConfiguration.nextButton.getSymbol(),
                        m.getPlayer().getLastKnownName(),
                        m.getPlayer().getLastKnownName(),
                        ChestUserInterface.groupManageCUI.playerHeadItemLore
                );
                view.addItem(new ChestButton(item) {
                    @Override
                    public void onClick(ClickType type) {
                        GroupCommand.removeMember(player, dominion.getName(), group.getNamePlain(), m.getPlayer().getLastKnownName(), page.toString());
                        GroupManage.show(player, dominion.getName(), group.getNamePlain(), page.toString());
                    }
                });
            }

            view.open();
        } catch (Exception e) {
            Notification.error(player, e.getMessage());
        }
    }
}
