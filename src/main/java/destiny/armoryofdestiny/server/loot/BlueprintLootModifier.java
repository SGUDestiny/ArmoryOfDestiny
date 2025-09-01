package destiny.armoryofdestiny.server.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static destiny.armoryofdestiny.server.item.BlueprintItem.RECIPE;

public class BlueprintLootModifier implements IGlobalLootModifier {
    private static final MapCodec<ResourceLocation> ENTRY_CODEC = ResourceLocation.CODEC.fieldOf("recipeID");

    public static final Supplier<Codec<BlueprintLootModifier>> CODEC = () ->
            RecordCodecBuilder.create(inst ->
                    inst.group(
                                    ENTRY_CODEC.forGetter((configuration) -> configuration.recipeID),
                                    LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(lm -> lm.conditions)
                            )
                            .apply(inst, BlueprintLootModifier::new));


    private final LootItemCondition[] conditions;
    private final Predicate<LootContext> orConditions;
    private final ResourceLocation recipeID;

    public BlueprintLootModifier(ResourceLocation recipeID, LootItemCondition[] conditionsIn) {
        this.recipeID = recipeID;
        this.conditions = conditionsIn;
        this.orConditions = LootItemConditions.orConditions(conditionsIn);
    }

    @NotNull
    @Override
    public ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        return this.orConditions.test(context) ? this.doApply(generatedLoot, context) : generatedLoot;
    }

    @Nonnull
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (context.getRandom().nextFloat() < 0.15) {
            generatedLoot.add(getBlueprint());
        }
        return generatedLoot;
    }

    private ItemStack getBlueprint() {
        CompoundTag tag = new CompoundTag();
        ResourceLocation location = ResourceLocation.tryParse("");
        if (recipeID != null) {
            location = recipeID;
        }
        tag.putString(RECIPE, location.toString());
        ItemStack stack = new ItemStack(ItemRegistry.BLUEPRINT.get());
        stack.setTag(tag);
        return stack;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
