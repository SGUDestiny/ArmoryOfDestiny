package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.server.item.utility.TooltipItem;
import net.minecraft.world.item.ItemStack;

import static destiny.armoryofdestiny.server.util.UtilityVariables.SECONDARY;

public class BloodVesselEmptyItem extends TooltipItem {
    public BloodVesselEmptyItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTrivia() {
        return false;
    }

    @Override
    public String getRarityTranslatable(ItemStack stack) {
        return SECONDARY;
    }

    @Override
    public boolean hasAbilities() {
        return false;
    }
}
