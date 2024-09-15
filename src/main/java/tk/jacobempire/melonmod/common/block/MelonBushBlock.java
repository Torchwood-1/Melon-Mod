package tk.jacobempire.melonmod.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import tk.jacobempire.melonmod.common.init.ModBlocks;

public class MelonBushBlock extends BushBlock {
	public MelonBushBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, IBlockReader reader, BlockPos pos) {
		return state.is(ModBlocks.MELON_GRASS_BLOCK.get()) || super.mayPlaceOn(state, reader, pos);
	}
}
