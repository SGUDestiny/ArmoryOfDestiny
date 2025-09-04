package destiny.armoryofdestiny.server.item.weapon;

import destiny.armoryofdestiny.server.item.utility.TooltipItem;

public class BloodVesselEmptyItem extends TooltipItem {
    public BloodVesselEmptyItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTrivia() {
        return false;
    }

    @Override
    public boolean hasAbilities() {
        return false;
    }
}
