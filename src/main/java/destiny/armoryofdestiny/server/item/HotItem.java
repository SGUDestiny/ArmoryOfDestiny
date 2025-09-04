package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.server.item.utility.TooltipItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static destiny.armoryofdestiny.server.util.UtilityVariables.HOT_ITEM;

public class HotItem extends TooltipItem {
    int damage = 2;
    int delay = 20;

    public HotItem(Properties properties) {
        super(properties);
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

    @Override
    public Component getName(ItemStack p_41458_) {
        return Component.translatable(this.getDescriptionId(p_41458_)).withStyle(ChatFormatting.YELLOW);
    }
}
