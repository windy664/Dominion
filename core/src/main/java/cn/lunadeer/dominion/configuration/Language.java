package cn.lunadeer.dominion.configuration;

import cn.lunadeer.dominion.Dominion;
import cn.lunadeer.dominion.api.dtos.flag.Flag;
import cn.lunadeer.dominion.api.dtos.flag.Flags;
import cn.lunadeer.dominion.commands.*;
import cn.lunadeer.dominion.handler.DominionEventHandler;
import cn.lunadeer.dominion.handler.GroupEventHandler;
import cn.lunadeer.dominion.handler.MemberEventHandler;
import cn.lunadeer.dominion.handler.SelectPointEventsHandler;
import cn.lunadeer.dominion.inputters.*;
import cn.lunadeer.dominion.managers.DatabaseTables;
import cn.lunadeer.dominion.managers.MultiServerManager;
import cn.lunadeer.dominion.managers.TeleportManager;
import cn.lunadeer.dominion.misc.Asserts;
import cn.lunadeer.dominion.misc.Converts;
import cn.lunadeer.dominion.misc.Others;
import cn.lunadeer.dominion.utils.VaultConnect.VaultConnect;
import cn.lunadeer.dominion.utils.command.InvalidArgumentException;
import cn.lunadeer.dominion.utils.command.NoPermissionException;
import cn.lunadeer.dominion.utils.configuration.*;
import cn.lunadeer.dominion.utils.stui.inputter.InputterRunner;
import cn.lunadeer.dominion.utils.webMap.BlueMapConnect;
import cn.lunadeer.dominion.utils.webMap.DynmapConnect;

@Headers({
        "Language file for Dominion plugin",
        "If you want to help translate this file, please refer to:",
        "https://dominion.lunadeer.cn/en/notes/doc/owner/config-ref/languages",
        "for more instructions.",
        "",
        "Most of the text support color codes,",
        "you can use §0-§9 for colors, §l for bold, §o for italic, §n for underline, §m for strikethrough, and §k for magic.",
        "Also support '&' as an alternative for '§'.",
})
public class Language extends ConfigurationFile {

    // languages file name list here will be saved to plugin data folder
    @HandleManually
    public enum LanguageCode {
        en_us,
        zh_cn,
        jp_jp,
    }

    public static Dominion.DominionText dominionText = new Dominion.DominionText();

    public static MultiServerManager.MultiServerManagerText multiServerManagerText = new MultiServerManager.MultiServerManagerText();

    public static Asserts.AssertsText assertsText = new Asserts.AssertsText();
    public static Converts.ConvertsText convertsText = new Converts.ConvertsText();
    public static Others.OthersText othersText = new Others.OthersText();

    public static VaultConnect.VaultConnectText vaultConnectText = new VaultConnect.VaultConnectText();

    // Event Handler
    public static DominionEventHandler.DominionEventHandlerText dominionEventHandlerText = new DominionEventHandler.DominionEventHandlerText();
    public static MemberEventHandler.MemberEventHandlerText memberEventHandlerText = new MemberEventHandler.MemberEventHandlerText();
    public static GroupEventHandler.GroupEventHandlerText groupEventHandlerText = new GroupEventHandler.GroupEventHandlerText();
    public static SelectPointEventsHandler.SelectPointEventsHandlerText selectPointEventsHandlerText = new SelectPointEventsHandler.SelectPointEventsHandlerText();

    // Inputter
    public static CreateDominionInputter.CreateDominionInputterText createDominionInputterText = new CreateDominionInputter.CreateDominionInputterText();
    public static CreateGroupInputter.CreateGroupInputterText createGroupInputterText = new CreateGroupInputter.CreateGroupInputterText();
    public static RenameDominionInputter.RenameDominionInputterText renameDominionInputterText = new RenameDominionInputter.RenameDominionInputterText();
    public static EditMessageInputter.EditMessageInputterText editMessageInputterText = new EditMessageInputter.EditMessageInputterText();
    public static CreateTemplateInputter.CreateTemplateInputterText createTemplateInputterText = new CreateTemplateInputter.CreateTemplateInputterText();
    public static RenameGroupInputter.RenameGroupInputterText renameGroupInputterText = new RenameGroupInputter.RenameGroupInputterText();
    public static ResizeDominionInputter.ResizeDominionInputterText resizeDominionInputterText = new ResizeDominionInputter.ResizeDominionInputterText();
    public static SearchPlayerInputter.SearchPlayerInputterText searchPlayerInputterText = new SearchPlayerInputter.SearchPlayerInputterText();
    public static SetMapColorInputter.SetMapColorInputterText setMapColorInputterText = new SetMapColorInputter.SetMapColorInputterText();
    public static RenameTemplateInputter.RenameTemplateInputterText renameTemplateInputterText = new RenameTemplateInputter.RenameTemplateInputterText();

    // Commands
    public static AdministratorCommand.AdministratorCommandText administratorCommandText = new AdministratorCommand.AdministratorCommandText();
    public static MigrationCommand.MigrationCommandText migrationCommandText = new MigrationCommand.MigrationCommandText();
    public static TemplateCommand.TemplateCommandText templateCommandText = new TemplateCommand.TemplateCommandText();
    public static GroupTitleCommand.GroupTitleCommandText groupTitleCommandText = new GroupTitleCommand.GroupTitleCommandText();
    public static CopyCommand.CopyCommandText copyCommandText = new CopyCommand.CopyCommandText();
    public static DominionOperateCommand.DominionOperateCommandText dominionOperateCommandText = new DominionOperateCommand.DominionOperateCommandText();
    public static DominionCreateCommand.DominionCreateCommandText dominionCreateCommandText = new DominionCreateCommand.DominionCreateCommandText();
    public static DominionFlagCommand.DominionFlagCommandText dominionFlagCommandText = new DominionFlagCommand.DominionFlagCommandText();
    public static GroupCommand.GroupCommandText groupCommandText = new GroupCommand.GroupCommandText();
    public static MemberCommand.MemberCommandText memberCommandText = new MemberCommand.MemberCommandText();

    public static Configuration.ConfigurationText configurationText = new Configuration.ConfigurationText();

    public static Limitation.LimitationText limitationText = new Limitation.LimitationText();

    public static DatabaseTables.DatabaseManagerText databaseManagerText = new DatabaseTables.DatabaseManagerText();

    public static TeleportManager.TeleportManagerText teleportManagerText = new TeleportManager.TeleportManagerText();

    // web map render
    public static BlueMapConnect.BlueMapConnectText blueMapConnectText = new BlueMapConnect.BlueMapConnectText();
    public static DynmapConnect.DynmapConnectText dynmapConnectText = new DynmapConnect.DynmapConnectText();


    public static CommandExceptionText commandExceptionText = new CommandExceptionText();

    public static class CommandExceptionText extends ConfigurationPart {
        public String noPermission = "You do not have permission {0} to do this.";
        public String invalidArguments = "Invalid arguments, usage e.g. {0}.";
    }

    public static InputterText inputterText = new InputterText();

    public static class InputterText extends ConfigurationPart {
        public String onlyPlayer = "TUI inputter can only be used by a player.";
        public String cancel = " [Send 'C' to cancel the inputter.]";
        public String inputterCancelled = "Inputter cancelled.";
    }

    @PreProcess
    public void loadFlagsText() {
        for (Flag flag : Flags.getAllFlags()) {
            if (getYaml().contains(flag.getDisplayNameKey())) {
                flag.setDisplayName(getYaml().getString(flag.getDisplayNameKey()));
            } else {
                getYaml().set(flag.getDisplayNameKey(), flag.getDisplayName());
            }
            if (getYaml().contains(flag.getDescriptionKey())) {
                flag.setDescription(getYaml().getString(flag.getDescriptionKey()));
            } else {
                getYaml().set(flag.getDescriptionKey(), flag.getDescription());
            }
        }
    }

    @PostProcess
    public static void setOtherStaticText() {
        // cn.lunadeer.dominion.utils.command
        InvalidArgumentException.MSG = commandExceptionText.invalidArguments;
        NoPermissionException.MSG = commandExceptionText.noPermission;

        InputterRunner.ONLY_PLAYER = inputterText.onlyPlayer;
        InputterRunner.CANCEL = inputterText.cancel;
        InputterRunner.INPUTTER_CANCELLED = inputterText.inputterCancelled;
    }

}
