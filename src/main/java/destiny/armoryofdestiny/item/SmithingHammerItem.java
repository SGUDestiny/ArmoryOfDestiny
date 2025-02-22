package destiny.armoryofdestiny.item;

import destiny.armoryofdestiny.item.utility.TooltipItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SmithingHammerItem extends TooltipItem {
    public SmithingHammerItem(Properties properties) {
        super(properties);
    }

    @Override
    public String getTriviaType() {
        return "smithing_hammer";
    }

    @Override
    public boolean hasRarity() {
        return false;
    }

    @Override
    public boolean hasAbilities() {
        return false;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack stack1) {
        return stack1.is(Items.IRON_INGOT);
    }
}
