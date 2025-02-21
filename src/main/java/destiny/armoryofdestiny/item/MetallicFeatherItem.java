package destiny.armoryofdestiny.item;

import destiny.armoryofdestiny.item.utility.TooltipItem;
import net.minecraft.world.item.ItemStack;

public class MetallicFeatherItem extends TooltipItem {
    public MetallicFeatherItem(Properties properties) {
        super(properties);
    }

    @Override
    public String getItemRarity(ItemStack stack) {
        return "secondary";
    }
}
