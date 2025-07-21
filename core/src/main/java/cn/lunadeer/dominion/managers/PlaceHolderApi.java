package cn.lunadeer.dominion.managers;

import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.dtos.GroupDTO;
import cn.lunadeer.dominion.cache.CacheManager;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PlaceHolderApi extends PlaceholderExpansion {

    private final JavaPlugin plugin;

    public static PlaceHolderApi instance = null;

    public PlaceHolderApi(JavaPlugin plugin) {
        this.plugin = plugin;
        this.register();
        instance = this;
    }

    public static String setPlaceholders(Player player, String text) {
        if (instance == null) {
            return text;
        }
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    @Override
    public String onPlaceholderRequest(Player bukkitPlayer, @NotNull String params) {
        if (params.equalsIgnoreCase("group_title")) {
            Integer usingId = CacheManager.instance.getPlayerCache().getPlayerUsingTitleId(bukkitPlayer.getUniqueId());
            GroupDTO group = CacheManager.instance.getGroup(usingId);
            if (group == null) {
                return "";
            }
            return group.getNameColoredBukkit();
        }
        if (params.equalsIgnoreCase("current_dominion")) {
            DominionDTO dominion = CacheManager.instance.getDominion(bukkitPlayer.getLocation());
            if (dominion == null) {
                return "";
            }
            return dominion.getName();
        }
        if (params.startsWith("tp_loc_")) { // %dominion_tp_loc_<dominion_name>_x%
            String parm = params.substring(8); // Remove "tp_loc_"
            String coordinate = parm.substring(parm.lastIndexOf('_') + 1); // Get the last part after the last '_'
            String dominionName = parm.substring(0, parm.lastIndexOf('_')); // Get the dominion name part

            DominionDTO dominion = CacheManager.instance.getDominion(dominionName);
            if (dominion == null) {
                return null; // Dominion not found
            }

            return switch (coordinate) {
                case "x" -> String.valueOf(dominion.getTpLocation().getBlockX());
                case "y" -> String.valueOf(dominion.getTpLocation().getBlockY());
                case "z" -> String.valueOf(dominion.getTpLocation().getBlockZ());
                default -> null; // Invalid coordinate
            };
        }
        return null; //
    }

    @Override
    public @NotNull String getIdentifier() {
        return "dominion";
    }

    @Override
    public @NotNull String getAuthor() {
        return "zhangyuheng";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

}
