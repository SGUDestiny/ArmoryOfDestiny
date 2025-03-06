package destiny.armoryofdestiny.server.block;

import destiny.armoryofdestiny.server.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolActions;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class BloomeryBottomBlock extends Block {
    public static final TagKey<Item> LOGS_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("minecraft", "logs"));

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final IntegerProperty LOGS = IntegerProperty.create("logs", 0, 4);

    public BloomeryBottomBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, Direction.NORTH)
                .setValue(LOGS, 0)
                .setValue(OPEN, false)
                .setValue(LIT, false)
        );
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);

        if (hitResult.getDirection() == state.getValue(HORIZONTAL_FACING)) {
            if (stack == ItemStack.EMPTY) {
                if (!player.isShiftKeyDown()) {
                    level.setBlock(pos, state.setValue(OPEN, !state.getValue(OPEN)), 2);
                    if (state.getValue(OPEN)) {
                        level.playSound(null, pos, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.BLOCKS);
                    } else {
                        level.playSound(null, pos, SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.BLOCKS);
                    }
                    return InteractionResult.SUCCESS;
                } else {
                    if (!state.getValue(LIT) && state.getValue(LOGS) > 0) {
                        level.setBlock(pos, state.setValue(LOGS, state.getValue(LOGS) - 1), 2);
                        player.addItem(new ItemStack(Items.OAK_LOG));
                        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.5F);

                        return InteractionResult.SUCCESS;
                    }
                }
            }

            if (state.getValue(OPEN)) {
                if (stack.getItem() == Items.FLINT_AND_STEEL) {
                    if (!state.getValue(LIT) && state.getValue(LOGS) > 0) {
                        if (!player.isCreative()) {
                            stack.setDamageValue(stack.getDamageValue() + 1);
                        }

                        level.setBlock(pos, state.setValue(LIT, true), 2);
                        level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS);

                        return InteractionResult.SUCCESS;
                    }
                } else if (stack.is(LOGS_TAG)) {
                    if (state.getValue(LOGS) < 4) {
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }

                        level.setBlock(pos, state.setValue(LOGS, state.getValue(LOGS) + 1), 2);
                        level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS);

                        return InteractionResult.SUCCESS;
                    }

                } else if (stack.canPerformAction(ToolActions.SHOVEL_DIG)) {
                    if (state.getValue(LIT)) {
                        if (!player.isCreative()) {
                            stack.setDamageValue(stack.getDamageValue() + 1);
                        }

                        level.setBlock(pos, state.setValue(LIT, false), 2);
                        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS);

                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity && state.getValue(LIT) ) {
            if (!(level.getBlockState(pos.above()).getBlock() instanceof BloomeryTopBlock)) {
                entity.hurt(level.damageSources().hotFloor(), 1);
            }
        }
    }

    @javax.annotation.Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite())
                .setValue(LOGS, 0)
                .setValue(OPEN, false)
                .setValue(LIT, false);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, LOGS, OPEN, LIT);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
