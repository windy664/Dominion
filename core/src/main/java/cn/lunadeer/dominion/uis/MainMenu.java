package cn.lunadeer.dominion.uis;

import cn.lunadeer.dominion.commands.AdministratorCommand;
import cn.lunadeer.dominion.commands.DominionCreateCommand;
import cn.lunadeer.dominion.configuration.ChestUserInterface;
import cn.lunadeer.dominion.configuration.Configuration;
import cn.lunadeer.dominion.configuration.Language;
import cn.lunadeer.dominion.inputters.CreateDominionInputter;
import cn.lunadeer.dominion.misc.CommandArguments;
import cn.lunadeer.dominion.uis.dominion.DominionList;
import cn.lunadeer.dominion.uis.template.TemplateList;
import cn.lunadeer.dominion.utils.Notification;
import cn.lunadeer.dominion.utils.command.SecondaryCommand;
import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;
import cn.lunadeer.dominion.utils.scui.ChestButton;
import cn.lunadeer.dominion.utils.scui.ChestUserInterfaceManager;
import cn.lunadeer.dominion.utils.scui.ChestView;
import cn.lunadeer.dominion.utils.scui.configuration.ButtonConfiguration;
import cn.lunadeer.dominion.utils.stui.ListView;
import cn.lunadeer.dominion.utils.stui.ViewStyles;
import cn.lunadeer.dominion.utils.stui.components.Line;
import cn.lunadeer.dominion.utils.stui.components.buttons.ListViewButton;
import cn.lunadeer.dominion.utils.stui.components.buttons.UrlButton;
import cn.lunadeer.dominion.utils.stui.inputter.InputterRunner;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

import static cn.lunadeer.dominion.Dominion.adminPermission;
import static cn.lunadeer.dominion.Dominion.defaultPermission;
import static cn.lunadeer.dominion.misc.Converts.toIntegrity;
import static cn.lunadeer.dominion.misc.Converts.toPlayer;


public class MainMenu extends AbstractUI {

    public static void show(CommandSender sender, String pageStr) {
        new MainMenu().displayByPreference(sender, pageStr);
    }

    public static SecondaryCommand menu = new SecondaryCommand("menu", List.of(
            new CommandArguments.OptionalPageArgument()
    )) {
        @Override
        public void executeHandler(CommandSender sender) {
            try {
                MainMenu.show(sender, getArgumentValue(0));
            } catch (Exception e) {
                Notification.error(sender, e.getMessage());
            }
        }
    }.needPermission(defaultPermission).register();

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ TUI ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static class MenuTuiText extends ConfigurationPart {
        public String title = "Dominion Menu";
        public String button = "MENU";
        public String adminOnlySection = "Only admin can see this section";
        public String documentButton = "DOCUMENT";
        public String documentDescription = "Open the documentation external link.";
        public String commandHelpButton = "COMMAND HELP";
        public String commandHelpDescription = "Open the command help external link.";
    }

    public static ListViewButton button(CommandSender sender) {
        return (ListViewButton) new ListViewButton(Language.menuTuiText.button) {
            @Override
            public void function(String pageStr) {
                MainMenu.show(sender, pageStr);
            }
        }.needPermission(defaultPermission);
    }

    @Override
    protected void showTUI(CommandSender sender, String... args) {
        try {
            Player player = toPlayer(sender);
            int page = toIntegrity(args[0]);

            Line create = Line.create()
                    .append(CreateDominionInputter.createOn(sender).needPermission(defaultPermission).build())
                    .append(Language.createDominionInputterText.description);
            Line list = Line.create()
                    .append(DominionList.button(sender).build())
                    .append(Language.dominionListTuiText.description);
            Line title = Line.create()
                    .append(TitleList.button(sender).build())
                    .append(Language.titleListTuiText.description);
            Line template = Line.create()
                    .append(TemplateList.button(sender).build())
                    .append(Language.templateListTuiText.description);
            Line help = Line.create()
                    .append(new UrlButton(Language.menuTuiText.commandHelpButton, Configuration.externalLinks.commandHelp).build())
                    .append(Language.menuTuiText.commandHelpDescription);
            Line link = Line.create()
                    .append(new UrlButton(Language.menuTuiText.documentButton, Configuration.externalLinks.documentation).build())
                    .append(Language.menuTuiText.documentDescription);
            Line migrate = Line.create()
                    .append(MigrateList.button(sender).build())
                    .append(Language.migrateListText.description);
            Line all = Line.create()
                    .append(AllDominion.button(sender).build())
                    .append(Language.allDominionTuiText.description);
            Line reload_cache = Line.create()
                    .append(AdministratorCommand.reloadCacheButton(sender).build())
                    .append(Language.administratorCommandText.reloadCacheDescription);
            Line reload_config = Line.create()
                    .append(AdministratorCommand.reloadConfigButton(sender).build())
                    .append(Language.administratorCommandText.reloadConfigDescription);
            ListView view = ListView.create(10, button(sender));
            view.title(Language.menuTuiText.title);
            view.navigator(Line.create().append(Language.menuTuiText.button));
            view.add(create);
            view.add(list);
            if (Configuration.groupTitle.enable) view.add(title);
            view.add(template);
            if (!Configuration.externalLinks.commandHelp.isEmpty()) view.add(help);
            if (!Configuration.externalLinks.documentation.isEmpty()) view.add(link);
            if (Configuration.residenceMigration) {
                view.add(migrate);
            }
            if (player.hasPermission(adminPermission)) {
                view.add(Line.create().append(""));
                view.add(Line.create().append(Component.text(Language.menuTuiText.adminOnlySection, ViewStyles.main_color)));
                view.add(all);
                view.add(reload_cache);
                view.add(reload_config);
            }
            view.showOn(player, page);
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ TUI ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ CUI ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static class MainMenuCui extends ConfigurationPart {
        public String title = "Dominion Main Menu";
        public List<String> adminLayout = List.of(
                "#########",
                "##A#B#C##",
                "##D#E#F##",
                "#########"
        );
        public List<String> userLayout = List.of(
                "#########",
                "##A#B#C##",
                "###D#E###",
                "#########"
        );
        public ButtonConfiguration createButton = new ButtonConfiguration(
                'A', Material.NETHER_STAR, "Create Dominion", List.of("Create a new dominion around you.")
        );
        public ButtonConfiguration listButton = new ButtonConfiguration(
                'B', Material.BOOKSHELF, "List Dominion", List.of("List all dominions you can manage.")
        );
        public ButtonConfiguration titleButton = new ButtonConfiguration(
                'C', Material.NAME_TAG, "Title List", List.of("List all titles you can use.")
        );
        public ButtonConfiguration templateButton = new ButtonConfiguration(
                'D', Material.PAPER, "Template List", List.of("Manage your templates.")
        );
        public ButtonConfiguration migrateButton = new ButtonConfiguration(
                'E', Material.ENDER_PEARL, "Migrate Residence", List.of("Migrate your residence to dominion.")
        );
        public ButtonConfiguration allButton = new ButtonConfiguration(
                'F', Material.DIAMOND, "All Dominion", List.of("List all dominions in the server.")
        );
    }

    @Override
    protected void showCUI(Player player, String... args) {
        try {
            ChestView view = ChestUserInterfaceManager.getInstance().getViewOf(player).setTitle(ChestUserInterface.mainMenuCui.title);

            if (player.hasPermission(adminPermission)) {
                view.setLayout(ChestUserInterface.mainMenuCui.adminLayout);
            } else {
                view.setLayout(ChestUserInterface.mainMenuCui.userLayout);
            }

            view.setButton(ChestUserInterface.mainMenuCui.createButton.getSymbol(),
                    new ChestButton(ChestUserInterface.mainMenuCui.createButton.getName(), ChestUserInterface.mainMenuCui.createButton.getMaterial()) {
                        @Override
                        public void onClick(ClickType type) {
                            new InputterRunner(player, Language.createDominionInputterText.hint) {
                                @Override
                                public void run(String input) {
                                    DominionCreateCommand.autoCreate(player, input);
                                    DominionList.show(player, "1");
                                }
                            };
                            view.close();
                        }
                    }.setLore(ChestUserInterface.mainMenuCui.createButton.getLore())
            );

            view.setButton(ChestUserInterface.mainMenuCui.listButton.getSymbol(),
                    new ChestButton(ChestUserInterface.mainMenuCui.listButton.getName(), ChestUserInterface.mainMenuCui.listButton.getMaterial()) {
                        @Override
                        public void onClick(ClickType type) {
                            DominionList.show(player, "1");
                        }
                    }.setLore(ChestUserInterface.mainMenuCui.listButton.getLore())
            );

            view.setButton(ChestUserInterface.mainMenuCui.titleButton.getSymbol(),
                    new ChestButton(ChestUserInterface.mainMenuCui.titleButton.getName(), ChestUserInterface.mainMenuCui.titleButton.getMaterial()) {
                        @Override
                        public void onClick(ClickType type) {
                            if (Configuration.groupTitle.enable) {
                                TitleList.show(player, "1");
                            }
                        }
                    }.setLore(ChestUserInterface.mainMenuCui.titleButton.getLore())
            );

            view.setButton(ChestUserInterface.mainMenuCui.templateButton.getSymbol(),
                    new ChestButton(ChestUserInterface.mainMenuCui.templateButton.getName(), ChestUserInterface.mainMenuCui.templateButton.getMaterial()) {
                        @Override
                        public void onClick(ClickType type) {
                            TemplateList.show(player, "1");
                        }
                    }.setLore(ChestUserInterface.mainMenuCui.templateButton.getLore())
            );

            view.setButton(ChestUserInterface.mainMenuCui.migrateButton.getSymbol(),
                    new ChestButton(ChestUserInterface.mainMenuCui.migrateButton.getName(), ChestUserInterface.mainMenuCui.migrateButton.getMaterial()) {
                        @Override
                        public void onClick(ClickType type) {
                            if (Configuration.residenceMigration) {
                                MigrateList.show(player, "1");
                            }
                        }
                    }.setLore(ChestUserInterface.mainMenuCui.migrateButton.getLore())
            );

            if (player.hasPermission(adminPermission)) {
                view.setButton(ChestUserInterface.mainMenuCui.allButton.getSymbol(),
                        new ChestButton(ChestUserInterface.mainMenuCui.allButton.getName(), ChestUserInterface.mainMenuCui.allButton.getMaterial()) {
                            @Override
                            public void onClick(ClickType type) {
                                AllDominion.show(player, "1");
                            }
                        }.setLore(ChestUserInterface.mainMenuCui.allButton.getLore())
                );
            }

            view.open();
        } catch (Exception e) {
            Notification.error(player, e.getMessage());
        }
    }
}
