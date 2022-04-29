package de.febanhd.spigotfactoryreloaded.power.model;

import de.febanhd.spigotfactoryreloaded.model.TickAble;
import de.febanhd.spigotfactoryreloaded.power.PowerManager;
import de.febanhd.spigotfactoryreloaded.power.model.components.PowerConsumer;
import de.febanhd.spigotfactoryreloaded.power.model.components.PowerSource;
import de.febanhd.spigotfactoryreloaded.utils.BlockUtil;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class PowerSystem implements PowerAble, TickAble {
    private static final int POWER_LIMIT = 1000000000;
    private final PowerManager powerManager;
    private final World world;
    private final List<PowerSystemComponent> components;
    private int currentPower;
    private int powerConsumerAmount;

    public PowerSystem(PowerManager powerManager, World world) {
        this.powerManager = powerManager;
        this.world = world;
        this.components = new CopyOnWriteArrayList<>();
    }

    public void applyPower(int power) {
        this.currentPower += power;
        if(currentPower >= POWER_LIMIT) {
            currentPower = POWER_LIMIT;
        }
    }

    public void addComponent(PowerSystemComponent component) {
        if(hasComponentAt(component.getBlock()))
            return;

        component.setSystem(this);

        if(component instanceof PowerConsumer) {
            powerConsumerAmount++;
        }

        this.components.add(component);
    }

    public void resetSystem() {
        this.powerManager.resetSystem(this);
    }

    public void tick() {
        //reset current power
        AtomicBoolean resetSystem = new AtomicBoolean(false);
        for (PowerSystemComponent component : this.components) {
            if(resetSystem.get())
                break;

            if(component.getSystem() != this) {
                this.components.remove(component);
                continue;
            }

            //TODO: check if chunk is loaded
            Block block = this.world.getBlockAt(component.getBlock().getLocation());

            if (this.getComponentAt(block).isEmpty()) {
                resetSystem.set(true);
                break;
            }

            for (Block neighborBlock : BlockUtil.getNeighborBlocks(block)) {
                if(!hasComponentAt(neighborBlock)) {
                    this.powerManager.createComponentIfValid(neighborBlock).ifPresent(this::addComponent);
                }
            }
        }
        if(resetSystem.get() || !hasPowerSource()) {
            this.resetSystem();
            return;
        }

        this.components.stream()
                .filter(component -> component instanceof TickAble)
                .forEach(component -> ((TickAble)component).tick());

        int powerPerConsumer = (int) Math.floor((float)this.currentPower / this.powerConsumerAmount);
        this.components.forEach(component -> {
            if(component instanceof PowerConsumer powerConsumer) {
                int power;
                int powerPerTick = powerConsumer.getPowerPerTick();
                if(powerPerConsumer > powerPerTick) {
                    power = powerPerTick;
                }else {
                    power = powerPerConsumer;
                }
                this.currentPower -= power;
                powerConsumer.givePower(power);
            }
        });

    }

    @Override
    public int getCurrentPower() {
        return currentPower;
    }

    public int getPowerOfComponent(PowerSystemComponent component) {
        if(component.getMaxPower() < this.currentPower) {
            return component.getMaxPower();
        }
        return this.currentPower;
    }

    @Override
    public int getMaxPower() {
        return -1;
    }

    private boolean hasComponentAt(Block block) {
        return getComponentAt(block).isPresent();
    }

    public Optional<PowerSystemComponent> getComponentAt(Block block) {
        return this.components
                .stream()
                .filter(component -> component.getBlock().equals(block) && block.getType() == component.getMaterial())
                .findFirst();
    }

    public List<PowerSystemComponent> getComponents() {
        return components;
    }

    public boolean hasPowerSource() {
        return this.components.stream().anyMatch(component -> component instanceof PowerSource);
    }
}
