package destiny.armoryofdestiny.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class AssemblyRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation recipeID;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;

    public AssemblyRecipe(NonNullList<Ingredient> ingredients) {
        this.recipeID = recipeID;
        this.ingredients = ingredients;
        this.result = result;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return itemStackMatches(container);
    }

    public boolean itemStackMatches(SimpleContainer container) {
        ItemStack stack = container.getItem(1);
        ItemStack recipeResult = ItemStack.of(stack.getTag().getCompound("blueprintItem"));

        return result == recipeResult;
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return recipeID;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<AssemblyRecipe> {
        private Type(){}
        public static final Type INSTANCE = new Type();
        public static final String ID = "assembly";
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }
}