package destiny.armoryofdestiny.server.util;

import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import destiny.armoryofdestiny.server.recipe.TinkeringRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.Optional;

public class UtilityCodecs
{
    public static final Codec<Ingredient> INGREDIENT_CODEC = new PrimitiveCodec<>()
    {
        @Override
        public <T> DataResult<Ingredient> read(DynamicOps<T> ops, T input)
        {
            try
            {
                return DataResult.success(CraftingHelper.getIngredient(ops.convertTo(JsonOps.INSTANCE, input).getAsJsonObject(), false));
            } catch (JsonSyntaxException error)
            {
                return DataResult.error(error::getMessage);
            }
        }

        @Override
        public <T> T write(DynamicOps<T> ops, Ingredient value)
        {
            return JsonOps.INSTANCE.convertTo(ops, value.toJson());
        }
    };

    public static final Codec<ItemStack> STACK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ItemStack::getItem),
            Codec.INT.fieldOf("amount").forGetter(ItemStack::getCount),
            CompoundTag.CODEC.optionalFieldOf("tag").forGetter(stack -> Optional.ofNullable(stack.getTag()))
    ).apply(instance, UtilityCodecs::createStack));

    public static ItemStack createStack(Item item, int amount, Optional<CompoundTag> tag)
    {
        CompoundTag nbt = tag.orElse(null);
        return new ItemStack(item, amount, nbt);
    }
}
