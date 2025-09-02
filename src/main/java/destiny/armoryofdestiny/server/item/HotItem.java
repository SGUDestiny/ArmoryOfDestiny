package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.server.item.utility.TooltipItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HotItem extends TooltipItem {
    int damage;
    int delay;

    public HotItem(Properties properties, int damage, int delay) {
        super(properties);
        this.damage = damage;
        this.delay = delay;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {
        if (level.getGameTime() % delay == 0) {
            if (entity instanceof Player player && !player.isCreative()) {
                player.hurt(level.damageSources().hotFloor(), damage);
            }
        }
    }
}
