package tk.jacobempire.melonmod.datagen.loot;

import static tk.jacobempire.melonmod.common.init.ModBlocks.*;

import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraftforge.fml.RegistryObject;
import tk.jacobempire.melonmod.common.init.ModBlocks;

public class ModBlockLootTables extends BlockLootTables {
	@Override
	protected void addTables() {
		this.add(MELON_GRASS.get(), block -> createGrassDrops(block, Items.MELON_SEEDS));
		this.add(MELON_GRASS_BLOCK.get(), block -> createSingleItemTableWithSilkTouch(block, MELON_DIRT.get()));
		this.add(MELON_STONE.get(), block -> createSingleItemTableWithSilkTouch(block, MOBBLESTONE.get()));
		this.add(MELON_STONE_SLAB.get(), BlockLootTables::createSlabItemTable);
		this.dropSelf(MELON_DIRT.get());
		this.dropSelf(MELON_ORE.get());
		this.dropSelf(MELON_STONE_STAIRS.get());
		this.dropSelf(MOBBLESTONE.get());
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
