package de.febanhd.spigotfactoryreloaded.data;

import com.google.common.collect.Maps;
import de.febanhd.spigotfactoryreloaded.data.impl.PipelineFilterBlockData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BlockDataRegistry {

    private static final HashMap<Integer, Supplier<BlockData>> REGISTRY = Maps.newHashMap();
    private static final HashMap<Class<? extends BlockData>, Integer> CLASS_REGISTRY = Maps.newHashMap();

    static {
        registerDataType(1, PipelineFilterBlockData::new);
    }

    private static void registerDataType(int id, Supplier<BlockData> supplier) {
        REGISTRY.put(id, supplier);
        CLASS_REGISTRY.put(supplier.get().getClass(), id);
    }

    public static BlockData createBlockData(int id) {
        return REGISTRY.get(id).get();
    }

    public static int getDataID(BlockData blockData) {
        return CLASS_REGISTRY.get(blockData.getClass());
    }

}
