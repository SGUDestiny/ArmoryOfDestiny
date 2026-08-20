package destiny.armoryofdestiny.server.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static destiny.armoryofdestiny.server.item.BlueprintItem.RECIPE;

public class BlueprintLootModifier implements IGlobalLootModifier {
    public static final Supplier<Codec<BlueprintLootModifier>> CODEC = () ->
            RecordCodecBuilder.create(inst ->
                    inst.group(
                                    Codec.FLOAT.fieldOf("baseChance").forGetter(lm -> lm.baseChance),
                                    Codec.FLOAT.fieldOf("decrementalChance").forGetter(lm -> lm.decrementalChance),
                                    ResourceLocation.CODEC.listOf().fieldOf("recipes").forGetter(lm -> lm.recipes),
                                    LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(lm -> lm.conditions)
                            )
                            .apply(inst, BlueprintLootModifier::new));

    private final float baseChance;
    private final float decrementalChance;
    private final List<ResourceLocation> recipes;
    private final LootItemCondition[] conditions;
    private final Predicate<LootContext> orConditions;

    public BlueprintLootModifier(float baseChance, float decrementalChance, List<ResourceLocation> recipes, LootItemCondition[] conditionsIn) {
        this.baseChance = baseChance;
        this.decrementalChance = decrementalChance;
        this.recipes = recipes;
        this.conditions = conditionsIn;
        this.orConditions = LootItemConditions.orConditions(conditionsIn);
    }

    @NotNull
    @Override
    public ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        return this.orConditions.test(context) ? this.doApply(generatedLoot, context) : generatedLoot;
    }

    @NotNull
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (recipes.isEmpty()) {
            return generatedLoot;
        }

        List<ResourceLocation> pool = new ArrayList<>(recipes);
        float step = decrementalChance / pool.size();
        int added = 0;

        while (!pool.isEmpty()) {
            float chance = baseChance - step * added;
            if (context.getRandom().nextFloat() >= chance) {
                break;
            }

            int index = context.getRandom().nextInt(pool.size());
            ResourceLocation recipeID = pool.remove(index);
            generatedLoot.add(getBlueprint(recipeID));
            added++;
        }

        return generatedLoot;
    }

    private ItemStack getBlueprint(ResourceLocation recipeID) {
        CompoundTag tag = new CompoundTag();
        tag.putString(RECIPE, recipeID.toString());
        ItemStack stack = new ItemStack(ItemRegistry.BLUEPRINT.get());
        stack.setTag(tag);
        return stack;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
