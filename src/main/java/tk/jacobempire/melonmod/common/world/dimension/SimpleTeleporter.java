package tk.jacobempire.melonmod.common.world.dimension;



import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import tk.jacobempire.melonmod.common.init.ModBlocks;

public class SimpleTeleporter extends Block {

    public SimpleTeleporter(AbstractBlock.Properties properties) {
        super(properties);
    }

    //public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
    //    return Block.box(24, 24, 24, 24, 24, 24);
    //}

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide() && player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            RegistryKey<World> key = worldIn.dimension() == ModDimensions.MELON_DIM ? World.OVERWORLD : ModDimensions.MELON_DIM;
            ServerWorld destinationWorld = worldIn.getServer().getLevel(key);
            if (destinationWorld != null && destinationWorld != worldIn) {
                BlockPos headPos = player.blockPosition().above();
                if (destinationWorld.getBlockState(headPos).isSuffocating(worldIn, pos)) {
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            for (int l = -1; l <= 1; l++) {
                                BlockPos bPos = pos.offset(i, j, l);
                                destinationWorld.setBlockAndUpdate(bPos, Blocks.AIR.defaultBlockState());
                            }
                        }
                    }
                } serverPlayer.teleportTo(destinationWorld, player.getX(), player.getY(), player.getZ(), serverPlayer.getYHeadRot(), player.xRot);
                destinationWorld.setBlockAndUpdate(pos, ModBlocks.MELON_ORE.get().defaultBlockState());
            }
        }
        return ActionResultType.SUCCESS;
    }
}

