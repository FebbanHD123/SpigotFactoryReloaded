package de.febanhd.spigotfactoryreloaded.power;

import com.google.common.collect.Lists;
import de.febanhd.spigotfactoryreloaded.SpigotFactoryReloadedPlugin;
import de.febanhd.spigotfactoryreloaded.model.TickAble;
import de.febanhd.spigotfactoryreloaded.power.model.PowerSystem;
import de.febanhd.spigotfactoryreloaded.power.model.PowerSystemComponent;
import de.febanhd.spigotfactoryreloaded.power.model.components.PowerConductor;
import de.febanhd.spigotfactoryreloaded.power.model.components.PowerSource;
import de.febanhd.spigotfactoryreloaded.power.model.components.consumers.FurnacePowerConsumer;
import de.febanhd.spigotfactoryreloaded.power.model.components.sources.CreativePowerSource;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.RayTraceResult;

import java.util.List;
import java.util.Optional;

public class PowerManager implements TickAble {

    private final List<PowerSystem> systems = Lists.newLinkedList();
    private final PowerSystemCreator creator = new PowerSystemCreator(this);

    public PowerManager() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SpigotFactoryReloadedPlugin.getPlugin(SpigotFactoryReloadedPlugin.class), () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                RayTraceResult rayTraceResult = player.rayTraceBlocks(4);
                if(rayTraceResult == null) return;
                Block block = rayTraceResult.getHitBlock();
                if(block != null) {
                    for (PowerSystem system : this.systems) {
                        system.getComponents().stream().filter(component -> component.getBlock().equals(block)).findFirst().ifPresent(component -> {
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§bPower: " + component.getCurrentPower() + "/"
                                    + component.getMaxPower() + " | System: " + (this.systems.indexOf(system) + 1)  + "/" +
                                    this.systems.size() + " all: " + system.getCurrentPower()));
                        });
                    }
                }
            });
        }, 1, 1);
    }

    @Override
    public void tick() {
        this.systems.forEach(PowerSystem::tick);
    }

    public void createSystemIfValid(Block block) {
        this.creator.createIfValid(block).ifPresent(this.systems::add);
    }

    public void removeSystem(PowerSystem system) {
        this.systems.remove(system);
    }

    public void resetSystem(PowerSystem system) {
        this.removeSystem(system);
        system.getComponents()
                .stream()
                .filter(component -> component instanceof PowerSource)
                .forEach(component -> this.createSystemIfValid(component.getBlock()));
    }

    public Optional<PowerSystemComponent> createComponentIfValid(Block block) {

        for (PowerSystem system : this.systems) {
            for (PowerSystemComponent component : system.getComponents()) {
                if(component.getBlock().equals(block) && component.getMaterial() == block.getType())
                    return Optional.of(component);
            }
        }

        if(block.getType() == Material.BEACON) {
            return Optional.of(new CreativePowerSource(block));
        }
        if(block.getType() == Material.IRON_BARS)
            return Optional.of(new PowerConductor(2000, block));

        if(block.getType() == Material.FURNACE)
            return Optional.of(new FurnacePowerConsumer(block));
        return Optional.empty();
    }
}
