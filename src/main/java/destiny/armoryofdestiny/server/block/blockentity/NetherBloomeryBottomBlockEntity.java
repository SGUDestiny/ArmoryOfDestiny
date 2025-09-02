package destiny.armoryofdestiny.server.block.blockentity;

import destiny.armoryofdestiny.server.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

import static destiny.armoryofdestiny.server.block.NetherBloomeryBottomBlock.LIT;
import static destiny.armoryofdestiny.server.block.NetherBloomeryBottomBlock.LOGS;

public class NetherBloomeryBottomBlockEntity extends BlockEntity {
    private static final String STORED_LOGS = "stored_logs";
    private static final String BURN_TICK = "burn_tick";

    private final List<ItemStack> logs = new ArrayList<>(4);
    private int burnTick = 0;
    private final int burnTimePerLog = 1200;

    public NetherBloomeryBottomBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.NETHER_BLOOMERY_BOTTOM.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, NetherBloomeryBottomBlockEntity bloomery) {
        if (level.isClientSide())
            return;

        if (state.getValue(LIT)) {
            if (bloomery.burnTick == 0) {
                if (!bloomery.logs.isEmpty()) {

                    bloomery.burnTick = bloomery.burnTimePerLog;
                    bloomery.logs.remove(bloomery.logs.size() - 1);
                    level.setBlockAndUpdate(pos, state.setValue(LOGS, state.getValue(LOGS) - 1));
                } else {
                    level.setBlockAndUpdate(pos, state.setValue(LIT, false));
                }
            } else {
                bloomery.burnTick--;
            }
        }
    }

    public void setBurnTick(int burnTick) {
        this.burnTick = burnTick;
    }

    public ItemStack takeLog() {
        ItemStack log = logs.get(logs.size() - 1).copy();
        log.setCount(1);
        logs.remove(logs.size() - 1);

        return log;
    }

    public void putLog(ItemStack log) {
        log.setCount(1);
        logs.add(log);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ListTag logsTag = tag.getList(STORED_LOGS, Tag.TAG_COMPOUND);
        logsTag.forEach(compound -> {
            CompoundTag compoundTag = ((CompoundTag) compound);
            ItemStack logStack = ItemStack.of(compoundTag);
            logs.add(logStack);
        });
        burnTick = tag.getInt(BURN_TICK);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        ListTag logsTag = new ListTag();
        for(ItemStack stack : logs)
            logsTag.add(stack.serializeNBT());
        tag.put(STORED_LOGS, logsTag);
        tag.putInt(BURN_TICK, burnTick);
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
        drops.addAll(logs);
        return drops;
    }

    private void markUpdated() {
        super.setChanged();
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }
}
