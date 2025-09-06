package destiny.armoryofdestiny.server.compatibility.jei;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.recipe.BloomingRecipe;
import destiny.armoryofdestiny.server.registry.BlockRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("removal")
public class BloomingCategory implements IRecipeCategory<BloomingRecipe> {
    public static final ResourceLocation RECIPE_ID = new ResourceLocation(ArmoryOfDestiny.MODID, "transmutation");
    public static final ResourceLocation TEXTURE = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/gui/jei_transmutation.png");
    public static final RecipeType<BloomingRecipe> BLOOMING_TYPE = new RecipeType<>(RECIPE_ID, BloomingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public BloomingCategory(IGuiHelper helper)
    {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 110, 105);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.BLOOMERY_TOP.get()));
    }

    @Override
    public RecipeType<BloomingRecipe> getRecipeType() {
        return BLOOMING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.armory_of_destiny.blooming");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BloomingRecipe bloomingRecipe, IFocusGroup iFocusGroup) {
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, BloomingRecipe recipe, IFocusGroup focuses) {
        IRecipeCategory.super.createRecipeExtras(builder, recipe, focuses);
    }
}
