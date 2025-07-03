package cn.lunadeer.dominion.inputters;

import cn.lunadeer.dominion.commands.DominionOperateCommand;
import cn.lunadeer.dominion.configuration.Language;
import cn.lunadeer.dominion.uis.dominion.DominionManage;
import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;
import cn.lunadeer.dominion.utils.stui.components.buttons.FunctionalButton;
import cn.lunadeer.dominion.utils.stui.inputter.InputterRunner;
import org.bukkit.command.CommandSender;

public class SetMapColorInputter {

    public static class SetMapColorInputterText extends ConfigurationPart {
        public String button = "COLOR";
        public String hint = "Enter the hex color code (e.g., #FF5733) to set the map color.";
        public String description = "Color of the dominion on the web map.";
    }

    public static FunctionalButton createOn(CommandSender sender, String dominionName) {
        return new FunctionalButton(Language.setMapColorInputterText.button) {
            @Override
            public void function() {
                new InputterRunner(sender, Language.setMapColorInputterText.hint) {
                    @Override
                    public void run(String input) {
                        DominionOperateCommand.setMapColor(sender, dominionName, input);
                        DominionManage.show(sender, dominionName, "1");
                    }
                };
            }
        };
    }
}
