package uk.jacobempire.melonmod.datagen.loot;

import static uk.jacobempire.melonmod.common.init.ModBlocks.*;

import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraftforge.fml.RegistryObject;
import uk.jacobempire.melonmod.common.init.ModBlocks;

public class ModBlockLootTables extends BlockLootTables {
    @Override
    protected void addTables() {
        add(MELON_GRASS, block -> createGrassDrops(block, Items.MELON_SEEDS));
        add(MELON_GRASS_BLOCK, block -> createSingleItemTableWithSilkTouch(block, MELON_DIRT.get()));
        add(MELON_STONE, block -> createSingleItemTableWithSilkTouch(block, MOBBLESTONE.get()));
        add(MELON_STONE_SLAB, BlockLootTables::createSlabItemTable);
        dropSelf(MELOBSIDIAN);
        dropSelf(MELON_DIRT);
        dropSelf(MELON_ORE);
        dropSelf(MELON_STONE_STAIRS);
        dropSelf(MOBBLESTONE);
        dropNone(MELON_FIRE);
    }

    protected void dropNone(RegistryObject<? extends Block> registryObject) {
        super.add(registryObject.get(), noDrop());
    }

    protected void add(RegistryObject<? extends Block> registryObject, Builder builder) {
        super.add(registryObject.get(), builder);
    }

    protected void add(RegistryObject<? extends Block> registryObject, Function<Block, Builder> builder) {
        super.add(registryObject.get(), builder);
    }

    public void dropSelf(RegistryObject<? extends Block> registryObject) {
        super.dropSelf(registryObject.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    protected static LootTable.Builder createGrassDrops(Block block, Item seeds) {
        return createShearsDispatchTable(block, applyExplosionDecay(block, ItemLootEntry
                .lootTableItem(seeds).when(RandomChance.randomChance(0.125F))
                .apply(ApplyBonus.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 2))));
    }
}
