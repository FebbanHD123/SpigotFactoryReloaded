package de.febanhd.spigotfactoryreloaded.utils;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Arrays;
import java.util.List;

public class BlockUtil {

    public static List<Block> getNeighborBlocks(Block block) {
        return Arrays.asList(
                block.getRelative(BlockFace.NORTH),
                block.getRelative(BlockFace.EAST),
                block.getRelative(BlockFace.SOUTH),
                block.getRelative(BlockFace.WEST),
                block.getRelative(BlockFace.UP),
                block.getRelative(BlockFace.DOWN)
        );
    }

}
