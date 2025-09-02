package destiny.armoryofdestiny.server.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.container.SuperheatedBloomingContainer;
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

public class SuperheatedBloomingRecipe implements Recipe<SuperheatedBloomingContainer> {
    public ResourceLocation recipeID;
    public Ingredient ingredient;
    public int meltTime;
    public ItemStack result;

    public static final Codec<SuperheatedBloomingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UtilityCodecs.INGREDIENT_CODEC.fieldOf("ingredient").forGetter(SuperheatedBloomingRecipe::getIngredient),
            Codec.INT.fieldOf("melt_time").forGetter(SuperheatedBloomingRecipe::getMeltTime),
            UtilityCodecs.STACK_CODEC.fieldOf("result").forGetter(SuperheatedBloomingRecipe::getResult)
    ).apply(instance, SuperheatedBloomingRecipe::new));

    public SuperheatedBloomingRecipe(Ingredient ingredient, int meltTime, ItemStack result)
    {
        this.ingredient = ingredient;
        this.meltTime = meltTime;
        this.result = result;
    }

    public SuperheatedBloomingRecipe(ResourceLocation recipeID, Ingredient ingredient, int meltTime, ItemStack result)
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
    public boolean matches(SuperheatedBloomingContainer container, Level level)
    {
        return ingredient.test(container.input);
    }

    @Override
    public ItemStack assemble(SuperheatedBloomingContainer container, RegistryAccess registryAccess)
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
        return SuperheatedBloomingRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType()
    {
        return SuperheatedBloomingRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<SuperheatedBloomingRecipe>
    {
        public static final SuperheatedBloomingRecipe.Type INSTANCE = new SuperheatedBloomingRecipe.Type();
        public static final String ID = "superheated_blooming";

        private Type()
        {

        }
    }

    public static class Serializer implements RecipeSerializer<SuperheatedBloomingRecipe>
    {
        public static final SuperheatedBloomingRecipe.Serializer INSTANCE = new SuperheatedBloomingRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ArmoryOfDestiny.MODID, "superheated_blooming");

        @Override
        public SuperheatedBloomingRecipe fromJson(ResourceLocation recipeID, JsonObject jsonRecipe)
        {
            SuperheatedBloomingRecipe recipe = SuperheatedBloomingRecipe.CODEC.parse(JsonOps.INSTANCE, jsonRecipe)
                    .getOrThrow(false,
                            s -> {
                                throw new JsonParseException(s);
                            });

            return new SuperheatedBloomingRecipe(recipeID, recipe.ingredient, recipe.meltTime, recipe.result);
        }

        @Override
        public @Nullable SuperheatedBloomingRecipe fromNetwork(ResourceLocation recipeID, FriendlyByteBuf buffer)
        {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int meltTime = buffer.readInt();
            ItemStack result = buffer.readItem();
            return new SuperheatedBloomingRecipe(recipeID, ingredient, meltTime, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SuperheatedBloomingRecipe recipe)
        {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeInt(recipe.meltTime);
            buffer.writeItem(recipe.result);
        }
    }
}
