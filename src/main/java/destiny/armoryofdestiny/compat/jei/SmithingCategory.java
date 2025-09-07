package destiny.armoryofdestiny.compat.jei;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.recipe.SmithingRecipe;
import destiny.armoryofdestiny.server.recipe.TinkeringRecipe;
import destiny.armoryofdestiny.server.registry.BlockRegistry;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import destiny.armoryofdestiny.server.util.ModUtil;
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
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static destiny.armoryofdestiny.server.item.BlueprintItem.RECIPE;

@SuppressWarnings("removal")
public class SmithingCategory implements IRecipeCategory<SmithingRecipe> {
    public static final ResourceLocation RECIPE_ID = new ResourceLocation(ArmoryOfDestiny.MODID, "smithing");
    public static final ResourceLocation TEXTURE = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/gui/jei_smithing.png");
    public static final RecipeType<SmithingRecipe> SMITHING_TYPE = new RecipeType<>(RECIPE_ID, SmithingRecipe.class);
    private static Minecraft minecraft = Minecraft.getInstance();

    private final IDrawable background;
    private final IDrawable icon;

    public SmithingCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 100, 193);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.ARMORERS_ANVIL.get()));
    }

    @Override
    public RecipeType<SmithingRecipe> getRecipeType() {
        return SMITHING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.armoryofdestiny.smithing");
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
    public void setRecipe(IRecipeLayoutBuilder builder, SmithingRecipe recipe, IFocusGroup iFocusGroup) {
        renderBlueprint(builder, recipe);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 81, 174).addItemStack(recipe.getResult());

        List<Ingredient> ingredientList = recipe.getIngredients();
        for (int i = 0; i < ingredientList.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 35, 136 - (i * 19)).addItemStack(ingredientList.get(i).getItems()[0]);
        }
    }

    private void renderBlueprint(IRecipeLayoutBuilder builder, SmithingRecipe recipe) {
        RecipeManager recipeManager = Objects.requireNonNull(minecraft.level).getRecipeManager();

        ItemStack parentStack = recipe.getParentItem();
        String itemKey = ForgeRegistries.ITEMS.getKey(parentStack.getItem()).toString();

        ResourceLocation blueprintRecipeID = ModUtil.stitchResourceLocationFromItem(itemKey, "tinkering/");
        TinkeringRecipe craftingRecipe = null;

        Optional<? extends Recipe<?>> optionalRecipe = recipeManager.byKey(blueprintRecipeID);
        if (optionalRecipe.isPresent() && optionalRecipe.get() instanceof TinkeringRecipe tinkeringRecipe)
            craftingRecipe = tinkeringRecipe;

        if (craftingRecipe == null) return;

        ItemStack blueprint = new ItemStack(ItemRegistry.BLUEPRINT.get());
        blueprint.getOrCreateTag().putString(RECIPE, blueprintRecipeID.toString());

        builder.addSlot(RecipeIngredientRole.INPUT, 35, 155).addItemStack(blueprint);
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, SmithingRecipe recipe, IFocusGroup focuses) {
        addCookTime(builder, recipe);
    }

    protected void addCookTime(IRecipeExtrasBuilder builder, SmithingRecipe recipe) {
        int hits = recipe.getHammerHits();
        if (hits <= 0) {
            hits = 0;
        }

        if (hits > 0) {
            Component timeString = Component.literal(hits + " ").append(Component.translatable("jei.armoryofdestiny.smithing.hits"));
            builder.addText(timeString, getWidth() - 20, 10)
                    .setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM)
                    .setTextAlignment(HorizontalAlignment.LEFT)
                    .setTextAlignment(VerticalAlignment.BOTTOM)
                    .setColor(0xFF808080);
        }
    }
}

