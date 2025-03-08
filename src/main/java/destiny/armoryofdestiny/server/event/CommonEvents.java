package destiny.armoryofdestiny.server.event;

import destiny.armoryofdestiny.server.potion.NonexistenceEffect;
import destiny.armoryofdestiny.server.registry.EffectRegistry;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static destiny.armoryofdestiny.server.item.BloodletterItem.*;

public class CommonEvents {
    @SubscribeEvent
    public void playerInteract(PlayerInteractEvent event) {
        if (event.getEntity().hasEffect(EffectRegistry.NONEXISTENCE.get())) {
            event.getEntity().removeEffect(EffectRegistry.NONEXISTENCE.get());
            event.getEntity().addEffect(new MobEffectInstance(MobEffects.WITHER, 120, 0));
        }
    }

    @SubscribeEvent
    public void livingExpireEffect(MobEffectEvent.Expired event) {
        if (event.getEffectInstance().getEffect() instanceof NonexistenceEffect) {
            event.getEntity().addEffect(new MobEffectInstance(MobEffects.WITHER, 120, 0));
        }
    }
    @SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Pre event) {
        if (event.getEntity().hasEffect(EffectRegistry.NONEXISTENCE.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (event.getEntity().hasEffect(EffectRegistry.NONEXISTENCE.get())) {
            event.getEntity().removeEffect(EffectRegistry.NONEXISTENCE.get());
            event.getEntity().addEffect(new MobEffectInstance(MobEffects.WITHER, 120, 0));
        }

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
