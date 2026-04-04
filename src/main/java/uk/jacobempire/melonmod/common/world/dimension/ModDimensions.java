package uk.jacobempire.melonmod.common.world.dimension;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import uk.jacobempire.melonmod.MelonMod;

public class ModDimensions {

    public static RegistryKey<World> MELON_DIM = makeKey("melon_dim");

    private static RegistryKey<World> makeKey(String name) {
        return makeKey(MelonMod.resource(name));
    }

    private static RegistryKey<World> makeKey(ResourceLocation location) {
        return RegistryKey.create(Registry.DIMENSION_REGISTRY, location);
    }

}
