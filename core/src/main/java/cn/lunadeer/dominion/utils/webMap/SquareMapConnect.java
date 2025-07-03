package cn.lunadeer.dominion.utils.webMap;

import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.utils.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SquareMapConnect extends WebMapRender {

    private final Squaremap api;

    private final Key MCAKey = Key.of("mca_layer_key");
    private final Key dominionKey = Key.of("dominion_layer_key");

    public SquareMapConnect() {
        WebMapRender.webMapInstances.add(this);
        api = SquaremapProvider.get();
        Bukkit.getWorlds().forEach(world -> {
            api.getWorldIfEnabled(BukkitAdapter.worldIdentifier(world)).ifPresent(mapWorld -> {
                SimpleLayerProvider dominionProvider = SimpleLayerProvider.builder("Dominion")
                        .showControls(true)
                        .build();
                mapWorld.layerRegistry().register(dominionKey, dominionProvider);

                SimpleLayerProvider MCAProvider = SimpleLayerProvider.builder("MCA")
                        .showControls(true)
                        .build();
                mapWorld.layerRegistry().register(MCAKey, MCAProvider);
            });
        });
    }

    @Override
    protected void renderDominions(@NotNull List<DominionDTO> dominions) {
        Scheduler.runTaskAsync(() -> {
            Map<World, List<DominionDTO>> dominionMap = dominions.stream()
                    .filter(dominion -> dominion.getWorld() != null)
                    .collect(Collectors.groupingBy(DominionDTO::getWorld));
            dominionMap.forEach((world, dominionList) -> {
                api.getWorldIfEnabled(BukkitAdapter.worldIdentifier(world)).ifPresent(mapWorld -> {
                    SimpleLayerProvider dominionProvider = (SimpleLayerProvider) mapWorld.layerRegistry().get(dominionKey);
                    dominionList.forEach(dominion -> {
                        Point p1 = Point.of(dominion.getCuboid().x1(), dominion.getCuboid().z1());
                        Point p2 = Point.of(dominion.getCuboid().x2(), dominion.getCuboid().z2());
                        int r = dominion.getColorR();
                        int g = dominion.getColorG();
                        int b = dominion.getColorB();

                        Marker marker = Marker.rectangle(p1, p2).markerOptions(
                                MarkerOptions.builder()
                                        .fillColor(new Color(r, g, b)).fillOpacity(0.2)
                                        .strokeColor(new Color(r, g, b)).strokeOpacity(0.8)
                                        .build()
                        );
                        Key key = Key.of("dominion_" + dominion.getId());
                        dominionProvider.removeMarker(key);
                        dominionProvider.addMarker(key, marker);
                    });
                    mapWorld.layerRegistry().unregister(dominionKey);
                    mapWorld.layerRegistry().register(dominionKey, dominionProvider);
                });
            });
        });
    }

    @Override
    protected void renderMCA(@NotNull Map<String, List<String>> mcaFiles) {
        Scheduler.runTaskAsync(() -> {
            mcaFiles.forEach((worldName, files) -> {
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    return; // Skip if the world is null
                }
                api.getWorldIfEnabled(BukkitAdapter.worldIdentifier(world)).ifPresent(mapWorld -> {
                    SimpleLayerProvider MCAProvider = (SimpleLayerProvider) mapWorld.layerRegistry().get(MCAKey);
                    files.forEach(file -> {
                        String[] cords = file.split("\\.");
                        int world_x1 = Integer.parseInt(cords[1]) * 512;
                        int world_z1 = Integer.parseInt(cords[2]) * 512;
                        int world_x2 = (Integer.parseInt(cords[1]) + 1) * 512;
                        int world_z2 = (Integer.parseInt(cords[2]) + 1) * 512;
                        Point p1 = Point.of(world_x1, world_z1);
                        Point p2 = Point.of(world_x2, world_z2);
                        Marker marker = Marker.rectangle(p1, p2).markerOptions(
                                MarkerOptions.builder()
                                        .fillColor(new Color(0, 204, 0)).fillOpacity(0.2)
                                        .strokeColor(new Color(0, 204, 0)).strokeOpacity(0.8)
                                        .build()
                        );
                        Key key = Key.of("mca_" + worldName + "_" + cords[1] + "_" + cords[2]);
                        MCAProvider.removeMarker(key);
                        MCAProvider.addMarker(key, marker);
                    });
                    mapWorld.layerRegistry().unregister(MCAKey);
                    mapWorld.layerRegistry().register(MCAKey, MCAProvider);
                });
            });
        });
    }
}
