package tk.jacobempire.melonmod.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import static tk.jacobempire.melonmod.common.init.ModBlocks.*;

public class ModItemModelProvider extends ItemModelProvider {

	public ModItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
		super(generator, modid, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		simpleBlockItem(MELON_DIRT.get());
		simpleBlockItem(MELON_STONE_SLAB.get());
		simpleBlockItem(MELON_STONE_STAIRS.get());
	}

	protected ItemModelBuilder simpleBlockItem(Block block) {
		return withExistingParent(block.getRegistryName().getPath(),
				new ResourceLocation(modid, "block/" + block.getRegistryName().getPath()));
	}

	protected ItemModelBuilder simpleItem(Item item) {
		return withExistingParent(item.getRegistryName().getPath(),
				new ResourceLocation("item/generated"))
				.texture("layer0", new ResourceLocation(modid,
						"item/" + item.getRegistryName().getPath()));
	}

	protected ItemModelBuilder handheldItem(Item item) {
		return withExistingParent(item.getRegistryName().getPath(),
				new ResourceLocation("item/handheld"))
				.texture("layer0", new ResourceLocation(modid,
						"item/" + item.getRegistryName().getPath()));
	}

}
