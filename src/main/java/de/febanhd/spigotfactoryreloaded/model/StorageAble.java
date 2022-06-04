package de.febanhd.spigotfactoryreloaded.model;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface StorageAble {

    List<ItemStack> getContent();

    void setContents(List<ItemStack> contents);

    void addItemStack(ItemStack stack);

}
