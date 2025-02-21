package destiny.armoryofdestiny.item;

import destiny.armoryofdestiny.item.utility.TooltipItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlueprintItem extends TooltipItem {

    public BlueprintItem(Properties properties) {
        super(properties);
    }

    public static int getBlueprintColor(ItemStack stack) {
        if (stack.getTag() != null) {
            String blueprintItem = stack.getOrCreateTag().getString("blueprintItem");

            switch (blueprintItem) {
                case "murasama":
                    return 0xE80000;
                case "gun_sheath":
                    return 0xBDBDBD;
                case "dragon_slayer":
                    return 0x474747;
                case "originium_catalyst":
                    return 0xFFA82D;
                case "punisher":
                    return 0x2FFFF8;
                case "sharp_irony":
                    return 0x4A5B7D;
            }
        }
        return 0xFFFFFF;
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
        return "blueprint";
    }

    @Override
    public boolean hasAbilities() {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);

        MutableComponent blueprint_item_title = Component.translatable("tooltip.line.dropdown")
                .append(Component.translatable("item.armoryofdestiny.blueprint.item")
                        .withStyle(ChatFormatting.GRAY))
                .append(Component.translatable(stack.getOrCreateTag().getString("blueprintItem"))
                        .withStyle(ChatFormatting.RED)
                        .withStyle(ChatFormatting.UNDERLINE));
        components.add(blueprint_item_title);
    }
}
