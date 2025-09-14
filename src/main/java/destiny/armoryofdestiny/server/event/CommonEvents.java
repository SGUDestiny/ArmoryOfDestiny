package destiny.armoryofdestiny.server.event;

import destiny.armoryofdestiny.server.potion.NonexistenceEffect;
import destiny.armoryofdestiny.server.registry.EffectRegistry;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import destiny.armoryofdestiny.server.registry.SoundRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.VanillaGameEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static destiny.armoryofdestiny.server.item.weapon.BloodletterItem.*;
import static destiny.armoryofdestiny.server.item.weapon.DragonSlayerItem.DAMAGE_DEALT;

public class CommonEvents {
    //So sculk sensors don't detect players with effect
    @SubscribeEvent
    public void vanillaGameEvent(VanillaGameEvent event) {
        if (event.getCause() instanceof Player player && player.hasEffect(EffectRegistry.NONEXISTENCE.get())) {
            event.setCanceled(true);
        }
    }

    //If player has effect, prevent mob targeting
    @SubscribeEvent
    public void livingChangeTarget(LivingChangeTargetEvent event) {
        if (event.getNewTarget() != null && event.getOriginalTarget().hasEffect(EffectRegistry.NONEXISTENCE.get())) {
            event.setCanceled(true);
        }
    }

    //If mob has player with effect targeted, remove target
    @SubscribeEvent
    public void livingTick(LivingEvent.LivingTickEvent event) {
        if (!event.getEntity().level().isClientSide) {
            if (event.getEntity() instanceof Mob mob) {
                if (mob.getTarget() instanceof Player player && player.hasEffect(EffectRegistry.NONEXISTENCE.get())) {
                    mob.setTarget(null);
                }
            }
        }
    }

    //When effect ends, apply withering
    @SubscribeEvent
    public void livingExpireEffect(MobEffectEvent.Expired event) {
        if (event.getEffectInstance().getEffect() instanceof NonexistenceEffect) {
            Level level = event.getEntity().level();
            LivingEntity entity = event.getEntity();

            event.getEntity().addEffect(new MobEffectInstance(MobEffects.WITHER, 120, 0));
            level.playSound(null, entity.blockPosition(), SoundRegistry.EDGE_OF_EXISTENCE_DEACTIVATE.get(), SoundSource.PLAYERS, 1, 1);
        }
    }

    //If player interacts, remove effect
    @SubscribeEvent
    public void playerInteract(PlayerInteractEvent event) {
        if (!event.getLevel().getBlockState(event.getPos()).isAir() && event.getEntity().hasEffect(EffectRegistry.NONEXISTENCE.get())) {
            Level level = event.getEntity().level();
            LivingEntity entity = event.getEntity();

            event.getEntity().addEffect(new MobEffectInstance(MobEffects.WITHER, 120, 0));
            entity.removeEffect(EffectRegistry.NONEXISTENCE.get());
            level.playSound(null, entity.blockPosition(), SoundRegistry.EDGE_OF_EXISTENCE_DEACTIVATE.get(), SoundSource.PLAYERS, 1, 1);
        }
    }

    //If player attacks an entity, remove effect and give withering to entity
    @SubscribeEvent
    public void attackEntity(AttackEntityEvent event) {
        Level level = event.getEntity().level();
        LivingEntity attacker = event.getEntity();
        Entity attacked = event.getTarget();

        if (attacker.hasEffect(EffectRegistry.NONEXISTENCE.get())) {
            if (attacked instanceof LivingEntity target) {
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, 240, 1));
                attacker.removeEffect(EffectRegistry.NONEXISTENCE.get());
                level.playSound(null, attacker.blockPosition(), SoundRegistry.EDGE_OF_EXISTENCE_DEACTIVATE.get(), SoundSource.PLAYERS, 1, 1);
            }
        }

        //If entity has effect, negate damage
        if (attacked instanceof LivingEntity target) {
            if (target.hasEffect(EffectRegistry.NONEXISTENCE.get())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        cancelNonExistenceOnHit(event);

        DamageSource source = event.getSource();
        if (source.getEntity() instanceof Player player) {
            ItemStack stack = player.getMainHandItem();

            dragonSlayerDamageEvent(event, stack);

            bloodletterFillVesselsOnHit(stack, source, player);

            fillBloodletterOnHit(event, player);
        }
    }

    private void cancelNonExistenceOnHit(LivingHurtEvent event) {
        if (event.getEntity().hasEffect(EffectRegistry.NONEXISTENCE.get())) {
            event.getEntity().removeEffect(EffectRegistry.NONEXISTENCE.get());
            event.getEntity().addEffect(new MobEffectInstance(MobEffects.WITHER, 120, 0));
        }
    }

    private void dragonSlayerDamageEvent(LivingHurtEvent event, ItemStack stack) {
        if (stack.getItem() == ItemRegistry.DRAGON_SLAYER.get()) {
            if (stack.getTag() != null) {
                float damageStored = stack.getOrCreateTag().getFloat(DAMAGE_DEALT);
                stack.getOrCreateTag().putFloat(DAMAGE_DEALT, damageStored + event.getAmount());
            } else {
                stack.getOrCreateTag().putFloat(DAMAGE_DEALT, event.getAmount());
            }
        }
    }

    private void bloodletterFillVesselsOnHit(ItemStack stack, DamageSource source, Player player) {
        if (stack.getItem() == ItemRegistry.BLOODLETTER.get()) {
            Level level = source.getEntity().level();
            ItemStack emptyVessels = null;

            //Check for empty blood vessels
            for (ItemStack thisInventoryStack : player.getInventory().items) {
                if (thisInventoryStack != null && thisInventoryStack.getItem() == ItemRegistry.BLOOD_VESSEL_EMPTY.get()) {
                    emptyVessels = thisInventoryStack;
                    break;
                }
            }

            //Roll vessel fill chance
            if (emptyVessels != null) {
                if (level.random.nextFloat() > 0.7) {
                    player.addItem(new ItemStack(ItemRegistry.BLOOD_VESSEL_FULL.get(), 1));
                    emptyVessels.shrink(1);
                }
            }
        }
    }

    private void fillBloodletterOnHit(LivingHurtEvent event, Player player) {
        //Scan inventory for bloodletter
        ItemStack bloodletter = null;
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack != null && itemStack.getItem() == ItemRegistry.BLOODLETTER.get()) {
                bloodletter = itemStack;
                break;
            }
        }

        //If found, fill bloodletter
        if (bloodletter != null) {
            if (bloodletter.getTag() != null) {
                if (0 >= bloodletter.getTag().getInt(ABILITY_TICK)) {
                    float damageStored = bloodletter.getOrCreateTag().getInt(STORED_BLOOD);
                    bloodletter.getOrCreateTag().putInt(STORED_BLOOD, (int) Math.min(maxBlood, (damageStored + event.getAmount())));
                }
            } else {
                bloodletter.getOrCreateTag().putInt(STORED_BLOOD, (int) Math.min(maxBlood, event.getAmount()));
            }
        }
    }
}
