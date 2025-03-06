package destiny.armoryofdestiny.server.block.blockentity;

import com.mojang.logging.LogUtils;
import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.registry.BlockEntityRegistry;
import destiny.armoryofdestiny.server.registry.BlockRegistry;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import destiny.armoryofdestiny.server.registry.SoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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

import static destiny.armoryofdestiny.server.block.ArmorersTinkeringTableBlock.HAS_BLUEPRINT;
import static destiny.armoryofdestiny.server.item.SharpIronyItem.AMMO_COUNT;
import static destiny.armoryofdestiny.server.item.SharpIronyItem.IS_OPEN;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ArmorersTinkeringTableBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final ItemStackHandler storedItems;
    private final ItemStackHandler blueprintSlot;
    private final ItemStackHandler inputSlot;
    private final ItemStackHandler hammerSlot;
    private final LazyOptional<IItemHandler> inputHandler;


    private List<ItemStack> recipeIngredients;
    private ItemStack recipeResult = ItemStack.EMPTY;
    private int currentIngredientIndex = -1;
    private ItemStack wantItemStack = ItemStack.EMPTY;

    public ArmorersTinkeringTableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ARMORERS_ASSEMBLY_TABLE.get(), pos, state);
        storedItems = createHandler(9);
        blueprintSlot = createHandler(1);
        inputSlot = createHandler(1);
        hammerSlot = createHandler(1);
        recipeIngredients = new ArrayList<>(9);
        inputHandler = LazyOptional.of(() -> inputSlot);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ArmorersTinkeringTableBlockEntity table) {
        if (!level.isClientSide) {
            if (table.hasBlueprint()) {
                //Begin crafting
                if (table.isSmithingCraftingTablePresent()) {
                    if (table.recipeIngredients.isEmpty()) {
                        String recipeID = table.getItemFromBlueprint();
                        table.recipeIngredients = table.getIngredientsFromRecipe(recipeID, table.recipeIngredients);
                        table.recipeResult = table.getResultItemFromRecipe(recipeID);
                        table.currentIngredientIndex = level.random.nextInt(0, table.recipeIngredients.size() - 1);
                        table.wantItemStack = table.recipeIngredients.get(table.currentIngredientIndex);
                        table.markUpdated();
                    }
                }
            }
        }
    }

    public void advanceCrafting(Level level, BlockPos pos, Player player, ItemStack heldItem) {
        if (isSmithingCraftingTablePresent()) {
            //Check if item in input slot is current ingredient
            if (getInputItem().is(wantItemStack.getItem())) {
                //Check if remaining ingredients is more than one
                int remainingIngredients = recipeIngredients.size();

                if (remainingIngredients > 1) {
                    recipeIngredients.remove(currentIngredientIndex);
                    currentIngredientIndex = level.random.nextInt(recipeIngredients.size());
                    markUpdated();
                    wantItemStack = recipeIngredients.get(currentIngredientIndex);

                    //Transfer input slot to next empty storage slot
                    for (int i = 0; storedItems.getSlots() > i; i++) {
                        if (storedItems.getStackInSlot(i).isEmpty()) {
                            storedItems.setStackInSlot(i, inputSlot.getStackInSlot(0));
                            inputSlot.setStackInSlot(0, ItemStack.EMPTY);
                            break;
                        }
                    }

                    markUpdated();
                } else if (remainingIngredients == 1) {
                    //Else finish crafting

                    //Set result slot to result item
                    setInputItem(recipeResult);
                    setBlueprintItem(ItemStack.EMPTY);
                    //Clear stored items
                    clearStoredItems();
                    //Clear ingredient list
                    recipeIngredients.clear();
                    recipeResult = ItemStack.EMPTY;
                    //Reset variables
                    currentIngredientIndex = -1;
                    wantItemStack = ItemStack.EMPTY;
                    level.setBlockAndUpdate(pos, getBlockState().setValue(HAS_BLUEPRINT, false));

                    level.playSound(null, pos, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 0.5F, 1);

                    markUpdated();
                }

                if (!player.isCreative()) {
                    heldItem.setDamageValue(heldItem.getDamageValue() + 1);
                }

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.CRIT, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, 8, 0.2, 0.1, 0.2, 0.05);
                }

                level.playSound(null, pos, SoundRegistry.SMITHING_HAMMER_HIT.get(), SoundSource.BLOCKS, 1, 1);
            }
        }
    }

    public ItemStack getInputItem() {
        if (!inputSlot.getStackInSlot(0).isEmpty()) {
            return inputSlot.getStackInSlot(0).copy();
        }
        return ItemStack.EMPTY;
    }

    public void setInputItem(ItemStack stack) {
        inputSlot.setStackInSlot(0, stack.copy());
    }

    public ItemStack getBlueprintItem() {
        if (!blueprintSlot.getStackInSlot(0).isEmpty()) {
            return blueprintSlot.getStackInSlot(0);
        }
        return ItemStack.EMPTY;
    }

    public void setBlueprintItem(ItemStack stack) {
        blueprintSlot.setStackInSlot(0, stack.copy());
    }

    public ItemStack getHammerSlot() {
        if (!hammerSlot.getStackInSlot(0).isEmpty()) {
            return hammerSlot.getStackInSlot(0);
        }
        return ItemStack.EMPTY;
    }

    public void setHammerSlot(ItemStack stack) {
        hammerSlot.setStackInSlot(0, stack.copy());
    }

    public boolean hasBlueprint() {
        return !getBlueprintItem().isEmpty();
    }

    public String getItemFromBlueprint() {
        ItemStack blueprint = getBlueprintItem();

        if (blueprint.getTag() != null) {
            return blueprint.getOrCreateTag().getString("blueprintItem");
        }
        return "";
    }

    public List<ItemStack> getIngredientsFromRecipe(String recipeID, List<ItemStack> ingredientList) {
        clearRecipeIngredients();

        if (recipeID.equals(ItemRegistry.MURASAMA.getKey().location().toString())) {
            ingredientList.add(0, itemStackFromItem(Items.ECHO_SHARD));
            ingredientList.add(1, itemStackFromItem(Items.ECHO_SHARD));
            ingredientList.add(2, itemStackFromItem(Items.ECHO_SHARD));
            ingredientList.add(3, itemStackFromItem(Items.ECHO_SHARD));
            ingredientList.add(4, itemStackFromItem(Items.REDSTONE_BLOCK));
            ingredientList.add(5, itemStackFromItem(Items.REDSTONE_BLOCK));
            ingredientList.add(6, itemStackFromItem(Items.IRON_BLOCK));
        } else if (recipeID.equals(ItemRegistry.GUN_SHEATH.getKey().location().toString())) {
            ingredientList.add(0, itemStackFromItem(Items.IRON_BLOCK));
            ingredientList.add(1, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(2, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(3, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(4, itemStackFromItem(Items.NETHERITE_INGOT));
            ingredientList.add(5, itemStackFromItem(Items.FLINT_AND_STEEL));
            ingredientList.add(6, itemStackFromItem(Items.TNT));
        } else if (recipeID.equals(ItemRegistry.ORIGINIUM_CATALYST.getKey().location().toString())) {
            ingredientList.add(0, itemStackFromItem(Items.BLAZE_POWDER));
            ingredientList.add(1, itemStackFromItem(Items.BLAZE_POWDER));
            ingredientList.add(2, itemStackFromItem(Items.BLAZE_POWDER));
            ingredientList.add(3, itemStackFromItem(Items.BLAZE_POWDER));
            ingredientList.add(4, itemStackFromItem(Items.AMETHYST_SHARD));
            ingredientList.add(5, itemStackFromItem(Items.AMETHYST_SHARD));
            ingredientList.add(6, itemStackFromItem(Items.IRON_NUGGET));
            ingredientList.add(7, itemStackFromItem(Items.IRON_NUGGET));
            ingredientList.add(8, itemStackFromItem(Items.IRON_INGOT));
        } else if (recipeID.equals(ItemRegistry.DRAGON_SLAYER.getKey().location().toString())) {
            ingredientList.add(0, itemStackFromItem(Items.IRON_BLOCK));
            ingredientList.add(1, itemStackFromItem(Items.IRON_BLOCK));
            ingredientList.add(2, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(3, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(4, itemStackFromItem(Items.OBSIDIAN));
            ingredientList.add(5, itemStackFromItem(Items.OBSIDIAN));
            ingredientList.add(6, itemStackFromItem(Items.NETHERITE_INGOT));
        }  else if (recipeID.equals(ItemRegistry.SHARP_IRONY.getKey().location().toString())) {
            ingredientList.add(0, itemStackFromItem(Items.NETHERITE_SCRAP));
            ingredientList.add(1, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(2, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(3, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(4, itemStackFromItem(ItemRegistry.METALLIC_FEATHER.get()));
            ingredientList.add(5, itemStackFromItem(ItemRegistry.METALLIC_FEATHER.get()));
            ingredientList.add(6, itemStackFromItem(ItemRegistry.METALLIC_FEATHER.get()));
            ingredientList.add(7, itemStackFromItem(ItemRegistry.METALLIC_FEATHER.get()));
            ingredientList.add(8, itemStackFromItem(ItemRegistry.METALLIC_FEATHER.get()));
        } else if (recipeID.equals(ItemRegistry.PUNISHER.getKey().location().toString())) {
            ingredientList.add(0, itemStackFromItem(Items.IRON_BLOCK));
            ingredientList.add(1, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(2, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(3, itemStackFromItem(Items.DIAMOND_BLOCK));
            ingredientList.add(4, itemStackFromItem(Items.FIREWORK_ROCKET));
            ingredientList.add(5, itemStackFromItem(Items.LEVER));
        } else if (recipeID.equals(ItemRegistry.BLOODLETTER.getKey().location().toString())) {
            ingredientList.add(0, itemStackFromItem(Items.LODESTONE));
            ingredientList.add(1, itemStackFromItem(Items.ENCHANTED_BOOK));
            ingredientList.add(2, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(3, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(4, itemStackFromItem(Items.REDSTONE));
            ingredientList.add(5, itemStackFromItem(Items.REDSTONE));
        } else if (recipeID.equals(ItemRegistry.CRUCIBLE_INACTIVE.getKey().location().toString())) {
            ingredientList.add(0, itemStackFromItem(Items.BLAZE_ROD));
            ingredientList.add(1, itemStackFromItem(Items.BLAZE_POWDER));
            ingredientList.add(2, itemStackFromItem(Items.BLAZE_POWDER));
            ingredientList.add(3, itemStackFromItem(Items.ENDER_EYE));
            ingredientList.add(4, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(5, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(5, itemStackFromItem(Items.NETHERITE_INGOT));
        }
        return ingredientList;
    }

    public ItemStack getResultItemFromRecipe(String recipeID) {
        if (recipeID.equals(ItemRegistry.MURASAMA.getKey().location().toString())) {
            return new ItemStack(ItemRegistry.MURASAMA.get(), 1);
        } else if (recipeID.equals(ItemRegistry.GUN_SHEATH.getKey().location().toString())) {
            return new ItemStack(ItemRegistry.GUN_SHEATH.get(), 1);
        } else if (recipeID.equals(ItemRegistry.ORIGINIUM_CATALYST.getKey().location().toString())) {
            return new ItemStack(ItemRegistry.ORIGINIUM_CATALYST.get(), 1);
        } else if (recipeID.equals(ItemRegistry.DRAGON_SLAYER.getKey().location().toString())) {
            return new ItemStack(ItemRegistry.DRAGON_SLAYER.get(), 1);
        }  else if (recipeID.equals(ItemRegistry.SHARP_IRONY.getKey().location().toString())) {
            ItemStack stack = new ItemStack(ItemRegistry.SHARP_IRONY.get(), 1);
            stack.getOrCreateTag().putBoolean(IS_OPEN, true);
            stack.getOrCreateTag().putInt(AMMO_COUNT, 5);
            return stack;
        } else if (recipeID.equals(ItemRegistry.PUNISHER.getKey().location().toString())) {
            return new ItemStack(ItemRegistry.PUNISHER.get(), 1);
        } else if (recipeID.equals(ItemRegistry.BLOODLETTER.getKey().location().toString())) {
            return new ItemStack(ItemRegistry.BLOODLETTER.get(), 1);
        } else if (recipeID.equals(ItemRegistry.CRUCIBLE_INACTIVE.getKey().location().toString())) {
            return new ItemStack(ItemRegistry.CRUCIBLE_INACTIVE.get(), 1);
        }
        return ItemStack.EMPTY;
    }

    public ItemStack itemStackFromItem(Item item) {
        return new ItemStack(item, 1);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        storedItems.deserializeNBT(compound.getCompound("StoredItems"));
        blueprintSlot.deserializeNBT(compound.getCompound("BlueprintSlot"));
        inputSlot.deserializeNBT(compound.getCompound("InputSLot"));
        hammerSlot.deserializeNBT(compound.getCompound("HammerSlot"));

        recipeIngredients.clear();
        ListTag ingredientsTag = compound.getList("RecipeIngredients", Tag.TAG_STRING);
        for (Tag tag : ingredientsTag) {
            ItemStack ingredientString = stringToItemStack(tag.getAsString());
            recipeIngredients.add(ingredientString);
        }
        recipeResult = ItemStack.of(compound.getCompound("RecipeResult"));
        currentIngredientIndex = compound.getInt("CurrentIngredientIndex");
        wantItemStack = ItemStack.of(compound.getCompound("WantItemStack"));
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("StoredItems", storedItems.serializeNBT());
        compound.put("BlueprintSlot", blueprintSlot.serializeNBT());
        compound.put("InputSLot", inputSlot.serializeNBT());
        compound.put("HammerSlot", hammerSlot.serializeNBT());

        ListTag recipeIngredientList = new ListTag();
        for (ItemStack ingredient : recipeIngredients) {
            recipeIngredientList.add(StringTag.valueOf(ingredient.getItem().toString()));
        }
        compound.put("RecipeIngredients", recipeIngredientList);
        compound.put("RecipeResult", recipeResult.save(new CompoundTag()));
        compound.putInt("CurrentIngredientIndex", currentIngredientIndex);
        compound.put("WantItemStack", wantItemStack.save(new CompoundTag()));
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

            return level.getBlockState(pos).getBlock() == BlockRegistry.ARMORERS_CRAFTING_TABLE.get() && facing == level.getBlockState(pos).getValue(HORIZONTAL_FACING);
        }
        return false;
    }

    public void clearStoredItems() {
        for (int i = 0;  storedItems.getSlots() > i; i++) {
            storedItems.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void clearRecipeIngredients() {
        recipeIngredients.clear();
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
        for (int i = 0; i < storedItems.getSlots(); ++i) {
            drops.add(storedItems.getStackInSlot(i));
            storedItems.setStackInSlot(i, ItemStack.EMPTY);
        }
        for (int i = 0; i < blueprintSlot.getSlots(); i++) {
            drops.add(blueprintSlot.getStackInSlot(i));
            blueprintSlot.setStackInSlot(i, ItemStack.EMPTY);
        }
        for (int i = 0; i < inputSlot.getSlots(); i++) {
            drops.add(inputSlot.getStackInSlot(i));
            inputSlot.setStackInSlot(i, ItemStack.EMPTY);
        }
        return drops;
    }

    public NonNullList<ItemStack> getDroppableInventoryWithHammer() {
        NonNullList<ItemStack> drops = getDroppableInventory();
        drops.add(getHammerSlot());
        return drops;
    }

    private ItemStackHandler createHandler(int size) {
        return new ItemStackHandler(size)
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

    private void markUpdated() {
        super.setChanged();
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }
}
