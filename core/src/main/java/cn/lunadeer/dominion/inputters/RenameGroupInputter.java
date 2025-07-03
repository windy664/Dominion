package cn.lunadeer.dominion.inputters;

import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.dtos.GroupDTO;
import cn.lunadeer.dominion.configuration.Language;
import cn.lunadeer.dominion.events.group.GroupRenamedEvent;
import cn.lunadeer.dominion.uis.dominion.manage.group.GroupSetting;
import cn.lunadeer.dominion.utils.ColorParser;
import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;
import cn.lunadeer.dominion.utils.stui.components.buttons.FunctionalButton;
import cn.lunadeer.dominion.utils.stui.inputter.InputterRunner;
import org.bukkit.command.CommandSender;

import static cn.lunadeer.dominion.misc.Converts.toDominionDTO;
import static cn.lunadeer.dominion.misc.Converts.toGroupDTO;

public class RenameGroupInputter {

    public static class RenameGroupInputterText extends ConfigurationPart {
        public String button = "RENAME";
        public String hint = "Enter new group name.";
        public String description = "Rename this group.";
    }

    public static FunctionalButton createOn(CommandSender sender, String dominionName, String oldGroupName) {
        return new FunctionalButton(Language.renameGroupInputterText.button) {
            @Override
            public void function() {
                new InputterRunner(sender, Language.renameGroupInputterText.hint) {
                    @Override
                    public void run(String input) {
                        DominionDTO dominion = toDominionDTO(dominionName);
                        GroupDTO group = toGroupDTO(dominion, oldGroupName);
                        new GroupRenamedEvent(sender, dominion, group, input).call();
                        GroupSetting.show(sender, dominionName, ColorParser.getPlainText(input), "1");
                    }
                };
            }
        };
    }
}
