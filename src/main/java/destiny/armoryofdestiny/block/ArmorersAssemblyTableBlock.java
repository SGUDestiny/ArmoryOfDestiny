package destiny.armoryofdestiny.block;

import destiny.armoryofdestiny.block.utility.TooltipBaseEntityBlock;
import destiny.armoryofdestiny.blockentity.ArmorersAssemblyTableBlockEntity;
import destiny.armoryofdestiny.item.BlueprintItem;
import destiny.armoryofdestiny.item.SmithingHammerItem;
import destiny.armoryofdestiny.registry.BlockEntityRegistry;
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

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ArmorersAssemblyTableBlock extends TooltipBaseEntityBlock {
    public static final BooleanProperty HAS_BLUEPRINT = BooleanProperty.create("has_blueprint");

    public ArmorersAssemblyTableBlock(Properties properties) {
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
            if (tileEntity instanceof ArmorersAssemblyTableBlockEntity table) {
                Containers.dropContents(level, pos, table.getDroppableInventory());
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

        //Check if block entity is present
        if (level.getBlockEntity(pos) instanceof ArmorersAssemblyTableBlockEntity table) {
            //If shifting, take blueprint if can
            if (player.isShiftKeyDown()) {
                if (table.hasBlueprint()) {
                    ItemStack stack = table.getBlueprintItem().copy();
                    player.addItem(stack);
                    table.setBlueprintItem(ItemStack.EMPTY);

                    level.playSound(null, pos, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 1, 1);
                    level.setBlockAndUpdate(table.getBlockPos(), table.getBlockState().setValue(HAS_BLUEPRINT, false));

                    BlockEntity tileEntity = level.getBlockEntity(pos);
                    if (tileEntity instanceof ArmorersAssemblyTableBlockEntity tableTile) {
                        Containers.dropContents(level, pos.above(), tableTile.getDroppableInventory());
                    }

                    return InteractionResult.SUCCESS;
                }
                //Else if player is holding a blueprint, try putting it
            } else if (heldItem.getItem() instanceof BlueprintItem) {
                if (table.getBlueprintItem().isEmpty()) {
                    table.setBlueprintItem(heldItem);
                    heldItem.shrink(1);

                    level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1, 1);
                    level.setBlockAndUpdate(table.getBlockPos(), table.getBlockState().setValue(HAS_BLUEPRINT, true));

                    return InteractionResult.SUCCESS;
                }
                //If table has a blueprint, proceed
            } else if (table.hasBlueprint()) {
                //Else if player is holding a smithing hammer, try advancing crafting
                if (heldItem.getItem() instanceof SmithingHammerItem) {
                    table.advanceCrafting(level, pos, player, heldItem);

                    return InteractionResult.SUCCESS;
                    //Else if player is holding book and quill, try copying blueprint
                } else if (heldItem.getItem() instanceof WritableBookItem) {
                    ItemStack blueprint = table.getBlueprintItem().copy();
                    blueprint.setCount(2);

                    player.addItem(blueprint);
                    heldItem.shrink(1);
                    table.setBlueprintItem(ItemStack.EMPTY);

                    level.setBlockAndUpdate(table.getBlockPos(), table.getBlockState().setValue(HAS_BLUEPRINT, false));

                    BlockEntity tileEntity = level.getBlockEntity(pos);
                    if (tileEntity instanceof ArmorersAssemblyTableBlockEntity tableTile) {
                        Containers.dropContents(level, pos.above(), tableTile.getDroppableInventory());
                    }

                    level.playSound(null, pos, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 1, 1);

                    return InteractionResult.SUCCESS;
                    //Else if hand isn't empty, try putting the item in
                } else if (!heldItem.isEmpty()) {
                    if (table.getInputItem().isEmpty()) {
                        ItemStack stack = heldItem.copy();
                        stack.setCount(1);

                        table.setInputItem(stack);

                        if (!player.isCreative()) {
                            heldItem.shrink(1);
                        }

                        level.playSound(null, pos, SoundEvents.GLOW_ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1, 1);

                        return InteractionResult.SUCCESS;
                    }
                    //Else if hand is empty, try taking current item
                } else if (heldItem.isEmpty()) {
                    if (!table.getInputItem().isEmpty()) {
                        ItemStack stack = table.getInputItem().copy();
                        player.addItem(stack);
                        table.setInputItem(ItemStack.EMPTY);

                        level.playSound(null, pos, SoundEvents.GLOW_ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1, 1);

                        return InteractionResult.SUCCESS;
                    }
                }
                //Else if result slot isn't empty, try taking result item
            } else if (!table.getInputItem().isEmpty()) {
                ItemStack stack = table.getInputItem().copy();
                player.addItem(stack);
                table.setInputItem(ItemStack.EMPTY);

                level.playSound(null, pos, SoundEvents.GLOW_ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1, 1);

                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    public static int getBlueprintColor(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof ArmorersAssemblyTableBlockEntity table) {
            String blueprintItem = table.getItemFromBlueprint();

            switch (blueprintItem) {
                case "murasama":
                    return 0xE80000;
                case "gun_sheath":
                    return 0xBDBDBD;
                case "dragon_slayer":
                    return 0x474747;
                case "originium_catalyst":
                    return 0xFFA82D;
                case "punisher":
                    return 0x2FFFF8;
                case "sharp_irony":
                    return 0x4A5B7D;
            }
        }
        return 0xFFFFFF;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.ASSEMBLY_TABLE.get().create(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
        return createTickerHelper(blockEntity, BlockEntityRegistry.ASSEMBLY_TABLE.get(), ArmorersAssemblyTableBlockEntity::tick);
    }

    @Override
    public String getTriviaType() {
        return "armorers_assembly_table";
    }

    @Override
    public String getItemRarity(ItemStack stack) {
        return "armorers_workshop_part";
    }
}
