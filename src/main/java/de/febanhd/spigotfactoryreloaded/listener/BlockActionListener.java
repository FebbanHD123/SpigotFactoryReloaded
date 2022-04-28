package de.febanhd.spigotfactoryreloaded.listener;

import de.febanhd.spigotfactoryreloaded.SpigotFactoryReloadedPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockActionListener implements Listener {

    private final SpigotFactoryReloadedPlugin plugin;

    public BlockActionListener(SpigotFactoryReloadedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        this.plugin.getPowerManager().createSystemIfValid(event.getBlock());
    }
}
