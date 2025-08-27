package destiny.armoryofdestiny.server.recipe;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.container.BloomingContainer;
import destiny.armoryofdestiny.server.container.TinkeringContainer;
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

import java.util.ArrayList;
import java.util.List;

public class BloomingRecipe implements Recipe<BloomingContainer>
{
    public ResourceLocation recipeID;
    public Ingredient ingredient;
    public int meltTime;
    public ItemStack result;

    public static final Codec<BloomingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UtilityCodecs.INGREDIENT_CODEC.fieldOf("ingredient").forGetter(BloomingRecipe::getIngredient),
            Codec.INT.fieldOf("melt_time").forGetter(BloomingRecipe::getMeltTime),
            UtilityCodecs.STACK_CODEC.fieldOf("result").forGetter(BloomingRecipe::getResult)
    ).apply(instance, BloomingRecipe::new));

    public BloomingRecipe(Ingredient ingredient, int meltTime, ItemStack result)
    {
        this.ingredient = ingredient;
        this.meltTime = meltTime;
        this.result = result;
    }

    public BloomingRecipe(ResourceLocation recipeID, Ingredient ingredient, int meltTime, ItemStack result)
    {
        this.recipeID = recipeID;
        this.ingredient = ingredient;
        this.meltTime = meltTime;
        this.result = result;
    }

    public ItemStack getResult()
    {
        return result;
    }

    public int getMeltTime()
    {
        return meltTime;
    }

    public Ingredient getIngredient()
    {
        return ingredient;
    }

    @Override
    public boolean matches(BloomingContainer container, Level level)
    {
        return ingredient.test(container.input) && container.meltTime > this.getMeltTime();
    }

    @Override
    public ItemStack assemble(BloomingContainer container, RegistryAccess registryAccess)
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

    public static class Type implements RecipeType<BloomingRecipe>
    {
        public static final Type INSTANCE = new Type();
        public static final String ID = "blooming";

        private Type()
        {

        }
    }

    public static class Serializer implements RecipeSerializer<BloomingRecipe>
    {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ArmoryOfDestiny.MODID, "blooming");

        @Override
        public BloomingRecipe fromJson(ResourceLocation recipeID, JsonObject jsonRecipe)
        {
            BloomingRecipe recipe = BloomingRecipe.CODEC.parse(JsonOps.INSTANCE, jsonRecipe)
                    .getOrThrow(false,
                            s -> {
                                throw new JsonParseException(s);
                            });

            return new BloomingRecipe(recipeID, recipe.ingredient, recipe.meltTime, recipe.result);
        }

        @Override
        public @Nullable BloomingRecipe fromNetwork(ResourceLocation recipeID, FriendlyByteBuf buffer)
        {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int meltTime = buffer.readInt();
            ItemStack result = buffer.readItem();
            return new BloomingRecipe(recipeID, ingredient, meltTime, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BloomingRecipe recipe)
        {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeInt(recipe.meltTime);
            buffer.writeItem(recipe.result);
        }
    }

}
