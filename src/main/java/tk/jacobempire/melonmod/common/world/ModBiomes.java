package tk.jacobempire.melonmod.common.world;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tk.jacobempire.melonmod.MelonMod;

public class ModBiomes {
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, MelonMod.MODID);
    public ModBiomes() {}
    @Deprecated
    public static final RegistryObject<Biome> MELON_BIOME = BIOMES.register("melon", BiomeMaker::theVoidBiome);;
    public static RegistryKey<Biome> MELON;

    private static RegistryKey<Biome> makeKey(String name) {
        return RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MelonMod.MODID, name));
    }
}
