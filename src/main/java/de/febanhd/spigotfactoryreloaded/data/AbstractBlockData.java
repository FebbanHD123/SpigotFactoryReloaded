package de.febanhd.spigotfactoryreloaded.data;

import de.febanhd.spigotfactoryreloaded.model.TickAble;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public abstract class AbstractBlockData implements BlockData, TickAble {

    private Block block;
    private Location currentLocation;
    private Material currentMaterial;
    private boolean shouldRemove;
    private int timer;
    private Block blockToChange;


    public AbstractBlockData(Block block) {
        this.block = block;
    }

    public AbstractBlockData() {
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public void setBlock(Block block) {
        if(this.block == null) {
            this.block = block;
            return;
        }
        this.blockToChange = block;
    }

    @Override
    public void handleRemove(Block block) {

    }

    @Override
    public final void tick() {
        if(currentMaterial != null && block.getType() != currentMaterial && currentLocation.equals(block.getLocation())) {
            shouldRemove = true;
            timer++;
        }else if (shouldRemove) {
            timer = 0;
            shouldRemove = false;
        }

        if(!shouldRemove) {
            currentMaterial = block.getType();
            currentLocation = block.getLocation();
            tick0();
        }
        if(blockToChange != null) {
            this.block = blockToChange;
            this.shouldRemove = false;
            currentMaterial = block.getType();
        }
    }

    public boolean shouldRemove() {
        return shouldRemove && timer >= 10;
    }

    public abstract void tick0();
}
