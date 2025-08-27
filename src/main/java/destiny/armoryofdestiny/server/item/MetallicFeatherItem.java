package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.server.entity.MetallicFeatherEntity;
import destiny.armoryofdestiny.server.item.utility.TooltipItem;
import destiny.armoryofdestiny.server.registry.SoundRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static destiny.armoryofdestiny.server.util.UtilityVariables.SECONDARY;

public class MetallicFeatherItem extends TooltipItem {
    public MetallicFeatherItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof MetallicFeatherItem) {
            if (stack.getCount() > 0) {
                if (!level.isClientSide) {
                    MetallicFeatherEntity feather = new MetallicFeatherEntity(level, player);
                    feather.setDeltaMovement(0, 0, 1);
                    feather.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 5.0F, 1.0F);
                    level.addFreshEntity(feather);
                }
                if (!player.isCreative()) {
                    player.hurt(player.damageSources().cactus(), 2);
                    player.invulnerableTime = 0;
                    stack.shrink(1);
                    player.getCooldowns().addCooldown(stack.getItem(), 5);
                }
                level.playSound(player, player.blockPosition(), SoundRegistry.SHARP_IRONY_THROW.get(), SoundSource.PLAYERS, 1, 1);

                return InteractionResultHolder.success(stack);
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public String getRarityTranslatable(ItemStack stack) {
        return SECONDARY;
    }
}
