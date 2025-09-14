package destiny.armoryofdestiny.server.block;

import destiny.armoryofdestiny.server.block.blockentity.BloomeryTopBlockEntity;
import destiny.armoryofdestiny.server.block.utility.TooltipBaseEntityBlock;
import destiny.armoryofdestiny.server.item.SmithingTongsItem;
import destiny.armoryofdestiny.server.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static destiny.armoryofdestiny.server.item.SmithingTongsItem.HELD_ITEM;
import static destiny.armoryofdestiny.server.util.UtilityVariables.BLOOMERY_TOP;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT;

public class BloomeryTopBlock extends TooltipBaseEntityBlock {
    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 14, 15);

    public BloomeryTopBlock(Properties build) {
        super(build);
        this.registerDefaultState(this.defaultBlockState().setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof BloomeryTopBlockEntity bloomery) {
                Containers.dropContents(level, pos, bloomery.getDroppableInventory());
                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (level.getBlockEntity(pos) instanceof BloomeryTopBlockEntity bloomery) {
            if (hitResult.getDirection() == state.getValue(HORIZONTAL_FACING)) {
                if (player.getCooldowns().isOnCooldown(stack.getItem())) {
                    return InteractionResult.PASS;
                }

                if (stack.getItem() instanceof SmithingTongsItem) {
                    //Put item using tongs
                    if (bloomery.getInput().isEmpty()) {
                        if (stack.getTag() != null && stack.getTag().get(HELD_ITEM) != null) {
                            ItemStack held_item = ItemStack.of(stack.getTag().getCompound(HELD_ITEM).copy());

                            if (!held_item.isEmpty()) {
                                bloomery.setInput(held_item);
                                stack.getOrCreateTag().put(HELD_ITEM, ItemStack.EMPTY.serializeNBT());

                                level.playSound(null, player.blockPosition().above(), SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS, 1, 1);

                                return InteractionResult.SUCCESS;
                            }
                        }
                    }

                    //Take item using tongs
                    if (!bloomery.getInput().isEmpty()) {
                        if (stack.getTag() != null && stack.getTag().get(HELD_ITEM) != null) {
                            ItemStack held_item = ItemStack.of(stack.getTag().getCompound(HELD_ITEM).copy());

                            if (held_item.isEmpty()) {
                                stack.getOrCreateTag().put(HELD_ITEM, bloomery.getInput().serializeNBT());
                                bloomery.setInput(ItemStack.EMPTY);

                                level.playSound(null, player.blockPosition().above(), SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 1, 1);

                                if (!player.isCreative()) {
                                    stack.setDamageValue(stack.getDamageValue() + 1);
                                }

                                return InteractionResult.SUCCESS;
                            }
                        }
                    }
                }

                //Put item into bloomery
                if (!stack.isEmpty()) {
                    if (bloomery.getInput().isEmpty()) {
                        ItemStack input = stack.copy();
                        input.setCount(1);
                        bloomery.setInput(input);

                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }

                        level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS);

                        return InteractionResult.SUCCESS;
                    }
                }

                if (stack.isEmpty()) {
                    //Take item from bloomery
                    if (!bloomery.getInput().isEmpty()) {
                        player.addItem(bloomery.getInput());
                        bloomery.setInput(ItemStack.EMPTY);

                        level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS);

                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(LIT)) {
            addParticles(pLevel, pPos, pRandom);
        }
    }

    private void addParticles(Level pLevel, BlockPos pPos, RandomSource pRandom) {
        double x = pPos.getX() + 0.5F;
        double y = pPos.getY();
        double z = pPos.getZ() + 0.5F;

        if (pRandom.nextDouble() < 0.3) {
            pLevel.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y + 1.2, z, 0, 0.010, 0);
        }

        if (pRandom.nextDouble() < 0.4) {
            pLevel.addParticle(ParticleTypes.LAVA, x, y, z, 0.0F, 0.0F, 0.0F);
        }

        if (pRandom.nextDouble() < 0.4) {
            pLevel.playLocalSound(pPos, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1F, 1F, false);
        }
    }

    @javax.annotation.Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite()).setValue(LIT, false);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, LIT);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.BLOOMERY_TOP.get().create(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
        return createTickerHelper(blockEntity, BlockEntityRegistry.BLOOMERY_TOP.get(), BloomeryTopBlockEntity::tick);
    }

    @Override
    public String getTriviaTranslatable() {
        return BLOOMERY_TOP;
    }
}
