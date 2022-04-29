package de.febanhd.spigotfactoryreloaded.power.model;

import org.bukkit.Material;
import org.bukkit.block.Block;

public abstract class PowerSystemComponent implements PowerAble {
    protected final int maxPower;
    protected PowerSystem system;
    protected Block block;
    protected Material material;

    public PowerSystemComponent(int maxPower, Block block) {
        this.maxPower = maxPower;
        this.block = block;
        this.material = block.getType();
    }

    @Override
    public int getMaxPower() {
        return this.maxPower;
    }

    public PowerSystem getSystem() {
        return system;
    }

    public void setSystem(PowerSystem system) {
        this.system = system;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Material getMaterial() {
        return material;
    }
}
