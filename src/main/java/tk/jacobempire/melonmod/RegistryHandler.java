package tk.jacobempire.melonmod;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tk.jacobempire.melonmod.common.init.ModBlocks;
import tk.jacobempire.melonmod.common.init.ModItems;

public class RegistryHandler {
    public static final DeferredRegister<Item> ITEMS;
    public static final DeferredRegister<Block> BLOCKS;

    public static ModItems modItems;
    public static ModBlocks modBlocks;
    public RegistryHandler() {

    }

    public static void init(){
        modItems = new ModItems();
        modBlocks = new ModBlocks();
    }

    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "melonmod");
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "melonmod");
    }
}
