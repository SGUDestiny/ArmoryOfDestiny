package destiny.armoryofdestiny.server.block;

import destiny.armoryofdestiny.server.block.blockentity.NetherBloomeryBottomBlockEntity;
import destiny.armoryofdestiny.server.block.utility.TooltipBaseEntityBlock;
import destiny.armoryofdestiny.server.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

import static destiny.armoryofdestiny.server.util.UtilityVariables.BLOOMERY_BOTTOM;
import static destiny.armoryofdestiny.server.util.UtilityVariables.BLOOMERY_TOP;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class NetherBloomeryBottomBlock extends TooltipBaseEntityBlock {
    public static final TagKey<Item> LOGS_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("minecraft", "logs"));

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final IntegerProperty LOGS = IntegerProperty.create("logs", 0, 4);

    public NetherBloomeryBottomBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, Direction.NORTH)
                .setValue(LOGS, 0)
                .setValue(OPEN, false)
                .setValue(LIT, false)
        );
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof NetherBloomeryBottomBlockEntity bloomery) {
                Containers.dropContents(level, pos, bloomery.getDroppableInventory());
                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (level.getBlockEntity(pos) instanceof NetherBloomeryBottomBlockEntity bloomery) {
            if (hitResult.getDirection() == state.getValue(HORIZONTAL_FACING)) {
                if (state.getValue(OPEN)) {
                    //Light bloomery up
                    if (stack.getItem() == Items.FLINT_AND_STEEL) {
                        if (!state.getValue(LIT)) {
                            if (state.getValue(LOGS) > 0) {
                                level.setBlockAndUpdate(pos, state.setValue(LIT, true));

                                if (!player.isCreative()) {
                                    stack.setDamageValue(stack.getDamageValue() + 1);
                                }

                                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS);

                                return InteractionResult.SUCCESS;
                            } else {
                                return InteractionResult.CONSUME;
                            }
                        }
                    }

                    //Extinguish bloomery
                    if (stack.canPerformAction(ToolActions.SHOVEL_DIG)) {
                        if (state.getValue(LIT)) {
                            level.setBlockAndUpdate(pos, state.setValue(LIT, false));
                            bloomery.setBurnTick(0);

                            if (!player.isCreative()) {
                                stack.setDamageValue(stack.getDamageValue() + 1);
                            }

                            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS);

                            return InteractionResult.SUCCESS;
                        }
                    }

                    //Put a log
                    if (stack.is(LOGS_TAG)) {
                        if (state.getValue(LOGS) < 4) {
                            ItemStack log = stack.copy();
                            log.setCount(1);

                            if (!player.isCreative()) {
                                stack.shrink(1);
                            }

                            bloomery.putLog(log);
                            level.setBlockAndUpdate(pos, state.setValue(LOGS, state.getValue(LOGS) + 1));

                            level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS);

                            return InteractionResult.SUCCESS;
                        }
                    }

                    //Take a log
                    if (stack.getItem() == Items.AIR) {
                        if (player.isCrouching()) {
                            if (!state.getValue(LIT)) {
                                if (state.getValue(LOGS) > 0) {
                                    player.addItem(bloomery.takeLog());
                                    level.setBlockAndUpdate(pos, state.setValue(LOGS, state.getValue(LOGS) - 1));

                                    level.playSound(null, pos, SoundEvents.WOOD_HIT, SoundSource.BLOCKS);

                                    return InteractionResult.SUCCESS;
                                }
                            }
                        }
                    }
                }

                //Open or close bloomery
                if (stack.getItem() == Items.AIR) {
                    if (!player.isCrouching()) {
                        level.setBlockAndUpdate(pos, state.cycle(OPEN));

                        if (state.getValue(OPEN)) {
                            level.playSound(null, pos, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.BLOCKS);
                        } else {
                            level.playSound(null, pos, SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.BLOCKS);
                        }

                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(LIT)) {
            addParticles(pLevel, pPos, pState, pRandom);
        }
    }

    private static void addParticles(Level pLevel, BlockPos pPos, BlockState pState, RandomSource pRandom) {
        double x = pPos.getX() + 0.5F;
        double y = pPos.getY();
        double z = pPos.getZ() + 0.5F;
        if (pRandom.nextDouble() < 0.1) {
            pLevel.playLocalSound(x, y, z, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F, 0.5F, false);
        }

        if (!pState.getValue(OPEN)) {
            Direction direction = pState.getValue(HORIZONTAL_FACING);
            Direction.Axis axis = direction.getAxis();
            double $$10 = pRandom.nextDouble() * 0.6 - 0.3;
            double $$11 = axis == Direction.Axis.X ? direction.getStepX() * 0.52 : $$10;
            double $$12 = pRandom.nextDouble() * 10.0F / 16.0F;
            double $$13 = axis == Direction.Axis.Z ? direction.getStepZ() * 0.52 : $$10;
            pLevel.addParticle(ParticleTypes.SMOKE, x + $$11, y + $$12, z + $$13, 0.0F, 0.0F, 0.0F);
            pLevel.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x + $$11, y + $$12, z + $$13, 0.0F, 0.0F, 0.0F);
        }

        if (pLevel.getBlockState(pPos.above()).isAir()) {
            if (pRandom.nextDouble() < 0.3) {
                pLevel.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y + 1.1, z, 0, 0.010, 0);
            }
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity && state.getValue(LIT) ) {
            if (!(level.getBlockState(pos.above()).getBlock() instanceof BloomeryTopBlock)) {
                entity.hurt(level.damageSources().hotFloor(), 1);
            }
        }
    }

    @javax.annotation.Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite())
                .setValue(LOGS, 0)
                .setValue(OPEN, false)
                .setValue(LIT, false);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, LOGS, OPEN, LIT);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public String getTriviaTranslatable() {
        return BLOOMERY_BOTTOM;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.NETHER_BLOOMERY_BOTTOM.get().create(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
        return createTickerHelper(blockEntity, BlockEntityRegistry.NETHER_BLOOMERY_BOTTOM.get(), NetherBloomeryBottomBlockEntity::tick);
    }
}
