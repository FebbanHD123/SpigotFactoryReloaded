package de.febanhd.spigotfactoryreloaded.listener;

import de.febanhd.spigotfactoryreloaded.SpigotFactoryReloadedPlugin;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;

public class HopperListener implements Listener {
    private final SpigotFactoryReloadedPlugin plugin;

    public HopperListener(SpigotFactoryReloadedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handleHopperTick(InventoryMoveItemEvent event) {
        if(event.getDestination().getType() != InventoryType.HOPPER) return;
        if(event.getDestination().getLocation().getBlock().getState() instanceof Hopper hopper) {
            this.plugin.getPipelineManager().add(hopper);
        }
    }

}
