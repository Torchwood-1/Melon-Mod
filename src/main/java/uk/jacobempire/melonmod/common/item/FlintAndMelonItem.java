package uk.jacobempire.melonmod.common.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.jacobempire.melonmod.common.block.MelonFireBlock;

public class FlintAndMelonItem extends FlintAndSteelItem {
    public FlintAndMelonItem(Properties properties) {
        super(properties);
    }

    public ActionResultType useOn(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (CampfireBlock.canLight(blockstate)) {
            world.playSound(player, blockpos, SoundEvents.FLINTANDSTEEL_USE,
                    SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
            world.setBlock(blockpos, blockstate.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
            if (player != null) {
                context.getItemInHand().hurtAndBreak(1, player, (entity) -> {
                    entity.broadcastBreakEvent(context.getHand());
                });
            }

            return ActionResultType.sidedSuccess(world.isClientSide());
        } else {
            BlockPos clickedFacePos = blockpos.relative(context.getClickedFace());
            if (MelonFireBlock.canBePlacedAt(world, clickedFacePos, context.getHorizontalDirection())) {
                world.playSound(player, clickedFacePos, SoundEvents.FLINTANDSTEEL_USE,
                        SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
                BlockState clickedFaceState = MelonFireBlock.getState(world, clickedFacePos);
                world.setBlock(clickedFacePos, clickedFaceState, 11);
                ItemStack itemstack = context.getItemInHand();
                if (player instanceof ServerPlayerEntity) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player,
                            clickedFacePos, itemstack);
                    itemstack.hurtAndBreak(1, player, (entity) -> {
                        entity.broadcastBreakEvent(context.getHand());
                    });
                }

                return ActionResultType.sidedSuccess(world.isClientSide());
            } else {
                return ActionResultType.FAIL;
            }
        }
    }

}
