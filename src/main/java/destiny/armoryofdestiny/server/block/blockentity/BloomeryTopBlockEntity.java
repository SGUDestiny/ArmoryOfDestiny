package destiny.armoryofdestiny.server.block.blockentity;

import destiny.armoryofdestiny.server.container.BloomingContainer;
import destiny.armoryofdestiny.server.recipe.BloomingRecipe;
import destiny.armoryofdestiny.server.registry.BlockEntityRegistry;
import destiny.armoryofdestiny.server.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

import static destiny.armoryofdestiny.server.block.BloomeryBottomBlock.LIT;

public class BloomeryTopBlockEntity extends BlockEntity {
    private static final String INPUT = "input";
    private static final String MELT_TICK = "melt_tick";

    private ItemStack input = ItemStack.EMPTY;
    private int meltTick = 0;

    public BloomeryTopBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.BLOOMERY_TOP.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BloomeryTopBlockEntity bloomery) {
        if (level.isClientSide())
            return;

        if (!bloomery.input.isEmpty()) {
            if (level.getBlockState(pos.below()).getBlock() == BlockRegistry.BLOOMERY_BOTTOM.get()) {
                if (level.getBlockState(pos.below()).getValue(LIT)) {
                    BloomingContainer container = new BloomingContainer(bloomery.input, 0);
                    BloomingRecipe recipe = bloomery.getRecipe(container);

                    if (recipe == null) {
                        return;
                    }
                    if (recipe.matches(container, level)) {
                        if (bloomery.meltTick == recipe.getMeltTime()) {
                            bloomery.input = recipe.getResult().copy();
                            bloomery.meltTick = 0;

                            level.setBlockAndUpdate(pos, state.setValue(LIT, false));

                            bloomery.markUpdated();
                        } else {
                            bloomery.meltTick++;

                            if (!state.getValue(LIT)) {
                                level.setBlockAndUpdate(pos, state.setValue(LIT, true));
                            }
                        }
                    } else {
                        level.setBlockAndUpdate(pos, state.setValue(LIT, false));
                        bloomery.meltTick = 0;
                    }
                } else {
                    level.setBlockAndUpdate(pos, state.setValue(LIT, false));
                    bloomery.meltTick = 0;
                }
            } else {
                level.setBlockAndUpdate(pos, state.setValue(LIT, false));
                bloomery.meltTick = 0;
            }
        } else {
            level.setBlockAndUpdate(pos, state.setValue(LIT, false));
            bloomery.meltTick = 0;
        }
    }

    public BloomingRecipe getRecipe(BloomingContainer container) {
        BloomingRecipe craftingRecipe = null;
        Optional<? extends Recipe<?>> optionalRecipe = level.getRecipeManager().getRecipeFor(BloomingRecipe.Type.INSTANCE, container, level);
        if (optionalRecipe.isPresent() && optionalRecipe.get() instanceof BloomingRecipe recipe)
            craftingRecipe = recipe;

        return craftingRecipe;
    }

    public ItemStack getInput() {
        ItemStack stack = input.copy();
        stack.setCount(1);

        return stack;
    }

    public void setInput(ItemStack stack) {
        stack.setCount(1);
        input = stack.copy();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        input = ItemStack.of(tag.getCompound(INPUT));
        meltTick = tag.getInt(MELT_TICK);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(INPUT, input.serializeNBT());
        tag.putInt(MELT_TICK, meltTick);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        drops.add(input);
        return drops;
    }

    private void markUpdated() {
        super.setChanged();
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }
}
