package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.server.item.utility.TooltipItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static destiny.armoryofdestiny.server.misc.UtilityVariables.ARMORERS_WORKSHOP_PART;
import static destiny.armoryofdestiny.server.misc.UtilityVariables.SMITHING_HAMMER;

public class SmithingHammerItem extends TooltipItem {
    public SmithingHammerItem(Properties properties) {
        super(properties);
    }

    @Override
    public String getTriviaType() {
        return SMITHING_HAMMER;
    }

    @Override
    public String getItemRarity(ItemStack stack) {
        return ARMORERS_WORKSHOP_PART;
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
