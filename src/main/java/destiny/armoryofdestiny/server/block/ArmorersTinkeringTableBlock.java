package destiny.armoryofdestiny.server.block;

import destiny.armoryofdestiny.server.block.utility.TooltipBaseEntityBlock;
import destiny.armoryofdestiny.server.block.blockentity.ArmorersTinkeringTableBlockEntity;
import destiny.armoryofdestiny.server.item.BlueprintItem;
import destiny.armoryofdestiny.server.item.SmithingHammerItem;
import destiny.armoryofdestiny.server.registry.BlockEntityRegistry;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import static destiny.armoryofdestiny.server.misc.UtilityVariables.ARMORERS_TINKERING_TABLE;
import static destiny.armoryofdestiny.server.misc.UtilityVariables.ARMORERS_WORKSHOP_PART;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ArmorersTinkeringTableBlock extends TooltipBaseEntityBlock {
    public static final BooleanProperty HAS_BLUEPRINT = BooleanProperty.create("has_blueprint");

    public ArmorersTinkeringTableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(HAS_BLUEPRINT, false).setValue(HORIZONTAL_FACING, Direction.NORTH));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(HAS_BLUEPRINT, HORIZONTAL_FACING);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof ArmorersTinkeringTableBlockEntity table) {
                Containers.dropContents(level, pos, table.getDroppableInventoryWithHammer());
                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @javax.annotation.Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HAS_BLUEPRINT, false).setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (hit.getDirection() == Direction.UP) {
            if (level.getBlockEntity(pos) instanceof ArmorersTinkeringTableBlockEntity table) {
                if (table.hasBlueprint()) {
                    if (heldItem.getItem() instanceof SmithingHammerItem) {
                        table.advanceCrafting(level, pos, player, heldItem);

                        return InteractionResult.SUCCESS;
                    } else if (heldItem.getItem() instanceof WritableBookItem) {
                        ItemStack blueprint = table.getBlueprintItem().copy();
                        blueprint.setCount(1);

                        player.addItem(blueprint);
                        player.addItem(blueprint);

                        if (!player.isCreative()) {
                            heldItem.shrink(1);
                        }

                        table.setBlueprintItem(ItemStack.EMPTY);

                        level.setBlockAndUpdate(table.getBlockPos(), table.getBlockState().setValue(HAS_BLUEPRINT, false));

                        BlockEntity tileEntity = level.getBlockEntity(pos);
                        if (tileEntity instanceof ArmorersTinkeringTableBlockEntity tableTile) {
                            Containers.dropContents(level, pos.above(), tableTile.getDroppableInventory());
                        }

                        level.playSound(null, pos, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 1, 1);

                        return InteractionResult.SUCCESS;
                    } else if (player.getMainHandItem().isEmpty() && player.isShiftKeyDown()) {
                        ItemStack stack = table.getBlueprintItem().copy();
                        player.addItem(stack);
                        table.setBlueprintItem(ItemStack.EMPTY);

                        level.playSound(null, pos, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 1, 1);
                        level.setBlockAndUpdate(table.getBlockPos(), table.getBlockState().setValue(HAS_BLUEPRINT, false));

                        BlockEntity tileEntity = level.getBlockEntity(pos);
                        if (tileEntity instanceof ArmorersTinkeringTableBlockEntity tableTile) {
                            Containers.dropContents(level, pos.above(), tableTile.getDroppableInventory());
                        }

                        return InteractionResult.SUCCESS;
                    } else if (table.getInputItem().isEmpty() && !player.getMainHandItem().isEmpty()) {
                        putInputItem(table, level, player, pos, heldItem);

                        return InteractionResult.SUCCESS;
                    } else if (!table.getInputItem().isEmpty() && player.getMainHandItem().isEmpty()) {
                        takeInputItem(table, level, player, pos);

                        return InteractionResult.SUCCESS;
                    }
                } else {
                    if (heldItem.getItem() instanceof BlueprintItem) {
                        table.setBlueprintItem(heldItem);

                        if (!player.isCreative()) {
                            heldItem.shrink(1);
                        }

                        level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1, 1);
                        level.setBlockAndUpdate(table.getBlockPos(), table.getBlockState().setValue(HAS_BLUEPRINT, true));

                        return InteractionResult.SUCCESS;
                    } else if (table.getInputItem().isEmpty() && !player.getMainHandItem().isEmpty()) {
                        putInputItem(table, level, player, pos, heldItem);

                        return InteractionResult.SUCCESS;
                    } else if (!table.getInputItem().isEmpty() && player.getMainHandItem().isEmpty()) {
                        takeInputItem(table, level, player, pos);

                        return InteractionResult.SUCCESS;
                    }
                }
            }
        } else if (hit.getDirection() == state.getValue(HORIZONTAL_FACING)) {
            if (level.getBlockEntity(pos) instanceof ArmorersTinkeringTableBlockEntity table) {
                if (table.getHammerSlot().isEmpty()) {
                    if (heldItem.getItem() instanceof SmithingHammerItem) {
                        ItemStack stack = heldItem.copy();
                        stack.setCount(1);

                        table.setHammerSlot(stack);

                        if (!player.isCreative()) {
                            heldItem.shrink(1);
                        }

                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1, 1);

                        return InteractionResult.SUCCESS;
                    }
                } else if (player.getMainHandItem().isEmpty()) {
                    ItemStack stack = table.getHammerSlot().copy();
                    player.addItem(stack);
                    table.setHammerSlot(ItemStack.EMPTY);

                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1, 1);

                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public void takeInputItem(ArmorersTinkeringTableBlockEntity table, Level level, Player player, BlockPos pos) {
        ItemStack stack = table.getInputItem().copy();
        player.addItem(stack);
        table.setInputItem(ItemStack.EMPTY);

        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1, 1);
    }

    public void putInputItem(ArmorersTinkeringTableBlockEntity table, Level level, Player player, BlockPos pos, ItemStack heldItem) {
        ItemStack stack = heldItem.copy();
        stack.setCount(1);

        table.setInputItem(stack);

        if (!player.isCreative()) {
            heldItem.shrink(1);
        }

        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1, 1);
    }

    public static int getBlueprintColor(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof ArmorersTinkeringTableBlockEntity table) {
            String blueprintItem = table.getItemFromBlueprint();

            if (blueprintItem.equals(ItemRegistry.MURASAMA.getKey().location().toString())) {
                return 0xE80000;
            } else if (blueprintItem.equals(ItemRegistry.GUN_SHEATH.getKey().location().toString())) {
                return 0xBDBDBD;
            } else if (blueprintItem.equals(ItemRegistry.ORIGINIUM_CATALYST.getKey().location().toString())) {
                return 0xFFA82D;
            } else if (blueprintItem.equals(ItemRegistry.DRAGON_SLAYER.getKey().location().toString())) {
                return 0x474747;
            }  else if (blueprintItem.equals(ItemRegistry.SHARP_IRONY.getKey().location().toString())) {
                return 0x4A5B7D;
            } else if (blueprintItem.equals(ItemRegistry.PUNISHER.getKey().location().toString())) {
                return 0x2FFFF8;
            } else if (blueprintItem.equals(ItemRegistry.BLOODLETTER.getKey().location().toString())) {
                return 0xC57070D;
            } else if (blueprintItem.equals(ItemRegistry.CRUCIBLE_INACTIVE.getKey().location().toString())) {
                return 0xFF4439;
            }
        }
        return 0xFFFFFF;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.ARMORERS_ASSEMBLY_TABLE.get().create(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
        return createTickerHelper(blockEntity, BlockEntityRegistry.ARMORERS_ASSEMBLY_TABLE.get(), ArmorersTinkeringTableBlockEntity::tick);
    }

    @Override
    public String getTriviaType() {
        return ARMORERS_TINKERING_TABLE;
    }

    @Override
    public String getItemRarity(ItemStack stack) {
        return ARMORERS_WORKSHOP_PART;
    }
}
