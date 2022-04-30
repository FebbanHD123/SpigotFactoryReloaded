package de.febanhd.spigotfactoryreloaded.data.impl;

import de.febanhd.spigotfactoryreloaded.data.AbstractBlockData;
import de.febanhd.spigotfactoryreloaded.model.TickAble;
import de.febanhd.spigotfactoryreloaded.utils.BlockUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class PipelineFilterBlockData extends AbstractBlockData {

    private final ItemStack[] items = new ItemStack[9];
    private Type type = Type.WHITELIST;
    private Filter filter = Filter.MATERIAL;
    private boolean prioritized = true;
    private int particleTimer;

    public PipelineFilterBlockData(Block block) {
        super(block);
        items[0] = new ItemStack(Material.GLASS);
        items[1] = new ItemStack(Material.GRASS_BLOCK);
    }

    public PipelineFilterBlockData() {

    }

    @Override
    public void encode(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(type.id);
        outputStream.writeInt(filter.id);
        outputStream.writeBoolean(this.prioritized);

        BukkitObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(outputStream);
        outputStream.writeInt(this.items.length);
        for (int i = 0; i < items.length; i++) {
            ItemStack stack = this.items[i];
            if (stack == null) {
                outputStream.writeInt(1);
            } else {
                outputStream.writeInt(0);
                objectOutputStream.writeObject(stack);
            }
        }
    }

    @Override
    public void decode(DataInputStream inputStream) throws IOException {
        this.type = Type.fromId(inputStream.readInt());
        this.filter = Filter.fromId(inputStream.readInt());
        this.prioritized = inputStream.readBoolean();

        BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(inputStream);
        int length = inputStream.readInt();
        for (int i = 0; i < length; i++) {
            int status = inputStream.readInt();
            if (status > 0) {
                this.items[i] = null;
                continue;
            }
            try {
                ItemStack stack = (ItemStack) objectInputStream.readObject();
                this.items[i] = stack;
            } catch (ClassNotFoundException e) {
                throw new IOException("Class not found: " + e.getMessage());
            }
        }
        items[0] = new ItemStack(Material.GLASS);
    }

    @Override
    public void tick0() {
        if (particleTimer > 0) {
            particleTimer--;
            return;
        }
        for (Entity nearbyEntity : this.getBlock().getWorld().getNearbyEntities(this.getBlock().getLocation(), 16, 16, 16, entity -> entity instanceof Player)) {
            Player player = (Player) nearbyEntity;
            player.spawnParticle(Particle.REDSTONE, BlockUtil.getCenterLocation(getBlock()), 1, new Particle.DustOptions(Color.ORANGE, 3));
        }
        particleTimer = 5;
    }

    public boolean isItemAllowed(ItemStack stack) {
        boolean filterResult = this.filter.test(this, stack);
        if (this.type == Type.WHITELIST && filterResult) {
            return true;
        } else
            return this.type == Type.BLACKLIST && !filterResult;
    }

    public boolean isPrioritized() {
        return prioritized;
    }

    @Override
    public void handleRemove(Block block) {
        //TODO: drop filter item
        Bukkit.broadcastMessage("Filter item dropped");
    }

    public enum Type {
        WHITELIST(1), BLACKLIST(2);

        private int id;

        Type(int id) {
            this.id = id;
        }

        private static Type fromId(int id) {
            for (Type value : Type.values()) {
                if (value.id == id)
                    return value;
            }
            return WHITELIST;
        }
    }

    public enum Filter {
        MATERIAL(1, (filterBlockData, testStack) -> Arrays.stream(filterBlockData.items).anyMatch(stack -> stack != null && stack.getType().equals(testStack.getType()))),
        EXACT(2, (filterBlockData, testStack) -> {
            return Arrays.stream(filterBlockData.items).anyMatch(stack -> {
                if (stack == null)
                    return false;

                ItemStack test = testStack.clone();
                test.setAmount(1);
                stack = stack.clone();
                stack.setAmount(1);
                return test.equals(stack);
            });
        });

        private final ItemStackTest test;
        private final int id;

        Filter(int id, ItemStackTest test) {
            this.test = test;
            this.id = id;
        }

        private static Filter fromId(int id) {
            for (Filter value : Filter.values()) {
                if (value.id == id)
                    return value;
            }
            return MATERIAL;
        }

        public boolean test(PipelineFilterBlockData filterBlockData, ItemStack testStack) {
            return this.test.test(filterBlockData, testStack);
        }

    }

    private interface ItemStackTest {

        boolean test(PipelineFilterBlockData filterBlockData, ItemStack testStack);

    }

}
