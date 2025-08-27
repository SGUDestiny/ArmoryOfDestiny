package destiny.armoryofdestiny.server.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.container.SmithingContainer;
import destiny.armoryofdestiny.server.container.TinkeringContainer;
import destiny.armoryofdestiny.server.util.UtilityCodecs;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SmithingRecipe implements Recipe<SmithingContainer>
{
    public ResourceLocation recipeID;
    public List<Ingredient> ingredients;
    public int hammerHits;
    public ItemStack result;

    public static final Codec<SmithingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UtilityCodecs.INGREDIENT_CODEC.listOf().fieldOf("ingredients").forGetter(SmithingRecipe::getIngredientList),
            Codec.INT.fieldOf("hammer_hits").forGetter(SmithingRecipe::getHammerHits),
            UtilityCodecs.STACK_CODEC.fieldOf("result").forGetter(SmithingRecipe::getResult)
    ).apply(instance, SmithingRecipe::new));

    public SmithingRecipe(List<Ingredient> ingredients, int hammerHits, ItemStack result)
    {
        this.ingredients = ingredients;
        this.hammerHits = hammerHits;
        this.result = result;
    }

    public SmithingRecipe(ResourceLocation recipeID, List<Ingredient> ingredients, int hammerHits, ItemStack result)
    {
        this.recipeID = recipeID;
        this.ingredients = ingredients;
        this.hammerHits = hammerHits;
        this.result = result;
    }

    public ItemStack getResult()
    {
        return result;
    }

    public int getHammerHits()
    {
        return hammerHits;
    }

    public List<Ingredient> getIngredientList()
    {
        return ingredients;
    }

    @Override
    public boolean matches(SmithingContainer container, Level level)
    {
        List<Boolean> test = new ArrayList<>();

        for (Ingredient ingredient : ingredients)
        {
            for (ItemStack stack : container.inputs)
                if (ingredient.test(stack))
                    test.add(true);
        }

        return test.size() == this.ingredients.size() && container.getHammerHits() == getHammerHits();
    }

    @Override
    public ItemStack assemble(SmithingContainer container, RegistryAccess registryAccess)
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
        NonNullList<Ingredient> list = NonNullList.createWithCapacity(getIngredientList().size());
        list.addAll(getIngredientList());
        return list;
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

    public static class Type implements RecipeType<SmithingRecipe>
    {
        public static final Type INSTANCE = new Type();
        public static final String ID = "smithing";

        private Type()
        {

        }
    }

    public static class Serializer implements RecipeSerializer<SmithingRecipe>
    {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ArmoryOfDestiny.MODID, "smithing");

        @Override
        public SmithingRecipe fromJson(ResourceLocation recipeID, JsonObject jsonRecipe)
        {
            SmithingRecipe recipe = SmithingRecipe.CODEC.parse(JsonOps.INSTANCE, jsonRecipe)
                    .getOrThrow(false,
                            s -> {
                                throw new JsonParseException(s);
                            });

            return new SmithingRecipe(recipeID, recipe.ingredients, recipe.hammerHits, recipe.result);
        }

        @Override
        public @Nullable SmithingRecipe fromNetwork(ResourceLocation recipeID, FriendlyByteBuf buffer)
        {
            List<Ingredient> stacks = buffer.readCollection(i -> new ArrayList<>(), Ingredient::fromNetwork);
            int hammerHits = buffer.readInt();
            ItemStack result = buffer.readItem();
            return new SmithingRecipe(recipeID, stacks, hammerHits, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SmithingRecipe recipe)
        {
            buffer.writeCollection(recipe.ingredients, (writeBuffer, ingredient) -> {
                ingredient.toNetwork(writeBuffer);
            });
            buffer.writeInt(recipe.hammerHits);
            buffer.writeItem(recipe.result);
        }
    }

}
