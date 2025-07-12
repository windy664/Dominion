package cn.lunadeer.dominion.uis.dominion.copy;

import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.cache.CacheManager;
import cn.lunadeer.dominion.commands.CopyCommand;
import cn.lunadeer.dominion.configuration.Language;
import cn.lunadeer.dominion.utils.Notification;
import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;
import cn.lunadeer.dominion.utils.stui.ListView;
import cn.lunadeer.dominion.utils.stui.components.Line;
import cn.lunadeer.dominion.utils.stui.components.buttons.FunctionalButton;
import cn.lunadeer.dominion.utils.stui.components.buttons.ListViewButton;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static cn.lunadeer.dominion.Dominion.defaultPermission;
import static cn.lunadeer.dominion.misc.Asserts.assertDominionAdmin;
import static cn.lunadeer.dominion.misc.Converts.*;

/**
 * Unified copy functionality for all dominion copy operations.
 * This class eliminates code duplication by providing a single implementation
 * that handles Environment, Guest, Member, and Group copying through an enum-based approach.
 */
public class DominionCopy {

    public enum CopyType {
        ENVIRONMENT,
        GUEST,
        MEMBER,
        GROUP;
    }

    public static class DominionCopyTuiText extends ConfigurationPart {
        public String back = "BACK";
        public String copy = "COPY FROM";
        public String title = "Select Dominion to Copy From";

        public String envButton = "ENV";
        public String envDescription = "Copy Env Settings From Other Dominion.";

        public String groupButton = "GROUPS";
        public String groupDescription = "Copy Group & Settings From Other Dominion.";

        public String guestButton = "GUEST";
        public String guestDescription = "Copy Guest Settings From Other Dominion.";

        public String memberButton = "MEMBERS";
        public String memberDescription = "Copy Member & Settings From Other Dominion.";

    }

    /**
     * Creates a button for the specified copy type
     */
    public static ListViewButton button(CommandSender sender, String toDominionName, CopyType copyType) {
        String buttonText;
        switch (copyType) {
            case ENVIRONMENT -> {
                buttonText = Language.dominionCopyTuiText.envButton;
            }
            case GUEST -> {
                buttonText = Language.dominionCopyTuiText.guestButton;
            }
            case MEMBER -> {
                buttonText = Language.dominionCopyTuiText.memberButton;
            }
            case GROUP -> {
                buttonText = Language.dominionCopyTuiText.groupButton;
            }
            default -> throw new IllegalArgumentException("Unknown copy type: " + copyType);
        }
        return (ListViewButton) new ListViewButton(buttonText) {
            @Override
            public void function(String pageStr) {
                show(sender, toDominionName, copyType, pageStr);
            }
        }.needPermission(defaultPermission);
    }

    /**
     * Shows the copy selection UI for the specified copy type
     */
    public static void show(CommandSender sender, String toDominionName, CopyType copyType, String pageStr) {
        try {
            DominionDTO dominion = toDominionDTO(toDominionName);
            Player player = toPlayer(sender);
            assertDominionAdmin(sender, dominion);
            int page = toIntegrity(pageStr);

            ListView view = ListView.create(10, button(sender, toDominionName, copyType));

            view.title(Language.dominionCopyTuiText.title)
                    .navigator(Line.create()
                            .append(CopyMenu.button(sender, toDominionName)
                                    .setText(Language.dominionCopyTuiText.back).build()));

            List<DominionDTO> dominions = CacheManager.instance.getPlayerOwnDominionDTOs(player.getUniqueId());
            for (DominionDTO fromDominion : dominions) {
                if (fromDominion.getId().equals(dominion.getId())) continue;
                String fromDominionName = fromDominion.getName();
                FunctionalButton item = (FunctionalButton) new FunctionalButton(Language.dominionCopyTuiText.copy) {
                    @Override
                    public void function() {
                        switch (copyType) {
                            case ENVIRONMENT -> CopyCommand.copyEnvironment(sender, fromDominionName, toDominionName);
                            case GUEST -> CopyCommand.copyGuest(sender, fromDominionName, toDominionName);
                            case MEMBER -> CopyCommand.copyMember(sender, fromDominionName, toDominionName);
                            case GROUP -> CopyCommand.copyGroup(sender, fromDominionName, toDominionName);
                        }
                    }
                }.needPermission(defaultPermission);

                view.add(Line.create()
                        .append(item.build())
                        .append(Component.text(fromDominionName))
                );
            }
            view.showOn(sender, page);
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }
}
