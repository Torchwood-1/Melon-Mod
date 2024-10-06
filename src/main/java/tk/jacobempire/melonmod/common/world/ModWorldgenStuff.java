package tk.jacobempire.melonmod.common.world;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import tk.jacobempire.melonmod.common.init.ModBlocks;

@EventBusSubscriber(bus = Bus.FORGE)
public class ModWorldgenStuff {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void loadBiome(BiomeLoadingEvent event) {
		BiomeGenerationSettingsBuilder generation = event.getGeneration();

		if (event.getCategory().equals(Biome.Category.JUNGLE)) {
			generateOre(generation, OreFeatureConfig.FillerBlockType.NATURAL_STONE,
					ModBlocks.MELON_ORE.get().defaultBlockState(), 5, 1, 15, 10);
		}

		if (event.getName() == null)
			return;

		if (event.getName().equals(ModBiomes.MELON.location())) {
			generateOre(generation, new BlockMatchRuleTest(ModBlocks.MELON_STONE.get()),
					ModBlocks.MELON_ORE.get().defaultBlockState(), 5, 15, 100, 10);
		}
	}

	private static void generateOre(BiomeGenerationSettingsBuilder settings, RuleTest fillerType, BlockState state,
			int veinSize, int minHeight, int maxHeight, int amount) {
		settings.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(fillerType, state, veinSize))
				.decorated(Placement.RANGE.configured(
						new TopSolidRangeConfig(minHeight, 0, maxHeight)).squared())
				.countRandom(amount));
	}
}
