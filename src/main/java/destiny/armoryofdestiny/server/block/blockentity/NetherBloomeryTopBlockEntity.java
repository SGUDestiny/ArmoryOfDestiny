package destiny.armoryofdestiny.server.block.blockentity;

import destiny.armoryofdestiny.server.container.SuperheatedBloomingContainer;
import destiny.armoryofdestiny.server.recipe.SuperheatedBloomingRecipe;
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

import static destiny.armoryofdestiny.server.block.NetherBloomeryBottomBlock.LIT;

public class NetherBloomeryTopBlockEntity extends BlockEntity {
    private static final String INPUT = "input";
    private static final String MELT_TICK = "melt_tick";

    private ItemStack input = ItemStack.EMPTY;
    private int meltTick = 0;

    public NetherBloomeryTopBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.NETHER_BLOOMERY_TOP.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, NetherBloomeryTopBlockEntity bloomery) {
        if (level.isClientSide())
            return;

        if (!bloomery.input.isEmpty()) {
            if (level.getBlockState(pos.below()).getBlock() == BlockRegistry.NETHER_BLOOMERY_BOTTOM.get()) {
                if (level.getBlockState(pos.below()).getValue(LIT)) {
                    SuperheatedBloomingContainer container = new SuperheatedBloomingContainer(bloomery.input, 0);
                    SuperheatedBloomingRecipe recipe = bloomery.getRecipe(container);

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

    public SuperheatedBloomingRecipe getRecipe(SuperheatedBloomingContainer container) {
        SuperheatedBloomingRecipe craftingRecipe = null;
        Optional<? extends Recipe<?>> optionalRecipe = level.getRecipeManager().getRecipeFor(SuperheatedBloomingRecipe.Type.INSTANCE, container, level);
        if (optionalRecipe.isPresent() && optionalRecipe.get() instanceof SuperheatedBloomingRecipe recipe)
            craftingRecipe = recipe;

        return craftingRecipe;
    }

    public ItemStack getInput() {
        return input.copy();
    }

    public void setInput(ItemStack stack) {
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
