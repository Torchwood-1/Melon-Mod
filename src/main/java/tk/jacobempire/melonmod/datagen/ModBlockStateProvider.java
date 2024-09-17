package tk.jacobempire.melonmod.datagen;

import static tk.jacobempire.melonmod.common.init.ModBlocks.*;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

	public ModBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
		super(gen, modid, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		ResourceLocation melonStoneTexture = blockTexture(MELON_STONE.get());

		simpleBlock(MELON_DIRT.get());
		slabBlock(MELON_STONE_SLAB.get(), MELON_STONE.getId(), melonStoneTexture);
		stairsBlock(MELON_STONE_STAIRS.get(), melonStoneTexture);
	}

}
