package de.febanhd.spigotfactoryreloaded.listener;

import de.febanhd.spigotfactoryreloaded.SpigotFactoryReloadedPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.io.IOException;

public class WorldListener implements Listener {

    private final SpigotFactoryReloadedPlugin plugin;

    public WorldListener(SpigotFactoryReloadedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handleWorldLoad(WorldLoadEvent event) {
        try {
            plugin.getLogger().info("Loading block data for world: " + event.getWorld().getName());
            plugin.getBlockDataManager().loadData(event.getWorld());
        } catch (IOException e) {
            plugin.getLogger().warning("Can't load block data for world: " + event.getWorld().getName());
            e.printStackTrace();
        }
    }

    @EventHandler
    public void handleWorldUnload(WorldUnloadEvent event) {
        try {
            plugin.getLogger().info("Saving block data for world: " + event.getWorld().getName());
            plugin.getBlockDataManager().safeData(event.getWorld());
        } catch (IOException e) {
            plugin.getLogger().warning("Can't save block data for world: " + event.getWorld().getName());
            e.printStackTrace();
        }
    }
}
