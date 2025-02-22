package destiny.armoryofdestiny.block;

import com.mojang.logging.LogUtils;
import destiny.armoryofdestiny.blockentity.AssemblyTableBlockEntity;
import destiny.armoryofdestiny.item.BlueprintItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
    private static final Logger LOGGER = LogUtils.getLogger();

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

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);

        //Check if block entity is present
        if (level.getBlockEntity(pos) instanceof AssemblyTableBlockEntity table) {
            //If shifting, take blueprint if can
            if (player.isShiftKeyDown()) {
                if (table.hasBlueprint()) {
                    ItemStack stack = table.getItem(1).copy();
                    player.addItem(stack);
                    table.setItem(1, ItemStack.EMPTY);

                    return InteractionResult.SUCCESS;
                }
            } else if (heldItem.getItem() instanceof BlueprintItem) {
                if (table.getItem(1).isEmpty()) {
                    table.setItem(1, heldItem.copy());
                    heldItem.shrink(1);

                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static int getBlueprintColor(Level level, BlockPos pos) {
        int color = 0xFFFFFF;
        if (level.getBlockEntity(pos) instanceof AssemblyTableBlockEntity table) {
            String blueprintItem = table.getBlueprintItem();

            switch (blueprintItem) {
                case "murasama":
                    color = 0xE80000;
                case "gun_sheath":
                    color = 0xBDBDBD;
                case "dragon_slayer":
                    color = 0x474747;
                case "originium_catalyst":
                    color = 0xFFA82D;
                case "punisher":
                    color = 0x2FFFF8;
                case "sharp_irony":
                    color = 0x4A5B7D;
            }
        }
        LOGGER.info("Blueprint color: " + color);
        return color;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AssemblyTableBlockEntity(pos, state);
    }
}
