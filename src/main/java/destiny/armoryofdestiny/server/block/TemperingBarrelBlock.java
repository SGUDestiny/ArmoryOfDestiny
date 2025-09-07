package destiny.armoryofdestiny.server.block;

import destiny.armoryofdestiny.server.block.utility.TooltipBlock;
import destiny.armoryofdestiny.server.container.TemperingContainer;
import destiny.armoryofdestiny.server.item.SmithingTongsItem;
import destiny.armoryofdestiny.server.recipe.TemperingRecipe;
import destiny.armoryofdestiny.server.util.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Recipe;
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

import java.util.Optional;

import static destiny.armoryofdestiny.server.item.SmithingTongsItem.HELD_ITEM;
import static destiny.armoryofdestiny.server.util.UtilityVariables.TEMPERING_BARREL;

public class TemperingBarrelBlock extends TooltipBlock {
    private static final VoxelShape SHAPE = ModUtil.buildShape(
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

        if (stack.getItem() instanceof SmithingTongsItem) {
            if (state.getValue(WATER) > 0) {
                if (stack.getTag() != null && stack.getTag().get(HELD_ITEM) != null) {
                    ItemStack held_item = ItemStack.of(stack.getTag().getCompound(HELD_ITEM));

                    TemperingContainer container = new TemperingContainer(held_item);
                    TemperingRecipe craftingRecipe = null;
                    Optional<? extends Recipe<?>> optionalRecipe = level.getRecipeManager().getRecipeFor(TemperingRecipe.Type.INSTANCE, container, level);
                    if (optionalRecipe.isPresent() && optionalRecipe.get() instanceof TemperingRecipe recipe)
                        craftingRecipe = recipe;

                    if (craftingRecipe == null) {
                        return InteractionResult.FAIL;
                    }

                    if (craftingRecipe.matches(container, level)) {
                        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1, 1);
                        level.playSound(null, pos, SoundEvents.PLAYER_SPLASH, SoundSource.BLOCKS, 1, 1);

                        stack.getOrCreateTag().put(HELD_ITEM, craftingRecipe.getResult().serializeNBT());
                        level.setBlockAndUpdate(pos, state.setValue(WATER, state.getValue(WATER) - 1));

                        if (!player.isCreative()) {
                            stack.setDamageValue(stack.getDamageValue() + 1);
                        }

                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }

        if (!stack.isEmpty()) {
            if (state.getValue(WATER) > 0) {
                TemperingContainer container = new TemperingContainer(stack);
                TemperingRecipe craftingRecipe = null;
                Optional<? extends Recipe<?>> optionalRecipe = level.getRecipeManager().getRecipeFor(TemperingRecipe.Type.INSTANCE, container, level);
                if (optionalRecipe.isPresent() && optionalRecipe.get() instanceof TemperingRecipe recipe)
                    craftingRecipe = recipe;

                if (craftingRecipe == null) {
                    return InteractionResult.FAIL;
                }

                if (craftingRecipe.matches(container, level)) {
                    level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1, 1);
                    level.playSound(null, pos, SoundEvents.PLAYER_SPLASH, SoundSource.BLOCKS, 1, 1);

                    stack.shrink(1);
                    player.addItem(craftingRecipe.getResult().copy());
                    level.setBlockAndUpdate(pos, state.setValue(WATER, state.getValue(WATER) - 1));

                    return InteractionResult.SUCCESS;
                }
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

    @Override
    public String getTriviaTranslatable() {
        return TEMPERING_BARREL;
    }
}
