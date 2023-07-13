package tk.jacobempire.melonmod.common.init;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.SoundType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tk.jacobempire.melonmod.MelonMod;

public class ModBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MelonMod.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MelonMod.MODID);


    public static final RegistryObject<Block> MELON_STONE = BLOCKS.register("melon_stone",
            () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(3.0F, 3.0F)
                    .harvestLevel(2)
                    .sound(SoundType.STONE)));
    public static final RegistryObject<Block> MELON_GRASS_BLOCK = BLOCKS.register("melon_grass_block",
            () -> new GrassBlock(Block.Properties.of(Material.DIRT, MaterialColor.DIRT)
                    .strength(3.0F, 3.0F)
                    .harvestLevel(2)
                    .sound(SoundType.GRASS)));
    public static final RegistryObject<Block> MELON_ORE = BLOCKS.register("melon_ore",
            () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(6.0F, 6.0F)
                    .harvestLevel(2)
                    .sound(SoundType.STONE)));


    public static final RegistryObject<Item> MELON_STONE_ITEM = ITEMS.register("melon_stone",
            () -> new BlockItem(MELON_STONE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MELON_GRASS_BLOCK_ITEM = ITEMS.register("melon_grass",
            () -> new BlockItem(MELON_GRASS_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MELON_ORE_ITEM = ITEMS.register("melon_ore",
            () -> new BlockItem(MELON_ORE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                MELON_STONE.get(),
                MELON_GRASS_BLOCK.get(),
                MELON_ORE.get()
        );
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                MELON_STONE_ITEM.get(),
                MELON_GRASS_BLOCK_ITEM.get(),
                MELON_ORE_ITEM.get()
        );
    }
}
