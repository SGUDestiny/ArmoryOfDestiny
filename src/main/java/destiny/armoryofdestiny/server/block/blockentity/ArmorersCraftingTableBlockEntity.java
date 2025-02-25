package destiny.armoryofdestiny.server.block.blockentity;

import destiny.armoryofdestiny.server.block.ArmorersCraftingTableBlock;
import destiny.armoryofdestiny.server.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.Container;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ArmorersCraftingTableBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> drawer = NonNullList.withSize(27, ItemStack.EMPTY);

    private ContainerOpenersCounter openersCounter = new ContainerOpenersCounter()
    {
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            ArmorersCraftingTableBlockEntity.this.playSound(state, SoundEvents.BARREL_OPEN);
            ArmorersCraftingTableBlockEntity.this.updateBlockState(state, true);
        }

        protected void onClose(Level level, BlockPos pos, BlockState state) {
            ArmorersCraftingTableBlockEntity.this.playSound(state, SoundEvents.BARREL_CLOSE);
            ArmorersCraftingTableBlockEntity.this.updateBlockState(state, false);
        }

        protected void openerCountChanged(Level level, BlockPos pos, BlockState sta, int arg1, int arg2) {
        }

        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof ChestMenu) {
                Container container = ((ChestMenu) player.containerMenu).getContainer();
                return container == ArmorersCraftingTableBlockEntity.this;
            } else {
                return false;
            }
        }
    };

    public ArmorersCraftingTableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ARMORERS_CRAFTING_TABLE.get(), pos, state);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        ContainerHelper.saveAllItems(compound, drawer);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        drawer = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, drawer);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return drawer;
    }

    @Override
    public void setItems(NonNullList<ItemStack> items) {
        drawer = items;
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("container.armoryofdestiny.armorers_crafting_table");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return ChestMenu.threeRows(id, player, this);
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    public void startOpen(Player pPlayer) {
        if (level != null && !this.remove && !pPlayer.isSpectator()) {
            this.openersCounter.incrementOpeners(pPlayer, level, this.getBlockPos(), this.getBlockState());
        }
    }

    public void stopOpen(Player pPlayer) {
        if (level != null && !this.remove && !pPlayer.isSpectator()) {
            this.openersCounter.decrementOpeners(pPlayer, level, this.getBlockPos(), this.getBlockState());
        }
    }

    public void recheckOpen() {
        if (level != null && !this.remove) {
            this.openersCounter.recheckOpeners(level, this.getBlockPos(), this.getBlockState());
        }
    }

    void updateBlockState(BlockState state, boolean open) {
        if (level != null) {
            this.level.setBlock(this.getBlockPos(), state.setValue(ArmorersCraftingTableBlock.OPEN, open), 3);
        }
    }

    private void playSound(BlockState state, SoundEvent sound) {
        if (level == null) return;

        Vec3i cabinetFacingVector = state.getValue(HORIZONTAL_FACING).getNormal();
        double x = (double) worldPosition.getX() + 0.5D + (double) cabinetFacingVector.getX() / 2.0D;
        double y = (double) worldPosition.getY() + 0.5D + (double) cabinetFacingVector.getY() / 2.0D;
        double z = (double) worldPosition.getZ() + 0.5D + (double) cabinetFacingVector.getZ() / 2.0D;
        level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }
}
