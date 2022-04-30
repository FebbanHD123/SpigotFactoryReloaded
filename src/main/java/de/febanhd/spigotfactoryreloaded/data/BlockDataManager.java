package de.febanhd.spigotfactoryreloaded.data;

import com.google.common.collect.Lists;
import de.febanhd.spigotfactoryreloaded.SpigotFactoryReloadedPlugin;
import de.febanhd.spigotfactoryreloaded.model.TickAble;
import de.febanhd.spigotfactoryreloaded.utils.BlockUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BlockDataManager implements TickAble {

    private final List<BlockData> blockDataList = Lists.newArrayList();
    private final SpigotFactoryReloadedPlugin plugin;

    public BlockDataManager(SpigotFactoryReloadedPlugin plugin) {
        this.plugin = plugin;
    }

    public void addBlockData(BlockData blockData) {
        this.blockDataList.add(blockData);
    }

    public void removeAllData(Block block) {
        removeBlockData(block, blockData -> true);
    }

    public void removeBlockData(Block block, Predicate<BlockData> predicate) {
        this.blockDataList.removeIf(blockData -> {
            if (blockData.getBlock().equals(block) && predicate.test(blockData)) {
                blockData.handleRemove(block);
                return true;
            }
            return false;
        });
    }

    public void transferBlockData(Block oldBlock, Block newBlock) {
        for (BlockData blockData : getBlockData(oldBlock, blockData -> true)) {
            blockData.setBlock(newBlock);
        }
    }

    public boolean hasBlockData(Block block) {
        return !this.getBlockData(block, blockData -> true).isEmpty();
    }

    public <T extends BlockData> Optional<T> getBlockData(Block block, Class<T> blockDataClass) {
        for (BlockData data : this.blockDataList) {
            if(data.getBlock().equals(block) && data.getClass().equals(blockDataClass))
                return (Optional<T>) Optional.of(data);
        }
        return Optional.empty();
    }

    public List<BlockData> getBlockData(Block block, Predicate<BlockData> predicate) {
        return this.blockDataList.stream().filter(blockData -> blockData.getBlock().equals(block) && predicate.test(blockData)).collect(Collectors.toList());
    }

    public void loadAll() {
        for (World world : Bukkit.getWorlds()) {
            try {
                plugin.getLogger().info("Loading block data for world: " + world.getName());
                plugin.getBlockDataManager().loadData(world);
            } catch (IOException e) {
                plugin.getLogger().warning("Can't load block data for world: " + world.getName());
                e.printStackTrace();
            }
        }
    }

    public void saveAll() {
        for (World world : Bukkit.getWorlds()) {
            try {
                plugin.getLogger().info("Saving block data for world: " + world.getName());
                plugin.getBlockDataManager().safeData(world);
            } catch (IOException e) {
                plugin.getLogger().warning("Can't save block data for world: " + world);
                e.printStackTrace();
            }
        }
    }

    public void safeData(World world) throws IOException {

        File file = new File(world.getWorldFolder(), "spigotfactory.dat");
        if(!file.exists())
            file.createNewFile();

        FileOutputStream outputStream = new FileOutputStream(file);
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        List<BlockData> dataList = blockDataList.stream().filter(blockData -> blockData.getBlock().getWorld().equals(world)).collect(Collectors.toList());

        dataOutputStream.writeInt(dataList.size());
        for (BlockData data : dataList) {
            this.writeDataToStream(data, dataOutputStream);
        }
        dataOutputStream.flush();
        outputStream.flush();
        dataOutputStream.close();
        outputStream.close();
        this.blockDataList.removeAll(dataList);
    }

    public void loadData(World world) throws IOException {
        File file = new File(world.getWorldFolder(), "spigotfactory.dat");
        if(!file.exists())
            return;

        FileInputStream fileInputStream = new FileInputStream(file);
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        int size = dataInputStream.readInt();
        BlockData[] data = new BlockData[size];
        for (int i = 0; i < size; i++) {
            data[i] = readBlockData(world, dataInputStream);
        }
        dataInputStream.close();
        fileInputStream.close();

        this.blockDataList.addAll(Arrays.asList(data));
    }

    private void writeDataToStream(BlockData data, DataOutputStream stream) throws IOException {
        stream.writeInt(BlockDataRegistry.getDataID(data));
        BlockUtil.blockToStream(data.getBlock(), stream);
        data.encode(stream);
    }

    private BlockData readBlockData(World world, DataInputStream stream) throws IOException {
        int id = stream.readInt();
        BlockData data = BlockDataRegistry.createBlockData(id);
        Block block = BlockUtil.blockFromStream(world, stream);
        data.setBlock(block);
        data.decode(stream);
        return data;
    }

    @Override
    public void tick() {
        this.blockDataList.stream().filter(blockData -> blockData instanceof TickAble).forEach(blockData -> {
            TickAble tickAble = (TickAble) blockData;
            tickAble.tick();
        });
        this.blockDataList.removeIf(blockData -> blockData instanceof AbstractBlockData abstractBlockData && abstractBlockData.shouldRemove());
    }
}
