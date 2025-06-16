package cn.lunadeer.dominion.v1_20_1.events.environment;

import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.dtos.flag.Flags;
import cn.lunadeer.dominion.cache.CacheManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

import static cn.lunadeer.dominion.misc.Others.checkEnvironmentFlag;

public class SnowMelt implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void handler(BlockFadeEvent event) {
        if (!event.getBlock().getType().name().contains("SNOW")) {
            return;
        }
        DominionDTO dom = CacheManager.instance.getDominion(event.getBlock().getLocation());
        checkEnvironmentFlag(dom, Flags.SNOW_MELT, event);
    }
}
