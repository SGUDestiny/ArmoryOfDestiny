package destiny.armoryofdestiny.server.block.blockentity;

import com.mojang.logging.LogUtils;
import destiny.armoryofdestiny.server.container.TinkeringContainer;
import destiny.armoryofdestiny.server.recipe.TinkeringRecipe;
import destiny.armoryofdestiny.server.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static destiny.armoryofdestiny.server.block.ArmorersTinkeringTableBlock.HAS_BLUEPRINT;
import static destiny.armoryofdestiny.server.item.SharpIronyItem.AMMO_COUNT;
import static destiny.armoryofdestiny.server.item.SharpIronyItem.IS_OPEN;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ArmorersTinkeringTableBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String STORED_ITEMS = "stored_items";
    private static final String BLUEPRINT = "blueprint";
    private static final String INPUT = "input";
    private static final String HAMMER = "hammer";
    private static final String DESIRED_ITEM = "desired_item";

    private final List<ItemStack> storedItems = new ArrayList<>();
    private ItemStack blueprint = ItemStack.EMPTY;
    private ItemStack input = ItemStack.EMPTY;
    private ItemStack hammer = ItemStack.EMPTY;
    private Ingredient desired = Ingredient.EMPTY;
    private TinkeringRecipe craftingRecipe = null;

    public ItemStack wantStack = ItemStack.EMPTY;

    public ArmorersTinkeringTableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ARMORERS_TINKERING_TABLE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ArmorersTinkeringTableBlockEntity table)
    {
        Random random = new Random();
        if (level.isClientSide() || !table.hasBlueprint() || !table.isSmithingCraftingTablePresent())
            return;

        if (table.craftingRecipe == null)
        {
            ResourceLocation recipeID = table.getRecipe();
            if(recipeID == null)
                return;

            Optional<? extends Recipe<?>> optionalRecipe = level.getRecipeManager().byKey(recipeID);
            if (optionalRecipe.isPresent() && optionalRecipe.get() instanceof TinkeringRecipe recipe)
                table.craftingRecipe = recipe;
        }
        else if(table.desired == Ingredient.EMPTY)
        {
            List<Ingredient> ingredientCopy = table.craftingRecipe.getIngredientList();
            for (Ingredient ingredient : ingredientCopy)
            {
                for(ItemStack storedItem : table.storedItems)
                    if(ingredient.test(storedItem))
                        ingredientCopy.remove(ingredient);
            }

            table.setDesired(ingredientCopy.get(random.nextInt(ingredientCopy.size())));
        }
    }

    public void advanceCrafting(Level level, BlockPos pos, Player player, ItemStack heldItem)
    {
        if (level.isClientSide || !isSmithingCraftingTablePresent())
        {
            TinkeringContainer container = new TinkeringContainer(storedItems);
            if (craftingRecipe.matches(container, level))
            {
                input = craftingRecipe.assemble(container, level.registryAccess());
                storedItems.clear();
                this.setDesired(Ingredient.EMPTY);
                level.setBlockAndUpdate(pos, getBlockState().setValue(HAS_BLUEPRINT, false));
                level.playSound(null, pos, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 0.5F, 1);

                markUpdated();
            } else if (desired.test(getInputItem()))
            {
                this.setDesired(Ingredient.EMPTY);
                this.storedItems.add(getInputItem());
                this.input = ItemStack.EMPTY;
            }
        }

        if (!player.isCreative())
        {
            heldItem.setDamageValue(heldItem.getDamageValue() + 1);
        }

        if (level instanceof ServerLevel serverLevel)
        {
            serverLevel.sendParticles(ParticleTypes.CRIT, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, 8, 0.2, 0.1, 0.2, 0.05);
        }

        level.playSound(null, pos, SoundRegistry.SMITHING_HAMMER_HIT.get(), SoundSource.BLOCKS, 1, 1);
    }


    public ItemStack getInputItem() {
        return input;
    }

    public void setInputItem(ItemStack stack) {
        this.input = stack.copy();
    }

    public ItemStack getBlueprintItem() {
        return blueprint;
    }

    public void setBlueprintItem(ItemStack stack) {
        this.blueprint = stack.copy();
    }

    public ItemStack getHammerSlot() {
        return hammer;
    }

    public void setHammerSlot(ItemStack stack) {
        this.hammer = stack.copy();
    }

    public boolean hasBlueprint() {
        return !getBlueprintItem().isEmpty();
    }

    public void setDesired(Ingredient ingredient)
    {
        this.desired = ingredient;
        this.wantStack = ingredient.getItems()[0];
    }

    @Nullable
    public ResourceLocation getRecipe() {
        ItemStack blueprint = getBlueprintItem();

        if (blueprint.getTag() != null) {
            return new ResourceLocation(blueprint.getOrCreateTag().getString("recipe"));
        }
        return null;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ListTag storedItemsTag = tag.getList(STORED_ITEMS, Tag.TAG_COMPOUND);
        storedItemsTag.forEach(compound -> {
            CompoundTag compoundTag = ((CompoundTag) compound);
            ItemStack storedStack = ItemStack.of(compoundTag);
            storedItems.add(storedStack);
        });
        blueprint = ItemStack.of(tag.getCompound(BLUEPRINT));
        input = ItemStack.of(tag.getCompound(INPUT));
        hammer = ItemStack.of(tag.getCompound(HAMMER));
        wantStack = ItemStack.of(tag.getCompound("want_item"));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        ListTag storedItemsTag = new ListTag();
        for(ItemStack stack : storedItems)
            storedItemsTag.add(stack.serializeNBT());
        tag.put(STORED_ITEMS, storedItemsTag);
        tag.put(BLUEPRINT, blueprint.serializeNBT());
        tag.put(INPUT, input.serializeNBT());
        tag.put(HAMMER, hammer.serializeNBT());
        tag.put("want_item", wantStack.serializeNBT());
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

    public Ingredient getDesiredItem() {
        return desired;
    }

    public ItemStack getWantStack()
    {
        return wantStack;
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
        drops.addAll(storedItems);
        drops.add(blueprint);
        drops.add(input);
        return drops;
    }

    public NonNullList<ItemStack> getDroppableInventoryWithHammer() {
        NonNullList<ItemStack> drops = getDroppableInventory();
        drops.add(getHammerSlot());
        return drops;
    }

    private void markUpdated() {
        super.setChanged();
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }


}
