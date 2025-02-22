package destiny.armoryofdestiny.blockentity;

import com.mojang.logging.LogUtils;
import destiny.armoryofdestiny.registry.BlockEntityRegistry;
import destiny.armoryofdestiny.registry.BlockRegistry;
import destiny.armoryofdestiny.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class AssemblyTableBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final ItemStackHandler inventory;
    private final LazyOptional<IItemHandler> inputHandler;
    //First slot reserved for blueprint
    //2-10 reserved for crafting ingredients
    //11 reserved for crafting result

    private static final int[] slotsTop = new int[]{0};
    private List<ItemStack> recipeIngredients = new ArrayList<>();
    private ItemStack recipeResult = ItemStack.EMPTY;
    private int currentIngredientIndex = -1;
    private ItemStack wantItemStack = ItemStack.EMPTY;
    private int craftingProgress = -1;

    public AssemblyTableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ASSEMBLY_TABLE.get(), pos, state);
        inventory = createHandler();
        inputHandler = LazyOptional.of(() -> inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AssemblyTableBlockEntity table) {
        if (!level.isClientSide) {
            if (table.hasBlueprint()) {
                //Begin crafting
                LOGGER.info("Trying to begin to craft");
                if (table.isSmithingCraftingTablePresent() && table.recipeIngredients.isEmpty()) {
                    String recipeID = table.getItemFromBlueprint();
                    LOGGER.info("Beginning to craft");
                    table.recipeIngredients = table.getIngredientsFromRecipe(recipeID);
                    table.recipeResult = table.getResultItemFromRecipe(recipeID);
                    table.currentIngredientIndex = level.random.nextInt(table.recipeIngredients.size());
                    table.craftingProgress = 2;
                    table.wantItemStack = table.recipeIngredients.get(table.currentIngredientIndex);
                }
            }
        }
    }

    public void advanceCrafting() {
        if (isSmithingCraftingTablePresent()) {
            //Check if item in indexed slot is current ingredient
            if (getItem(craftingProgress) == wantItemStack) {
                //Check if remaining ingredients is more than one
                int remainingIngredients = recipeIngredients.size();

                if (remainingIngredients > 1) {
                    recipeIngredients.remove(currentIngredientIndex);
                    currentIngredientIndex = level.random.nextInt(recipeIngredients.size());
                    craftingProgress++;
                    wantItemStack = recipeIngredients.get(craftingProgress);
                } else if (remainingIngredients == 1) {
                    //Else finish crafting

                    //Set result slot to result item
                    setItem(11, recipeResult);
                    //Clear slots 2-10
                    clearIngredientSlots();
                    //Clear ingredient list
                    recipeIngredients.clear();
                    //Reset variables
                    currentIngredientIndex = -1;
                    craftingProgress = -1;
                    wantItemStack = ItemStack.EMPTY;
                }
            }
        }
    }

    @Override
    public int[] getSlotsForFace(Direction p_19238_) {
        return slotsTop;
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_19235_, ItemStack p_19236_, @Nullable Direction p_19237_) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int p_19239_, ItemStack p_19240_, Direction p_19241_) {
        return false;
    }

    @Override
    public Component getDisplayName() {
        return getDefaultName();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.armoryofdestiny.assembly_table");
    }

    //Creates a menu?
    @Override
    protected AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_) {
        return null;
    }

    //Gets container size
    @Override
    public int getContainerSize() {
        return inventory.getSlots();
    }

    //Gets if container is empty
    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); i++) {
            if (!getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    //Gets stack from index
    @Override
    public ItemStack getItem(int index) {
        if (!inventory.getStackInSlot(index).isEmpty()) {
            ItemStack itemStack = inventory.getStackInSlot(index);
            markUpdated();
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    //Used to remove only a certain amount of stack at index
    @Override
    public ItemStack removeItem(int index, int count) {
        if (!inventory.getStackInSlot(index).isEmpty()) {
            ItemStack itemstack;

            if (inventory.getStackInSlot(index).getCount() <= count) {
                itemstack = inventory.getStackInSlot(index);
                inventory.setStackInSlot(index, ItemStack.EMPTY);
                return itemstack;
            } else {
                itemstack = inventory.getStackInSlot(index).split(count);

                if (inventory.getStackInSlot(index).isEmpty()) {
                    inventory.setStackInSlot(index, ItemStack.EMPTY);
                }

                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    //Used to completely remove a stack at index
    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = inventory.getStackInSlot(index);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            inventory.setStackInSlot(index, ItemStack.EMPTY);
            return stack;
        }
    }

    //Sets index to given stack
    @Override
    public void setItem(int index, ItemStack stack) {
        stack.setCount(1);
        inventory.setStackInSlot(index, stack);
        markUpdated();
    }

    //No idea
    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    //Clears the stacks completely
    @Override
    public void clearContent() {
        for (int i = 0; inventory.getSlots() > i; i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public boolean hasBlueprint() {
        return !getItem(1).equals(ItemStack.EMPTY);
    }

    public String getItemFromBlueprint() {
        ItemStack blueprint = getItem(1);

        if (blueprint.getTag() != null) {
            return blueprint.getOrCreateTag().getString("blueprintItem");
        }
        return null;
    }

    public List<ItemStack> getIngredientsFromRecipe(String recipeID) {
        List<ItemStack> ingredientList = new ArrayList<>();

        switch (recipeID) {
            case "murasama":
                ingredientList.set(1, new ItemStack(Items.ECHO_SHARD, 1));
                ingredientList.set(2, new ItemStack(Items.ECHO_SHARD, 1));
                ingredientList.set(3, new ItemStack(Items.ECHO_SHARD, 1));
                ingredientList.set(4, new ItemStack(Items.ECHO_SHARD, 1));
                ingredientList.set(5, new ItemStack(Items.REDSTONE_BLOCK, 1));
                ingredientList.set(6, new ItemStack(Items.REDSTONE_BLOCK, 1));
                ingredientList.set(7, new ItemStack(Items.IRON_BLOCK, 1));
            case "gun_sheath":
                ingredientList.set(1, new ItemStack(Items.IRON_BLOCK, 1));
                ingredientList.set(2, new ItemStack(Items.IRON_INGOT, 1));
                ingredientList.set(3, new ItemStack(Items.IRON_INGOT, 1));
                ingredientList.set(4, new ItemStack(Items.IRON_INGOT, 1));
                ingredientList.set(5, new ItemStack(Items.NETHERITE_INGOT, 1));
                ingredientList.set(6, new ItemStack(Items.FLINT_AND_STEEL, 1));
                ingredientList.set(7, new ItemStack(Items.TNT, 1));
        }
        return ingredientList;
    }

    public ItemStack getResultItemFromRecipe(String recipeID) {
        ItemStack resultItem = ItemStack.EMPTY;

        switch (recipeID) {
            case "murasama":
                resultItem = new ItemStack(ItemRegistry.MURASAMA.get(), 1);
            case "gun_sheath":
                resultItem = new ItemStack(ItemRegistry.GUN_SHEATH.get(), 1);
        }
        return resultItem;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        inventory.deserializeNBT(compound.getCompound("Inventory"));
        recipeIngredients.clear();
        ListTag ingredientsTag = compound.getList("RecipeIngredients", Tag.TAG_STRING);
        for (Tag tag : ingredientsTag) {
            String ingredientString = tag.getAsString();
            ResourceLocation location = new ResourceLocation(ingredientString);
            Item item = BuiltInRegistries.ITEM.get(location);
            recipeIngredients.add(new ItemStack(item));
        }
        recipeResult = ItemStack.of(compound.getCompound("RecipeResult"));
        currentIngredientIndex = compound.getInt("CurrentIngredientIndex");
        wantItemStack = ItemStack.of(compound.getCompound("WantItemStack"));
        craftingProgress = compound.getInt("CraftingProgress");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("Inventory", inventory.serializeNBT());
        ListTag recipeIngredientList = new ListTag();
        for (ItemStack ingredient : recipeIngredients) {
            String string = ingredient.toString();
            recipeIngredientList.add(StringTag.valueOf(string));
        }
        compound.put("RecipeIngredients", recipeIngredientList);
        compound.put("RecipeResult", recipeResult.save(new CompoundTag()));
        compound.putInt("CurrentIngredientIndex", currentIngredientIndex);
        compound.put("WantItemStack", wantItemStack.save(new CompoundTag()));
        compound.putInt("CraftingProgress", craftingProgress);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public boolean isSmithingCraftingTablePresent() {
        if (level != null) {
            Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
            BlockPos pos = worldPosition.relative(facing.getCounterClockWise());
            pos.north(1);

            boolean bool = level.getBlockState(pos).getBlock() == BlockRegistry.SMITHING_CRAFTING_TABLE.get() && facing == level.getBlockState(pos).getValue(HORIZONTAL_FACING);
            LOGGER.info("Returning bool: " + bool);
            return bool;
        }
        LOGGER.info("Returned false");
        return false;
    }

    public int getCraftingProgress() {
        return craftingProgress;
    }

    public void clearIngredientSlots() {
        //- 1 to account for result slot
        int size = inventory.getSlots() - 1;

        for (int i = 1; size > i; i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public ItemStack getWantItem() {
        return wantItemStack;
    }

    public String getBlueprintItem() {
        return getItem(1).getOrCreateTag().getString("blueprintItem");
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap.equals(ForgeCapabilities.ITEM_HANDLER)) {
            return inputHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < 11; ++i) {
            drops.add(inventory.getStackInSlot(i));
        }
        return drops;
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(11)
        {
            @Override
            protected void onContentsChanged(int slot) {
                markUpdated();
            }
        };
    }

    private void markUpdated() {
        super.setChanged();
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }
}
