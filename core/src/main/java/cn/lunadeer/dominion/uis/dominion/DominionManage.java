package cn.lunadeer.dominion.uis.dominion;

import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.commands.DominionOperateCommand;
import cn.lunadeer.dominion.configuration.ChestUserInterface;
import cn.lunadeer.dominion.configuration.Configuration;
import cn.lunadeer.dominion.configuration.Language;
import cn.lunadeer.dominion.inputters.EditMessageInputter;
import cn.lunadeer.dominion.inputters.RenameDominionInputter;
import cn.lunadeer.dominion.inputters.SetMapColorInputter;
import cn.lunadeer.dominion.misc.CommandArguments;
import cn.lunadeer.dominion.uis.AbstractUI;
import cn.lunadeer.dominion.uis.MainMenu;
import cn.lunadeer.dominion.uis.dominion.copy.CopyMenu;
import cn.lunadeer.dominion.uis.dominion.manage.EnvSetting;
import cn.lunadeer.dominion.uis.dominion.manage.GuestSetting;
import cn.lunadeer.dominion.uis.dominion.manage.Info;
import cn.lunadeer.dominion.uis.dominion.manage.group.GroupList;
import cn.lunadeer.dominion.uis.dominion.manage.member.MemberList;
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
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

import static cn.lunadeer.dominion.Dominion.defaultPermission;
import static cn.lunadeer.dominion.misc.Asserts.assertDominionAdmin;
import static cn.lunadeer.dominion.misc.Converts.*;
import static cn.lunadeer.dominion.utils.Misc.formatString;

public class DominionManage extends AbstractUI {

    public static void show(CommandSender sender, String dominionName, String pageStr) {
        new DominionManage().displayByPreference(sender, dominionName, pageStr);
    }

    public static SecondaryCommand manage = new SecondaryCommand("manage", List.of(
            new CommandArguments.RequiredDominionArgument(),
            new CommandArguments.OptionalPageArgument()
    )) {
        @Override
        public void executeHandler(CommandSender sender) {
            show(sender, getArgumentValue(0), getArgumentValue(1));
        }
    }.needPermission(defaultPermission).register();

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ TUI ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static class DominionManageTuiText extends ConfigurationPart {
        public String title = "Manage {0}";
        public String button = "MANAGE";
        public String setTpButton = "SET TP";
        public String setTpDescription = "Set your current location as tp location.";
    }

    public static ListViewButton button(CommandSender sender, String dominionName) {
        return (ListViewButton) new ListViewButton(Language.dominionManageTuiText.button) {
            @Override
            public void function(String pageStr) {
                show(sender, dominionName, pageStr);
            }
        }.needPermission(defaultPermission);
    }

    @Override
    protected void showTUI(CommandSender sender, String... args) {
        Player player = toPlayer(sender);
        DominionDTO dominion = toDominionDTO(args[1]);
        assertDominionAdmin(player, dominion);
        int page = toIntegrity(args[2], 1);

        Line size_info = Line.create()
                .append(Info.button(sender, args[1]).build())
                .append(Language.sizeInfoTuiText.description);
        Line env_info = Line.create()
                .append(EnvSetting.button(sender, args[1]).build())
                .append(Language.envSettingTuiText.description);
        Line flag_info = Line.create()
                .append(GuestSetting.button(sender, args[1]).build())
                .append(Language.guestSettingTuiText.description);
        Line member_list = Line.create()
                .append(MemberList.button(sender, args[1]).build())
                .append(Language.memberListTuiText.description);
        Line group_list = Line.create()
                .append(GroupList.button(sender, args[1]).build())
                .append(Language.groupListTuiText.description);
        Line set_tp = Line.create()
                .append(new FunctionalButton(Language.dominionManageTuiText.setTpButton) {
                    @Override
                    public void function() {
                        DominionOperateCommand.setTp(sender, args[1]);
                    }
                }.build())
                .append(Language.dominionManageTuiText.setTpDescription);
        Line rename = Line.create()
                .append(RenameDominionInputter.createTuiButtonOn(sender, args[1]).needPermission(defaultPermission).build())
                .append(Language.renameDominionInputterText.description);
        Line enter_msg = Line.create()
                .append(EditMessageInputter.createEnterTuiButtonOn(sender, args[1]).needPermission(defaultPermission).build())
                .append(Language.editMessageInputterText.enterDescription);
        Line leave_msg = Line.create()
                .append(EditMessageInputter.createLeaveTuiButtonOn(sender, args[1]).needPermission(defaultPermission).build())
                .append(Language.editMessageInputterText.leaveDescription);
        Line map_color = Line.create()
                .append(SetMapColorInputter.createTuiButtonOn(sender, args[1]).build())
                .append(Component.text(Language.setMapColorInputterText.description)
                        .append(Component.text(dominion.getColor(),
                                TextColor.color(dominion.getColorR(), dominion.getColorG(), dominion.getColorB()))));
        Line copy_menu = Line.create()
                .append(CopyMenu.button(sender, args[1]).build())
                .append(Language.copyMenuTuiText.description);
        ListView view = ListView.create(10, button(sender, dominion.getName()));
        view.title(formatString(Language.dominionManageTuiText.title, dominion.getName()))
                .navigator(Line.create()
                        .append(MainMenu.button(sender).build())
                        .append(DominionList.button(sender).build())
                        .append(dominion.getName()))
                .add(size_info)
                .add(env_info)
                .add(flag_info)
                .add(member_list)
                .add(group_list)
                .add(set_tp)
                .add(rename)
                .add(enter_msg)
                .add(leave_msg);
        if (Configuration.webMapRenderer.blueMap || Configuration.webMapRenderer.dynmap) {
            view.add(map_color);
        }
        view.add(copy_menu);
        view.showOn(player, page);
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ TUI ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ CUI ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static class DominionManageCui extends ConfigurationPart {
        public String title = "Manage {0}";
        public ListViewConfiguration listConfiguration = new ListViewConfiguration(
                'i',
                List.of(
                        "<########",
                        "#i#i#i#i#",
                        "#i#i#i#i#",
                        "p#######n"
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

        public ButtonConfiguration dominionInfoButton = ButtonConfiguration.createMaterial(
                'i', Material.PAPER,
                "Dominion Info",
                List.of(
                        "View the information",
                        "of this dominion."
                )
        );

        public ButtonConfiguration envSettingButton = ButtonConfiguration.createMaterial(
                'i', Material.COMPASS,
                "Environment Settings",
                List.of(
                        "Set the environment",
                        "settings for this",
                        "dominion."
                )
        );

        public ButtonConfiguration guestSettingButton = ButtonConfiguration.createMaterial(
                'i', Material.OAK_DOOR,
                "Guest Settings",
                List.of("Set the guest",
                        "flags for this",
                        "dominion.")
        );

        public ButtonConfiguration memberListButton = ButtonConfiguration.createMaterial(
                'i', Material.PLAYER_HEAD,
                "Member List",
                List.of("View and manage",
                        "the members of",
                        "this dominion.")
        );

        public ButtonConfiguration groupListButton = ButtonConfiguration.createMaterial(
                'i', Material.CHEST,
                "Group List",
                List.of("View and manage",
                        "the groups of",
                        "this dominion.")
        );

        public ButtonConfiguration setTpButton = ButtonConfiguration.createMaterial(
                'i', Material.ENDER_PEARL,
                "Set TP",
                List.of("Set your current",
                        "location as the",
                        "teleport location",
                        "for this dominion.")
        );

        public ButtonConfiguration renameButton = ButtonConfiguration.createMaterial(
                'i', Material.NAME_TAG,
                "Rename Dominion",
                List.of("Rename this dominion.")
        );

        public ButtonConfiguration enterMessageButton = ButtonConfiguration.createMaterial(
                'i', Material.WRITABLE_BOOK,
                "Edit Enter Message",
                List.of("Edit the message",
                        "shown when a player",
                        "enters this dominion.")
        );

        public ButtonConfiguration leaveMessageButton = ButtonConfiguration.createMaterial(
                'i', Material.BOOK,
                "Edit Leave Message",
                List.of("Edit the message",
                        "shown when a player",
                        "leaves this dominion.")
        );

        public ButtonConfiguration setMapColorButton = ButtonConfiguration.createMaterial(
                'i', Material.PAINTING,
                "Set Map Color",
                List.of("Set the color",
                        "of this dominion",
                        "the map.")
        );

        public ButtonConfiguration copyMenuButton = ButtonConfiguration.createMaterial(
                'i', Material.GLASS,
                "Copy Menu",
                List.of("Open the copy",
                        "menu for this",
                        "dominion.")
        );

        public ButtonConfiguration deleteButton = ButtonConfiguration.createMaterial(
                'i', Material.BARRIER,
                "Delete Dominion",
                List.of("Delete this",
                        "dominion permanently.")
        );
    }

    @Override
    protected void showCUI(Player player, String... args) {
        DominionDTO dominion = toDominionDTO(args[0]);
        assertDominionAdmin(player, dominion);

        ChestListView view = ChestUserInterfaceManager.getInstance().getListViewOf(player);
        view.setTitle(formatString(ChestUserInterface.dominionManageCui.title, dominion.getName()));
        view.applyListConfiguration(ChestUserInterface.dominionManageCui.listConfiguration, toIntegrity(args[1]));

        view.setButton(ChestUserInterface.dominionManageCui.backButton.getSymbol(),
                new ChestButton(ChestUserInterface.dominionManageCui.backButton) {
                    @Override
                    public void onClick(ClickType type) {
                        DominionList.show(player, "1");
                    }
                }
        );

        view.addItem(new ChestButton(ChestUserInterface.dominionManageCui.dominionInfoButton) {
            @Override
            public void onClick(ClickType type) {
                Info.show(player, dominion.getName());
            }
        });

        view.addItem(new ChestButton(ChestUserInterface.dominionManageCui.envSettingButton) {
            @Override
            public void onClick(ClickType type) {
                EnvSetting.show(player, dominion.getName(), "1");
            }
        });

        view.addItem(new ChestButton(ChestUserInterface.dominionManageCui.guestSettingButton) {
            @Override
            public void onClick(ClickType type) {
                GuestSetting.show(player, dominion.getName(), "1");
            }
        });

        view.addItem(new ChestButton(ChestUserInterface.dominionManageCui.memberListButton) {
            @Override
            public void onClick(ClickType type) {
                MemberList.show(player, dominion.getName(), "1");
            }
        });

        view.addItem(new ChestButton(ChestUserInterface.dominionManageCui.groupListButton) {
            @Override
            public void onClick(ClickType type) {
                GroupList.show(player, dominion.getName(), "1");
            }
        });

        view.addItem(new ChestButton(ChestUserInterface.dominionManageCui.setTpButton) {
            @Override
            public void onClick(ClickType type) {
                DominionOperateCommand.setTp(player, dominion.getName());
            }
        });

        view.addItem(new ChestButton(ChestUserInterface.dominionManageCui.renameButton) {
            @Override
            public void onClick(ClickType type) {
                RenameDominionInputter.createOn(player, dominion.getName());
                view.close();
            }
        });

        view.addItem(new ChestButton(ChestUserInterface.dominionManageCui.enterMessageButton) {
            @Override
            public void onClick(ClickType type) {
                EditMessageInputter.createEnterOn(player, dominion.getName());
                view.close();
            }
        });

        view.addItem(new ChestButton(ChestUserInterface.dominionManageCui.leaveMessageButton) {
            @Override
            public void onClick(ClickType type) {
                EditMessageInputter.createLeaveOn(player, dominion.getName());
                view.close();
            }
        });

        view.addItem(new ChestButton(ChestUserInterface.dominionManageCui.setMapColorButton) {
            @Override
            public void onClick(ClickType type) {
                SetMapColorInputter.createOn(player, dominion.getName());
                view.close();
            }
        });

        view.addItem(new ChestButton(ChestUserInterface.dominionManageCui.copyMenuButton) {
            @Override
            public void onClick(ClickType type) {
                CopyMenu.show(player, dominion.getName(), "1");
            }
        });

        view.addItem(new ChestButton(ChestUserInterface.dominionManageCui.deleteButton) {
            @Override
            public void onClick(ClickType type) {
                DominionOperateCommand.delete(player, dominion.getName(), "");
                view.close();
            }
        });

        view.open();
    }
}
