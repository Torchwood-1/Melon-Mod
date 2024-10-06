package tk.jacobempire.melonmod.common.world;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import tk.jacobempire.melonmod.MelonMod;

public class ModBiomes {
	public ModBiomes() {
	}

	public static RegistryKey<Biome> MELON = makeKey("melon");

	private static RegistryKey<Biome> makeKey(String name) {
		return RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MelonMod.MODID, name));
	}
}
