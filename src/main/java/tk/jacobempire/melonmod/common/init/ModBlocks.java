package tk.jacobempire.melonmod.common.init;

import java.util.function.Supplier;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tk.jacobempire.melonmod.MelonMod;
import tk.jacobempire.melonmod.common.block.MelonBushBlock;
import tk.jacobempire.melonmod.common.block.MelonGrassBlock;
import tk.jacobempire.melonmod.common.world.dimension.SimpleTeleporter;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            MelonMod.MODID);

    public static final RegistryObject<Block> MELON_STONE = registerBlock("melon_stone",
            () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(3.0F, 3.0F)
                    .harvestLevel(2)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)),
            ItemGroup.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<Block> MOBBLESTONE = registerBlock("mobblestone",
            () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(3.0F, 3.0F)
                    .harvestLevel(2)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)),
            ItemGroup.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<Block> MELON_GRASS_BLOCK = registerBlock("melon_grass_block",
            () -> new MelonGrassBlock(Block.Properties.of(Material.GRASS, MaterialColor.COLOR_LIGHT_GREEN)
                    .randomTicks()
                    .strength(0.6F)
                    .sound(SoundType.GRASS)),
            ItemGroup.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<Block> MELON_DIRT = registerBlock("melon_dirt",
            () -> new Block(AbstractBlock.Properties.of(Material.DIRT, MaterialColor.COLOR_RED)
                    .strength(0.5F)
                    .sound(SoundType.GRAVEL)),
            ItemGroup.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<Block> MELON_ORE = registerBlock("melon_ore",
            () -> new SimpleTeleporter(Block.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(6.0F, 6.0F)
                    .harvestLevel(2)
                    .sound(SoundType.STONE)),
            ItemGroup.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<Block> MELON_GRASS = registerBlock("melon_grass",
            () -> new MelonBushBlock(AbstractBlock.Properties.of(Material.REPLACEABLE_PLANT)
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .noOcclusion()),
            ItemGroup.TAB_DECORATIONS);

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block, ItemGroup tab) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(tab)));
    }

    @SuppressWarnings("unused")
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, ItemGroup.TAB_BUILDING_BLOCKS);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, ItemGroup tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
