package uk.jacobempire.melonmod.common.block;

import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.SixWayBlock;
import net.minecraft.block.SoulFireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import uk.jacobempire.melonmod.common.init.ModBlocks;

public class MelonFireBlock extends FireBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
    public static final BooleanProperty NORTH = SixWayBlock.NORTH;
    public static final BooleanProperty EAST = SixWayBlock.EAST;
    public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
    public static final BooleanProperty WEST = SixWayBlock.WEST;
    public static final BooleanProperty UP = SixWayBlock.UP;

    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = SixWayBlock.PROPERTY_BY_DIRECTION
            .entrySet().stream().filter((direction) -> {
                return direction.getKey() != Direction.DOWN;
            }).collect(Util.toMap());

    public MelonFireBlock(Properties properties) {
        super(properties);

        bootStrap(this);
    }

    public static BlockState getState(IBlockReader blockReader, BlockPos blockPos) {
        BlockPos blockpos = blockPos.below();
        BlockState blockstate = blockReader.getBlockState(blockpos);
        // TODO: add meloul fire
        return SoulFireBlock.canSurviveOnBlock(blockstate.getBlock()) ? Blocks.SOUL_FIRE.defaultBlockState()
                : (ModBlocks.MELON_FIRE.get()).getStateForPlacement(blockReader, blockPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getStateForPlacement(context.getLevel(), context.getClickedPos());
    }

    protected BlockState getStateForPlacement(IBlockReader blockReader, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = blockReader.getBlockState(blockpos);
        if (!this.canCatchFire(blockReader, pos, Direction.UP)
                && !blockstate.isFaceSturdy(blockReader, blockpos, Direction.UP)) {
            BlockState defaultState = this.defaultBlockState();

            for (Direction direction : Direction.values()) {
                BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(direction);
                if (booleanproperty != null) {
                    defaultState = defaultState.setValue(booleanproperty, Boolean.valueOf(
                            this.canCatchFire(blockReader, pos.relative(direction), direction.getOpposite())));
                }
            }

            return defaultState;
        } else {
            return this.defaultBlockState();
        }
    }

    /**
     * Side sensitive version that calls the block function.
     *
     * @param world The current world
     * @param pos   Block position
     * @param face  The side the fire is coming from
     * @return True if the face can catch fire.
     */
    public boolean canCatchFire(IBlockReader world, BlockPos pos, Direction face) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        if (block instanceof IFlamelonable) {
            return ((IFlamelonable) block).isFlamelonable(blockState, world, pos, face);
        }
        return blockState.isFlammable(world, pos, face);
    }

    boolean isFlamelonable(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return getBurnOdd(state) > 0;
    }

    private static int getFireTickDelay(Random random) {
        return 30 + random.nextInt(10);
    }

    private void tryCatchFire(World world, BlockPos pos,
            int n, Random random, int age, Direction face) {

        int fmelammability = world.getBlockState(pos).getFlammability(world, pos, face);
        if (random.nextInt(n) < fmelammability) {
            BlockState blockstate = world.getBlockState(pos);
            if (random.nextInt(age + 10) < 5 && !world.isRainingAt(pos)) {
                int newAge = Math.min(age + random.nextInt(5) / 4, 15);
                world.setBlock(pos, this.getStateWithAge(world, pos, newAge), 3);
            } else {
                // world.setBlock(pos.relative(face), Blocks.MELON.defaultBlockState(), 3);
                world.removeBlock(pos, false);
            }

            // blockstate.catchFire(world, pos, face, null);
            if (blockstate.getBlock() instanceof IFlamelonable) {
                ((IFlamelonable) blockstate.getBlock()).catchMelonFire(blockstate, world, pos, face, null);
            } else {
                catchMelonFire(blockstate, world, pos, face, null);
            }
        }

    }

    void catchMelonFire(BlockState state, World world, BlockPos pos, @Nullable Direction face,
            @Nullable LivingEntity igniter) {

        world.setBlock(pos.relative(face), getStateWithAge(world, pos, 0), 3);
    }

    private BlockState getStateWithAge(IWorld world, BlockPos pos, int age) {
        BlockState blockstate = getState(world, pos);
        return blockstate.is(ModBlocks.MELON_FIRE.get()) ? blockstate.setValue(AGE, Integer.valueOf(age)) : blockstate;
    }

    private boolean isValidFireLocation(IBlockReader reader, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (this.canCatchFire(reader, pos.relative(direction), direction.getOpposite())) {
                return true;
            }
        }

        return false;
    }

    private int getFireOdds(IWorldReader worldReader, BlockPos pos) {
        if (!worldReader.isEmptyBlock(pos)) {
            return 0;
        } else {
            int i = 0;

            for (Direction direction : Direction.values()) {
                BlockState blockstate = worldReader.getBlockState(pos.relative(direction));
                i = Math.max(blockstate.getFireSpreadSpeed(worldReader, pos.relative(direction),
                        direction.getOpposite()), i);
            }

            return i;
        }
    }

    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // LogManager.getLogger().info("hello world from MelonFireBlock.tick()");
        world.getBlockTicks().scheduleTick(pos, this, getFireTickDelay(world.random));
        if (world.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            if (!state.canSurvive(world, pos)) {
                world.removeBlock(pos, false);
            }

            BlockState blockstate = world.getBlockState(pos.below());
            boolean isFireSource = blockstate.isFireSource(world, pos, Direction.UP);
            int age = state.getValue(AGE);
            if (!isFireSource && world.isRaining() && this.isNearRain(world, pos)
                    && random.nextFloat() < 0.2F + (float) age * 0.03F) {
                world.removeBlock(pos, false);
            } else {
                int newAge = Math.min(15, age + random.nextInt(3) / 2);
                if (age != newAge) {
                    state = state.setValue(AGE, Integer.valueOf(newAge));
                    world.setBlock(pos, state, 4);
                }

                if (!isFireSource) {
                    if (!this.isValidFireLocation(world, pos)) {
                        BlockPos blockpos = pos.below();
                        if (!world.getBlockState(blockpos).isFaceSturdy(world, blockpos, Direction.UP)
                                || age > 3) {
                            world.removeBlock(pos, false);
                        }

                        return;
                    }

                    if (age == 15 && random.nextInt(4) == 0
                            && !this.canCatchFire(world, pos.below(), Direction.UP)) {
                        world.removeBlock(pos, false);
                        return;
                    }
                }

                boolean flag1 = world.isHumidAt(pos);
                int k = flag1 ? -50 : 0;
                this.tryCatchFire(world, pos.east(), 300 + k, random, age, Direction.WEST);
                this.tryCatchFire(world, pos.west(), 300 + k, random, age, Direction.EAST);
                this.tryCatchFire(world, pos.below(), 250 + k, random, age, Direction.UP);
                this.tryCatchFire(world, pos.above(), 250 + k, random, age, Direction.DOWN);
                this.tryCatchFire(world, pos.north(), 300 + k, random, age, Direction.SOUTH);
                this.tryCatchFire(world, pos.south(), 300 + k, random, age, Direction.NORTH);
                BlockPos.Mutable mutable = new BlockPos.Mutable();

                for (int l = -1; l <= 1; ++l) {
                    for (int i1 = -1; i1 <= 1; ++i1) {
                        for (int j1 = -1; j1 <= 4; ++j1) {
                            if (l != 0 || j1 != 0 || i1 != 0) {
                                int k1 = 100;
                                if (j1 > 1) {
                                    k1 += (j1 - 1) * 100;
                                }

                                mutable.setWithOffset(pos, l, j1, i1);
                                int l1 = this.getFireOdds(world, mutable);
                                if (l1 > 0) {
                                    int i2 = (l1 + 40 + world.getDifficulty().getId() * 7) / (age + 30);
                                    if (flag1) {
                                        i2 /= 2;
                                    }

                                    if (i2 > 0 && random.nextInt(k1) <= i2 && (!world.isRaining()
                                            || !this.isNearRain(world, mutable))) {
                                        int j2 = Math.min(15, age + random.nextInt(5) / 4);
                                        world.setBlock(mutable,
                                                this.getStateWithAge(world, mutable, j2), 3);
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    protected boolean isNearRain(World world, BlockPos pos) {
        return world.isRainingAt(pos) || world.isRainingAt(pos.west())
                || world.isRainingAt(pos.east()) || world.isRainingAt(pos.north())
                || world.isRainingAt(pos.south());
    }

    @Override
    protected boolean canBurn(BlockState state) {
        return getFlamelonOdds(state) > 0;
    }

    @Deprecated // Forge: Use IForgeBlockState.getFlammability, Public for default
                // implementation only.
    public int getBurnOdd(BlockState p_220274_1_) {
        return p_220274_1_.hasProperty(BlockStateProperties.WATERLOGGED)
                && p_220274_1_.getValue(BlockStateProperties.WATERLOGGED) ? 0
                        : this.burnOdds.getInt(p_220274_1_.getBlock());
    }

    @Deprecated // Forge: Use IForgeBlockState.getFireSpreadSpeed
    public int getFlamelonOdds(BlockState state) {
        return state.hasProperty(BlockStateProperties.WATERLOGGED)
                && state.getValue(BlockStateProperties.WATERLOGGED) ? 0
                        : this.flameOdds.getInt(state.getBlock());
    }

    private final Object2IntMap<Block> flameOdds = new Object2IntOpenHashMap<>();
    private final Object2IntMap<Block> burnOdds = new Object2IntOpenHashMap<>();

    private void setFlamelonable(Block block, int flameOdd, int burnOdd) {
        if (block == Blocks.AIR)
            throw new IllegalArgumentException("Tried to set air on fire... This is bad.");
        this.flameOdds.put(block, flameOdd);
        this.burnOdds.put(block, burnOdd);
    }

    public void bootStrap(MelonFireBlock melonFireBlock) {
        melonFireBlock.setFlamelonable(Blocks.OAK_PLANKS, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.SPRUCE_PLANKS, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.BIRCH_PLANKS, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.JUNGLE_PLANKS, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.ACACIA_PLANKS, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.DARK_OAK_PLANKS, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.OAK_SLAB, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.SPRUCE_SLAB, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.BIRCH_SLAB, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.JUNGLE_SLAB, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.ACACIA_SLAB, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.DARK_OAK_SLAB, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.OAK_FENCE_GATE, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.SPRUCE_FENCE_GATE, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.BIRCH_FENCE_GATE, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.JUNGLE_FENCE_GATE, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.DARK_OAK_FENCE_GATE, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.ACACIA_FENCE_GATE, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.OAK_FENCE, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.SPRUCE_FENCE, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.BIRCH_FENCE, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.JUNGLE_FENCE, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.DARK_OAK_FENCE, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.ACACIA_FENCE, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.OAK_STAIRS, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.BIRCH_STAIRS, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.SPRUCE_STAIRS, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.JUNGLE_STAIRS, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.ACACIA_STAIRS, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.DARK_OAK_STAIRS, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.OAK_LOG, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.SPRUCE_LOG, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.BIRCH_LOG, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.JUNGLE_LOG, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.ACACIA_LOG, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.DARK_OAK_LOG, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.STRIPPED_OAK_LOG, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.STRIPPED_SPRUCE_LOG, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.STRIPPED_BIRCH_LOG, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.STRIPPED_JUNGLE_LOG, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.STRIPPED_ACACIA_LOG, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.STRIPPED_DARK_OAK_LOG, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.STRIPPED_OAK_WOOD, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.STRIPPED_SPRUCE_WOOD, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.STRIPPED_BIRCH_WOOD, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.STRIPPED_JUNGLE_WOOD, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.STRIPPED_ACACIA_WOOD, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.STRIPPED_DARK_OAK_WOOD, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.OAK_WOOD, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.SPRUCE_WOOD, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.BIRCH_WOOD, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.JUNGLE_WOOD, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.ACACIA_WOOD, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.DARK_OAK_WOOD, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.OAK_LEAVES, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.SPRUCE_LEAVES, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.BIRCH_LEAVES, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.JUNGLE_LEAVES, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.ACACIA_LEAVES, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.DARK_OAK_LEAVES, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.BOOKSHELF, 30, 20);
        melonFireBlock.setFlamelonable(Blocks.TNT, 15, 100);
        melonFireBlock.setFlamelonable(Blocks.GRASS, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.FERN, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.DEAD_BUSH, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.SUNFLOWER, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.LILAC, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.ROSE_BUSH, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.PEONY, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.TALL_GRASS, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.LARGE_FERN, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.DANDELION, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.POPPY, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.BLUE_ORCHID, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.ALLIUM, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.AZURE_BLUET, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.RED_TULIP, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.ORANGE_TULIP, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.WHITE_TULIP, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.PINK_TULIP, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.OXEYE_DAISY, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.CORNFLOWER, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.LILY_OF_THE_VALLEY, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.WITHER_ROSE, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.WHITE_WOOL, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.ORANGE_WOOL, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.MAGENTA_WOOL, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.LIGHT_BLUE_WOOL, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.YELLOW_WOOL, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.LIME_WOOL, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.PINK_WOOL, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.GRAY_WOOL, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.LIGHT_GRAY_WOOL, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.CYAN_WOOL, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.PURPLE_WOOL, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.BLUE_WOOL, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.BROWN_WOOL, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.GREEN_WOOL, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.RED_WOOL, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.BLACK_WOOL, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.VINE, 15, 100);
        melonFireBlock.setFlamelonable(Blocks.COAL_BLOCK, 5, 5);
        melonFireBlock.setFlamelonable(Blocks.HAY_BLOCK, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.TARGET, 15, 20);
        melonFireBlock.setFlamelonable(Blocks.WHITE_CARPET, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.ORANGE_CARPET, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.MAGENTA_CARPET, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.LIGHT_BLUE_CARPET, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.YELLOW_CARPET, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.LIME_CARPET, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.PINK_CARPET, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.GRAY_CARPET, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.LIGHT_GRAY_CARPET, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.CYAN_CARPET, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.PURPLE_CARPET, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.BLUE_CARPET, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.BROWN_CARPET, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.GREEN_CARPET, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.RED_CARPET, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.BLACK_CARPET, 60, 20);
        melonFireBlock.setFlamelonable(Blocks.DRIED_KELP_BLOCK, 30, 60);
        melonFireBlock.setFlamelonable(Blocks.BAMBOO, 60, 60);
        melonFireBlock.setFlamelonable(Blocks.SCAFFOLDING, 60, 60);
        melonFireBlock.setFlamelonable(Blocks.LECTERN, 30, 20);
        melonFireBlock.setFlamelonable(Blocks.COMPOSTER, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.SWEET_BERRY_BUSH, 60, 100);
        melonFireBlock.setFlamelonable(Blocks.BEEHIVE, 5, 20);
        melonFireBlock.setFlamelonable(Blocks.BEE_NEST, 30, 20);
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE, NORTH, EAST, SOUTH, WEST, UP);
    }

}
