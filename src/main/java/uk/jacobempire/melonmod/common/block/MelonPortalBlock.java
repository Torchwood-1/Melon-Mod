package uk.jacobempire.melonmod.common.block;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.PortalSize;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.jacobempire.melonmod.common.MelonTeleporter;
import uk.jacobempire.melonmod.common.entity.EntityMixinAccessor;
import uk.jacobempire.melonmod.common.init.ModEntities;
import uk.jacobempire.melonmod.common.world.dimension.ModDimensions;

public class MelonPortalBlock extends NetherPortalBlock {
    public MelonPortalBlock(Properties properties) {
        super(properties);
    }

    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!entity.isPassenger() && !entity.isVehicle() && entity.canChangeDimensions()) {
            handleInsidePortal(entity, pos);
        }
    }

    public static void handleInsidePortal(Entity entity, BlockPos pos) {
        EntityMixinAccessor ema = (EntityMixinAccessor) entity;

        if (entity.isOnPortalCooldown()) {
            entity.setPortalCooldown();
        } else {
            if (!entity.level.isClientSide && !pos.equals(ema.getPortalEntrancePos())) {
                ema.setPortalEntrancePos(pos.immutable());
            }

            ema.setInsideMelonPortal(true);
        }
    }

    public static boolean handleMelonPortal(Entity entity) {
        if (entity.level instanceof ServerWorld && entity instanceof EntityMixinAccessor) {
            EntityMixinAccessor ema = (EntityMixinAccessor) entity;
            int portalWaitTime = entity.getPortalWaitTime();
            ServerWorld serverworld = (ServerWorld) entity.level;
            if (ema.isInsideMelonPortal()) {
                MinecraftServer minecraftserver = serverworld.getServer();
                RegistryKey<World> dim = entity.level.dimension() == ModDimensions.MELON_DIM ? World.OVERWORLD
                        : ModDimensions.MELON_DIM;
                ServerWorld world = minecraftserver.getLevel(dim);
                ema.setPortalTime(ema.getPortalTime() + 1);
                if (world != null && minecraftserver.isNetherEnabled() && !entity.isPassenger()
                        && ema.getPortalTime() >= portalWaitTime) {
                    entity.level.getProfiler().push("portal");
                    ema.setPortalTime(portalWaitTime);
                    entity.setPortalCooldown();
                    entity.changeDimension(world, new MelonTeleporter(world));
                    entity.level.getProfiler().pop();
                }

                ema.setInsideMelonPortal(false);
            } else {
                if (ema.getPortalTime() > 0) {
                    ema.setPortalTime(ema.getPortalTime() - 4);
                }

                if (ema.getPortalTime() < 0) {
                    ema.setPortalTime(0);
                }
            }

            return true;
        }

        return false;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.dimensionType().natural() && world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)
                && random.nextInt(2000) < world.getDifficulty().getId()) {
            while (world.getBlockState(pos).is(this)) {
                pos = pos.below();
            }

            if (world.getBlockState(pos).isValidSpawn(world, pos, ModEntities.MELON_GOBLIN.get())) {
                Entity entity = ModEntities.MELON_GOBLIN.get()
                        .spawn(world, null, null, null, pos.above(),
                                SpawnReason.STRUCTURE, false, false);

                if (entity != null) {
                    entity.setPortalCooldown();
                }
            }
        }

    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(100) == 0) {
            world.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                    SoundEvents.GENERIC_EAT, SoundCategory.BLOCKS, 0.5F,
                    random.nextFloat() * 0.4F + 0.8F, false);
        }

        for (int i = 0; i < 4; ++i) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            double d3 = (random.nextFloat() - 0.5D) * 0.5D;
            double d4 = (random.nextFloat() - 0.5D) * 0.5D;
            double d5 = (random.nextFloat() - 0.5D) * 0.5D;
            int j = random.nextInt(2) * 2 - 1;
            if (!world.getBlockState(pos.west()).is(this)
                    && !world.getBlockState(pos.east()).is(this)) {
                x = (double) pos.getX() + 0.5D + 0.25D * (double) j;
                d3 = (double) (random.nextFloat() * 2.0F * (float) j);
            } else {
                z = (double) pos.getZ() + 0.5D + 0.25D * (double) j;
                d5 = (double) (random.nextFloat() * 2.0F * (float) j);
            }

            world.addParticle(ParticleTypes.PORTAL, x, y, z, d3, d4, d5);
        }

    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState state2,
            IWorld world, BlockPos pos, BlockPos pos2) {
        Direction.Axis axis = direction.getAxis();
        Direction.Axis stateAxis = state.getValue(AXIS);
        boolean flag = stateAxis != axis && axis.isHorizontal();
        return !flag && !state2.is(this)
                && !(new PortalSize(world, pos, stateAxis)).isComplete()
                        ? Blocks.AIR.defaultBlockState()
                        : super.updateShape(state, direction, state2, world,
                                pos, pos2);
    }

}
