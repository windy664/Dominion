package cn.lunadeer.dominion.utils;

import cn.lunadeer.dominion.managers.PlaceHolderApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Misc {

    public static boolean isPaper() {
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.ScheduledTask");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static String formatString(String str, Object... args) {
        String formatStr = str;
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                args[i] = "[null for formatString (args[" + i + "])]";
            }
            formatStr = formatStr.replace("{" + i + "}", args[i].toString());
        }
        return formatStr;
    }

    public static List<String> formatStringList(List<String> list, Object... args) {
        List<String> formattedList = new ArrayList<>(list);
        for (int i = 0; i < args.length; i++) {
            for (int j = 0; j < list.size(); j++) {
                formattedList.set(j, formattedList.get(j).replace("{" + i + "}", args[i].toString()));
            }
        }
        return formattedList;
    }

    public static List<String> foldLore2Line(String str, int length) {
        List<String> result = new ArrayList<>();
        if (str.length() > length) {
            result.add(str.substring(0, length));
            if (str.length() > 2 * length) {
                result.add(str.substring(length, 2 * length - 3) + "...");
            } else {
                result.add(str.substring(length));
            }
        } else {
            result.add(str);
            result.add("");
        }
        return result;
    }

    public static List<String> listClassOfPackage(JavaPlugin plugin, String packageName) {
        List<String> classesInPackage = new ArrayList<>();
        // list all classes in the packageName package
        String path = packageName.replace('.', '/');
        URL packageDir = plugin.getClass().getClassLoader().getResource(path);
        if (packageDir == null) {
            return classesInPackage;
        }
        String packageDirPath = packageDir.getPath();
        XLogger.debug("packageDirPath raw: {0}", packageDirPath);
        // if the package is in a jar file, unpack it and list the classes
        packageDirPath = packageDirPath.substring(0, packageDirPath.indexOf("jar!") + 4);
        packageDirPath = packageDirPath.replace("file:", "");
        packageDirPath = packageDirPath.replace("!", "");
        packageDirPath = java.net.URLDecoder.decode(packageDirPath, java.nio.charset.StandardCharsets.UTF_8);
        XLogger.debug("packageDirPath processed: {0}", packageDirPath);
        // unpack the jar file
        XLogger.debug("Unpacking class in jar: {0}", packageDirPath);
        File jarFile = new File(packageDirPath);
        if (!jarFile.exists() || !jarFile.isFile()) {
            XLogger.debug("Skipping {0} because it is not a jar file", packageDirPath);
            return classesInPackage;
        }
        // list the classes in the jar file
        try (java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile)) {
            jar.stream().filter(entry -> entry.getName().endsWith(".class") && entry.getName().startsWith(path))
                    .forEach(entry -> classesInPackage.add(entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6)));
        } catch (Exception e) {
            XLogger.debug("Failed to list classes in jar: {0}", e.getMessage());
            return classesInPackage;
        }
        return classesInPackage;
    }

    /**
     * Set placeholder for the message.
     * <p>
     * Use this method instead of PlaceholderAPI directly to avoid not installed PlaceholderAPI
     * throwing NoClassDefFoundError.
     *
     * @param player  the player
     * @param message the message
     * @return the message with placeholder
     */
    public static String setPlaceholder(Player player, String message) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PlaceHolderApi.setPlaceholders(player, message);
        } else {
            return message;
        }
    }

}
