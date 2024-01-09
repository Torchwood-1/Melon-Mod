package tk.jacobempire.melonmod.common.world.dimension;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class ModDimensions {

    public static RegistryKey<World> MELON_DIM;

    public ModDimensions() {
    }

    static {
        MELON_DIM = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("melonmod", "melon_dim"));
    }
}
