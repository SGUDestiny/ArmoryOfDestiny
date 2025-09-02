package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.server.item.utility.TooltipBlockItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class HotBlockItem extends TooltipBlockItem {
    int damage;
    int delay;

    public HotBlockItem(Block block, Properties properties, int damage, int delay) {
        super(block, properties);
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
