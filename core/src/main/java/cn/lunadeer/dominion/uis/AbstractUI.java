package cn.lunadeer.dominion.uis;

import cn.lunadeer.dominion.utils.Notification;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractUI {

    protected abstract void showTUI(CommandSender sender, String... args) throws Exception;

    protected abstract void showCUI(Player player, String... args) throws Exception;

    protected void displayByPreference(CommandSender sender, String... args) {
        try {
            if (sender instanceof Player player) {
                // todo show ui depending on player preference
                if (true) {
                    showCUI(player, args);
                } else {
                    showTUI(sender, args);
                }
            } else {
                showTUI(sender, args);
            }
        } catch (Exception e) {
            Notification.error(sender, e.getMessage());
        }
    }
}
