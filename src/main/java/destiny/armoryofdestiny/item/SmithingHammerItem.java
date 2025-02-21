package destiny.armoryofdestiny.item;

import destiny.armoryofdestiny.item.utility.TooltipItem;

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
}
