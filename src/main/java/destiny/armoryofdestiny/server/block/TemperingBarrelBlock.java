package destiny.armoryofdestiny.server.block;

import destiny.armoryofdestiny.server.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TemperingBarrelBlock extends Block {
    private static final VoxelShape SHAPE = MathUtil.buildShape(
            Block.box(2, 1, 2, 3, 16, 14),
            Block.box(13, 1, 2, 14, 16, 14),
            Block.box(2, 0, 2, 14, 1, 14),
            Block.box(3, 1, 13, 13, 16, 14),
            Block.box(3, 1, 2, 13, 16, 3)
    );

    public static final IntegerProperty WATER = IntegerProperty.create("water", 0, 6);

    public TemperingBarrelBlock(Properties build) {
        super(build);
        this.registerDefaultState(this.defaultBlockState().setValue(WATER, 0));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (stack.getItem() == Items.WATER_BUCKET) {
            level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1, 1);
            level.setBlockAndUpdate(pos, state.setValue(WATER, 6));

            if (!player.isCreative()) {
                stack.shrink(1);
                player.addItem(new ItemStack(Items.BUCKET));
            }

            return InteractionResult.SUCCESS;
        }

        if (stack.getItem() == Items.BUCKET) {
            if (state.getValue(WATER) == 6) {
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1, 1);
                level.setBlockAndUpdate(pos, state.setValue(WATER, 0));

                if (!player.isCreative()) {
                    stack.shrink(1);
                    player.addItem(new ItemStack(Items.WATER_BUCKET));
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @javax.annotation.Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(WATER, 0);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATER);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
}
