package de.febanhd.spigotfactoryreloaded.power.model.components;

import de.febanhd.spigotfactoryreloaded.model.TickAble;
import de.febanhd.spigotfactoryreloaded.power.model.PowerSystemComponent;
import org.bukkit.block.Block;

public abstract class PowerConsumer extends PowerSystemComponent implements TickAble {

    private final int powerPerTick;
    private final int workPower;
    private final long workDelay;
    private long lastWorkTime;
    private int currentPower;

    public PowerConsumer(int maxPower, Block block, int powerPerTick, int workPower, long workDelay) {
        super(maxPower, block);
        this.powerPerTick = powerPerTick;
        this.workPower = workPower;
        this.workDelay = workDelay;
    }

    @Override
    public int getCurrentPower() {
        return currentPower;
    }

    @Override
    public final void tick() {
        if(this.lastWorkTime + workDelay < System.currentTimeMillis() && this.currentPower >= this.workPower && canWork()) {
            this.lastWorkTime = System.currentTimeMillis();
            this.currentPower -= this.workPower;
            this.work();
        }
    }

    public void givePower(int power) {
        this.currentPower += power;
    }

    protected abstract void work();

    protected abstract boolean canWork();

    public int getPowerPerTick() {
        return powerPerTick;
    }

    public int getWorkPower() {
        return workPower;
    }

    public long getWorkDelay() {
        return workDelay;
    }
}
