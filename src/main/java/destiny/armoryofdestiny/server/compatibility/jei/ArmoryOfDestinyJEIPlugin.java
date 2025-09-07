package destiny.armoryofdestiny.server.compatibility.jei;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.recipe.BloomingRecipe;
import destiny.armoryofdestiny.server.recipe.SuperheatedBloomingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import mezz.jei.api.recipe.RecipeType;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class ArmoryOfDestinyJEIPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_LOCATION = new ResourceLocation(ArmoryOfDestiny.MODID, "jei_plugin");
    private static Minecraft minecraft = Minecraft.getInstance();

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_LOCATION;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new BloomingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new SuperheatedBloomingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        RecipeManager recipeManager = Objects.requireNonNull(minecraft.level).getRecipeManager();

        List<BloomingRecipe> bloomingRecipes = recipeManager.getAllRecipesFor(BloomingRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(BloomingCategory.RECIPE_ID, BloomingRecipe.class), bloomingRecipes);

        List<SuperheatedBloomingRecipe> superheatedBloomingRecipes = recipeManager.getAllRecipesFor(SuperheatedBloomingRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(SuperheatedBloomingCategory.RECIPE_ID, SuperheatedBloomingRecipe.class), superheatedBloomingRecipes);
    }
}
