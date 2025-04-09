package uk.jacobempire.melonmod.common.world;

import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import uk.jacobempire.melonmod.MelonMod;
import uk.jacobempire.melonmod.carver.MelonCarver;

public class ModCarvers {
    public static final DeferredRegister<WorldCarver<?>> WORLD_CARVERS = DeferredRegister.create(ForgeRegistries.WORLD_CARVERS, MelonMod.MODID);
    public static final RegistryObject<MelonCarver> CARVER = WORLD_CARVERS.register("melon_carver", () -> {
        return new MelonCarver(ProbabilityConfig.CODEC);
    });

    public ModCarvers() {
    }
}
