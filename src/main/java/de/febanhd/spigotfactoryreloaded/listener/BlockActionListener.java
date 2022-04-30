package de.febanhd.spigotfactoryreloaded.listener;

import de.febanhd.spigotfactoryreloaded.SpigotFactoryReloadedPlugin;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class BlockActionListener implements Listener {

    private final SpigotFactoryReloadedPlugin plugin;

    public BlockActionListener(SpigotFactoryReloadedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        this.plugin.getPowerManager().createSystemIfValid(event.getBlock());
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event) {
        this.plugin.getBlockDataManager().removeAllData(event.getBlock());
    }

    @EventHandler
    public void handlePistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (this.plugin.getBlockDataManager().hasBlockData(block)) {
                Block nextBlock = block.getRelative(event.getDirection());
                this.plugin.getBlockDataManager().transferBlockData(block, nextBlock);
            }
        }
    }

    @EventHandler
    public void handlePistonRetract(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            if (this.plugin.getBlockDataManager().hasBlockData(block)) {
                Block nextBlock = block.getRelative(event.getDirection());
                this.plugin.getBlockDataManager().transferBlockData(block, nextBlock);
            }
        }
    }
}
