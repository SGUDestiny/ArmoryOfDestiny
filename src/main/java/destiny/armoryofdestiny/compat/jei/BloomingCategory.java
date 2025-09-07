package destiny.armoryofdestiny.compat.jei;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.recipe.BloomingRecipe;
import destiny.armoryofdestiny.server.registry.BlockRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("removal")
public class BloomingCategory implements IRecipeCategory<BloomingRecipe> {
    public static final ResourceLocation RECIPE_ID = new ResourceLocation(ArmoryOfDestiny.MODID, "blooming");
    public static final ResourceLocation TEXTURE = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/gui/jei_blooming.png");
    public static final RecipeType<BloomingRecipe> BLOOMING_TYPE = new RecipeType<>(RECIPE_ID, BloomingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public BloomingCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 78, 77);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.BLOOMERY_TOP.get()));
    }

    @Override
    public RecipeType<BloomingRecipe> getRecipeType() {
        return BLOOMING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.armoryofdestiny.blooming");
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
    public void setRecipe(IRecipeLayoutBuilder builder, BloomingRecipe recipe, IFocusGroup iFocusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 11, 23).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 59, 23).addItemStack(recipe.getResult());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, BloomingRecipe recipe, IFocusGroup focuses) {
        addCookTime(builder, recipe);
    }

    protected void addCookTime(IRecipeExtrasBuilder builder, BloomingRecipe recipe) {
        int cookTime = recipe.getMeltTime();
        if (cookTime <= 0) {
            cookTime = 0;
        }
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            builder.addText(timeString, getWidth() - 41, 16)
                    .setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)
                    .setTextAlignment(HorizontalAlignment.RIGHT)
                    .setTextAlignment(VerticalAlignment.BOTTOM)
                    .setColor(0xFF808080);
        }
    }
}
