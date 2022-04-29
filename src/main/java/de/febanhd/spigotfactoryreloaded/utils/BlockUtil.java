package de.febanhd.spigotfactoryreloaded.utils;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.lang.invoke.CallSite;
import java.util.Arrays;
import java.util.List;

public class BlockUtil {

    public static List<Block> getNeighborBlocks(Block block, BlockFace... faceBlackList) {
        List<BlockFace> blackList = Arrays.asList(faceBlackList);
        List<Block> blocks = Lists.newArrayList();
        if(!blackList.contains(BlockFace.NORTH))
            blocks.add(block.getRelative(BlockFace.NORTH));
        if(!blackList.contains(BlockFace.EAST))
            blocks.add(block.getRelative(BlockFace.EAST));
        if(!blackList.contains(BlockFace.SOUTH))
            blocks.add(block.getRelative(BlockFace.SOUTH));
        if(!blackList.contains(BlockFace.WEST))
            blocks.add(block.getRelative(BlockFace.WEST));
        if(!blackList.contains(BlockFace.UP))
            blocks.add(block.getRelative(BlockFace.UP));
        if(!blackList.contains(BlockFace.DOWN))
            blocks.add(block.getRelative(BlockFace.DOWN));

        return blocks;
    }

    public static Location getCenterLocation(Block block) {
        Location location = block.getLocation().clone();
        location.setX(block.getX() + 0.5D);
        location.setY(block.getY() - 0.5D);
        location.setZ(block.getZ() + 0.5D);
        return location;
    }

}
