package destiny.armoryofdestiny.compat.jei;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.recipe.TemperingRecipe;
import destiny.armoryofdestiny.server.registry.BlockRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
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
public class TemperingCategory implements IRecipeCategory<TemperingRecipe> {
    public static final ResourceLocation RECIPE_ID = new ResourceLocation(ArmoryOfDestiny.MODID, "tempering");
    public static final ResourceLocation TEXTURE = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/gui/jei_tempering.png");
    public static final RecipeType<TemperingRecipe> TEMPERING_TYPE = new RecipeType<>(RECIPE_ID, TemperingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public TemperingCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 66, 73);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.TEMPERING_BARREL.get()));
    }

    @Override
    public RecipeType<TemperingRecipe> getRecipeType() {
        return TEMPERING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.armoryofdestiny.tempering");
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
    public void setRecipe(IRecipeLayoutBuilder builder, TemperingRecipe recipe, IFocusGroup iFocusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 3).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 47, 48).addItemStack(recipe.getResult());
    }
}
