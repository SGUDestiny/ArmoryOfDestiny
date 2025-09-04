package destiny.armoryofdestiny.server.block;

import destiny.armoryofdestiny.server.block.blockentity.ArmorersAnvilBlockEntity;
import destiny.armoryofdestiny.server.block.utility.TooltipBaseEntityBlock;
import destiny.armoryofdestiny.server.item.BlueprintItem;
import destiny.armoryofdestiny.server.item.SmithingHammerItem;
import destiny.armoryofdestiny.server.item.SmithingTongsItem;
import destiny.armoryofdestiny.server.registry.BlockEntityRegistry;
import destiny.armoryofdestiny.server.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static destiny.armoryofdestiny.server.block.ArmorersTinkeringTableBlock.HAS_BLUEPRINT;
import static destiny.armoryofdestiny.server.item.SmithingTongsItem.HELD_ITEM;
import static destiny.armoryofdestiny.server.util.UtilityVariables.ARMORERS_ANVIL;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ArmorersAnvilBlock extends TooltipBaseEntityBlock {
    private static final VoxelShape SHAPE_NORTH = MathUtil.buildShape(
            Block.box(0, 9, 3, 16, 16, 13),
            Block.box(3, 4, 5, 13, 9, 11),
            Block.box(2, 3, 4, 14, 4, 12),
            Block.box(1, 0, 3, 15, 3, 13),
            Block.box(-7, 13, 4, 0, 16, 12),
            Block.box(16, 11, 5, 24, 16, 11)
    );
    private static final VoxelShape SHAPE_SOUTH = MathUtil.buildShape(
            Block.box(0, 9, 3, 16, 16, 13),
            Block.box(3, 4, 5, 13, 9, 11),
            Block.box(2, 3, 4, 14, 4, 12),
            Block.box(1, 0, 3, 15, 3, 13),
            Block.box(16, 13, 4, 23, 16, 12),
            Block.box(-8, 11, 5, 0, 16, 11)
    );
    private static final VoxelShape SHAPE_WEST = MathUtil.buildShape(
            Block.box(3, 9, 0, 13, 16, 16),
            Block.box(5, 4, 3, 11, 9, 13),
            Block.box(4, 3, 2, 12, 4, 14),
            Block.box(3, 0, 1, 13, 3, 15),
            Block.box(4, 13, 16, 12, 16, 23),
            Block.box(5, 11, -8, 11, 16, 0)
    );
    private static final VoxelShape SHAPE_EAST = MathUtil.buildShape(
            Block.box(3, 9, 0, 13, 16, 16),
            Block.box(5, 4, 3, 11, 9, 13),
            Block.box(4, 3, 2, 12, 4, 14),
            Block.box(3, 0, 1, 13, 3, 15),
            Block.box(4, 13, -7, 12, 16, 0),
            Block.box(5, 11, 16, 11, 16, 24)
    );

    public ArmorersAnvilBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(HAS_BLUEPRINT, false));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof ArmorersAnvilBlockEntity anvil) {
                Containers.dropContents(level, pos, anvil.getDroppableInventory());
                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (level.getBlockEntity(pos) instanceof ArmorersAnvilBlockEntity anvil) {
            if (hitResult.getDirection() == state.getValue(HORIZONTAL_FACING)) {
                if (stack.getItem() instanceof SmithingTongsItem) {
                    //Put tongs
                    if (anvil.getTongs().isEmpty()) {
                        anvil.setTongs(stack.copy());

                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }

                        level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS);

                        return InteractionResult.SUCCESS;
                    }

                    //Take tongs
                    if (stack.isEmpty()) {
                        if (!anvil.getTongs().isEmpty()) {
                            player.addItem(anvil.getTongs().copy());
                            anvil.setTongs(ItemStack.EMPTY);

                            level.setBlockAndUpdate(pos, state);

                            level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS);

                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }

            if (stack.getItem() instanceof SmithingTongsItem) {
                //Put item using tongs
                if (anvil.getStoredItemAmount() < 8) {
                    if (stack.getTag() != null && stack.getTag().get(HELD_ITEM) != null) {
                        ItemStack held_item = ItemStack.of(stack.getTag().getCompound(HELD_ITEM).copy());

                        if (!held_item.isEmpty()) {
                            anvil.addStoredItem(held_item);
                            stack.getOrCreateTag().put(HELD_ITEM, ItemStack.EMPTY.serializeNBT());

                            level.playSound(null, player.blockPosition().above(), SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS, 1, 1);

                            return InteractionResult.SUCCESS;
                        }
                    }
                }

                //Take item using tongs
                if (anvil.getStoredItemAmount() > 0) {
                    if (stack.getTag() != null && stack.getTag().get(HELD_ITEM) != null) {
                        ItemStack held_item = ItemStack.of(stack.getTag().getCompound(HELD_ITEM).copy());

                        if (held_item.isEmpty()) {
                            stack.getOrCreateTag().put(HELD_ITEM, anvil.getLastStoredItem().copy().serializeNBT());
                            anvil.removeLastStoredItem();

                            level.playSound(null, player.blockPosition().above(), SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 1, 1);

                            level.setBlockAndUpdate(pos, state);

                            if (!player.isCreative()) {
                                stack.setDamageValue(stack.getDamageValue() + 1);
                            }

                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }

            if (player.isCrouching()) {
                //Take blueprint
                if (stack.isEmpty()) {
                    if (!anvil.getBlueprint().isEmpty()) {
                        int size = anvil.getStoredItemAmount();

                        for (int i = 0; i < size; i++) {
                            ItemStack copy = anvil.getLastStoredItem().copy();
                            copy.setCount(1);

                            player.addItem(copy);
                            anvil.removeLastStoredItem();
                        }

                        player.addItem(anvil.getBlueprint().copy());
                        anvil.setBlueprint(ItemStack.EMPTY);
                        level.setBlockAndUpdate(pos, state.setValue(HAS_BLUEPRINT, false));
                        level.playSound(null, pos, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS);

                        return InteractionResult.SUCCESS;
                    }
                }
            }

            //Put blueprint
            if (stack.getItem() instanceof BlueprintItem) {
                if (anvil.getStoredItemAmount() == 0) {
                    if (anvil.getBlueprint().isEmpty()) {
                        anvil.setBlueprint(stack);

                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }

                        level.setBlockAndUpdate(pos, state.setValue(HAS_BLUEPRINT, true));
                        level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS);

                        return InteractionResult.SUCCESS;
                    }
                }
            }

            //Advance crafting with hammer
            if (stack.getItem() instanceof SmithingHammerItem) {
                if (anvil.advanceCrafting(level, pos, player)) {
                    level.setBlockAndUpdate(pos, state);
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.PASS;
                }
            }

            //Take item from anvil
            if (stack.isEmpty()) {
                if (anvil.getStoredItemAmount() > 0) {
                    ItemStack copy = anvil.getLastStoredItem().copy();
                    copy.setCount(1);

                    player.addItem(copy);
                    anvil.removeLastStoredItem();

                    level.setBlockAndUpdate(pos, state);

                    level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS);

                    return InteractionResult.SUCCESS;
                }
            }

            //Add item to anvil
            if (!stack.isEmpty()) {
                if (anvil.getStoredItemAmount() < 8) {
                    ItemStack copy = stack.copy();
                    copy.setCount(1);

                    anvil.addStoredItem(copy);

                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }

                    level.setBlockAndUpdate(pos, state);

                    level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS);

                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }

    @javax.annotation.Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite()).setValue(HAS_BLUEPRINT, false);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, HAS_BLUEPRINT);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
    {
        switch (pState.getValue(HORIZONTAL_FACING)) {
            case NORTH:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            case EAST:
                return SHAPE_EAST;
            case WEST:
                return SHAPE_WEST;
            default:
                return SHAPE_NORTH;
        }
    }

    @Override
    public String getTriviaTranslatable() {
        return ARMORERS_ANVIL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.ARMORERS_ANVIL.get().create(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
        return createTickerHelper(blockEntity, BlockEntityRegistry.ARMORERS_ANVIL.get(), ArmorersAnvilBlockEntity::tick);
    }
}
