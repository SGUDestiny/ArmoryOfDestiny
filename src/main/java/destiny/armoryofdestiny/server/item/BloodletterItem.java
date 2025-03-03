package destiny.armoryofdestiny.server.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import destiny.armoryofdestiny.client.render.item.BloodletterItemRenderer;
import destiny.armoryofdestiny.server.item.utility.TooltipAxeItem;
import destiny.armoryofdestiny.server.item.utility.TooltipSwordItem;
import destiny.armoryofdestiny.server.registry.SoundRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static destiny.armoryofdestiny.server.misc.UtilityVariables.*;

public class BloodletterItem extends TooltipAxeItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final String ABILITY_TICK = "abilityTick";
    public static final String STORED_BLOOD = "storedBlood";
    public static final int maxBlood = 200;
    public static final int bloodPerTick = maxBlood / 20;
    public static final int bloodPerHit = maxBlood / 10;
    public static final int frameAmount = 5;
    public static final int frameDuration = 4;

    public static final UUID ABILITY_MULTIPLIER_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C23A23DB6CF");

    @OnlyIn(Dist.CLIENT)
    private float attackDamage;
    private double attackSpeed;
    private double attackKnockback;
    private double entityReach;

    public float attackDamageAbility = 16;
    public float attackSpeedAbility = 1.5F;
    public float attackKnockbackAbility = 1.0F;

    public BloodletterItem(Item.Properties build) {
        super(Tiers.NETHERITE, 0, 0, build);
        this.attackDamage = 7.0F;
        this.attackSpeed = 0F;
        this.attackKnockback = -0.5F;
        this.entityReach = 2;

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BloodletterItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new BloodletterItemRenderer();

                return this.renderer;
            }
        });
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> PlayState.STOP));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity entity1, LivingEntity entity2) {
        stack.hurtAndBreak(1, entity2, (entity3) -> {
            entity3.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });

        entity1.invulnerableTime = 0;

        if (stack.getTag() != null) {
            int abilityTick = stack.getTag().getInt(ABILITY_TICK);
            int storedBlood = stack.getTag().getInt(STORED_BLOOD);

            if (abilityTick > 0) {
                entity1.playSound(SoundRegistry.BLOODLETTER_HIT.get(), 1, 1);
                stack.getOrCreateTag().putInt(STORED_BLOOD, Math.max(0, storedBlood - bloodPerHit));
            }
        }
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getTag() != null) {
            int storedBlood = stack.getTag().getInt(STORED_BLOOD);
            int abilityTick = stack.getTag().getInt(ABILITY_TICK);

            //Activation
            if (storedBlood > 0 && abilityTick == 0) {
                stack.getOrCreateTag().putInt(ABILITY_TICK, 1);

                player.playSound(SoundRegistry.BLOODLETTER_ACTIVATE.get(), 1, 1);

                player.getCooldowns().addCooldown(stack.getItem(), 180);

                return InteractionResultHolder.success(stack);
                //Deactivation
            } else if (abilityTick > 0) {
                stack.getOrCreateTag().putInt(ABILITY_TICK, 0);

                player.playSound(SoundRegistry.BLOODLETTER_DEACTIVATE.get(), 1, 1);

                player.getCooldowns().addCooldown(stack.getItem(), 180);

                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            if (stack.getTag() != null) {
                if (stack.getOrCreateTag().getInt(ABILITY_TICK) > 0) {
                    builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ABILITY_MULTIPLIER_UUID, "Active ability", attackDamageAbility, AttributeModifier.Operation.ADDITION));
                    builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ABILITY_MULTIPLIER_UUID, "Active ability", attackSpeedAbility, AttributeModifier.Operation.ADDITION));
                    builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(ABILITY_MULTIPLIER_UUID, "Active ability", attackKnockbackAbility, AttributeModifier.Operation.ADDITION));
                }
            }
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.attackSpeed, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.attackKnockback, AttributeModifier.Operation.ADDITION));
            builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.entityReach, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack stack1) {
        return stack1.is(Items.NETHERITE_INGOT);
    }

    @Override
    public String getItemRarity(ItemStack stack) {
        return LEGENDARY;
    }

    @Override
    public String getTriviaType() {
        return RAPIER;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb(0F, 0.8F, 0.9F);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getTag().getInt(STORED_BLOOD) > 0;
        }
        return false;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if (stack.getTag() != null) {
            int storedBlood = stack.getTag().getInt(STORED_BLOOD);

            return Math.round((float) storedBlood / maxBlood * 13.0F);
        }
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);

        if (isShift(level) && stack.getTag() != null) {
            MutableComponent damage_total = Component.translatable("tooltip.line.dropdown")
                    .append(Component.translatable("item.armoryofdestiny.bloodletter.ability.1.stored_blood")
                            .withStyle(ChatFormatting.GRAY)).append(Component.literal("" + stack.getTag().getInt(STORED_BLOOD)).withStyle(ChatFormatting.DARK_GRAY));
            components.add(damage_total);
        }
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {
        if (entity instanceof Player player) {
            //Ability ticker
            if (stack.getTag() != null) {
                int abilityTick = stack.getOrCreateTag().getInt(ABILITY_TICK);
                int storedBlood = stack.getOrCreateTag().getInt(STORED_BLOOD);
                int frameTime = frameAmount * frameDuration;

                if (storedBlood > 0) {
                    if (abilityTick > 0 && abilityTick < frameTime) {
                        stack.getOrCreateTag().putInt(ABILITY_TICK, abilityTick + 1);
                    } else if (abilityTick == frameTime) {
                        stack.getOrCreateTag().putInt(ABILITY_TICK, 1);
                        stack.getOrCreateTag().putInt(STORED_BLOOD, Math.max(0, storedBlood - bloodPerTick));
                    }
                } else if (abilityTick != 0) {
                    stack.getOrCreateTag().putInt(ABILITY_TICK, 0);

                    player.playSound(SoundRegistry.BLOODLETTER_DEACTIVATE.get(), 1, 1);
                }
            }
        }
    }
}
