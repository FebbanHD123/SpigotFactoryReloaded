package de.febanhd.spigotfactoryreloaded.power.model.components;

import de.febanhd.spigotfactoryreloaded.model.TickAble;
import de.febanhd.spigotfactoryreloaded.power.model.PowerSystemComponent;
import org.bukkit.block.Block;

public abstract class PowerSource extends PowerSystemComponent implements TickAble {

    private final int powerPerTick;

    public PowerSource(int maxPower, Block block, int powerPerTick) {
        super(maxPower, block);
        this.powerPerTick = powerPerTick;
    }

    @Override
    public final void tick() {
        this.system.applyPower(powerPerTick);
    }
}
