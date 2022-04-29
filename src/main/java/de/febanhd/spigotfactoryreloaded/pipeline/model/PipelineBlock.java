package de.febanhd.spigotfactoryreloaded.pipeline.model;

import com.google.common.collect.Lists;
import de.febanhd.spigotfactoryreloaded.model.TickAble;
import de.febanhd.spigotfactoryreloaded.pipeline.model.blocks.PipelineBlockHopper;
import de.febanhd.spigotfactoryreloaded.pipeline.model.blocks.PipelineBlockPipe;
import de.febanhd.spigotfactoryreloaded.utils.BlockUtil;
import org.bukkit.block.Block;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class PipelineBlock implements TickAble {

    private static final List<PipelineBlock> ACTIVE_BLOCKS = Lists.newArrayList();

    public static void tickAll() {
        List<PipelineBlock> listCopy = Lists.newArrayList(ACTIVE_BLOCKS);
        listCopy.forEach(PipelineBlock::tick);
    }

    protected final Block block;
    protected PipelineBlock lastBlock;
    protected PipelineItem item;

    public PipelineBlock(Block block, PipelineBlock lastBlock) {
        this.block = block;
        this.lastBlock = lastBlock;
        activate();
    }

    public void activate() {
        ACTIVE_BLOCKS.add(this);
    }

    @Override
    public void tick() {
        if(item != null) {
            this.processItem(this.item);
        }
    }

    public abstract void processItem(PipelineItem item);

    protected void finish() {
        ACTIVE_BLOCKS.remove(this);
    }

    public void setItem(PipelineItem item) {
        this.item = item;
    }

    protected Optional<PipelineBlock> getNextBlock(PipelineBlock currentBlock) {
        List<Block> neighborBlocks = BlockUtil.getNeighborBlocks(currentBlock.block);
        neighborBlocks.removeIf(block -> this.lastBlock != null && this.lastBlock.block.getLocation().equals(block.getLocation()));
        Collections.shuffle(neighborBlocks);
        Optional<PipelineBlock> block;
        int i = 0;
        do {
            block = getBlockIfValid(neighborBlocks.get(i), this);
            i++;
        }while (i < neighborBlocks.size() && block.isEmpty());
        return block;
    }

    public static Optional<PipelineBlock> getBlockIfValid(Block block, PipelineBlock currentBlock) {
        Optional<PipelineBlock> blockOptional = switch (block.getType()) {

            case GLASS ->  Optional.of(new PipelineBlockPipe(block, currentBlock));
            case HOPPER -> Optional.of(new PipelineBlockHopper(block, currentBlock));

            default -> Optional.empty();
        };
        if(blockOptional.isEmpty())
            return blockOptional;

        if(currentBlock.item != null) {
            PipelineBlock nextBlock = blockOptional.get();
            if (!nextBlock.canCollectItem(currentBlock.item)) {
                return Optional.empty();
            }
        }
        return blockOptional;

    }

    public abstract boolean canCollectItem(PipelineItem item);

    public void setLastBlock(PipelineBlock lastBlock) {
        this.lastBlock = lastBlock;
    }

    public PipelineBlock getLastBlock() {
        return lastBlock;
    }
}
