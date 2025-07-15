package cn.lunadeer.dominion.uis;

import cn.lunadeer.dominion.api.dtos.PlayerDTO;
import cn.lunadeer.dominion.cache.CacheManager;
import cn.lunadeer.dominion.utils.Notification;
import cn.lunadeer.dominion.utils.XLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public abstract class AbstractUI {

    protected abstract void showTUI(CommandSender sender, String... args) throws Exception;

    protected abstract void showCUI(Player player, String... args) throws Exception;

    protected void displayByPreference(CommandSender sender, String... args) {
        try {
            if (sender instanceof Player player) {
                PlayerDTO playerDTO = CacheManager.instance.getPlayer(player.getUniqueId());
                if (playerDTO == null) {
                    showTUI(sender, args);
                    XLogger.warn("PlayerDTO not found for player: " + player.getName() + ". Showing TUI instead.");
                    return;
                }
                if (Objects.requireNonNull(playerDTO.getUiPreference()) == PlayerDTO.UI_TYPE.CUI) {
                    showCUI(player, args);
                } else {
                    showTUI(sender, args);
                }
            } else {
                showTUI(sender, args);
            }
        } catch (Exception e) {
            Notification.error(sender, e);
        }
    }
}
