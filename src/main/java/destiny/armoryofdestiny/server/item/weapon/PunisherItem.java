package destiny.armoryofdestiny.server.item.weapon;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import destiny.armoryofdestiny.client.render.item.PunisherItemRenderer;
import destiny.armoryofdestiny.server.item.utility.TooltipSwordItem;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import destiny.armoryofdestiny.server.registry.SoundRegistry;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Consumer;

import static destiny.armoryofdestiny.server.util.UtilityVariables.HAMMER;

public class PunisherItem extends TooltipSwordItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final String ANIMATION_TICK = "animationTick";
    public static final String STRIKES_REMAINING = "strikesRemaining";

    public static final UUID ABILITY_MULTIPLIER_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A23DB5CF");

    private float attackDamage;
    private double attackSpeed;
    private double attackKnockback;

    public float attackDamageAbility = 7;
    public double attackSpeedAbility = 0.8;
    public double attackKnockbackAbility = 2.8;

    public PunisherItem(Item.Properties build) {
        super(Tiers.NETHERITE, 0, 0, build);
        this.attackDamage = 17.0F;
        this.attackSpeed = -3.2F;
        this.attackKnockback = -1.2F;

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private PunisherItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new PunisherItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> PlayState.STOP));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            if (stack.getTag() != null) {
                if (stack.getOrCreateTag().getInt(STRIKES_REMAINING) > 0) {
                    builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ABILITY_MULTIPLIER_UUID, "Active ability", attackDamageAbility, AttributeModifier.Operation.ADDITION));
                    builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ABILITY_MULTIPLIER_UUID, "Active ability", attackSpeedAbility, AttributeModifier.Operation.ADDITION));
                    builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(ABILITY_MULTIPLIER_UUID, "Active ability", attackKnockbackAbility, AttributeModifier.Operation.ADDITION));
                }
            }
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.attackSpeed, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.attackKnockback, AttributeModifier.Operation.ADDITION));

            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getMainHandItem().getItem() == ItemRegistry.PUNISHER.get()) {
            if (!(stack.getOrCreateTag().getInt(STRIKES_REMAINING) > 0)) {
                stack.getOrCreateTag().putInt(STRIKES_REMAINING, 3);
                stack.getOrCreateTag().putInt(ANIMATION_TICK, 1);

                level.playSound(player, player.blockPosition(), SoundRegistry.PUNISHER_ACTIVATE.get(), SoundSource.PLAYERS, 1, 1);
            } else {
                return InteractionResultHolder.pass(stack);
            }
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity entity1, LivingEntity entity2) {
        stack.hurtAndBreak(1, entity2, (entity3) -> {
            entity3.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });

        if (stack.getTag() != null) {
            int strikesRemaining = stack.getOrCreateTag().getInt(STRIKES_REMAINING);

            if (strikesRemaining > 1) {
                entity1.playSound(SoundRegistry.PUNISHER_ATTACK.get(), 1, 1);
                stack.getOrCreateTag().putInt(STRIKES_REMAINING, strikesRemaining - 1);
            } else if (strikesRemaining == 1){
                entity1.playSound(SoundRegistry.PUNISHER_ATTACK.get(), 1, 1);
                entity1.playSound(SoundRegistry.PUNISHER_DEACTIVATE.get(), 1, 1);
                stack.getOrCreateTag().putInt(STRIKES_REMAINING, 0);
            }
        }
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {
        if (entity instanceof Player player) {
            if (stack.getTag() != null) {
                int strikesRemaining = stack.getOrCreateTag().getInt(STRIKES_REMAINING);
                int animationTick = stack.getOrCreateTag().getInt(ANIMATION_TICK);

                if (strikesRemaining > 0) {
                    if (animationTick > 0) {
                        stack.getOrCreateTag().putInt(ANIMATION_TICK, animationTick + 1);
                    }
                    if (animationTick == 8) {
                        stack.getOrCreateTag().putInt(ANIMATION_TICK, 1);
                    }
                } else if (animationTick > 0){
                    stack.getOrCreateTag().putInt(ANIMATION_TICK, 0);
                    player.getCooldowns().addCooldown(stack.getItem(), 200);
                }
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack stack1) {
        return stack1.is(Items.DIAMOND);
    }

    @Override
    public String getTriviaTranslatable() {
        return HAMMER;
    }
}
