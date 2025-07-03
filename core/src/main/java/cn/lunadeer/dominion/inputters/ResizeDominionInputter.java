package cn.lunadeer.dominion.inputters;

import cn.lunadeer.dominion.commands.DominionOperateCommand;
import cn.lunadeer.dominion.configuration.Language;
import cn.lunadeer.dominion.events.dominion.modify.DominionReSizeEvent;
import cn.lunadeer.dominion.uis.dominion.manage.SetSize;
import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;
import cn.lunadeer.dominion.utils.stui.components.buttons.FunctionalButton;
import cn.lunadeer.dominion.utils.stui.inputter.InputterRunner;
import org.bukkit.command.CommandSender;

import static cn.lunadeer.dominion.utils.Misc.formatString;

public class ResizeDominionInputter {
    public static class ResizeDominionInputterText extends ConfigurationPart {
        public String expand = "EXPAND";
        public String contract = "CONTRACT";
        public String expandHint = "Enter the new size of the {0} expand to {1}.";
        public String contractHint = "Enter the new size of the {0} contract to {1}.";
    }

    public static FunctionalButton createExpandOn(CommandSender sender, String dominionName, DominionReSizeEvent.DIRECTION direction) {
        return new FunctionalButton(Language.resizeDominionInputterText.expand) {
            @Override
            public void function() {
                new InputterRunner(sender, formatString(Language.resizeDominionInputterText.expandHint,
                        dominionName, direction.name())) {
                    @Override
                    public void run(String input) {
                        String numberInput = input.replaceAll("[^\\d.]", ""); // prevent some stupid noob chat plugins from modifying input with special characters
                        DominionOperateCommand.resize(sender, dominionName, DominionReSizeEvent.TYPE.EXPAND.name(), numberInput, direction.name());
                        SetSize.show(sender, dominionName);
                    }
                };
            }
        };
    }

    public static FunctionalButton createContractOn(CommandSender sender, String dominionName, DominionReSizeEvent.DIRECTION direction) {
        return new FunctionalButton(Language.resizeDominionInputterText.contract) {
            @Override
            public void function() {
                new InputterRunner(sender, formatString(Language.resizeDominionInputterText.contractHint,
                        dominionName, direction.name())) {
                    @Override
                    public void run(String input) {
                        String numberInput = input.replaceAll("[^\\d.]", ""); // prevent some stupid noob chat plugins from modifying input with special characters
                        DominionOperateCommand.resize(sender, dominionName, DominionReSizeEvent.TYPE.CONTRACT.name(), numberInput, direction.name());
                        SetSize.show(sender, dominionName);
                    }
                };
            }
        };
    }


}
