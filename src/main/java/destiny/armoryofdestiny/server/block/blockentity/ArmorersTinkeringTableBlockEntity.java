package destiny.armoryofdestiny.server.block.blockentity;

import com.mojang.logging.LogUtils;
import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.recipe.TinkeringRecipe;
import destiny.armoryofdestiny.server.registry.BlockEntityRegistry;
import destiny.armoryofdestiny.server.registry.BlockRegistry;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import destiny.armoryofdestiny.server.registry.SoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
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
import java.util.Optional;

import static destiny.armoryofdestiny.server.block.ArmorersTinkeringTableBlock.HAS_BLUEPRINT;
import static destiny.armoryofdestiny.server.item.SharpIronyItem.AMMO_COUNT;
import static destiny.armoryofdestiny.server.item.SharpIronyItem.IS_OPEN;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ArmorersTinkeringTableBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String STORED_ITEMS = "StoredItems";
    private static final String BLUEPRINT_SLOT = "BlueprintSlot";
    private static final String INPUT_SLOT = "InputSlot";
    private static final String HAMMER_SLOT = "HammerSlot";
    private static final String RECIPE_INGREDIENTS = "RecipeIngredients";
    private static final String RECIPE_RESULT = "RecipeResult";
    private static final String CURRENT_INGREDIENT_INDEX = "CurrentIngredientIndex";
    private static final String WANT_ITEM_STACK = "WantItemStack";

    private final ItemStackHandler storedItems;
    private final ItemStackHandler blueprintSlot;
    private final ItemStackHandler inputSlot;
    private final ItemStackHandler hammerSlot;
    private final LazyOptional<IItemHandler> inputHandler;

    private List<Ingredient> recipeIngredients;
    private ItemStack recipeResult = ItemStack.EMPTY;
    private int currentIngredientIndex = -1;
    private Ingredient wantItemStack = Ingredient.EMPTY;

    public ArmorersTinkeringTableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ARMORERS_TINKERING_TABLE.get(), pos, state);
        storedItems = createHandler(16);
        blueprintSlot = createHandler(1);
        inputSlot = createHandler(1);
        hammerSlot = createHandler(1);
        recipeIngredients = new ArrayList<>(16);
        inputHandler = LazyOptional.of(() -> inputSlot);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ArmorersTinkeringTableBlockEntity table) {
        if (!level.isClientSide) {
            if (table.hasBlueprint()) {
                //Begin crafting
                if (table.isSmithingCraftingTablePresent()) {
                    if (table.recipeIngredients.isEmpty()) {
                        ResourceLocation recipeID = table.getItemFromBlueprint();

                        Optional<? extends Recipe<?>> optionalRecipe = level.getRecipeManager().byKey(recipeID);
                        if(optionalRecipe.isPresent() && optionalRecipe.get() instanceof TinkeringRecipe recipe) {
                            if (table.currentIngredientIndex == -1) {
                                table.recipeIngredients = recipe.getIngredientList();
                                table.recipeResult = recipe.getResult();
                                table.currentIngredientIndex = level.random.nextInt(0, table.recipeIngredients.size() - 1);
                                table.wantItemStack = table.recipeIngredients.get(table.currentIngredientIndex);
                                table.markUpdated();
                            } else {

                                table.recipeIngredients = recipe.getIngredientList();
                                table.wantItemStack = table.recipeIngredients.get(table.currentIngredientIndex);
                                table.markUpdated();
                            }
                        }

/*                        List<TinkeringRecipe> recipes = level.getRecipeManager().getAllRecipesFor(TinkeringRecipe.Type.INSTANCE);
                        if (recipes.removeIf(recipe -> !recipe.recipeID.equals(recipeID))) {
                            TinkeringRecipe recipe = recipes.get(0);

                            if (table.currentIngredientIndex == -1) {
                                table.recipeIngredients = recipe.getIngredientList();
                                table.recipeResult = recipe.getResult();
                                table.currentIngredientIndex = level.random.nextInt(0, table.recipeIngredients.size() - 1);
                                table.wantItemStack = table.recipeIngredients.get(table.currentIngredientIndex);
                                table.markUpdated();
                            } else {

                                table.recipeIngredients = recipe.getIngredientList();
                                table.wantItemStack = table.recipeIngredients.get(table.currentIngredientIndex);
                                table.markUpdated();
                            }
                        }*/
                    }
                }
            }
        }
    }

    public void advanceCrafting(Level level, BlockPos pos, Player player, ItemStack heldItem) {
        if (!level.isClientSide) {
            if (isSmithingCraftingTablePresent()) {
                //Check if item in input slot is current ingredient
                if (wantItemStack.test(getInputItem())) {
                    //Check if remaining ingredients is more than one
                    int remainingIngredients = recipeIngredients.size();

                    if (remainingIngredients > 1) {
                        recipeIngredients.remove(currentIngredientIndex);
                        currentIngredientIndex = level.random.nextInt(recipeIngredients.size());

                        wantItemStack = recipeIngredients.get(currentIngredientIndex);

                        //Transfer input slot to next empty storage slot
                        for (int i = 0; storedItems.getSlots() > i; i++) {
                            if (storedItems.getStackInSlot(i).isEmpty()) {
                                storedItems.setStackInSlot(i, inputSlot.getStackInSlot(0).copy());
                                inputSlot.setStackInSlot(0, ItemStack.EMPTY);
                                break;
                            }
                        }

                        markUpdated();
                    } else if (remainingIngredients == 1) {
                        //Else finish crafting

                        //Set result slot to result item
                        setInputItem(recipeResult);
                        //Clear stored items
                        clearStoredItems();
                        //Clear ingredient list
                        clearVariables();
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
    }

    public void clearVariables() {
        setBlueprintItem(ItemStack.EMPTY);
        recipeIngredients.clear();
        recipeResult = ItemStack.EMPTY;
        currentIngredientIndex = -1;
        wantItemStack = Ingredient.EMPTY;
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

    public ResourceLocation getItemFromBlueprint() {
        ItemStack blueprint = getBlueprintItem();

        if (blueprint.getTag() != null) {
            return new ResourceLocation(blueprint.getOrCreateTag().getString("blueprintItem"));
        }
        return new ResourceLocation("");
    }

    public List<ItemStack> getIngredientsFromRecipe(String recipeID, List<ItemStack> ingredientList) {
        clearRecipeIngredients();

        if (recipeID.equals(ItemRegistry.MURASAMA.getKey().location().toString())) {
            ingredientList.add(0, itemStackFromItem(Items.ECHO_SHARD));
            ingredientList.add(1, itemStackFromItem(Items.ECHO_SHARD));
            ingredientList.add(2, itemStackFromItem(Items.ECHO_SHARD));
            ingredientList.add(3, itemStackFromItem(Items.REDSTONE));
            ingredientList.add(4, itemStackFromItem(Items.REDSTONE));
            ingredientList.add(5, itemStackFromItem(Items.REDSTONE));
            ingredientList.add(6, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(7, itemStackFromItem(Items.IRON_INGOT));
        } else if (recipeID.equals(ItemRegistry.GUN_SHEATH.getKey().location().toString())) {
            ingredientList.add(0, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(1, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(2, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(3, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(4, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(5, itemStackFromItem(Items.NETHERITE_INGOT));
            ingredientList.add(6, itemStackFromItem(Items.FLINT_AND_STEEL));
            ingredientList.add(7, itemStackFromItem(Items.TNT));
            ingredientList.add(8, itemStackFromItem(Items.LEVER));
        } else if (recipeID.equals(ItemRegistry.ORIGINIUM_CATALYST.getKey().location().toString())) {
            ingredientList.add(0, itemStackFromItem(Items.BLAZE_POWDER));
            ingredientList.add(1, itemStackFromItem(Items.BLAZE_POWDER));
            ingredientList.add(2, itemStackFromItem(Items.BLAZE_POWDER));
            ingredientList.add(3, itemStackFromItem(Items.AMETHYST_SHARD));
            ingredientList.add(4, itemStackFromItem(Items.AMETHYST_SHARD));
            ingredientList.add(5, itemStackFromItem(Items.AMETHYST_SHARD));
            ingredientList.add(6, itemStackFromItem(Items.IRON_NUGGET));
            ingredientList.add(7, itemStackFromItem(Items.IRON_NUGGET));
            ingredientList.add(8, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(9, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(10, itemStackFromItem(Items.IRON_INGOT));
        } else if (recipeID.equals(ItemRegistry.DRAGON_SLAYER.getKey().location().toString())) {
            ingredientList.add(0, itemStackFromItem(Items.IRON_BLOCK));
            ingredientList.add(1, itemStackFromItem(Items.IRON_BLOCK));
            ingredientList.add(2, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(3, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(4, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(5, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(6, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(7, itemStackFromItem(Items.OBSIDIAN));
            ingredientList.add(8, itemStackFromItem(Items.OBSIDIAN));
            ingredientList.add(9, itemStackFromItem(Items.OBSIDIAN));
            ingredientList.add(10, itemStackFromItem(Items.NETHERITE_INGOT));
            ingredientList.add(11, itemStackFromItem(Items.LEATHER));
            ingredientList.add(12, itemStackFromItem(Items.LEATHER));
            ingredientList.add(13, itemStackFromItem(Items.LEATHER));
        }  else if (recipeID.equals(ItemRegistry.SHARP_IRONY.getKey().location().toString())) {
            ingredientList.add(0, itemStackFromItem(Items.IRON_NUGGET));
            ingredientList.add(1, itemStackFromItem(Items.IRON_NUGGET));
            ingredientList.add(2, itemStackFromItem(Items.IRON_NUGGET));
            ingredientList.add(3, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(4, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(5, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(6, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(7, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(8, itemStackFromItem(ItemRegistry.METALLIC_FEATHER.get()));
            ingredientList.add(9, itemStackFromItem(ItemRegistry.METALLIC_FEATHER.get()));
            ingredientList.add(10, itemStackFromItem(ItemRegistry.METALLIC_FEATHER.get()));
            ingredientList.add(11, itemStackFromItem(ItemRegistry.METALLIC_FEATHER.get()));
            ingredientList.add(12, itemStackFromItem(ItemRegistry.METALLIC_FEATHER.get()));
        } else if (recipeID.equals(ItemRegistry.PUNISHER.getKey().location().toString())) {
            ingredientList.add(0, itemStackFromItem(Items.IRON_BLOCK));
            ingredientList.add(1, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(2, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(3, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(4, itemStackFromItem(Items.DIAMOND_BLOCK));
            ingredientList.add(5, itemStackFromItem(Items.HOPPER));
            ingredientList.add(6, itemStackFromItem(Items.HOPPER));
            ingredientList.add(7, itemStackFromItem(Items.HOPPER));
            ingredientList.add(8, itemStackFromItem(Items.LEVER));
        } else if (recipeID.equals(ItemRegistry.BLOODLETTER.getKey().location().toString())) {
            ingredientList.add(0, itemStackFromItem(Items.LODESTONE));
            ingredientList.add(1, itemStackFromItem(Items.ENCHANTED_BOOK));
            ingredientList.add(2, itemStackFromItem(Items.CHAIN));
            ingredientList.add(3, itemStackFromItem(Items.CHAIN));
            ingredientList.add(4, itemStackFromItem(Items.CHAIN));
            ingredientList.add(5, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(6, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(7, itemStackFromItem(Items.REDSTONE));
            ingredientList.add(8, itemStackFromItem(Items.REDSTONE));
            ingredientList.add(9, itemStackFromItem(Items.REDSTONE));
            ingredientList.add(10, itemStackFromItem(Items.LEATHER));
            ingredientList.add(11, itemStackFromItem(Items.LEATHER));
        } else if (recipeID.equals(ItemRegistry.CRUCIBLE_INACTIVE.getKey().location().toString())) {
            ingredientList.add(0, itemStackFromItem(Items.BLAZE_POWDER));
            ingredientList.add(1, itemStackFromItem(Items.BLAZE_POWDER));
            ingredientList.add(2, itemStackFromItem(Items.BLAZE_POWDER));
            ingredientList.add(3, itemStackFromItem(Items.ENDER_EYE));
            ingredientList.add(4, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(5, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(6, itemStackFromItem(Items.IRON_INGOT));
            ingredientList.add(7, itemStackFromItem(Items.IRON_BLOCK));
            ingredientList.add(8, itemStackFromItem(Items.NETHERITE_INGOT));
            ingredientList.add(9, itemStackFromItem(Items.LEATHER));
            ingredientList.add(10, itemStackFromItem(Items.LEATHER));
        }
        return ingredientList;
    }

    public ItemStack getResultItemFromRecipe(String recipeID) {
        if (recipeID.equals(ItemRegistry.MURASAMA.getKey().location().toString())) {
            return new ItemStack(ItemRegistry.MURASAMA.get(), 1);
        } else if (recipeID.equals(ItemRegistry.GUN_SHEATH.getKey().location().toString())) {
            return new ItemStack(ItemRegistry.GUN_SHEATH.get(), 1);
        } else if (recipeID.equals(ItemRegistry.ORIGINIUM_CATALYST.getKey().location().toString())) {
            return new ItemStack(ItemRegistry.ORIGINIUM_CATALYST.get(), 16);
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
        storedItems.deserializeNBT(compound.getCompound(STORED_ITEMS));
        blueprintSlot.deserializeNBT(compound.getCompound(BLUEPRINT_SLOT));
        inputSlot.deserializeNBT(compound.getCompound(INPUT_SLOT));
        hammerSlot.deserializeNBT(compound.getCompound(HAMMER_SLOT));

        recipeResult = ItemStack.of(compound.getCompound(RECIPE_RESULT));
        currentIngredientIndex = compound.getInt(CURRENT_INGREDIENT_INDEX);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put(STORED_ITEMS, storedItems.serializeNBT());
        compound.put(BLUEPRINT_SLOT, blueprintSlot.serializeNBT());
        compound.put(INPUT_SLOT, inputSlot.serializeNBT());
        compound.put(HAMMER_SLOT, hammerSlot.serializeNBT());

        compound.put(RECIPE_RESULT, recipeResult.save(new CompoundTag()));
        compound.putInt(CURRENT_INGREDIENT_INDEX, currentIngredientIndex);
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

    public Ingredient getWantItem() {
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
