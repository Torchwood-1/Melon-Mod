//package tk.jacobempire.melonmod.common.init;
//
//import net.minecraft.block.*;
//import net.minecraft.block.material.Material;
//import net.minecraft.block.material.MaterialColor;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.RenderTypeLookup;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemGroup;
//import net.minecraftforge.fml.RegistryObject;
//import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import tk.jacobempire.melonmod.MelonMod;
//import tk.jacobempire.melonmod.RegistryHandler;
//import tk.jacobempire.melonmod.common.item.BaseBlockItem;
//
//import java.util.function.Supplier;
//
//
//public class ModBlocks {
////    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MelonMod.MODID);
//
//    public static RegistryObject<Block> MELON_ORE;
//
//
//    public static <B extends Block> RegistryObject<Block> registerBlock(Supplier<B> block, String name, Item.Properties properties, boolean needsItem) {
//        RegistryObject<Block> blockObj = RegistryHandler.BLOCKS.register(name, block);
//        if (needsItem) {
//            RegistryHandler.ITEMS.register(name, () -> {
//                return new BaseBlockItem((Block)blockObj.get(), properties);
//            });
//        }
//
//        return blockObj;
//    }
//
//    public static <B extends Block> RegistryObject<Block> registerBlock(Supplier<B> block, String name) {
//        return registerBlock(block, name, new Item.Properties(), true);
//    }
//
//    public static <B extends Block> RegistryObject<Block> registerBlock(Supplier<B> block, String name, boolean needsItem) {
//        return registerBlock(block, name, new Item.Properties(), needsItem);
//    }
//
//    public static <B extends Block> RegistryObject<Block> registerBlock(Supplier<B> block, String name, Item.Properties properties) {
//        return registerBlock(block, name, properties, true);
//    }
//
//    public static <B extends Block> RegistryObject<Block> registerBlock(Supplier<B> block, String name, ItemGroup itemgroup) {
//        return registerBlock(block, name, (new Item.Properties()).tab(itemgroup), true);
//    }
//
//    public static void registerRenderType(Block block, RenderType renderType) {
//        RenderTypeLookup.setRenderLayer(block, renderType);
//    }
//
//    static{
//        MELON_ORE = registerBlock(() -> {
//        return new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE).strength(6.0F, 6.0F).requiresCorrectToolForDrops().sound(SoundType.STONE));
//        }, "melon_ore", ItemGroup.TAB_BUILDING_BLOCKS);
//    }
//
////    public static void registerBlocks() {
////        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
////    }
//}

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

    public static final RegistryObject<Block> MELON_ORE = BLOCKS.register("melon_ore",
            () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(6.0F, 6.0F)
                    .harvestLevel(2)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Item> MELON_ORE_ITEM = ITEMS.register("melon_ore",
            () -> new BlockItem(MELON_ORE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                MELON_ORE.get()
        );
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                MELON_ORE_ITEM.get()
        );
    }
}
