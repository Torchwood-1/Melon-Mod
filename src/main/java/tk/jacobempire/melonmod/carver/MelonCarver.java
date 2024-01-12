package tk.jacobempire.melonmod.carver;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraftforge.fml.common.Mod;
import tk.jacobempire.melonmod.common.init.ModBlocks;

public class MelonCarver extends CaveWorldCarver {
    public MelonCarver(Codec<ProbabilityConfig> codec) {
        super(codec, 256);
        this.replaceableBlocks = ImmutableSet.of(ModBlocks.MELON_GRASS_BLOCK.get(), ModBlocks.MELON_STONE.get());
    }
}
