package tk.jacobempire.melonmod.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import tk.jacobempire.melonmod.MelonMod;

@EventBusSubscriber(modid = MelonMod.MODID, bus = Bus.MOD)
public class DataGenerators {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(new ModRecipeProvider(generator));
		generator.addProvider(new ModBlockStateProvider(generator, MelonMod.MODID, existingFileHelper));
		generator.addProvider(new ModItemModelProvider(generator, MelonMod.MODID, existingFileHelper));
	}
}
