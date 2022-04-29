package de.febanhd.spigotfactoryreloaded.pipeline.model.blocks;

import de.febanhd.spigotfactoryreloaded.pipeline.model.PipelineBlock;
import de.febanhd.spigotfactoryreloaded.pipeline.model.PipelineItem;
import de.febanhd.spigotfactoryreloaded.utils.BlockUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Map;

public class PipelineBlockHopper extends PipelineBlock {

    public PipelineBlockHopper(Block block, PipelineBlock lastBlock) {
        super(block, lastBlock);
    }

    @Override
    public void processItem(PipelineItem item) {
        item.removeFromWorld();
        if(block.getState() instanceof Hopper hopper) {
            hopper.getInventory().addItem(item.getItemStack());
        }
        this.finish();
    }

    @Override
    public boolean canCollectItem(PipelineItem item) {
        if(block.getState() instanceof Hopper hopper) {
            Inventory testInventory = Bukkit.createInventory(null, InventoryType.HOPPER);
            testInventory.setContents(hopper.getInventory().getContents());
            Map<Integer, ItemStack> map = testInventory.addItem(item.getItemStack());
            return map.isEmpty();
        }
        return false;
    }
}