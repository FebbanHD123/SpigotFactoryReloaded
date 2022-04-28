package de.febanhd.spigotfactoryreloaded.power.model.components.sources;

import de.febanhd.spigotfactoryreloaded.power.model.components.PowerSource;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class CreativePowerSource extends PowerSource {

    public CreativePowerSource(Block block) {
        super(1000, block, 500);
    }

    @Override
    public int getCurrentPower() {
        return 1000;
    }

    public static ItemStack create() {
        return new ItemStack(Material.BEACON);
    }
}
