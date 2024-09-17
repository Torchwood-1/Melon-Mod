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
import tk.jacobempire.melonmod.common.block.MelonBushBlock;
import tk.jacobempire.melonmod.common.block.MelonGrassBlock;
import tk.jacobempire.melonmod.common.world.dimension.SimpleTeleporter;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MelonMod.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MelonMod.MODID);


    public static final RegistryObject<Block> MELON_STONE = BLOCKS.register("melon_stone",
            () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(3.0F, 3.0F)
                    .harvestLevel(2)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> MOBBLESTONE = BLOCKS.register("mobblestone",
            () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(3.0F, 3.0F)
                    .harvestLevel(2)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> MELON_GRASS_BLOCK = BLOCKS.register("melon_grass_block",
            () -> new MelonGrassBlock(Block.Properties.of(Material.GRASS, MaterialColor.COLOR_LIGHT_GREEN)
                    .randomTicks()
                    .strength(0.6F)
                    .sound(SoundType.GRASS)));

    public static final RegistryObject<Block> MELON_DIRT = BLOCKS.register("melon_dirt",
            () -> new Block(AbstractBlock.Properties.of(Material.DIRT, MaterialColor.COLOR_RED)
                    .strength(0.5F)
                    .sound(SoundType.GRAVEL)));

//    public static final RegistryObject<Block> MELON_ORE = BLOCKS.register("melon_ore",
//            () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE)
//                    .strength(6.0F, 6.0F)
//                    .harvestLevel(2)
//                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> MELON_ORE = BLOCKS.register("melon_ore",
            () -> new SimpleTeleporter(Block.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(6.0F, 6.0F)
                    .harvestLevel(2)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> MELON_GRASS = BLOCKS.register("melon_grass",
            () -> new MelonBushBlock(AbstractBlock.Properties.of(Material.REPLACEABLE_PLANT)
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .noOcclusion()));

    //TODO: make a method to add blocks and their items at the same time

    public static final RegistryObject<Item> MELON_STONE_ITEM = ITEMS.register("melon_stone",
            () -> new BlockItem(MELON_STONE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    public static final RegistryObject<Item> MOBBLESTONE_ITEM = ITEMS.register("mobblestone",
            () -> new BlockItem(MOBBLESTONE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MELON_GRASS_BLOCK_ITEM = ITEMS.register("melon_grass_block",
            () -> new BlockItem(MELON_GRASS_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MELON_ORE_ITEM = ITEMS.register("melon_ore",
            () -> new BlockItem(MELON_ORE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MELON_GRASS_ITEM = ITEMS.register("melon_grass",
            () -> new BlockItem(MELON_GRASS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MELON_DIRT_ITEM = ITEMS.register("melon_dirt",
            () -> new BlockItem(MELON_DIRT.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                MELON_STONE.get(),
                MELON_GRASS_BLOCK.get(),
                MELON_ORE.get(),
                MOBBLESTONE.get(),
                MELON_GRASS_BLOCK.get(),
                MELON_GRASS.get()
        );
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                MELON_STONE_ITEM.get(),
                MELON_GRASS_BLOCK_ITEM.get(),
                MELON_ORE_ITEM.get(),
                MOBBLESTONE_ITEM.get(),
                MELON_GRASS_BLOCK_ITEM.get(),
                MELON_GRASS_ITEM.get()
        );
    }
}
