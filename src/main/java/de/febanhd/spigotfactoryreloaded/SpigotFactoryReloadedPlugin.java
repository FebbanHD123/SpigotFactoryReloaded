package de.febanhd.spigotfactoryreloaded;

import de.febanhd.spigotfactoryreloaded.listener.BlockActionListener;
import de.febanhd.spigotfactoryreloaded.listener.HopperListener;
import de.febanhd.spigotfactoryreloaded.pipeline.PipelineManager;
import de.febanhd.spigotfactoryreloaded.power.PowerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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
