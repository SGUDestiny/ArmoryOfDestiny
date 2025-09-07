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
public class TinkeringCategory implements IRecipeCategory<TinkeringRecipe> {
    public static final ResourceLocation RECIPE_ID = new ResourceLocation(ArmoryOfDestiny.MODID, "tinkering");
    public static final ResourceLocation TEXTURE = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/gui/jei_tinkering.png");
    public static final RecipeType<TinkeringRecipe> TINKERING_TYPE = new RecipeType<>(RECIPE_ID, TinkeringRecipe.class);
    private static Minecraft minecraft = Minecraft.getInstance();

    private final IDrawable background;
    private final IDrawable icon;

    public TinkeringCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 90, 93);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.ARMORERS_TINKERING_TABLE.get()));
    }

    @Override
    public RecipeType<TinkeringRecipe> getRecipeType() {
        return TINKERING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.armoryofdestiny.tinkering");
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
    public void setRecipe(IRecipeLayoutBuilder builder, TinkeringRecipe recipe, IFocusGroup iFocusGroup) {
        renderBlueprint(builder, recipe);

        List<Ingredient> ingredientList = recipe.getIngredients();
        for (int i = 0; i < ingredientList.size(); i++) {
            int x = 3 + (i % 4) * 19;
            int y = 3 + (i / 4) * 19;
            builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(ingredientList.get(i));
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 71, 73).addItemStack(recipe.getResult());
    }

    private void renderBlueprint(IRecipeLayoutBuilder builder, TinkeringRecipe recipe) {
        RecipeManager recipeManager = Objects.requireNonNull(minecraft.level).getRecipeManager();

        ItemStack result = recipe.getResult();
        String itemKey = ForgeRegistries.ITEMS.getKey(result.getItem()).toString();

        ResourceLocation blueprintRecipeID = ModUtil.stitchResourceLocationFromItem(itemKey, "tinkering/");
        TinkeringRecipe craftingRecipe = null;

        Optional<? extends Recipe<?>> optionalRecipe = recipeManager.byKey(blueprintRecipeID);
        if (optionalRecipe.isPresent() && optionalRecipe.get() instanceof TinkeringRecipe tinkeringRecipe)
            craftingRecipe = tinkeringRecipe;

        if (craftingRecipe == null) return;

        ItemStack blueprint = new ItemStack(ItemRegistry.BLUEPRINT.get());
        blueprint.getOrCreateTag().putString(RECIPE, blueprintRecipeID.toString());

        builder.addSlot(RecipeIngredientRole.INPUT, 32, 41).addItemStack(blueprint);
    }
}
