package destiny.armoryofdestiny.client;

import destiny.armoryofdestiny.server.item.BlueprintItem;
import destiny.armoryofdestiny.server.recipe.TinkeringRecipe;
import destiny.armoryofdestiny.server.registry.RecipeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientUtils
{
	public static int getBlueprintRecipeColor(ItemStack stack)
	{
		if(stack.getItem() instanceof BlueprintItem blueprint)
		{
			Level level = Minecraft.getInstance().level;
			ResourceLocation recipeKey = blueprint.getRecipeKey(stack);
			if(level == null || recipeKey == null) return 0xFFFFFF;
			Optional<? extends Recipe<?>> recipeOptional = level.getRecipeManager().byKey(recipeKey);
			if(recipeOptional.isPresent() && recipeOptional.get() instanceof TinkeringRecipe recipe)
				return recipe.getBlueprintColor();

		}
		return 0xFFFFFF;
	}

	public static List<TinkeringRecipe> getTinkeringRecipes()
	{
		Level level = Minecraft.getInstance().level;
		if(level != null)
			return level.getRecipeManager().getAllRecipesFor(TinkeringRecipe.Type.INSTANCE);

		return new ArrayList<>();
	}
}
