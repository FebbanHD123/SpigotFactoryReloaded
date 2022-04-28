package de.febanhd.spigotfactoryreloaded.power.model.components;

import de.febanhd.spigotfactoryreloaded.power.model.PowerSystemComponent;
import org.bukkit.block.Block;

public class PowerConductor extends PowerSystemComponent {

    public PowerConductor(int maxPower, Block block) {
        super(maxPower, block);
    }

    @Override
    public int getCurrentPower() {
        return this.system != null ? this.system.getPowerOfComponent(this) : 0;
    }
}
