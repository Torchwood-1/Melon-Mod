package tk.jacobempire.melonmod.common.block;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import tk.jacobempire.melonmod.common.init.ModBlocks;

public class MelonGrassBlock extends GrassBlock {

	public MelonGrassBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isToolEffective(BlockState state, ToolType tool) {
		if (tool.equals(ToolType.SHOVEL) || tool.equals(ToolType.AXE)) {
			return true;
		}
		return false;
	}

	private static boolean canBeGrass(BlockState state, IWorldReader reader, BlockPos pos) {
		BlockPos blockpos = pos.above();
		BlockState blockstate = reader.getBlockState(blockpos);
		if (blockstate.is(Blocks.SNOW) && blockstate.getValue(SnowBlock.LAYERS) == 1) {
			return true;
		} else if (blockstate.getFluidState().getAmount() == 8) {
			return false;
		} else {
			int i = LightEngine.getLightBlockInto(reader, state, pos, blockstate,
					blockpos, Direction.UP, blockstate.getLightBlock(reader, blockpos));
			return i < reader.getMaxLightLevel();
		}
	}

	private static boolean canPropagate(BlockState state, IWorldReader reader, BlockPos pos) {
		BlockPos blockpos = pos.above();
		return canBeGrass(state, reader, pos)
				&& !reader.getFluidState(blockpos).is(FluidTags.WATER);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!canBeGrass(state, world, pos)) {
			if (!world.isAreaLoaded(pos, 3))
				return; // Forge: prevent loading unloaded chunks when checking neighbor's light and
					// spreading

			world.setBlockAndUpdate(pos, ModBlocks.MELON_DIRT.get().defaultBlockState());
		} else {
			if (world.getMaxLocalRawBrightness(pos.above()) >= 9) {
				BlockState blockstate = this.defaultBlockState();

				for (int i = 0; i < 4; ++i) {
					BlockPos blockpos = pos.offset(random.nextInt(3) - 1,
							random.nextInt(5) - 3, random.nextInt(3) - 1);

					BlockState targetState = world.getBlockState(blockpos);
					boolean canChange = targetState.is(Blocks.DIRT)
							|| targetState.is(ModBlocks.MELON_DIRT.get());
					// || targetState.is(Blocks.GRASS_BLOCK);

					if (canChange && canPropagate(blockstate, world, blockpos)) {
						BlockState aboveState = world.getBlockState(blockpos.above());
						if (aboveState.is(Blocks.GRASS)) {
							world.setBlock(blockpos.above(),
									ModBlocks.MELON_GRASS.get().defaultBlockState(),
									2);
						}

						world.setBlockAndUpdate(blockpos,
								blockstate.setValue(SNOWY, Boolean.valueOf(
										aboveState.is(Blocks.SNOW))));

					}
				}
			}

		}
	}
}
