package cn.lunadeer.dominion.v1_20_1.events.environment.Wither;

import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.dtos.flag.Flags;
import cn.lunadeer.dominion.cache.CacheManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import static cn.lunadeer.dominion.misc.Others.checkEnvironmentFlag;

public class BreakBlockOnHarmed implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void handler(EntityChangeBlockEvent event) {
        if (event.getEntity().getType() != org.bukkit.entity.EntityType.WITHER) {
            return;
        }
        DominionDTO dom = CacheManager.instance.getDominion(event.getBlock().getLocation());
        checkEnvironmentFlag(dom, Flags.WITHER_SPAWN, event);
    }
}
