package destiny.armoryofdestiny.server.block;

import destiny.armoryofdestiny.server.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ArmorersAnvilBlock extends FallingBlock {
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
        this.registerDefaultState(this.defaultBlockState().setValue(HORIZONTAL_FACING, Direction.NORTH));
    }

    @javax.annotation.Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
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
    protected void falling(FallingBlockEntity entity) {
        entity.setHurtsEntities(10, 100);
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state1, BlockState state2, FallingBlockEntity entity) {
        if (!entity.isSilent()) {
            level.levelEvent(1031, pos, 0);
        }

    }
}
