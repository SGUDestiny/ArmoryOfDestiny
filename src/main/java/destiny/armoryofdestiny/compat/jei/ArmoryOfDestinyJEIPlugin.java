package destiny.armoryofdestiny.compat.jei;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.recipe.*;
import destiny.armoryofdestiny.server.registry.BlockRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import mezz.jei.api.recipe.RecipeType;

import java.util.List;
import java.util.Objects;

import static destiny.armoryofdestiny.compat.jei.BloomingCategory.BLOOMING_TYPE;
import static destiny.armoryofdestiny.compat.jei.SmithingCategory.SMITHING_TYPE;
import static destiny.armoryofdestiny.compat.jei.SuperheatedBloomingCategory.SUPERHEATED_BLOOMING_TYPE;
import static destiny.armoryofdestiny.compat.jei.TemperingCategory.TEMPERING_TYPE;
import static destiny.armoryofdestiny.compat.jei.TinkeringCategory.TINKERING_TYPE;

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
        registration.addRecipeCategories(new TemperingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new SmithingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new TinkeringCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.BLOOMERY_TOP.get()), BLOOMING_TYPE);
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.NETHER_BLOOMERY_TOP.get()), SUPERHEATED_BLOOMING_TYPE);
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.TEMPERING_BARREL.get()), TEMPERING_TYPE);
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.ARMORERS_ANVIL.get()), SMITHING_TYPE);
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.ARMORERS_TINKERING_TABLE.get()), TINKERING_TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        RecipeManager recipeManager = Objects.requireNonNull(minecraft.level).getRecipeManager();

        List<BloomingRecipe> bloomingRecipes = recipeManager.getAllRecipesFor(BloomingRecipe.Type.INSTANCE);
        List<SuperheatedBloomingRecipe> superheatedBloomingRecipes = recipeManager.getAllRecipesFor(SuperheatedBloomingRecipe.Type.INSTANCE);
        List<TemperingRecipe> temperingRecipes = recipeManager.getAllRecipesFor(TemperingRecipe.Type.INSTANCE);
        List<SmithingRecipe> smithingRecipes = recipeManager.getAllRecipesFor(SmithingRecipe.Type.INSTANCE);
        List<TinkeringRecipe> tinkeringRecipes = recipeManager.getAllRecipesFor(TinkeringRecipe.Type.INSTANCE);

        registration.addRecipes(new RecipeType<>(BloomingCategory.RECIPE_ID, BloomingRecipe.class), bloomingRecipes);
        registration.addRecipes(new RecipeType<>(SuperheatedBloomingCategory.RECIPE_ID, SuperheatedBloomingRecipe.class), superheatedBloomingRecipes);
        registration.addRecipes(new RecipeType<>(TemperingCategory.RECIPE_ID, TemperingRecipe.class), temperingRecipes);
        registration.addRecipes(new RecipeType<>(SmithingCategory.RECIPE_ID, SmithingRecipe.class), smithingRecipes);
        registration.addRecipes(new RecipeType<>(TinkeringCategory.RECIPE_ID, TinkeringRecipe.class), tinkeringRecipes);
    }
}
