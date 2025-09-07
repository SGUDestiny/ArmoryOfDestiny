package destiny.armoryofdestiny.server.compatibility.jei;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.recipe.SuperheatedBloomingRecipe;
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
public class SuperheatedBloomingCategory implements IRecipeCategory<SuperheatedBloomingRecipe> {
    public static final ResourceLocation RECIPE_ID = new ResourceLocation(ArmoryOfDestiny.MODID, "superheated_blooming");
    public static final ResourceLocation TEXTURE = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/gui/jei_blooming.png");
    public static final RecipeType<SuperheatedBloomingRecipe> SUPERHEATED_BLOOMING_TYPE = new RecipeType<>(RECIPE_ID, SuperheatedBloomingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public SuperheatedBloomingCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 76, 75);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.NETHER_BLOOMERY_TOP.get()));
    }

    @Override
    public RecipeType<SuperheatedBloomingRecipe> getRecipeType() {
        return SUPERHEATED_BLOOMING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.armory_of_destiny.superheated_blooming");
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
    public void setRecipe(IRecipeLayoutBuilder builder, SuperheatedBloomingRecipe recipe, IFocusGroup iFocusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 11, 21).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 58, 21).addItemStack(recipe.getResult());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, SuperheatedBloomingRecipe recipe, IFocusGroup focuses) {
        addCookTime(builder, recipe);
    }

    protected void addCookTime(IRecipeExtrasBuilder builder, SuperheatedBloomingRecipe recipe) {
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