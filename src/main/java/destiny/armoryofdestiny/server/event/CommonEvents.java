package destiny.armoryofdestiny.server.event;

import destiny.armoryofdestiny.server.registry.ItemRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static destiny.armoryofdestiny.server.item.BloodletterItem.*;

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
            } else if (stack.getItem() == ItemRegistry.BLOODLETTER.get()) {
                //Vessel logic
                Level level = source.getEntity().level();
                ItemStack emptyVessels = null;

                for (ItemStack itemStack : player.getInventory().items) {
                    if (itemStack != null && itemStack.getItem() == ItemRegistry.BLOOD_VESSEL_EMPTY.get()) {
                        emptyVessels = itemStack;
                        break;
                    }
                }

                if (emptyVessels != null) {
                    if (level.random.nextFloat() > 0.7) {
                        player.addItem(new ItemStack(ItemRegistry.BLOOD_VESSEL_FULL.get(), 1));
                        emptyVessels.shrink(1);
                    }
                }
            }

            //Bloodletter
            ItemStack bloodletter = null;
            for (ItemStack itemStack : player.getInventory().items) {
                if (itemStack != null && itemStack.getItem() == ItemRegistry.BLOODLETTER.get()) {
                    bloodletter = itemStack;
                    break;
                }
            }

            if (bloodletter != null) {
                if (bloodletter.getTag() != null) {
                    if (0 >= bloodletter.getTag().getInt(ABILITY_TICK)) {
                        float damageStored = bloodletter.getOrCreateTag().getInt("storedBlood");

                        bloodletter.getOrCreateTag().putInt("storedBlood", (int) Math.min(maxBlood, (damageStored + event.getAmount())));
                    }
                } else {
                    bloodletter.getOrCreateTag().putInt("storedBlood", (int) Math.min(maxBlood, event.getAmount()));
                }
            }
        }
    }
}
