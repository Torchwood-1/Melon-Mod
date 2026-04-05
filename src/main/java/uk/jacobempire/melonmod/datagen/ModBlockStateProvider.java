package uk.jacobempire.melonmod.datagen;

import static uk.jacobempire.melonmod.common.init.ModBlocks.*;

import net.minecraft.block.Block;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ResourceLocation melonStoneTexture = blockTexture(MELON_STONE.get());

        portalBlock(MELON_PORTAL, modLoc("block/melon_fire_0"));
        simpleBlock(MELOBSIDIAN,
                models().cubeColumn("melobsidian", mcLoc("block/obsidian"), mcLoc("block/melon_top")));
        simpleBlock(MELON_DIRT);
        simpleBlock(MELON_GRASS, tintedCross("melon_grass", modLoc("block/melon_grass")));
        simpleBlock(MELON_ORE);
        simpleBlock(MELON_STONE);
        simpleBlock(MOBBLESTONE);
        slabBlock(MELON_STONE_SLAB.get(), MELON_STONE.getId(), melonStoneTexture);
        stairsBlock(MELON_STONE_STAIRS.get(), melonStoneTexture);
    }

    protected void portalBlock(RegistryObject<? extends NetherPortalBlock> registryObject, ResourceLocation texture) {
        String name = registryObject.getId().getPath();
        portalBlock(registryObject,
                models().withExistingParent(name + "_ns", mcLoc("block/nether_portal_ns"))
                        .texture("portal", texture)
                        .texture("particle", texture),
                models().withExistingParent(name + "_ew", mcLoc("block/nether_portal_ew"))
                        .texture("portal", texture)
                        .texture("particle", texture));
    }

    protected void portalBlock(RegistryObject<? extends NetherPortalBlock> registryObject,
            ModelFile northSouth, ModelFile eastWest) {
        getVariantBuilder(registryObject.get())
                .partialState().with(NetherPortalBlock.AXIS, Axis.Z)
                .modelForState().modelFile(northSouth).addModel()
                .partialState().with(NetherPortalBlock.AXIS, Axis.X)
                .modelForState().modelFile(eastWest).addModel();
    }

    protected void simpleBlock(RegistryObject<? extends Block> registryObject) {
        super.simpleBlock(registryObject.get());
    }

    public void simpleBlock(RegistryObject<? extends Block> registryObject, ModelFile model) {
        super.simpleBlock(registryObject.get(), model);
    }

    public BlockModelBuilder tintedCross(String name, ResourceLocation cross) {
        return models().singleTexture(name,
                mcLoc(ModelProvider.BLOCK_FOLDER + "/tinted_cross"),
                "cross", cross);
    }
}
