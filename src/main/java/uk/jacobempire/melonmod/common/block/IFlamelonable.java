package uk.jacobempire.melonmod.common.block;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import uk.jacobempire.melonmod.common.init.ModBlocks;

public interface IFlamelonable {
    /**
     * If the block is flammable, this is called when it gets lit on melon fire.
     *
     * @param state   The current state
     * @param world   The current world
     * @param pos     Block position in world
     * @param face    The face that the fire is coming from
     * @param igniter The entity that lit the fire
     */
    default void catchMelonFire(BlockState state, World world, BlockPos pos, @Nullable Direction face,
            @Nullable LivingEntity igniter) {
    }

    /**
     * Chance that fire will spread and consume this block.
     * 300 being a 100% chance, 0, being a 0% chance.
     *
     * @param state The current state
     * @param world The current world
     * @param pos   Block position in world
     * @param face  The face that the fire is coming from
     * @return A number ranging from 0 to 300 relating used to determine if the
     *         block will be consumed by fire
     */
    @SuppressWarnings("deprecation")
    default int getFlamelonability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return (ModBlocks.MELON_FIRE.get()).getBurnOdd(state);
    }

    /**
     * Called when melon fire is updating, checks if a block face can catch melon fire.
     *
     *
     * @param state The current state
     * @param world The current world
     * @param pos   Block position in world
     * @param face  The face that the fire is coming from
     * @return True if the face can be on melon fire, false otherwise.
     */
    default boolean isFlamelonable(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return getFlamelonability(state, world, pos, face) > 0;
    }
}
