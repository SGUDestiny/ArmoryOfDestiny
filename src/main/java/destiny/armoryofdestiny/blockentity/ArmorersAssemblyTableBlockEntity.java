package destiny.armoryofdestiny.blockentity;

import com.mojang.logging.LogUtils;
import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.registry.BlockEntityRegistry;
import destiny.armoryofdestiny.registry.BlockRegistry;
import destiny.armoryofdestiny.registry.ItemRegistry;
import destiny.armoryofdestiny.registry.SoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static destiny.armoryofdestiny.block.ArmorersAssemblyTableBlock.HAS_BLUEPRINT;
import static destiny.armoryofdestiny.item.SharpIronyItem.AMMO_COUNT;
import static destiny.armoryofdestiny.item.SharpIronyItem.IS_OPEN;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ArmorersAssemblyTableBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final ItemStackHandler inventory;
    private final LazyOptional<IItemHandler> inputHandler;
    //First slot reserved for blueprint
    //1-9 reserved for crafting ingredients
    //10 reserved for crafting result

    private static final int[] slotsTop = new int[]{0};
    private List<String> recipeIngredients = new ArrayList<>(9);
    private ItemStack recipeResult = ItemStack.EMPTY;
    private int currentIngredientIndex = -1;
    private ItemStack wantItemStack = ItemStack.EMPTY;
    private int craftingProgress = -1;

    public ArmorersAssemblyTableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ASSEMBLY_TABLE.get(), pos, state);
        inventory = createHandler();
        inputHandler = LazyOptional.of(() -> inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ArmorersAssemblyTableBlockEntity table) {
        if (!level.isClientSide) {
            if (table.hasBlueprint()) {
                //Begin crafting
                if (table.isSmithingCraftingTablePresent()) {
                    if (table.recipeIngredients.isEmpty()) {
                        String recipeID = table.getItemFromBlueprint();
                        table.recipeIngredients = table.getIngredientsFromRecipe(recipeID, table.recipeIngredients);
                        table.recipeResult = table.getResultItemFromRecipe(recipeID);
                        table.currentIngredientIndex = level.random.nextInt(0, table.recipeIngredients.size() - 1);
                        table.craftingProgress = 1;
                        table.wantItemStack = table.stringToItemStack(table.recipeIngredients.get(table.currentIngredientIndex));
                        table.markUpdated();
                    }
                }
            }
        }
    }

    public void advanceCrafting(Level level, BlockPos pos, Player player, ItemStack heldItem) {
        if (isSmithingCraftingTablePresent()) {
            //Check if item in indexed slot is current ingredient
            if (getItem(craftingProgress).is(wantItemStack.getItem())) {
                //Check if remaining ingredients is more than one
                int remainingIngredients = recipeIngredients.size();

                if (remainingIngredients > 1) {
                    craftingProgress++;

                    recipeIngredients.remove(currentIngredientIndex);
                    currentIngredientIndex = level.random.nextInt(recipeIngredients.size());
                    wantItemStack = stringToItemStack(recipeIngredients.get(currentIngredientIndex));

                    markUpdated();
                } else if (remainingIngredients == 1) {
                    //Else finish crafting

                    //Set result slot to result item
                    setItem(10, recipeResult);
                    setItem(0, ItemStack.EMPTY);
                    //Clear slots 2-10
                    clearIngredientSlots();
                    //Clear ingredient list
                    recipeIngredients.clear();
                    //Reset variables
                    currentIngredientIndex = -1;
                    craftingProgress = -1;
                    wantItemStack = ItemStack.EMPTY;
                    level.setBlockAndUpdate(pos, getBlockState().setValue(HAS_BLUEPRINT, false));

                    level.playSound(null, pos, SoundEvents.ANVIL_DESTROY, SoundSource.BLOCKS, 0.5F, 1);

                    markUpdated();
                }

                if (!player.isCreative()) {
                    heldItem.setDamageValue(heldItem.getDamageValue() + 1);
                }

                level.playSound(null, pos, SoundRegistry.SMITHING_HAMMER_HIT.get(), SoundSource.BLOCKS, 1, 1);
            }
        }
    }

    //Gets stack from index
    public ItemStack getItem(int index) {
        if (!inventory.getStackInSlot(index).isEmpty()) {
            ItemStack itemStack = inventory.getStackInSlot(index);
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    //Sets index to given stack
    public void setItem(int index, ItemStack stack) {
        inventory.setStackInSlot(index, stack);
    }

    public boolean hasBlueprint() {
        return !getItem(0).equals(ItemStack.EMPTY);
    }

    public String getItemFromBlueprint() {
        ItemStack blueprint = getItem(0);

        if (blueprint.getTag() != null) {
            return blueprint.getOrCreateTag().getString("blueprintItem");
        }
        return "";
    }

    public List<String> getIngredientsFromRecipe(String recipeID, List<String> ingredientList) {
        ingredientList.clear();

        switch (recipeID) {
            case "murasama":
                ingredientList.add(0, Items.ECHO_SHARD.toString());
                ingredientList.add(1, Items.ECHO_SHARD.toString());
                ingredientList.add(2, Items.ECHO_SHARD.toString());
                ingredientList.add(3, Items.ECHO_SHARD.toString());
                ingredientList.add(4, Items.REDSTONE_BLOCK.toString());
                ingredientList.add(5, Items.REDSTONE_BLOCK.toString());
                ingredientList.add(6, Items.IRON_BLOCK.toString());
                break;
            case "gun_sheath":
                ingredientList.add(0, Items.IRON_BLOCK.toString());
                ingredientList.add(1, Items.IRON_INGOT.toString());
                ingredientList.add(2, Items.IRON_INGOT.toString());
                ingredientList.add(3, Items.IRON_INGOT.toString());
                ingredientList.add(4, Items.NETHERITE_INGOT.toString());
                ingredientList.add(5, Items.FLINT_AND_STEEL.toString());
                ingredientList.add(6, Items.TNT.toString());
                break;
            case "originium_catalyst":
                ingredientList.add(0, Items.BLAZE_POWDER.toString());
                ingredientList.add(1, Items.BLAZE_POWDER.toString());
                ingredientList.add(2, Items.BLAZE_POWDER.toString());
                ingredientList.add(3, Items.BLAZE_POWDER.toString());
                ingredientList.add(4, Items.AMETHYST_SHARD.toString());
                ingredientList.add(5, Items.AMETHYST_SHARD.toString());
                ingredientList.add(6, Items.IRON_NUGGET.toString());
                ingredientList.add(7, Items.IRON_NUGGET.toString());
                ingredientList.add(8, Items.IRON_INGOT.toString());
                break;
            case "dragon_slayer":
                ingredientList.add(0, Items.IRON_BLOCK.toString());
                ingredientList.add(1, Items.IRON_BLOCK.toString());
                ingredientList.add(2, Items.IRON_INGOT.toString());
                ingredientList.add(3, Items.IRON_INGOT.toString());
                ingredientList.add(4, Items.OBSIDIAN.toString());
                ingredientList.add(5, Items.OBSIDIAN.toString());
                ingredientList.add(6, Items.NETHERITE_INGOT.toString());
                break;
            case "sharp_irony":
                ingredientList.add(0, Items.NETHERITE_SCRAP.toString());
                ingredientList.add(1, Items.IRON_INGOT.toString());
                ingredientList.add(2, Items.IRON_INGOT.toString());
                ingredientList.add(3, Items.IRON_INGOT.toString());
                ingredientList.add(4, ItemRegistry.METALLIC_FEATHER.get().asItem().toString());
                ingredientList.add(5, ItemRegistry.METALLIC_FEATHER.get().asItem().toString());
                ingredientList.add(6, ItemRegistry.METALLIC_FEATHER.get().asItem().toString());
                ingredientList.add(7, ItemRegistry.METALLIC_FEATHER.get().asItem().toString());
                ingredientList.add(8, ItemRegistry.METALLIC_FEATHER.get().asItem().toString());
                break;
            case "punisher":
                ingredientList.add(0, Items.IRON_BLOCK.toString());
                ingredientList.add(1, Items.IRON_INGOT.toString());
                ingredientList.add(2, Items.IRON_INGOT.toString());
                ingredientList.add(3, Items.DIAMOND_BLOCK.toString());
                ingredientList.add(4, Items.FIREWORK_ROCKET.toString());
                ingredientList.add(5, Items.LEVER.toString());
                break;
        }
        return ingredientList;
    }

    public ItemStack getResultItemFromRecipe(String recipeID) {
        ItemStack resultItem = ItemStack.EMPTY;

        switch (recipeID) {
            case "murasama":
                resultItem = new ItemStack(ItemRegistry.MURASAMA.get(), 1);
                break;
            case "gun_sheath":
                resultItem = new ItemStack(ItemRegistry.GUN_SHEATH.get(), 1);
                break;
            case "originium_catalyst":
                resultItem = new ItemStack(ItemRegistry.ORIGINIUM_CATALYST.get());
                resultItem.setCount(16);
                break;
            case "dragon_slayer":
                resultItem = new ItemStack(ItemRegistry.DRAGON_SLAYER.get(), 1);
                break;
            case "sharp_irony":
                resultItem = new ItemStack(ItemRegistry.SHARP_IRONY.get(), 1);
                resultItem.getOrCreateTag().putBoolean(IS_OPEN, true);
                resultItem.getOrCreateTag().putInt(AMMO_COUNT, 5);
                break;
            case "punisher":
                resultItem = new ItemStack(ItemRegistry.PUNISHER.get(), 1);
                break;
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
            recipeIngredients.add(ingredientString);
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
        for (String ingredient : recipeIngredients) {
            recipeIngredientList.add(StringTag.valueOf(ingredient));
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
            BlockPos pos = worldPosition.relative(facing.getClockWise());
            pos.north(1);

            boolean bool = level.getBlockState(pos).getBlock() == BlockRegistry.ARMORERS_CRAFTING_TABLE.get() && facing == level.getBlockState(pos).getValue(HORIZONTAL_FACING);
            return bool;
        }
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
        for (int i = 0; i < inventory.getSlots(); ++i) {
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

    public ItemStack stringToItemStack(String itemID) {
        ResourceLocation location;

        if (itemID.equals("metallic_feather")) {
            location = new ResourceLocation(ArmoryOfDestiny.MODID, itemID);
        } else {
            location = new ResourceLocation(itemID);
        }
        Item item = ForgeRegistries.ITEMS.getValue(location);
        ItemStack stack = new ItemStack(item);

        return stack;
    }

    public void clearRecipeIngredients() {
        recipeIngredients.clear();
    }

    private void markUpdated() {
        super.setChanged();
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }
}
