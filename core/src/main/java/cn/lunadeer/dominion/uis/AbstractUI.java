package cn.lunadeer.dominion.uis;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractUI {

    protected abstract void showTUI(CommandSender sender, String... args);

    protected abstract void showCUI(Player player, String... args);

    protected void displayByPreference(CommandSender sender, String... args) {
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
    }
}
