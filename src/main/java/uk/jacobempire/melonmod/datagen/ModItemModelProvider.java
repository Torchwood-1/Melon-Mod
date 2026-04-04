package uk.jacobempire.melonmod.datagen;

import static uk.jacobempire.melonmod.common.init.ModBlocks.*;
import static uk.jacobempire.melonmod.common.init.ModItems.*;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // blocks
        simpleBlockItem(MELOBSIDIAN);
        simpleBlockItem(MELON_DIRT);
        simpleBlockItem(MELON_GRASS);
        simpleBlockItem(MELON_GRASS_BLOCK);
        simpleBlockItem(MELON_ORE);
        simpleBlockItem(MELON_STONE);
        simpleBlockItem(MELON_STONE_SLAB);
        simpleBlockItem(MELON_STONE_STAIRS);
        simpleBlockItem(MOBBLESTONE);

        // armor
        simpleItem(MELON_BOOTS);
        simpleItem(MELON_CHESTPLATE);
        simpleItem(MELON_HELMET);
        simpleItem(MELON_LEGGINGS);

        simpleItem(MELON_HORSE_ARMOR);

        // elytra
        elytra(MELON_ELYTRA);

        // tools
        handheldItem(MELON_AXE);
        handheldItem(MELON_HOE);
        handheldItem(MELON_PICKAXE);
        handheldItem(MELON_SHOVEL);
        handheldItem(MELON_SWORD);
        simpleItem(FLINT_AND_MELON, mcLoc("item/flint_and_steel"));

        // spawn eggs
        spawnEggItem(MELON_GOBLIN_SPAWN_EGG)
                .texture("layer0", modLoc("item/melon_goblin_spawn_egg"))
                .texture("layer1", modLoc("item/melon_goblin_spawn_egg_overlay"));

        // resources
        handheldItem(MELON_STICK);
        simpleItem(MELON_INGOT);
    }

    private ItemModelBuilder elytra(RegistryObject<? extends ElytraItem> registryObject) {
        return simpleItem(registryObject)
                .override()
                .predicate(mcLoc("broken"), 1)
                .model(simpleItem("broken_" + registryObject.getId().getPath()))
                .end();
    }

    protected ItemModelBuilder withExistingParent(RegistryObject<?> registryObject, ResourceLocation parent) {
        return getBuilder(registryObject.getId().getPath()).parent(getExistingFile(parent));
    }

    protected ItemModelBuilder spawnEggItem(RegistryObject<? extends Item> registryObject) {
        return withExistingParent(registryObject, mcLoc("item/template_spawn_egg"));
    }

    protected ItemModelBuilder simpleBlockItem(RegistryObject<? extends Block> registryObject) {
        return simpleBlockItem(registryObject.get());
    }

    protected ItemModelBuilder simpleBlockItem(Block block) {
        return withExistingParent(block.getRegistryName().getPath(),
                new ResourceLocation(modid, "block/" + block.getRegistryName().getPath()));
    }

    protected ItemModelBuilder simpleItem(RegistryObject<? extends Item> item) {
        return simpleItem(item.get());
    }

    protected ItemModelBuilder simpleItem(RegistryObject<? extends Item> registryObject, ResourceLocation texture) {
        return simpleItem(registryObject.getId().getPath(), texture);
    }

    protected ItemModelBuilder simpleItem(String name, ResourceLocation texture) {
        return withExistingParent(name,
                mcLoc("item/generated"))
                .texture("layer0", texture);
    }

    protected ItemModelBuilder simpleItem(String name) {
        return simpleItem(name, modLoc("item/" + name));
    }

    protected ItemModelBuilder simpleItem(Item item) {
        return simpleItem(item.getRegistryName().getPath());
    }

    protected ItemModelBuilder handheldItem(RegistryObject<? extends Item> registryObject) {
        return handheldItem(registryObject.get());
    }

    protected ItemModelBuilder handheldItem(Item item) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/handheld"))
                .texture("layer0", modLoc("item/" + item.getRegistryName().getPath()));
    }

}
