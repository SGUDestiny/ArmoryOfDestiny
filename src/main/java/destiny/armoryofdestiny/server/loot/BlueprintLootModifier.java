package destiny.armoryofdestiny.server.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
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

public class BlueprintLootModifier implements IGlobalLootModifier {
    private static final MapCodec<ResourceKey<Item>> ENTRY_CODEC = ResourceKey.codec(Registries.ITEM).fieldOf("blueprintItem");

    public static final Supplier<Codec<BlueprintLootModifier>> CODEC = () ->
            RecordCodecBuilder.create(inst ->
                    inst.group(
                                    ENTRY_CODEC.forGetter((configuration) -> configuration.blueprintItem),
                                    LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(lm -> lm.conditions)
                            )
                            .apply(inst, BlueprintLootModifier::new));


    private final LootItemCondition[] conditions;
    private final Predicate<LootContext> orConditions;
    private final ResourceKey<Item> blueprintItem;

    public BlueprintLootModifier(ResourceKey<Item> blueprintItem, LootItemCondition[] conditionsIn) {
        this.blueprintItem = blueprintItem;
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
        if (context.getRandom().nextFloat() < getChance()) {
            generatedLoot.add(getBlueprint());
        }
        return generatedLoot;
    }

    private float getChance() {
        float chanceLegendary = 0.1F;
        float chanceUnique = 0.2F;

        if (blueprintItem.equals(ItemRegistry.MURASAMA.getKey())) {
            return chanceLegendary;
        }
        if (blueprintItem.equals(ItemRegistry.GUN_SHEATH.getKey())) {
            return chanceLegendary;
        }
        if (blueprintItem.equals(ItemRegistry.DRAGON_SLAYER.getKey())) {
            return chanceLegendary;
        }
        if (blueprintItem.equals(ItemRegistry.BLOODLETTER.getKey())) {
            return chanceLegendary;
        }
        if (blueprintItem.equals(ItemRegistry.PUNISHER.getKey())) {
            return chanceUnique;
        }
        if (blueprintItem.equals(ItemRegistry.SHARP_IRONY.getKey())) {
            return chanceUnique;
        }
        if (blueprintItem.equals(ItemRegistry.ORIGINIUM_CATALYST.getKey())) {
            return chanceUnique;
        }
        return 0F;
    }

    private String getRarity() {
        String rarityLegendary = "legendary";
        String rarityUnique = "unique";

        if (blueprintItem.equals(ItemRegistry.MURASAMA.getKey())) {
            return rarityLegendary;
        }
        if (blueprintItem.equals(ItemRegistry.GUN_SHEATH.getKey())) {
            return rarityLegendary;
        }
        if (blueprintItem.equals(ItemRegistry.DRAGON_SLAYER.getKey())) {
            return rarityLegendary;
        }
        if (blueprintItem.equals(ItemRegistry.BLOODLETTER.getKey())) {
            return rarityLegendary;
        }
        if (blueprintItem.equals(ItemRegistry.PUNISHER.getKey())) {
            return rarityUnique;
        }
        if (blueprintItem.equals(ItemRegistry.SHARP_IRONY.getKey())) {
            return rarityUnique;
        }
        if (blueprintItem.equals(ItemRegistry.ORIGINIUM_CATALYST.getKey())) {
            return rarityUnique;
        }
        return "";
    }

    private ItemStack getBlueprint() {
        CompoundTag tag = new CompoundTag();
        ResourceKey<Item> key = ItemRegistry.MURASAMA.getKey();
        String rarity = "";
        if (blueprintItem != null) {
            key = blueprintItem;
            rarity = getRarity();
        }
        tag.putString("blueprintItem", key.location().toString());
        tag.putString("blueprintRarity", rarity);
        ItemStack stack = new ItemStack(ItemRegistry.BLUEPRINT.get());
        stack.setTag(tag);
        return stack;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
