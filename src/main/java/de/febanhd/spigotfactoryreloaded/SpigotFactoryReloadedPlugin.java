package de.febanhd.spigotfactoryreloaded;

import de.febanhd.spigotfactoryreloaded.listener.BlockActionListener;
import de.febanhd.spigotfactoryreloaded.listener.HopperListener;
import de.febanhd.spigotfactoryreloaded.pipeline.PipelineManager;
import de.febanhd.spigotfactoryreloaded.pipeline.model.PipelineBlock;
import de.febanhd.spigotfactoryreloaded.power.PowerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;

import java.util.stream.Stream;

public class SpigotFactoryReloadedPlugin extends JavaPlugin {

    private PowerManager powerManager;
    private PipelineManager pipelineManager;

    @Override
    public void onEnable() {
        powerManager = new PowerManager();
        pipelineManager = new PipelineManager();

        //register listeners
        Stream.of(
                new BlockActionListener(this),
                new HopperListener(this)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));

        startTicking();
    }

    public void startTicking() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            powerManager.tick();
            pipelineManager.tick();
        }, 1, 1);

    }

    public PowerManager getPowerManager() {
        return powerManager;
    }

    public PipelineManager getPipelineManager() {
        return pipelineManager;
    }
}
