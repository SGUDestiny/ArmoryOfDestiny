package destiny.armoryofdestiny.server.event;

import destiny.armoryofdestiny.server.registry.ItemRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonEvents {
    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof Player player) {
            ItemStack stack = player.getMainHandItem();

            if (stack.getItem() == ItemRegistry.DRAGON_SLAYER.get()) {
                if (stack.getTag() != null) {
                    float damageStored = stack.getOrCreateTag().getInt("damageDealt");

                    stack.getOrCreateTag().putInt("damageDealt", (int) (damageStored + event.getAmount()));
                } else {
                    stack.getOrCreateTag().putInt("damageDealt", (int) event.getAmount());
                }
            }
        }
    }

    @SubscribeEvent
    public void onColorRegistration(RegisterColorHandlersEvent.Item event) {

    }
}
