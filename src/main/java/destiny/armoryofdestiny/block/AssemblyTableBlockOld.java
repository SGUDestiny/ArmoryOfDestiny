package destiny.armoryofdestiny.block;

import destiny.armoryofdestiny.blockentity.ArmorersAssemblyTableBlockEntity;
import destiny.armoryofdestiny.blockentity.AssemblyTableBlockEntityOld;
import destiny.armoryofdestiny.item.BlueprintItem;
import destiny.armoryofdestiny.registry.BlockEntityRegistry;
import destiny.armoryofdestiny.registry.ItemRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class AssemblyTableBlockOld extends BaseEntityBlock {
    public static final BooleanProperty HAS_BLUEPRINT = BooleanProperty.create("has_blueprint");

    public AssemblyTableBlockOld(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(HAS_BLUEPRINT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING, HAS_BLUEPRINT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hit.getDirection() != Direction.UP) return InteractionResult.PASS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof AssemblyTableBlockEntityOld table)) return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);

        // Blueprint handling
        if (isControl(level)) {
            if (stack.getItem() instanceof BlueprintItem && !state.getValue(HAS_BLUEPRINT)) {
                if (!level.isClientSide) {
                    table.setBlueprint(stack.split(1));
                    level.setBlock(pos, state.setValue(HAS_BLUEPRINT, true), Block.UPDATE_ALL);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else if (state.getValue(HAS_BLUEPRINT) && stack.isEmpty()) {
                if (!level.isClientSide) {
                    player.addItem(table.removeBlueprint());
                    level.setBlock(pos, state.setValue(HAS_BLUEPRINT, false), Block.UPDATE_ALL);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            return InteractionResult.PASS;
        }

        // Crafting interactions
        if (stack.getItem() == ItemRegistry.SMITHING_HAMMER.get()) {
            return table.tryAdvanceCrafting(player, stack) ?
                    InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
        } else if (stack.isEmpty()) {
            return table.tryTakeResult(player) ?
                    InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
        } else if (stack.getItem() == table.getCurrentIngredient().getItem()) {

        }

        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            Containers.dropContents(level, pos, (Container) level.getBlockEntity(pos));
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AssemblyTableBlockEntityOld(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, BlockEntityRegistry.ARMORERS_ASSEMBLY_TABLE.get(), ArmorersAssemblyTableBlockEntity::tick);
    }

    public boolean isControl (Level level) {
        if (level instanceof ClientLevel) {
            return Screen.hasControlDown();
        }
        return Screen.hasControlDown();
    }
}
