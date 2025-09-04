package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.server.item.utility.TooltipItem;
import destiny.armoryofdestiny.server.recipe.SmithingRecipe;
import destiny.armoryofdestiny.server.recipe.TinkeringRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static destiny.armoryofdestiny.server.util.UtilityVariables.BLUEPRINT;

public class BlueprintItem extends TooltipItem {
    public static final String RECIPE = "recipe";

    public BlueprintItem(Properties properties) {
        super(properties);
    }

    @Override
    public String getTriviaTranslatable() {
        return BLUEPRINT;
    }

    @Override
    public boolean hasAbilities() {
        return false;
    }

    public int getRecipeColor(ItemStack stack)
    {
        Level level = Minecraft.getInstance().level;
        ResourceLocation recipeKey = getRecipeKey(stack);
        if(level == null || recipeKey == null)
            return 0xFFFFFF;
        Optional<? extends Recipe<?>> recipeOptional = level.getRecipeManager().byKey(recipeKey);
        if(recipeOptional.isPresent() && recipeOptional.get() instanceof TinkeringRecipe recipe)
            return recipe.getBlueprintColor();

        return 0xFFFFFF;
    }

    @Nullable
    public ResourceLocation getRecipeKey(ItemStack stack)
    {
        if(stack.getTag() != null && stack.getTag().contains("recipe"))
        {
            String recipeString = stack.getTag().getString("recipe");
            ResourceLocation recipeKey = ResourceLocation.tryParse(recipeString);

            return recipeKey;
        }

        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        if (level == null) return;

        ResourceLocation recipeID = ResourceLocation.tryParse(stack.getOrCreateTag().getString(RECIPE));
        TinkeringRecipe craftingRecipe = null;

        Optional<? extends Recipe<?>> optionalRecipe = level.getRecipeManager().byKey(recipeID);
        if (optionalRecipe.isPresent() && optionalRecipe.get() instanceof TinkeringRecipe recipe)
            craftingRecipe = recipe;

        if (craftingRecipe == null) return;

        List<Ingredient> ingredientList = craftingRecipe.getIngredientList();
        ItemStack result = craftingRecipe.getResult();

        MutableComponent blueprint_item_title = Component.translatable("tooltip.armoryofdestiny.dropdown")
                .append(Component.translatable("item.armoryofdestiny.blueprint.item")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.translatable(result.getDescriptionId())
                        .withStyle(ChatFormatting.RED));
        components.add(blueprint_item_title);

        if (!isShift()) {
            MutableComponent blueprint_ingredients_collapsed = Component.translatable("tooltip.armoryofdestiny.collapsed")
                    .append(Component.translatable("tooltip.armoryofdestiny.ability.collapsed")
                            .withStyle(ChatFormatting.GRAY));
            components.add(blueprint_ingredients_collapsed);
        } else {
            MutableComponent blueprint_ingredients_expanded = Component.translatable("tooltip.armoryofdestiny.expanded")
                    .append(Component.translatable("item.armoryofdestiny.blueprint.ingredients")
                            .withStyle(ChatFormatting.GRAY));
            components.add(blueprint_ingredients_expanded);

            for (int i = 0; ingredientList.size() > i; i++) {
                MutableComponent blueprint_ingredient = Component.translatable("tooltip.armoryofdestiny.bullet_point")
                        .append(Component.translatable(ingredientList.get(i).getItems()[0].getDescriptionId())
                                .withStyle(ChatFormatting.GRAY));
                components.add(blueprint_ingredient);

                String itemKey = ForgeRegistries.ITEMS.getKey(ingredientList.get(i).getItems()[0].getItem()).toString();

                int colonIndex = itemKey.indexOf(':');
                String firstPart = itemKey.substring(0, colonIndex + 1);
                String secondPart = itemKey.substring(colonIndex + 1);
                ResourceLocation recipeIDMaterial = ResourceLocation.tryParse(firstPart + "smithing/hot_" + secondPart);
                SmithingRecipe craftingRecipeMaterial = null;

                Optional<? extends Recipe<?>> craftingRecipeMaterialOptional = level.getRecipeManager().byKey(recipeIDMaterial);
                if (craftingRecipeMaterialOptional.isPresent() && craftingRecipeMaterialOptional.get() instanceof SmithingRecipe recipeMaterial)
                    craftingRecipeMaterial = recipeMaterial;

                if (craftingRecipeMaterial == null) return;

                List<Ingredient> ingredientListMaterial = craftingRecipeMaterial.getIngredientList();

                for (int ii = 0; ingredientListMaterial.size() > ii; ii++) {
                    MutableComponent material_ingredient = Component.translatable("tooltip.armoryofdestiny.dropdown")
                            .append(Component.translatable(ingredientListMaterial.get(ii).getItems()[0].getDescriptionId())
                                    .withStyle(ChatFormatting.DARK_GRAY));
                    components.add(material_ingredient);
                }
            }
        }
    }
}
