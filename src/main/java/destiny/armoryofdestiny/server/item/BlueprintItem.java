package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.server.item.utility.TooltipItem;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static destiny.armoryofdestiny.server.misc.UtilityVariables.BLUEPRINT;

public class BlueprintItem extends TooltipItem {

    public BlueprintItem(Properties properties) {
        super(properties);
    }

    public static int getBlueprintColor(ItemStack stack) {
        if (stack.getTag() != null) {
            String blueprintItem = stack.getOrCreateTag().getString("blueprintItem");

            if (blueprintItem.equals(ItemRegistry.MURASAMA.getKey().location().toString())) {
                return 0xE80000;
            } else if (blueprintItem.equals(ItemRegistry.GUN_SHEATH.getKey().location().toString())) {
                return 0xBDBDBD;
            } else if (blueprintItem.equals(ItemRegistry.ORIGINIUM_CATALYST.getKey().location().toString())) {
                return 0xFFA82D;
            } else if (blueprintItem.equals(ItemRegistry.DRAGON_SLAYER.getKey().location().toString())) {
                return 0x474747;
            }  else if (blueprintItem.equals(ItemRegistry.SHARP_IRONY.getKey().location().toString())) {
                return 0x4A5B7D;
            } else if (blueprintItem.equals(ItemRegistry.PUNISHER.getKey().location().toString())) {
                return 0x2FFFF8;
            }  else if (blueprintItem.equals(ItemRegistry.BLOODLETTER.getKey().location().toString())) {
                return 0xC57070D;
            }
        }
        return 0xFFFFFF;
    }

    public static List<String> getBlueprintIngredients(String blueprintItem) {
        List<String> ingredientList = new ArrayList<>();

        if (blueprintItem.equals(ItemRegistry.MURASAMA.getKey().location().toString())) {
            ingredientList.add(0, Items.ECHO_SHARD.getDescriptionId());
            ingredientList.add(1, Items.ECHO_SHARD.getDescriptionId());
            ingredientList.add(2, Items.ECHO_SHARD.getDescriptionId());
            ingredientList.add(3, Items.ECHO_SHARD.getDescriptionId());
            ingredientList.add(4, Items.REDSTONE_BLOCK.getDescriptionId());
            ingredientList.add(5, Items.REDSTONE_BLOCK.getDescriptionId());
            ingredientList.add(6, Items.IRON_BLOCK.getDescriptionId());
        } else if (blueprintItem.equals(ItemRegistry.GUN_SHEATH.getKey().location().toString())) {
            ingredientList.add(0, Items.IRON_BLOCK.getDescriptionId());
            ingredientList.add(1, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(2, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(3, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(4, Items.NETHERITE_INGOT.getDescriptionId());
            ingredientList.add(5, Items.FLINT_AND_STEEL.getDescriptionId());
            ingredientList.add(6, Items.TNT.getDescriptionId());
        } else if (blueprintItem.equals(ItemRegistry.ORIGINIUM_CATALYST.getKey().location().toString())) {
            ingredientList.add(0, Items.BLAZE_POWDER.getDescriptionId());
            ingredientList.add(1, Items.BLAZE_POWDER.getDescriptionId());
            ingredientList.add(2, Items.BLAZE_POWDER.getDescriptionId());
            ingredientList.add(3, Items.BLAZE_POWDER.getDescriptionId());
            ingredientList.add(4, Items.AMETHYST_SHARD.getDescriptionId());
            ingredientList.add(5, Items.AMETHYST_SHARD.getDescriptionId());
            ingredientList.add(6, Items.IRON_NUGGET.getDescriptionId());
            ingredientList.add(7, Items.IRON_NUGGET.getDescriptionId());
            ingredientList.add(8, Items.IRON_INGOT.getDescriptionId());
        } else if (blueprintItem.equals(ItemRegistry.DRAGON_SLAYER.getKey().location().toString())) {
            ingredientList.add(0, Items.IRON_BLOCK.getDescriptionId());
            ingredientList.add(1, Items.IRON_BLOCK.getDescriptionId());
            ingredientList.add(2, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(3, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(4, Items.OBSIDIAN.getDescriptionId());
            ingredientList.add(5, Items.OBSIDIAN.getDescriptionId());
            ingredientList.add(6, Items.NETHERITE_INGOT.getDescriptionId());
        }  else if (blueprintItem.equals(ItemRegistry.SHARP_IRONY.getKey().location().toString())) {
            ingredientList.add(0, Items.NETHERITE_SCRAP.getDescriptionId());
            ingredientList.add(1, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(2, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(3, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(4, ItemRegistry.METALLIC_FEATHER.get().getDescriptionId());
            ingredientList.add(5, ItemRegistry.METALLIC_FEATHER.get().getDescriptionId());
            ingredientList.add(6, ItemRegistry.METALLIC_FEATHER.get().getDescriptionId());
            ingredientList.add(7, ItemRegistry.METALLIC_FEATHER.get().getDescriptionId());
            ingredientList.add(8, ItemRegistry.METALLIC_FEATHER.get().getDescriptionId());
        } else if (blueprintItem.equals(ItemRegistry.PUNISHER.getKey().location().toString())) {
            ingredientList.add(0, Items.IRON_BLOCK.getDescriptionId());
            ingredientList.add(1, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(2, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(3, Items.DIAMOND_BLOCK.getDescriptionId());
            ingredientList.add(4, Items.FIREWORK_ROCKET.getDescriptionId());
            ingredientList.add(5, Items.LEVER.getDescriptionId());
        } else if (blueprintItem.equals(ItemRegistry.BLOODLETTER.getKey().location().toString())) {
            ingredientList.add(0, Items.LODESTONE.getDescriptionId());
            ingredientList.add(1, Items.ENCHANTED_BOOK.getDescriptionId());
            ingredientList.add(2, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(3, Items.IRON_INGOT.getDescriptionId());
            ingredientList.add(4, Items.REDSTONE.getDescriptionId());
            ingredientList.add(5, Items.REDSTONE.getDescriptionId());
        }
        return ingredientList;
    }

    @Override
    public String getItemRarity(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getOrCreateTag().getString("blueprintRarity");
        }
        return "none";
    }

    @Override
    public String getTriviaType() {
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

        MutableComponent blueprint_item_title = Component.translatable("tooltip.line.dropdown")
                .append(Component.translatable("item.armoryofdestiny.blueprint.item")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.translatable(BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(keyLocation)).getDescriptionId())
                        .withStyle(ChatFormatting.RED));
        components.add(blueprint_item_title);

        if (!isShift(level)) {
            MutableComponent blueprint_ingredients_collapsed = Component.translatable("tooltip.line.collapsed")
                    .append(Component.translatable("tooltip.line.ability.collapsed")
                            .withStyle(ChatFormatting.DARK_GRAY));
            components.add(blueprint_ingredients_collapsed);
        } else {
            MutableComponent blueprint_ingredients_expanded = Component.translatable("tooltip.line.expanded")
                    .append(Component.translatable("item.armoryofdestiny.blueprint.ingredients")
                            .withStyle(ChatFormatting.DARK_GRAY));
            components.add(blueprint_ingredients_expanded);

            for (int i = 0; ingredientList.size() > i; i++) {
                MutableComponent blueprint_ingredient = Component.translatable("tooltip.line.dropdown")
                        .append(Component.translatable(ingredientList.get(i))
                                .withStyle(ChatFormatting.GRAY));
                components.add(blueprint_ingredient);
            }
        }
    }
}
