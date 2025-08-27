package destiny.armoryofdestiny.server.block;

import destiny.armoryofdestiny.server.block.utility.TooltipBaseEntityBlock;
import destiny.armoryofdestiny.server.block.blockentity.ArmorersCraftingTableBlockEntity;
import destiny.armoryofdestiny.server.menu.SmithingCraftingMenu;
import destiny.armoryofdestiny.server.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

import static destiny.armoryofdestiny.server.util.UtilityVariables.ARMORERS_CRAFTING_TABLE;
import static destiny.armoryofdestiny.server.util.UtilityVariables.ARMORERS_WORKSHOP_PART;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ArmorersCraftingTableBlock extends TooltipBaseEntityBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    private static final Component CONTAINER_TITLE = Component.translatable("container.crafting");

    public ArmorersCraftingTableBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(OPEN, false));
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (result.getDirection() == Direction.UP) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                player.openMenu(state.getMenuProvider(level, pos));
                player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
                return InteractionResult.CONSUME;
            }
        } else if (result.getDirection() == state.getValue(HORIZONTAL_FACING)) {
            if (!level.isClientSide) {
                BlockEntity tile = level.getBlockEntity(pos);
                if (tile instanceof ArmorersCraftingTableBlockEntity) {
                    player.openMenu((ArmorersCraftingTableBlockEntity) tile);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof Container) {
                Containers.dropContents(level, pos, (Container) tileEntity);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof ArmorersCraftingTableBlockEntity) {
            ((ArmorersCraftingTableBlockEntity) tileEntity).recheckOpen();
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof ArmorersCraftingTableBlockEntity) {
                ((ArmorersCraftingTableBlockEntity) tileEntity).setCustomName(stack.getHoverName());
            }
        }
    }

    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((p_52229_, p_52230_, p_52231_) -> {
            return new SmithingCraftingMenu(p_52229_, p_52230_, ContainerLevelAccess.create(level, pos));
        }, CONTAINER_TITLE);
    }

    @javax.annotation.Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite()).setValue(OPEN, false);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, OPEN);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.ARMORERS_CRAFTING_TABLE.get().create(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public String getTriviaTranslatable() {
        return ARMORERS_CRAFTING_TABLE;
    }

    @Override
    public String getRarityTranslatable(ItemStack stack) {
        return ARMORERS_WORKSHOP_PART;
    }
}
