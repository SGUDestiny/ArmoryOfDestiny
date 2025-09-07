package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.server.item.utility.TooltipItem;
import destiny.armoryofdestiny.server.recipe.SmithingRecipe;
import destiny.armoryofdestiny.server.util.ModUtil;
import net.minecraft.ChatFormatting;
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

import static destiny.armoryofdestiny.server.util.UtilityVariables.MATERIAL;

public class MaterialItem extends TooltipItem {
    public MaterialItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasAbilities() {
        return false;
    }

    @Override
    public String getTriviaTranslatable() {
        return MATERIAL;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        if (level == null) return;

        String itemKey = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();

        ResourceLocation recipeID = ModUtil.stitchResourceLocationFromItem(itemKey, "smithing/hot_");
        SmithingRecipe craftingRecipe = null;

        Optional<? extends Recipe<?>> optionalRecipe = level.getRecipeManager().byKey(recipeID);
        if (optionalRecipe.isPresent() && optionalRecipe.get() instanceof SmithingRecipe recipe)
            craftingRecipe = recipe;

        if (craftingRecipe == null) return;

        List<Ingredient> ingredientList = craftingRecipe.getIngredientList();
        ItemStack parent = craftingRecipe.getParentItem();

        MutableComponent blueprint_item_title = Component.translatable("tooltip.armoryofdestiny.dropdown")
                .append(Component.translatable("item.armoryofdestiny.material.item")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.translatable(parent.getDescriptionId())
                        .withStyle(ChatFormatting.RED));
        components.add(blueprint_item_title);

        if (!isShift()) {
            MutableComponent blueprint_ingredients_collapsed = Component.translatable("tooltip.armoryofdestiny.collapsed")
                    .append(Component.translatable("tooltip.armoryofdestiny.ability.collapsed")
                            .withStyle(ChatFormatting.DARK_GRAY));
            components.add(blueprint_ingredients_collapsed);
        } else {
            MutableComponent blueprint_ingredients_expanded = Component.translatable("tooltip.armoryofdestiny.expanded")
                    .append(Component.translatable("item.armoryofdestiny.material.ingredients")
                            .withStyle(ChatFormatting.DARK_GRAY));
            components.add(blueprint_ingredients_expanded);

            for (int i = 0; ingredientList.size() > i; i++) {
                MutableComponent blueprint_ingredient = Component.translatable("tooltip.armoryofdestiny.dropdown")
                        .append(Component.translatable(ingredientList.get(i).getItems()[0].getDescriptionId())
                                .withStyle(ChatFormatting.DARK_GRAY));
                components.add(blueprint_ingredient);
            }
        }
    }
}
