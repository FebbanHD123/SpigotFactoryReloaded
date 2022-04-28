package de.febanhd.spigotfactoryreloaded.power.model.components.consumers;

import de.febanhd.spigotfactoryreloaded.power.model.components.PowerConsumer;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;

public class FurnacePowerConsumer extends PowerConsumer {

    public FurnacePowerConsumer(Block block) {
        super(10000, block, 50, 1000, 1000);
    }

    @Override
    protected void work() {
        if(block.getState() instanceof Furnace furnace) {
            furnace.setBurnTime((short) 150);
            furnace.update();
        }
    }

    @Override
    protected boolean canWork() {
        return true;
    }
}
