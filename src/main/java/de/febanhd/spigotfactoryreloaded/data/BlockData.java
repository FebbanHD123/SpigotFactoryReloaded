package de.febanhd.spigotfactoryreloaded.data;

import org.bukkit.block.Block;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface BlockData {
    void encode(DataOutputStream outputStream) throws IOException;
    void decode(DataInputStream inputStream) throws IOException;
    Block getBlock();
    void setBlock(Block block);
    void handleRemove(Block block);
}
