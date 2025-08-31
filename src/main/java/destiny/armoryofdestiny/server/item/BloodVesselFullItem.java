package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.server.item.utility.TooltipItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BloodVesselFullItem extends TooltipItem {
    public BloodVesselFullItem(Properties build) {
        super(build);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        float maxHealth = player.getMaxHealth();
        float currentHealth = player.getHealth();
        float healAmount = (maxHealth - currentHealth) / 2;

        player.getCooldowns().addCooldown(stack.getItem(), 20);

        player.heal(healAmount);
        stack.shrink(1);

        level.playSound(player, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1, 1.15F);

        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean hasTrivia() {
        return false;
    }
}
