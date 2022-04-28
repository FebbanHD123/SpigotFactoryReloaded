package de.febanhd.spigotfactoryreloaded.power;

import de.febanhd.spigotfactoryreloaded.power.model.PowerSystem;
import de.febanhd.spigotfactoryreloaded.power.model.PowerSystemComponent;
import de.febanhd.spigotfactoryreloaded.power.model.components.PowerSource;
import org.bukkit.block.Block;

import java.util.Optional;

public class PowerSystemCreator {

    private final PowerManager powerManager;

    public PowerSystemCreator(PowerManager powerManager) {
        this.powerManager = powerManager;
    }

    public Optional<PowerSystem> createIfValid(Block block) {
        PowerSystemComponent startComponent = null;
        Optional<PowerSystemComponent> componentOptional = powerManager.createComponentIfValid(block);
        if(componentOptional.isEmpty())
            return Optional.empty();

        PowerSystemComponent component = componentOptional.get();
        if(component instanceof PowerSource) {
            startComponent = component;
        }

        if(startComponent != null) {
            PowerSystem system = new PowerSystem(powerManager, block.getWorld());
            system.addComponent(startComponent);
            return Optional.of(system);
        }
        return Optional.empty();
    }
}
