package destiny.armoryofdestiny.block;

import com.mojang.logging.LogUtils;
import destiny.armoryofdestiny.blockentity.AssemblyTableBlockEntity;
import destiny.armoryofdestiny.item.BlueprintItem;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class AssemblyTableBlock extends BaseEntityBlock {
    public static final BooleanProperty HAS_BLUEPRINT = BooleanProperty.create("has_blueprint");

    public AssemblyTableBlock(Properties properties) {
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
            if (tileEntity instanceof AssemblyTableBlockEntity table) {
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
        if (level.getBlockEntity(pos) instanceof AssemblyTableBlockEntity table) {
            //If shifting, take blueprint if can
            if (player.isShiftKeyDown()) {
                if (table.hasBlueprint()) {
                    ItemStack stack = table.getItem(0).copy();
                    player.addItem(stack);
                    table.setItem(0, ItemStack.EMPTY);

                    level.playSound(null, pos, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 1, 1);
                    level.setBlockAndUpdate(table.getBlockPos(), table.getBlockState().setValue(HAS_BLUEPRINT, false));

                    return InteractionResult.SUCCESS;
                }
            } else if (heldItem.getItem() instanceof BlueprintItem) {
                if (table.getItem(0).isEmpty()) {
                    table.setItem(0, heldItem.copy());
                    heldItem.shrink(1);

                    level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1, 1);
                    level.setBlockAndUpdate(table.getBlockPos(), table.getBlockState().setValue(HAS_BLUEPRINT, true));

                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static int getBlueprintColor(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof AssemblyTableBlockEntity table) {
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
        return new AssemblyTableBlockEntity(pos, state);
    }
}
