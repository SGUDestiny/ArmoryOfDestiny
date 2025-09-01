package destiny.armoryofdestiny.server.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import destiny.armoryofdestiny.ArmoryOfDestiny;
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
import java.util.Iterator;
import java.util.List;

public class TinkeringRecipe implements Recipe<TinkeringContainer>
{
    public ResourceLocation recipeID;
    public int color;
    public List<Ingredient> ingredients;
    public ItemStack result;

    public static final Codec<TinkeringRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("blueprint_color").forGetter(TinkeringRecipe::getBlueprintColor),
            UtilityCodecs.INGREDIENT_CODEC.listOf().fieldOf("ingredients").forGetter(TinkeringRecipe::getIngredientList),
            UtilityCodecs.STACK_CODEC.fieldOf("result").forGetter(TinkeringRecipe::getResult)
    ).apply(instance, TinkeringRecipe::new));

    public TinkeringRecipe(int color, List<Ingredient> ingredients, ItemStack result)
    {
        this.color = color;
        this.ingredients = ingredients;
        this.result = result;
    }

    public TinkeringRecipe(ResourceLocation recipeID, int color, List<Ingredient> ingredients, ItemStack result)
    {
        this.recipeID = recipeID;
        this.color = color;
        this.ingredients = ingredients;
        this.result = result;
    }

    public ItemStack getResult()
    {
        return result;
    }

    public int getBlueprintColor()
    {
        return color;
    }

    public List<Ingredient> getIngredientList()
    {
        return ingredients;
    }

    @Override
    public boolean matches(TinkeringContainer container, Level level)
    {
        List<Ingredient> ingredientList = new ArrayList<>(ingredients);
        List<ItemStack> storedList = new ArrayList<>(container.inputs);
        List<Boolean> test = new ArrayList<>();
        Iterator<Ingredient> ingredientCopy = ingredientList.iterator();
        while (ingredientCopy.hasNext())
        {
            Ingredient ingredient = ingredientCopy.next();
            for (ItemStack storedItem : storedList)
                if (ingredient.test(storedItem))
                {
                    ingredientCopy.remove();
                    storedList.remove(storedItem);
                    test.add(true);
                    break;
                }
        }

        return test.size() == ingredients.size();
    }

    @Override
    public ItemStack assemble(TinkeringContainer container, RegistryAccess registryAccess)
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

    public static class Type implements RecipeType<TinkeringRecipe>
    {
        public static final Type INSTANCE = new Type();
        public static final String ID = "tinkering";

        private Type()
        {

        }
    }

    public static class Serializer implements RecipeSerializer<TinkeringRecipe>
    {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ArmoryOfDestiny.MODID, "tinkering");

        @Override
        public TinkeringRecipe fromJson(ResourceLocation recipeID, JsonObject jsonRecipe)
        {
            TinkeringRecipe recipe = TinkeringRecipe.CODEC.parse(JsonOps.INSTANCE, jsonRecipe)
                    .getOrThrow(false,
                            s -> {
                                throw new JsonParseException(s);
                            });

            return new TinkeringRecipe(recipeID, recipe.color, recipe.ingredients, recipe.result);
        }

        @Override
        public @Nullable TinkeringRecipe fromNetwork(ResourceLocation recipeID, FriendlyByteBuf buffer)
        {
            int color = buffer.readInt();
            List<Ingredient> stacks = buffer.readCollection(i -> new ArrayList<>(), Ingredient::fromNetwork);
            ItemStack result = buffer.readItem();
            return new TinkeringRecipe(recipeID, color, stacks, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, TinkeringRecipe recipe)
        {
            buffer.writeInt(recipe.color);
            buffer.writeCollection(recipe.ingredients, (writeBuffer, ingredient) -> {
                ingredient.toNetwork(writeBuffer);
            });
            buffer.writeItem(recipe.result);
        }
    }

}
