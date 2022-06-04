package de.febanhd.spigotfactoryreloaded.listener;

import de.febanhd.spigotfactoryreloaded.SpigotFactoryReloadedPlugin;
import de.febanhd.spigotfactoryreloaded.pipeline.blockdata.PipelineFilterBlockData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {
    private final SpigotFactoryReloadedPlugin plugin;

    public PlayerListener(SpigotFactoryReloadedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();
            if(clickedBlock.getType() == Material.GLASS && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.GOLD_INGOT) {
                if (plugin.getBlockDataManager().getBlockData(clickedBlock, PipelineFilterBlockData.class).isPresent()) {
                    event.getPlayer().sendMessage("Has already a filter");
                    return;
                }
                PipelineFilterBlockData filterBlockData = new PipelineFilterBlockData(clickedBlock);
                plugin.getBlockDataManager().addBlockData(filterBlockData);
                event.getPlayer().sendMessage("Added filter to block");
            }
        }
    }
}
