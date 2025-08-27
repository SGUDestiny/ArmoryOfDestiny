package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.server.item.utility.TooltipItem;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static destiny.armoryofdestiny.server.util.UtilityVariables.BLUEPRINT;

public class BlueprintItem extends TooltipItem {

    public BlueprintItem(Properties properties) {
        super(properties);
    }

    public static List<String> getBlueprintIngredients(String blueprintItem) {
        List<String> ingredientList = new ArrayList<>();

        if (blueprintItem.equals(ItemRegistry.MURASAMA.getKey().location().toString())) {
            ingredientList.add(0, Items.ECHO_SHARD.getDescriptionId());
            ingredientList.add(1, Items.ECHO_SHARD.getDescriptionId());
            ingredientList.add(2, Items.ECHO_SHARD.getDescriptionId());
            ingredientList.add(3, Items.REDSTONE.getDescriptionId());
            ingredientList.add(4, Items.REDSTONE.getDescriptionId());
            ingredientList.add(5, Items.REDSTONE.getDescriptionId());
            ingredientList.add(6, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(7, Items.IRON_INGOT.getDescriptionId());
        } else if (blueprintItem.equals(ItemRegistry.GUN_SHEATH.getKey().location().toString())) {
            ingredientList.add(0, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(1, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(2, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(3, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(4, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(5, Items.NETHERITE_INGOT.getDescriptionId());
            ingredientList.add(6, Items.FLINT_AND_STEEL.getDescriptionId());
            ingredientList.add(7, Items.TNT.getDescriptionId());
            ingredientList.add(8, Items.LEVER.getDescriptionId());
        } else if (blueprintItem.equals(ItemRegistry.ORIGINIUM_CATALYST.getKey().location().toString())) {
            ingredientList.add(0, Items.BLAZE_POWDER.getDescriptionId());
            ingredientList.add(1, Items.BLAZE_POWDER.getDescriptionId());
            ingredientList.add(2, Items.BLAZE_POWDER.getDescriptionId());
            ingredientList.add(3, Items.AMETHYST_SHARD.getDescriptionId());
            ingredientList.add(4, Items.AMETHYST_SHARD.getDescriptionId());
            ingredientList.add(5, Items.AMETHYST_SHARD.getDescriptionId());
            ingredientList.add(6, Items.IRON_NUGGET.getDescriptionId());
            ingredientList.add(7, Items.IRON_NUGGET.getDescriptionId());
            ingredientList.add(8, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(9, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(10, Items.IRON_INGOT.getDescriptionId());
        } else if (blueprintItem.equals(ItemRegistry.DRAGON_SLAYER.getKey().location().toString())) {
            ingredientList.add(0, Items.IRON_BLOCK.getDescriptionId());
            ingredientList.add(1, Items.IRON_BLOCK.getDescriptionId());
            ingredientList.add(2, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(3, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(4, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(5, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(6, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(7, Items.OBSIDIAN.getDescriptionId());
            ingredientList.add(8, Items.OBSIDIAN.getDescriptionId());
            ingredientList.add(9, Items.OBSIDIAN.getDescriptionId());
            ingredientList.add(10, Items.NETHERITE_INGOT.getDescriptionId());
            ingredientList.add(11, Items.LEATHER.getDescriptionId());
            ingredientList.add(12, Items.LEATHER.getDescriptionId());
            ingredientList.add(13, Items.LEATHER.getDescriptionId());
        }  else if (blueprintItem.equals(ItemRegistry.SHARP_IRONY.getKey().location().toString())) {
            ingredientList.add(0, Items.IRON_NUGGET.getDescriptionId());
            ingredientList.add(1, Items.IRON_NUGGET.getDescriptionId());
            ingredientList.add(2, Items.IRON_NUGGET.getDescriptionId());
            ingredientList.add(3, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(4, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(5, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(6, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(7, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(8, ItemRegistry.METALLIC_FEATHER.get().getDescriptionId());
            ingredientList.add(9, ItemRegistry.METALLIC_FEATHER.get().getDescriptionId());
            ingredientList.add(10, ItemRegistry.METALLIC_FEATHER.get().getDescriptionId());
            ingredientList.add(11, ItemRegistry.METALLIC_FEATHER.get().getDescriptionId());
            ingredientList.add(12, ItemRegistry.METALLIC_FEATHER.get().getDescriptionId());
        } else if (blueprintItem.equals(ItemRegistry.PUNISHER.getKey().location().toString())) {
            ingredientList.add(0, Items.IRON_BLOCK.getDescriptionId());
            ingredientList.add(1, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(2, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(3, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(4, Items.DIAMOND_BLOCK.getDescriptionId());
            ingredientList.add(5, Items.HOPPER.getDescriptionId());
            ingredientList.add(6, Items.HOPPER.getDescriptionId());
            ingredientList.add(7, Items.HOPPER.getDescriptionId());
            ingredientList.add(8, Items.LEVER.getDescriptionId());
        } else if (blueprintItem.equals(ItemRegistry.BLOODLETTER.getKey().location().toString())) {
            ingredientList.add(0, Items.LODESTONE.getDescriptionId());
            ingredientList.add(1, Items.ENCHANTED_BOOK.getDescriptionId());
            ingredientList.add(2, Items.CHAIN.getDescriptionId());
            ingredientList.add(3, Items.CHAIN.getDescriptionId());
            ingredientList.add(4, Items.CHAIN.getDescriptionId());
            ingredientList.add(5, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(6, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(7, Items.REDSTONE.getDescriptionId());
            ingredientList.add(8, Items.REDSTONE.getDescriptionId());
            ingredientList.add(9, Items.REDSTONE.getDescriptionId());
            ingredientList.add(10, Items.LEATHER.getDescriptionId());
            ingredientList.add(11, Items.LEATHER.getDescriptionId());
        } else if (blueprintItem.equals(ItemRegistry.CRUCIBLE_INACTIVE.getKey().location().toString())) {
            ingredientList.add(0, Items.BLAZE_POWDER.getDescriptionId());
            ingredientList.add(1, Items.BLAZE_POWDER.getDescriptionId());
            ingredientList.add(2, Items.BLAZE_POWDER.getDescriptionId());
            ingredientList.add(3, Items.ENDER_EYE.getDescriptionId());
            ingredientList.add(4, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(5, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(6, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(7, Items.IRON_BLOCK.getDescriptionId());
            ingredientList.add(8, Items.NETHERITE_INGOT.getDescriptionId());
            ingredientList.add(9, Items.LEATHER.getDescriptionId());
            ingredientList.add(10, Items.LEATHER.getDescriptionId());
        }
        return ingredientList;
    }

    @Override
    public String getRarityTranslatable(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getOrCreateTag().getString("blueprintRarity");
        }
        return super.getRarityTranslatable(stack);
    }

    @Override
    public String getTriviaTranslatable() {
        return BLUEPRINT;
    }

    @Override
    public boolean hasAbilities() {
        return false;
    }

    public Item getItemFromResourceKeyLocation(String keyLocation) {
        return BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(keyLocation));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        String keyLocation = stack.getOrCreateTag().getString("blueprintItem");
        List<String> ingredientList = getBlueprintIngredients(keyLocation);

        MutableComponent blueprint_item_title = Component.translatable("tooltip.armoryofdestiny.dropdown")
                .append(Component.translatable("item.armoryofdestiny.blueprint.item")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.translatable(BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(keyLocation)).getDescriptionId())
                        .withStyle(ChatFormatting.RED));
        components.add(blueprint_item_title);

        if (!isShift(level)) {
            MutableComponent blueprint_ingredients_collapsed = Component.translatable("tooltip.armoryofdestiny.collapsed")
                    .append(Component.translatable("tooltip.armoryofdestiny.ability.collapsed")
                            .withStyle(ChatFormatting.DARK_GRAY));
            components.add(blueprint_ingredients_collapsed);
        } else {
            MutableComponent blueprint_ingredients_expanded = Component.translatable("tooltip.armoryofdestiny.expanded")
                    .append(Component.translatable("item.armoryofdestiny.blueprint.ingredients")
                            .withStyle(ChatFormatting.DARK_GRAY));
            components.add(blueprint_ingredients_expanded);

            for (int i = 0; ingredientList.size() > i; i++) {
                MutableComponent blueprint_ingredient = Component.translatable("tooltip.armoryofdestiny.dropdown")
                        .append(Component.translatable(ingredientList.get(i))
                                .withStyle(ChatFormatting.GRAY));
                components.add(blueprint_ingredient);
            }
        }
    }
}
