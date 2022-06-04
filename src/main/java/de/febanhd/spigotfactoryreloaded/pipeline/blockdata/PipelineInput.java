package de.febanhd.spigotfactoryreloaded.pipeline.blockdata;

import de.febanhd.spigotfactoryreloaded.data.AbstractBlockData;
import de.febanhd.spigotfactoryreloaded.model.StorageAble;
import de.febanhd.spigotfactoryreloaded.pipeline.model.PipelineBlock;
import de.febanhd.spigotfactoryreloaded.pipeline.model.PipelineItem;
import de.febanhd.spigotfactoryreloaded.pipeline.model.blocks.PipelineBlockHopper;
import de.febanhd.spigotfactoryreloaded.utils.BlockUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PipelineInput extends AbstractBlockData {

    @Override
    public void tick0() {
        if(this.getBlock() instanceof StorageAble storageAble) {
            for (ItemStack stack : storageAble.getContent()) {
                for (Block neighborBlock : BlockUtil.getNeighborBlocks(getBlock())) {
                    PipelineBlock.getBlockIfValid(neighborBlock, new PipelineBlockHopper(this, getBlock(), null)).ifPresent(pipelineBlock -> {
                        Location spawnLocation = BlockUtil.getCenterLocation(neighborBlock).add(0, 0.5, 0);
                        PipelineItem pipelineItem = new PipelineItem(stack);
                        if(pipelineBlock.canCollectItem(pipelineItem)) {
                            pipelineItem.spawnArmorStand(spawnLocation);
                            pipelineBlock.setItem(pipelineItem);
                            hopper.getInventory().setItem(inventoryIndex, null);
                            created.set(true);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void encode(DataOutputStream outputStream) throws IOException {

    }

    @Override
    public void decode(DataInputStream inputStream) throws IOException {

    }
}
