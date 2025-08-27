package destiny.armoryofdestiny.server.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.container.BloomingContainer;
import destiny.armoryofdestiny.server.container.TemperingContainer;
import destiny.armoryofdestiny.server.util.UtilityCodecs;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class TemperingRecipe implements Recipe<TemperingContainer>
{
    public ResourceLocation recipeID;
    public Ingredient ingredient;
    public int coolTime;
    public ItemStack result;

    public static final Codec<TemperingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UtilityCodecs.INGREDIENT_CODEC.fieldOf("ingredient").forGetter(TemperingRecipe::getIngredient),
            Codec.INT.fieldOf("cool_time").forGetter(TemperingRecipe::getCoolTime),
            UtilityCodecs.STACK_CODEC.fieldOf("result").forGetter(TemperingRecipe::getResult)
    ).apply(instance, TemperingRecipe::new));

    public TemperingRecipe(Ingredient ingredient, int coolTime, ItemStack result)
    {
        this.ingredient = ingredient;
        this.coolTime = coolTime;
        this.result = result;
    }

    public TemperingRecipe(ResourceLocation recipeID, Ingredient ingredient, int coolTime, ItemStack result)
    {
        this.recipeID = recipeID;
        this.ingredient = ingredient;
        this.coolTime = coolTime;
        this.result = result;
    }

    public ItemStack getResult()
    {
        return result;
    }

    public int getCoolTime()
    {
        return coolTime;
    }

    public Ingredient getIngredient()
    {
        return ingredient;
    }

    @Override
    public boolean matches(TemperingContainer container, Level level)
    {
        return ingredient.test(container.input) && container.coolTime > this.getCoolTime();
    }

    @Override
    public ItemStack assemble(TemperingContainer container, RegistryAccess registryAccess)
    {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1)
    {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess)
    {
        return result.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients()
    {
        return NonNullList.of(ingredient);
    }

    @Override
    public ResourceLocation getId()
    {
        return recipeID;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType()
    {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<TemperingRecipe>
    {
        public static final Type INSTANCE = new Type();
        public static final String ID = "blooming";

        private Type()
        {

        }
    }

    public static class Serializer implements RecipeSerializer<TemperingRecipe>
    {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ArmoryOfDestiny.MODID, "blooming");

        @Override
        public TemperingRecipe fromJson(ResourceLocation recipeID, JsonObject jsonRecipe)
        {
            TemperingRecipe recipe = TemperingRecipe.CODEC.parse(JsonOps.INSTANCE, jsonRecipe)
                    .getOrThrow(false,
                            s -> {
                                throw new JsonParseException(s);
                            });

            return new TemperingRecipe(recipeID, recipe.ingredient, recipe.coolTime, recipe.result);
        }

        @Override
        public @Nullable TemperingRecipe fromNetwork(ResourceLocation recipeID, FriendlyByteBuf buffer)
        {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int meltTime = buffer.readInt();
            ItemStack result = buffer.readItem();
            return new TemperingRecipe(recipeID, ingredient, meltTime, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, TemperingRecipe recipe)
        {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeInt(recipe.coolTime);
            buffer.writeItem(recipe.result);
        }
    }

}
