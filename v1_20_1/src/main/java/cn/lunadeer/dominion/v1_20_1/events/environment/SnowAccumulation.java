package cn.lunadeer.dominion.v1_20_1.events.environment;

import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.dtos.flag.Flags;
import cn.lunadeer.dominion.cache.CacheManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import static cn.lunadeer.dominion.misc.Others.checkEnvironmentFlag;

public class SnowAccumulation implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void handler(BlockFormEvent event) {
        if (!event.getNewState().getType().name().contains("SNOW")) {
            return;
        }
        DominionDTO dom = CacheManager.instance.getDominion(event.getBlock().getLocation());
        checkEnvironmentFlag(dom, Flags.SNOW_ACCUMULATION, event);
    }
}
