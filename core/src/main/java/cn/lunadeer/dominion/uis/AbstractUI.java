package cn.lunadeer.dominion.uis;

import cn.lunadeer.dominion.api.dtos.PlayerDTO;
import cn.lunadeer.dominion.cache.CacheManager;
import cn.lunadeer.dominion.utils.Notification;
import cn.lunadeer.dominion.utils.XLogger;
import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public abstract class AbstractUI {

    public static class ConsoleText extends ConfigurationPart {
        public String inGameOnly = "This ui is not available in console mode. Please use it in-game.";
        public String pageInfo = "§7 Page {0}/{1} - Total {2}";
        public String descPrefix = "§7      - §f{0}";
    }

    protected abstract void showTUI(Player player, String... args) throws Exception;

    protected abstract void showCUI(Player player, String... args) throws Exception;

    protected abstract void showConsole(CommandSender sender, String... args) throws Exception;

    protected void displayByPreference(CommandSender sender, String... args) {
        try {
            if (sender instanceof Player player) {
                PlayerDTO playerDTO = CacheManager.instance.getPlayer(player.getUniqueId());
                if (playerDTO == null) {
                    showTUI(player, args);
                    XLogger.warn("PlayerDTO not found for player: " + player.getName() + ". Showing TUI instead.");
                    return;
                }
                if (Objects.requireNonNull(playerDTO.getUiPreference()) == PlayerDTO.UI_TYPE.CUI) {
                    showCUI(player, args);
                } else {
                    showTUI(player, args);
                }
            } else {
                Notification.info(sender, "--------------------------------------------------");
                showConsole(sender, args);
                Notification.info(sender, "--------------------------------------------------");
            }
        } catch (Exception e) {
            Notification.error(sender, e);
        }
    }
}
