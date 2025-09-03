package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.server.item.utility.TooltipBlockItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import static destiny.armoryofdestiny.server.util.UtilityVariables.HOT_ITEM;

public class HotBlockItem extends TooltipBlockItem {
    int damage = 2;
    int delay = 20;

    public HotBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {
        if (level.getGameTime() % delay == 0) {
            if (entity instanceof Player player && !player.isCreative()) {
                player.hurt(level.damageSources().hotFloor(), damage);
            }
        }
    }

    @Override
    public String getTriviaTranslatable() {
        return HOT_ITEM;
    }

    @Override
    public boolean hasAbilities() {
        return false;
    }
}
