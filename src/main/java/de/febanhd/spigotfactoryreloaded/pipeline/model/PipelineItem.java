package de.febanhd.spigotfactoryreloaded.pipeline.model;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class PipelineItem {

    private static final double CUSTOM_NAME_Y_OFFSET = 0.1;
    private final ItemStack itemStack;
    private ArmorStand armorStand;

    public PipelineItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void spawnArmorStand(Location spawnLocation) {
        if(this.armorStand != null && !this.armorStand.isDead())
            throw new IllegalStateException("Armorstand is already spawned");
        ArmorStand armorStand = (ArmorStand) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ARMOR_STAND);
        armorStand.setSmall(true);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.getEquipment().setHelmet(this.itemStack);
        if(this.itemStack.getAmount() > 1) {
            armorStand.setCustomName(this.itemStack.getAmount() + "x");
            armorStand.setCustomNameVisible(true);
        }
        this.armorStand = armorStand;
    }

    public void removeFromWorld() {
        this.armorStand.remove();
    }

    public Location getLocation() {
        Location location = this.armorStand.getLocation();
        if(armorStand.isCustomNameVisible())
            location.add(0, CUSTOM_NAME_Y_OFFSET, 0);
        return location.add(0, 1, 0);
    }

    public void setLocation(Location location, PipelineBlock block) {
        location.setDirection(block.block.getLocation().toVector().subtract(block.lastBlock.block.getLocation().toVector()));
        if(this.armorStand.isCustomNameVisible()) {
            location.subtract(0, CUSTOM_NAME_Y_OFFSET, 0);
        }
        armorStand.teleport(location.subtract(0, 1, 0));
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
