package uk.jacobempire.melonmod.common;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.PortalInfo;
import net.minecraft.block.PortalSize;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.TeleportationRepositioner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.DimensionType;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import uk.jacobempire.melonmod.common.entity.EntityMixinAccessor;
import uk.jacobempire.melonmod.common.entity.ServerPlayerEntityMixinAccessor;
import uk.jacobempire.melonmod.common.init.ModBlocks;
import uk.jacobempire.melonmod.common.world.dimension.ModDimensions;

public class MelonTeleporter extends Teleporter {

    public MelonTeleporter(ServerWorld world) {
        super(world);
    }

    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw,
            Function<Boolean, Entity> repositionEntity) {
        PortalInfo portalinfo = this.getPortalInfo(entity, currentWorld,
                world -> findDimensionEntryPoint(entity, world));

        RegistryKey<World> currentDim = currentWorld.dimension();
        RegistryKey<World> destDim = destWorld.dimension();

        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            ServerPlayerEntityMixinAccessor spma = (ServerPlayerEntityMixinAccessor) player;

            // boolean spawnPortal = true;

            currentWorld.getProfiler().push("moving");
            if (currentDim == World.OVERWORLD && destDim == ModDimensions.MELON_DIM) {
                spma.setEnteredMelonDimPosition(entity.position());
            }

            currentWorld.getProfiler().pop();
            currentWorld.getProfiler().push("placing");
            player.setLevel(destWorld);
            destWorld.addDuringPortalTeleport(player);
            // player.setRot(portalinfo.yRot, portalinfo.xRot);
            player.yRot = (portalinfo.yRot % 360);
            player.xRot = (portalinfo.xRot % 360);
            player.moveTo(portalinfo.pos.x, portalinfo.pos.y, portalinfo.pos.z);
            currentWorld.getProfiler().pop();
            triggerDimensionChangeTriggers(player, currentWorld);
            return player;
        } else {
            // TODO: make sure portal works for non-players
            return super.placeEntity(entity, currentWorld, destWorld, yaw, repositionEntity);
        }
    }

    public Optional<TeleportationRepositioner.Result> createPortal(BlockPos pos, Direction.Axis axis) {
        Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        double d0 = -1.0D;
        BlockPos blockpos = null;
        double d1 = -1.0D;
        BlockPos blockpos1 = null;
        WorldBorder worldborder = this.level.getWorldBorder();
        int i = this.level.getHeight() - 1;
        BlockPos.Mutable blockpos$mutable = pos.mutable();

        for (BlockPos.Mutable blockpos$mutable1 : BlockPos.spiralAround(pos, 16, Direction.EAST,
                Direction.SOUTH)) {
            int j = Math.min(i, this.level.getHeight(Heightmap.Type.MOTION_BLOCKING, blockpos$mutable1.getX(),
                    blockpos$mutable1.getZ()));
            int k = 1;
            if (worldborder.isWithinBounds(blockpos$mutable1)
                    && worldborder.isWithinBounds(blockpos$mutable1.move(direction, 1))) {
                blockpos$mutable1.move(direction.getOpposite(), 1);

                for (int l = j; l >= 0; --l) {
                    blockpos$mutable1.setY(l);
                    if (this.level.isEmptyBlock(blockpos$mutable1)) {
                        int i1;
                        for (i1 = l; l > 0 && this.level.isEmptyBlock(blockpos$mutable1.move(Direction.DOWN)); --l) {
                        }

                        if (l + 4 <= i) {
                            int j1 = i1 - l;
                            if (j1 <= 0 || j1 >= 3) {
                                blockpos$mutable1.setY(l);
                                if (this.canHostFrame(blockpos$mutable1, blockpos$mutable, direction, 0)) {
                                    double d2 = pos.distSqr(blockpos$mutable1);
                                    if (this.canHostFrame(blockpos$mutable1, blockpos$mutable, direction, -1)
                                            && this.canHostFrame(blockpos$mutable1, blockpos$mutable, direction, 1)
                                            && (d0 == -1.0D || d0 > d2)) {
                                        d0 = d2;
                                        blockpos = blockpos$mutable1.immutable();
                                    }

                                    if (d0 == -1.0D && (d1 == -1.0D || d1 > d2)) {
                                        d1 = d2;
                                        blockpos1 = blockpos$mutable1.immutable();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (d0 == -1.0D && d1 != -1.0D) {
            blockpos = blockpos1;
            d0 = d1;
        }

        if (d0 == -1.0D) {
            blockpos = (new BlockPos(pos.getX(),
                    MathHelper.clamp(pos.getY(), 70, this.level.getHeight() - 10), pos.getZ()))
                    .immutable();
            Direction direction1 = direction.getClockWise();
            if (!worldborder.isWithinBounds(blockpos)) {
                return Optional.empty();
            }

            for (int l1 = -1; l1 < 2; ++l1) {
                for (int k2 = 0; k2 < 2; ++k2) {
                    for (int i3 = -1; i3 < 3; ++i3) {
                        BlockState blockstate1 = i3 < 0 ? ModBlocks.MELOBSIDIAN.get().defaultBlockState()
                                : Blocks.AIR.defaultBlockState();
                        blockpos$mutable.setWithOffset(blockpos, k2 * direction.getStepX() + l1 * direction1.getStepX(),
                                i3, k2 * direction.getStepZ() + l1 * direction1.getStepZ());
                        this.level.setBlockAndUpdate(blockpos$mutable, blockstate1);
                    }
                }
            }
        }

        for (int k1 = -1; k1 < 3; ++k1) {
            for (int i2 = -1; i2 < 4; ++i2) {
                if (k1 == -1 || k1 == 2 || i2 == -1 || i2 == 3) {
                    blockpos$mutable.setWithOffset(blockpos, k1 * direction.getStepX(), i2, k1 * direction.getStepZ());
                    this.level.setBlock(blockpos$mutable, ModBlocks.MELOBSIDIAN.get().defaultBlockState(), 3);
                }
            }
        }

        BlockState blockstate = ModBlocks.MELON_PORTAL.get().defaultBlockState().setValue(NetherPortalBlock.AXIS, axis);

        for (int j2 = 0; j2 < 2; ++j2) {
            for (int l2 = 0; l2 < 3; ++l2) {
                blockpos$mutable.setWithOffset(blockpos, j2 * direction.getStepX(), l2, j2 * direction.getStepZ());
                this.level.setBlock(blockpos$mutable, blockstate, 18);
            }
        }

        return Optional.of(new TeleportationRepositioner.Result(blockpos.immutable(), 2, 3));
    }

    private boolean canHostFrame(BlockPos pos, BlockPos.Mutable mutable,
            Direction direction, int p_242955_4_) {
        Direction clockwise = direction.getClockWise();

        for (int i = -1; i < 3; ++i) {
            for (int j = -1; j < 4; ++j) {
                mutable.setWithOffset(pos, direction.getStepX() * i + clockwise.getStepX() * p_242955_4_,
                        j, direction.getStepZ() * i + clockwise.getStepZ() * p_242955_4_);
                if (j < 0 && !this.level.getBlockState(mutable).getMaterial().isSolid()) {
                    return false;
                }

                if (j >= 0 && !this.level.isEmptyBlock(mutable)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Nullable
    public PortalInfo findDimensionEntryPoint(Entity entity, ServerWorld world) {
        boolean flag = entity.level.dimension() == World.END && world.dimension() == World.OVERWORLD;
        boolean flag1 = world.dimension() == World.END;
        if (!flag && !flag1) {
            boolean flag2 = world.dimension() == World.NETHER;
            if (entity.level.dimension() != World.NETHER && !flag2) {
                return null;
            } else {
                WorldBorder worldborder = world.getWorldBorder();
                double d0 = Math.max(-2.9999872E7D, worldborder.getMinX() + 16.0D);
                double d1 = Math.max(-2.9999872E7D, worldborder.getMinZ() + 16.0D);
                double d2 = Math.min(2.9999872E7D, worldborder.getMaxX() - 16.0D);
                double d3 = Math.min(2.9999872E7D, worldborder.getMaxZ() - 16.0D);
                double d4 = DimensionType.getTeleportationScale(entity.level.dimensionType(),
                        world.dimensionType());
                BlockPos blockpos1 = new BlockPos(MathHelper.clamp(entity.getX() * d4, d0, d2), entity.getY(),
                        MathHelper.clamp(entity.getZ() * d4, d1, d3));
                return this.getExitPortal(entity, world, blockpos1, flag2).map((p_242275_2_) -> {
                    EntityMixinAccessor ema = (EntityMixinAccessor) entity;
                    BlockState blockstate = entity.level
                            .getBlockState(ema.getPortalEntrancePos());
                    Direction.Axis direction$axis;
                    Vector3d vector3d;
                    if (blockstate.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
                        direction$axis = blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS);
                        TeleportationRepositioner.Result teleportationrepositioner$result = TeleportationRepositioner
                                .getLargestRectangleAround(ema.getPortalEntrancePos(), direction$axis, 21,
                                        Direction.Axis.Y,
                                        21, (p_242276_2_) -> {
                                            return entity.level.getBlockState(p_242276_2_) == blockstate;
                                        });
                        vector3d = getRelativePortalPosition(entity, direction$axis, teleportationrepositioner$result);
                    } else {
                        direction$axis = Direction.Axis.X;
                        vector3d = new Vector3d(0.5D, 0.0D, 0.0D);
                    }

                    return PortalSize.createPortalInfo(world, p_242275_2_, direction$axis, vector3d,
                            entity.getDimensions(entity.getPose()), entity.getDeltaMovement(), entity.yRot,
                            entity.xRot);
                }).orElse((PortalInfo) null);
            }
        } else {
            BlockPos blockpos;
            if (flag1) {
                blockpos = ServerWorld.END_SPAWN_POINT;
            } else {
                blockpos = world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                        world.getSharedSpawnPos());
            }

            return new PortalInfo(new Vector3d((double) blockpos.getX() + 0.5D, (double) blockpos.getY(),
                    (double) blockpos.getZ() + 0.5D), entity.getDeltaMovement(), entity.yRot, entity.xRot);
        }
    }

    public Optional<TeleportationRepositioner.Result> getExitPortal(Entity entity, ServerWorld world,
            BlockPos pos, boolean p_241830_3_) {
        Optional<TeleportationRepositioner.Result> optional = findPortalAround(pos, p_241830_3_);
        if (optional.isPresent()) {
            return optional;
        } else {
            Direction.Axis direction$axis = this.level
                    .getBlockState(((EntityMixinAccessor) entity).getPortalEntrancePos())
                    .getOptionalValue(NetherPortalBlock.AXIS).orElse(Direction.Axis.X);
            Optional<TeleportationRepositioner.Result> optional1 = this // world.getPortalForcer()
                    .createPortal(pos, direction$axis);
            if (!optional1.isPresent()) {
                LogManager.getLogger().error("Unable to create a portal, likely target out of worldborder");
            }

            return optional1;
        }
    }

    @Override
    public Optional<TeleportationRepositioner.Result> findPortalAround(BlockPos pos, boolean p_242957_2_) {
        PointOfInterestManager pointofinterestmanager = this.level.getPoiManager();
        int i = p_242957_2_ ? 16 : 128;
        pointofinterestmanager.ensureLoadedAndValid(this.level, pos, i);
        Optional<PointOfInterest> optional = pointofinterestmanager.getInSquare((poiType) -> {
            return poiType == PointOfInterestType.NETHER_PORTAL; // TODO: add melon portal poi
        }, pos, i, PointOfInterestManager.Status.ANY)
                .sorted(Comparator.<PointOfInterest>comparingDouble((poi) -> {
                    return poi.getPos().distSqr(pos);
                }).thenComparingInt((poi) -> {
                    return poi.getPos().getY();
                })).filter((poi) -> {
                    return this.level.getBlockState(poi.getPos())
                            .hasProperty(BlockStateProperties.HORIZONTAL_AXIS);
                }).findFirst();
        return optional.map((poi) -> {
            BlockPos blockpos = poi.getPos();
            this.level.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(blockpos), 3, blockpos);
            BlockState blockstate = this.level.getBlockState(blockpos);
            return TeleportationRepositioner.getLargestRectangleAround(blockpos,
                    blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21,
                    (blockPos) -> {
                        return this.level.getBlockState(blockPos) == blockstate;
                    });
        });
    }

    public static Vector3d getRelativePortalPosition(Entity entity, Direction.Axis axis,
            TeleportationRepositioner.Result result) {
        return PortalSize.getRelativePosition(result, axis, entity.position(),
                entity.getDimensions(entity.getPose()));
    }

    private void triggerDimensionChangeTriggers(ServerPlayerEntity player, ServerWorld world) {
        RegistryKey<World> newDim = world.dimension();
        RegistryKey<World> oldDim = player.level.dimension();
        CriteriaTriggers.CHANGED_DIMENSION.trigger(player, newDim, oldDim);
        ServerPlayerEntityMixinAccessor spma = (ServerPlayerEntityMixinAccessor) player;
        if (newDim == World.NETHER && oldDim == World.OVERWORLD && spma.getEnteredMelonDimPosition() != null) {
            CriteriaTriggers.NETHER_TRAVEL.trigger(player, spma.getEnteredMelonDimPosition());
        }

        if (oldDim != World.NETHER) {
            spma.setEnteredMelonDimPosition(null);
        }

    }
}
