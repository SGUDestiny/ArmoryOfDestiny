package destiny.armoryofdestiny.server.util;

import destiny.armoryofdestiny.server.block.blockentity.ArmorersAnvilBlockEntity;
import destiny.armoryofdestiny.server.block.blockentity.ArmorersTinkeringTableBlockEntity;
import destiny.armoryofdestiny.server.item.BlueprintItem;
import destiny.armoryofdestiny.server.recipe.TinkeringRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class UtilityVariables {
    public static final String NONE = "tooltip.armoryofdestiny.none";

    //Trivia
    public static final String KATANA = "tooltip.armoryofdestiny.trivia.katana";
    public static final String THROWING_FAN = "tooltip.armoryofdestiny.trivia.throwing_fan";
    public static final String HAMMER = "tooltip.armoryofdestiny.trivia.hammer";
    public static final String GREATSWORD = "tooltip.armoryofdestiny.trivia.greatsword";
    public static final String BLUEPRINT = "tooltip.armoryofdestiny.trivia.blueprint";
    public static final String SMITHING_HAMMER = "tooltip.armoryofdestiny.trivia.smithing_hammer";
    public static final String ARMORERS_CRAFTING_TABLE = "tooltip.armoryofdestiny.trivia.armorers_crafting_table";
    public static final String ARMORERS_TINKERING_TABLE = "tooltip.armoryofdestiny.trivia.armorers_tinkering_table";
    public static final String RAPIER = "tooltip.armoryofdestiny.trivia.rapier";
    public static final String HOT_ITEM = "tooltip.armoryofdestiny.trivia.hot_item";
    public static final String MATERIAL = "tooltip.armoryofdestiny.trivia.material";
    public static final String SMITHING_TONGS = "tooltip.armoryofdestiny.trivia.smithing_tongs";
    public static final String BLOOMERY_TOP = "tooltip.armoryofdestiny.trivia.bloomery_top";
    public static final String BLOOMERY_BOTTOM = "tooltip.armoryofdestiny.trivia.bloomery_bottom";
    public static final String ARMORERS_ANVIL = "tooltip.armoryofdestiny.trivia.armorers_anvil";

    public static int getBlueprintColor(ItemStack stack) {
        if (stack.getTag() != null && stack.getItem() instanceof BlueprintItem blueprint)
            return blueprint.getRecipeColor(stack);
        return 0xFFFFFF;
    }

    public static int getBlueprintColor(Level level, BlockPos pos)
    {
        if(level == null || pos == null)
            return 0xFFFFFF;
        if (level.getBlockEntity(pos) instanceof ArmorersTinkeringTableBlockEntity table) {
            ResourceLocation recipeKey = table.getRecipe();
            if(recipeKey != null)
            {
                Optional<? extends Recipe<?>> recipeOptional = level.getRecipeManager().byKey(recipeKey);
                if(recipeOptional.isPresent() && recipeOptional.get() instanceof TinkeringRecipe recipe)
                    return recipe.getBlueprintColor();
            }
        }
        if (level.getBlockEntity(pos) instanceof ArmorersAnvilBlockEntity anvil) {
            ResourceLocation recipeKey = new ResourceLocation(anvil.getBlueprint().getOrCreateTag().getString("recipe"));
            if(recipeKey != null)
            {
                Optional<? extends Recipe<?>> recipeOptional = level.getRecipeManager().byKey(recipeKey);
                if(recipeOptional.isPresent() && recipeOptional.get() instanceof TinkeringRecipe recipe)
                    return recipe.getBlueprintColor();
            }
        }
        return 0xFFFFFF;
    }
}
