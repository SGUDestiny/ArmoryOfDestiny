package destiny.armoryofdestiny.blockentity;

import com.google.gson.JsonElement;
import destiny.armoryofdestiny.registry.BlockEntityRegistry;
import destiny.armoryofdestiny.registry.BlockRegistry;
import destiny.armoryofdestiny.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AssemblyTableBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    private NonNullList<ItemStack> stacks = NonNullList.withSize(11, ItemStack.EMPTY);
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
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AssemblyTableBlockEntity table) {
        if (!level.isClientSide) {
            if (table.hasBlueprint()) {
                //Begin crafting
                if (table.isSmithingCraftingTablePresent() && table.recipeIngredients.isEmpty()) {
                    String recipeID = table.getItemFromBlueprint();

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
        return this.stacks.size();
    }

    //Gets if container is empty
    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (!this.getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    //Gets stack from index
    @Override
    public ItemStack getItem(int index) {
        return this.stacks.get(index);
    }

    //Used to remove only a certain amount of stack at index
    @Override
    public ItemStack removeItem(int index, int count) {
        if (!this.stacks.get(index).isEmpty()) {
            ItemStack itemstack;

            if (this.stacks.get(index).getCount() <= count) {
                itemstack = this.stacks.get(index);
                this.stacks.set(index, ItemStack.EMPTY);
                return itemstack;
            } else {
                itemstack = this.stacks.get(index).split(count);

                if (this.stacks.get(index).isEmpty()) {
                    this.stacks.set(index, ItemStack.EMPTY);
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
        ItemStack stack = this.stacks.get(index);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.stacks.set(index, ItemStack.EMPTY);
            return stack;
        }
    }

    //Sets index to given stack
    @Override
    public void setItem(int index, ItemStack stack) {
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameTags(stack, this.stacks.get(index));
        this.stacks.set(index, stack);
        if (!stack.isEmpty()) {
            stack.setCount(1);
        }
        this.markUpdated();
    }

    //No idea
    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    //Clears the stacks completely
    @Override
    public void clearContent() {
        this.stacks.clear();
    }

    public boolean hasBlueprint() {
        return !getItem(0).isEmpty();
    }

    public String getItemFromBlueprint() {
        ItemStack blueprint = getItem(0);

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
        this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.stacks);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        ContainerHelper.saveAllItems(compound, this.stacks);

        ListTag recipeIngredients = new ListTag();
        for (ItemStack ingredient : this.recipeIngredients) {
            String string = ingredient.toString();
            recipeIngredients.add(StringTag.valueOf(string));
        }
        tag.put("recipeIngredients", recipeIngredients);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        if (packet != null && packet.getTag() != null) {
            this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(packet.getTag(), this.stacks);
        }
    }

    public CompoundTag getUpdateTag() {
        CompoundTag compoundtag = new CompoundTag();
        ContainerHelper.saveAllItems(compoundtag, this.stacks, true);
        return compoundtag;
    }

    public boolean isSmithingCraftingTablePresent() {
        if (level != null) {
            Direction facing = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            BlockPos pos = worldPosition.relative(facing.getCounterClockWise());

            return level.getBlockState(pos).getBlock() == BlockRegistry.SMITHING_CRAFTING_TABLE.get();
        }
        return false;
    }

    public int getCraftingProgress() {
        return this.craftingProgress;
    }

    public void clearIngredientSlots() {
        //- 1 to account for result slot
        int size = stacks.size() - 1;

        for (int i = 1; size > i; i++) {
            stacks.set(i, ItemStack.EMPTY);
        }
    }

    public ItemStack getWantItem() {
        return this.wantItemStack;
    }

    public NonNullList<ItemStack> getStacks() {
        return this.stacks;
    }

    public String getBlueprintItem() {
        return this.getItem(1).getOrCreateTag().getString("blueprintItem");
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }
}
