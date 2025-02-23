package destiny.armoryofdestiny.blockentity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import destiny.armoryofdestiny.item.BlueprintItem;
import destiny.armoryofdestiny.registry.BlockEntityRegistry;
import destiny.armoryofdestiny.registry.BlockRegistry;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.IntStream;

public class AssemblyTableBlockEntityOld extends BaseContainerBlockEntity implements WorldlyContainer {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int[] SLOTS = IntStream.range(0, 11).toArray();
    private final ItemStackHandler inventory = new ItemStackHandler(11);
    private final LazyOptional<IItemHandler> holder = LazyOptional.of(() -> inventory);

    private List<Ingredient> remainingIngredients = new ArrayList<>();
    private ItemStack result = ItemStack.EMPTY;
    private int currentIngredientIndex = -1;

    public AssemblyTableBlockEntityOld(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ARMORERS_ASSEMBLY_TABLE.get(), pos, state);
    }

    // Region: Inventory Implementation
    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        return slot == 0 && dir == Direction.UP && stack.getItem() instanceof BlueprintItem;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return slot == 10 && dir == Direction.UP;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.armoryofdestiny.assembly_table");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return null;
    }

    @Override
    public int getContainerSize() { return 11; }

    @Override
    public boolean isEmpty() { return inventory.getSlots() == 0; }

    @Override
    public ItemStack getItem(int slot) { return inventory.getStackInSlot(slot); }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return inventory.extractItem(slot, amount, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) { return inventory.extractItem(slot, 64, false); }

    @Override
    public void setItem(int slot, ItemStack stack) { inventory.setStackInSlot(slot, stack); }

    @Override
    public boolean stillValid(Player player) { return true; }

    @Override
    public void clearContent() { for(int i = 0; i < 11; i++) inventory.setStackInSlot(i, ItemStack.EMPTY); }
    // End Region

    // Region: Crafting Logic

    public static void tick(Level level, BlockPos pos, BlockState state, AssemblyTableBlockEntityOld table) {
        if (level == null) return;

        if (table.remainingIngredients.isEmpty() && table.hasBlueprint()) {
            table.loadRecipeFromBlueprint();
        }

        if (table.currentIngredientIndex == -1 && !table.remainingIngredients.isEmpty()) {
            table.currentIngredientIndex = level.random.nextInt(table.remainingIngredients.size());
            table.setChanged();
        }
    }

    private void loadRecipeFromBlueprint() {
        System.out.println("Trying to get ingredients");
        ItemStack blueprint = getItem(0);
        if (!(blueprint.getItem() instanceof BlueprintItem)) return;

        CompoundTag nbt = blueprint.getTag();
        if (nbt == null || !nbt.contains("blueprintItem")) return;

        String targetItem = nbt.getString("blueprintItem");

/*        level.getRecipeManager().getAllRecipesFor(AssemblyRecipe.TYPE).stream()
                .filter(recipe -> {
                    ItemStack resultStack = recipe.getResultItem(null);
                    System.out.println("resultStack: " + resultStack);
                    System.out.println("targetItem: " + targetItem);
                    return resultStack.getItem().toString().equals(targetItem);
                })
                .findFirst()
                .ifPresent(recipe -> {
                    System.out.println("Getting ingredients");
                    remainingIngredients = new ArrayList<>(recipe.getIngredients());
                    System.out.println("Ingredients: " + recipe.getIngredients());
                    result = recipe.getResultItem(null).copy();
                });*/
    }

    public boolean tryAdvanceCrafting(Player player, ItemStack hammer) {
        LOGGER.info("Current ingredient: {}", remainingIngredients.get(currentIngredientIndex));
        LOGGER.info("Slot contents: {}", inventory.getStackInSlot(1 + currentIngredientIndex));

        if (!checkSmithingTableAdjacency() || currentIngredientIndex == -1 || remainingIngredients.isEmpty())
            return false;

        Ingredient required = remainingIngredients.get(currentIngredientIndex);
        ItemStack available = inventory.getStackInSlot(1 + currentIngredientIndex);

        if (required.test(available)) {
            available.shrink(1);
            remainingIngredients.remove(currentIngredientIndex);
            currentIngredientIndex = -1;

            // Play sound and particles
            level.playSound(null, worldPosition, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.8f, 1.2f);
            if (level instanceof ServerLevel slevel) {
                slevel.sendParticles(ParticleTypes.CRIT,
                        worldPosition.getX() + 0.5,
                        worldPosition.getY() + 1.1,
                        worldPosition.getZ() + 0.5,
                        8, 0.2, 0.1, 0.2, 0.05);
            }

            // Damage hammer
            hammer.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));

            if (remainingIngredients.isEmpty()) {
                inventory.setStackInSlot(10, result.copy());
                result = ItemStack.EMPTY;
                level.playSound(null, worldPosition, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 1.0f, 0.8f);
            }
            setChanged();
            return true;
        }
        return false;
    }

    public boolean tryTakeResult(Player player) {
        if (inventory.getStackInSlot(10).isEmpty()) return false;

        player.addItem(inventory.getStackInSlot(10));
        inventory.setStackInSlot(10, ItemStack.EMPTY);
        return true;
    }
    // End Region

    // Blueprint handling
    public void setBlueprint(ItemStack blueprint) {
        inventory.setStackInSlot(0, blueprint);
    }

    public ItemStack removeBlueprint() {
        ItemStack blueprint = inventory.getStackInSlot(0);
        inventory.setStackInSlot(0, ItemStack.EMPTY);
        return blueprint;
    }

    private boolean hasBlueprint() {
        return !getItem(0).isEmpty();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("Inventory"));

        remainingIngredients.clear();
        ListTag ingredientsTag = tag.getList("Ingredients", Tag.TAG_STRING);
        for (Tag t : ingredientsTag) {
            String ingredientString = t.getAsString();
            remainingIngredients.add(parseIngredientFromString(ingredientString));
        }

        currentIngredientIndex = tag.getInt("CurrentIngredient");
        result = ItemStack.of(tag.getCompound("Result"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());

        // Save ingredients as string list
        ListTag ingredientsTag = new ListTag();
        for (Ingredient ingredient : remainingIngredients) {
            // Serialize using JSON representation
            JsonElement json = ingredient.toJson();
            String identifier = parseIdentifierFromJson(json);
            ingredientsTag.add(StringTag.valueOf(identifier));
        }
        tag.put("Ingredients", ingredientsTag);

        // Save other fields
        tag.putInt("CurrentIngredient", currentIngredientIndex);
        tag.put("Result", result.save(new CompoundTag()));
    }

    private String parseIdentifierFromJson(JsonElement json) {
        if (json.isJsonObject()) {
            JsonObject obj = json.getAsJsonObject();
            if (obj.has("item")) {
                return obj.get("item").getAsString();
            }
            if (obj.has("tag")) {
                return "#" + obj.get("tag").getAsString();
            }
        }
        if (json.isJsonArray()) {
            // Handle first element of array (for multi-item ingredients)
            JsonElement first = json.getAsJsonArray().get(0);
            return parseIdentifierFromJson(first);
        }
        throw new IllegalStateException("Invalid ingredient format: " + json);
    }

    private Ingredient parseIngredientFromString(String input) {
        if (input.startsWith("#")) {
            ResourceLocation tagId = new ResourceLocation(input.substring(1));
            return Ingredient.of(TagKey.create(Registries.ITEM, tagId));
        }
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(input));
        return item != null ? Ingredient.of(item) : Ingredient.EMPTY;
    }

    private boolean checkSmithingTableAdjacency() {
        if (level == null) return false;

        Direction facing = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockPos[] checkPositions = {
                worldPosition.relative(facing.getClockWise()),
                worldPosition.relative(facing.getCounterClockWise()),
                worldPosition.relative(facing.getOpposite())
        };

        for (BlockPos pos : checkPositions) {
            if (level.getBlockState(pos).getBlock() == BlockRegistry.ARMORERS_CRAFTING_TABLE.get()) {
                return true;
            }
        }
        return false;
    }

    // Add accessor methods
    public int getCurrentIngredientIndex() {
        return currentIngredientIndex;
    }

    public ItemStack getCurrentIngredient() {
        return remainingIngredients.get(currentIngredientIndex).getItems()[currentIngredientIndex];
    }

    public List<Ingredient> getRemainingIngredients() {
        return Collections.unmodifiableList(remainingIngredients);
    }

    public ItemStack getResultItem() {
        return inventory.getStackInSlot(10);
    }
}