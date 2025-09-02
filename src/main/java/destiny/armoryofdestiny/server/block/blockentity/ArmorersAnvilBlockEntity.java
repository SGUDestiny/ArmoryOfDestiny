package destiny.armoryofdestiny.server.block.blockentity;

import destiny.armoryofdestiny.server.container.SmithingContainer;
import destiny.armoryofdestiny.server.container.TinkeringContainer;
import destiny.armoryofdestiny.server.recipe.SmithingRecipe;
import destiny.armoryofdestiny.server.recipe.TinkeringRecipe;
import destiny.armoryofdestiny.server.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class ArmorersAnvilBlockEntity extends BlockEntity {
    private static final String STORED_ITEMS = "stored_items";
    private static final String HAMMER_HITS = "hammer_hits";
    private static final String BLUEPRINT = "blueprint";

    private final List<ItemStack> storedItems = new ArrayList<>();
    private int hammer_hits = -1;
    private ItemStack blueprint = ItemStack.EMPTY;

    public ArmorersAnvilBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ARMORERS_ANVIL.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ArmorersAnvilBlockEntity anvil)
    {
        if (level.isClientSide())
            return;
    }

    public boolean advanceCrafting(Level level, BlockPos pos, Player player) {
        if (!blueprint.isEmpty() && blueprint.getTag() != null && blueprint.getTag().get("recipe") != null) {
            ResourceLocation recipeID = new ResourceLocation(blueprint.getOrCreateTag().getString("recipe"));
            TinkeringRecipe tinkeringRecipe = null;
            Optional<? extends Recipe<?>> optionalRecipeTinkering = level.getRecipeManager().byKey(recipeID);
            if (optionalRecipeTinkering.isPresent() && optionalRecipeTinkering.get() instanceof TinkeringRecipe recipe)
                tinkeringRecipe = recipe;

            if (tinkeringRecipe == null) {
                return false;
            }

            SmithingContainer container = new SmithingContainer(storedItems, 0);
            SmithingRecipe craftingRecipe = null;
            Optional<? extends Recipe<?>> optionalRecipe = level.getRecipeManager().getRecipeFor(SmithingRecipe.Type.INSTANCE, container, level);
            if (optionalRecipe.isPresent() && optionalRecipe.get() instanceof SmithingRecipe recipe)
                craftingRecipe = recipe;

            if (craftingRecipe == null) {
                return false;
            }

            if (tinkeringRecipe.getResult().getItem() == craftingRecipe.getParentItem().getItem()) {
                if (craftingRecipe.matches(container, level)) {
                    if (hammer_hits == -1) {
                        hammer_hits = craftingRecipe.getHammerHits() - 1;

                        level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1, 1);
                    } else if (hammer_hits == 0) {
                        storedItems.clear();
                        storedItems.add(craftingRecipe.getResult());
                        hammer_hits = -1;

                        level.playSound(null, pos, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 1, 1);
                    } else {
                        hammer_hits--;

                        level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1, 1);
                    }
                    doHammerStuff(player, pos, player.getItemInHand(InteractionHand.MAIN_HAND));

                    return true;
                }
            }
        } else {
            SmithingContainer container = new SmithingContainer(storedItems, 0);
            SmithingRecipe craftingRecipe = null;
            Optional<? extends Recipe<?>> optionalRecipe = level.getRecipeManager().getRecipeFor(SmithingRecipe.Type.INSTANCE, container, level);
            if (optionalRecipe.isPresent() && optionalRecipe.get() instanceof SmithingRecipe recipe)
                craftingRecipe = recipe;

            if (craftingRecipe == null) {
                return false;
            }

            if (craftingRecipe.getParentItem().isEmpty()) {
                if (craftingRecipe.matches(container, level)) {
                    if (hammer_hits == -1) {
                        hammer_hits = craftingRecipe.getHammerHits() - 1;

                        level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1, 1);
                    } else if (hammer_hits == 0) {
                        storedItems.clear();
                        storedItems.add(craftingRecipe.getResult());
                        hammer_hits = -1;

                        level.playSound(null, pos, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 1, 1);
                    } else {
                        hammer_hits--;

                        level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1, 1);
                    }
                    doHammerStuff(player, pos, player.getItemInHand(InteractionHand.MAIN_HAND));

                    return true;
                }
            }
        }

        return false;
    }

    public void doHammerStuff(Player player, BlockPos pos, ItemStack heldItem) {
        if (!player.isCreative())
        {
            heldItem.setDamageValue(heldItem.getDamageValue() + 1);
            player.getCooldowns().addCooldown(heldItem.getItem(), 20);
        }

        if (level instanceof ServerLevel serverLevel)
        {
            serverLevel.sendParticles(ParticleTypes.CRIT, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, 8, 0.2, 0.1, 0.2, 0.05);
        }
    }

    public ItemStack getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(ItemStack blueprint) {
        this.blueprint = blueprint.copy();
    }

    public int getStoredItemAmount() {
        return storedItems.size();
    }

    public List<ItemStack> getAllStoredItems() {
        return storedItems;
    }

    public void addStoredItem(ItemStack stack) {
        this.storedItems.add(stack.copy());
    }

    public void removeLastStoredItem() {
        storedItems.remove(storedItems.size() - 1);
    }

    public ItemStack getLastStoredItem() {
        return storedItems.get(storedItems.size() - 1);
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
        hammer_hits = tag.getInt(HAMMER_HITS);
        blueprint = ItemStack.of(tag.getCompound(BLUEPRINT));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        ListTag storedItemsTag = new ListTag();
        for(ItemStack stack : storedItems)
            storedItemsTag.add(stack.serializeNBT());
        tag.put(STORED_ITEMS, storedItemsTag);
        tag.putInt(HAMMER_HITS, hammer_hits);
        tag.put(BLUEPRINT, blueprint.serializeNBT());
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
        drops.addAll(storedItems);
        drops.add(blueprint);
        return drops;
    }

    private void markUpdated() {
        super.setChanged();
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }
}
