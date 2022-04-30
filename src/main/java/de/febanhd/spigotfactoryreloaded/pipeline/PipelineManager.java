package de.febanhd.spigotfactoryreloaded.pipeline;

import com.google.common.collect.Lists;
import de.febanhd.spigotfactoryreloaded.SpigotFactoryReloadedPlugin;
import de.febanhd.spigotfactoryreloaded.model.TickAble;
import de.febanhd.spigotfactoryreloaded.pipeline.model.PipelineBlock;
import de.febanhd.spigotfactoryreloaded.pipeline.model.PipelineItem;
import de.febanhd.spigotfactoryreloaded.pipeline.model.blocks.PipelineBlockHopper;
import de.febanhd.spigotfactoryreloaded.utils.BlockUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PipelineManager implements TickAble {

    private final List<Block> hopperBlocks = Lists.newCopyOnWriteArrayList();
    private final long HOPPER_DELAY = 1500;
    private long lastHopperTick;
    private final SpigotFactoryReloadedPlugin plugin;
    //TODO: make hopper above glass not interactable


    public PipelineManager(SpigotFactoryReloadedPlugin plugin) {
        this.plugin = plugin;
    }

    public void add(Hopper hopper) {
        if(!this.hopperBlocks.contains(hopper.getLocation().getBlock()))
            this.hopperBlocks.add(hopper.getLocation().getBlock());
    }

    @Override
    public void tick() {
        hopperBlocks.removeIf(block -> !this.isValidHopper(block));
        if(lastHopperTick + HOPPER_DELAY < System.currentTimeMillis()) {
            lastHopperTick = System.currentTimeMillis();

            hopperBlocks.forEach(block -> {
                if (block.getState() instanceof Hopper hopper) {
                    List<ItemStack> inventoryContents = Arrays.asList(hopper.getInventory().getContents());
                    List<ItemStack> shuffledContents = Lists.newArrayList(inventoryContents);
                    Collections.shuffle(shuffledContents);
                    for (ItemStack stack : shuffledContents) {
                        int inventoryIndex = inventoryContents.indexOf(stack);
                        if (stack == null)
                            continue;
                        hopper.getInventory().setItem(inventoryIndex, null);
                        Block hopperDestinationBlock = hopper.getBlock().getRelative(BlockFace.DOWN);
                        PipelineBlock.getBlockIfValid(hopperDestinationBlock, new PipelineBlockHopper(this, block, null)).ifPresent(pipelineBlock -> {
                            Location spawnLocation = BlockUtil.getCenterLocation(hopperDestinationBlock).add(0, 0.5, 0);
                            pipelineBlock.setItem(new PipelineItem(spawnLocation, stack));
                        });
                        break;
                    }
                }
            });
        }
        PipelineBlock.tickAll();
    }

    private boolean isValidHopper(Block block) {
        return block.getState() instanceof Hopper && block.getRelative(BlockFace.DOWN).getType() == Material.GLASS;
    }

    public SpigotFactoryReloadedPlugin getPlugin() {
        return plugin;
    }
}
