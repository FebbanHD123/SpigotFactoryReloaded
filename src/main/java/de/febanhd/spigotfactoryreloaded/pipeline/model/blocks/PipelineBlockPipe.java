package de.febanhd.spigotfactoryreloaded.pipeline.model.blocks;

import de.febanhd.spigotfactoryreloaded.pipeline.model.PipelineBlock;
import de.febanhd.spigotfactoryreloaded.pipeline.model.PipelineItem;
import de.febanhd.spigotfactoryreloaded.utils.BlockUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class PipelineBlockPipe extends PipelineBlock {

    private final static float SPEED = 0.1F;
    private final Location centerLocation;
    private int steps = -1;

    public PipelineBlockPipe(Block block, PipelineBlock lastBlock) {
        super(block, lastBlock);
        this.centerLocation = BlockUtil.getCenterLocation(block);
    }

    @Override
    public void processItem(PipelineItem item) {
        if(steps < 0)
            return;

        Location itemLocation = item.getLocation();

        //go to next block
        if(steps < 1) {
            this.finish();
            getNextBlock(this).ifPresentOrElse(pipelineBlock -> pipelineBlock.setItem(item), () -> {
                if(this.lastBlock.canCollectItem(item)) {
                    lastBlock.setLastBlock(this);
                    lastBlock.setItem(item);
                    lastBlock.activate();
                    return;
                }
                forceStop(itemLocation);
            });
            return;
        }
        steps--;
        if(this.block.getType() != Material.GLASS) {
            forceStop(itemLocation);
            finish();
            return;
        }
        Vector vector = centerLocation.toVector().subtract(itemLocation.toVector()).normalize().multiply(SPEED);
        Location nextLocation = itemLocation.clone().add(vector);
        item.setLocation(nextLocation, this);
    }

    private void forceStop(Location itemLocation) {
        item.removeFromWorld();
        itemLocation.getWorld().dropItem(itemLocation, item.getItemStack());
    }

    @Override
    public void setItem(PipelineItem item) {
        super.setItem(item);
        double distance = centerLocation.distance(item.getLocation());
        this.steps = (int) Math.floor(distance / SPEED);
    }

    @Override
    public boolean canCollectItem(PipelineItem item) {
        return true;
    }
}
