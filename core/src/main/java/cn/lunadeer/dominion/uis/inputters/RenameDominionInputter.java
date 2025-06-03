package cn.lunadeer.dominion.uis.inputters;

import cn.lunadeer.dominion.commands.DominionOperateCommand;
import cn.lunadeer.dominion.configuration.Language;
import cn.lunadeer.dominion.uis.tuis.dominion.DominionManage;
import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;
import cn.lunadeer.dominion.utils.stui.components.buttons.FunctionalButton;
import cn.lunadeer.dominion.utils.stui.inputter.InputterRunner;
import org.bukkit.command.CommandSender;

public class RenameDominionInputter {

    public static class RenameDominionInputterText extends ConfigurationPart {
        public String button = "RENAME";
        public String hint = "Enter new dominion name.";
        public String description = "Rename this dominion.";
    }

    public static FunctionalButton createOn(CommandSender sender, String dominionName) {
        return new FunctionalButton(Language.renameDominionInputterText.button) {
            @Override
            public void function() {
                new InputterRunner(sender, Language.renameDominionInputterText.hint) {
                    @Override
                    public void run(String input) {
                        DominionOperateCommand.rename(sender, dominionName, input);
                        DominionManage.show(sender, input, "1");
                    }
                };
            }
        };
    }
}
