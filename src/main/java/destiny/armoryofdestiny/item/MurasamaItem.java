package destiny.armoryofdestiny.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import destiny.armoryofdestiny.item.utility.ItemTiers;
import destiny.armoryofdestiny.item.utility.TooltipSwordItem;
import destiny.armoryofdestiny.registry.ItemRegistry;
import destiny.armoryofdestiny.registry.SoundRegistry;
import destiny.armoryofdestiny.client.render.item.MurasamaItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
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
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Consumer;

public class MurasamaItem extends TooltipSwordItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    boolean played = false;

    @OnlyIn(Dist.CLIENT)
    private float attackDamage;
    private double attackSpeed;
    private double attackKnockback;
    private int entityReach;

    public static final String ABILITY_TICK = "abilityTick";
    public static final UUID ABILITY_MULTIPLIER_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A23DB6CF");

    public float attackDamageAbility = 12;
    public float attackSpeedAbility = 3.6F;
    public float attackKnockbackAbility = 1.4F;

    public MurasamaItem(Item.Properties build) {
        super(ItemTiers.MURASAMA, 0, 0.0F, build);
        this.attackDamage = 11.0F;
        this.attackSpeed = -0.4F;
        this.attackKnockback = -2.6F;
        this.entityReach = 1;

        // Register our item as server-side handled.
        // This enables both animation data syncing and server-side animation triggering
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private MurasamaItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new MurasamaItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 20, state -> PlayState.STOP));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        //Sheathing mechanic
        if (player.getOffhandItem().getItem() == ItemRegistry.GUN_SHEATH.get()) {
            player.getOffhandItem().shrink(1);
            player.setItemInHand(hand, new ItemStack(ItemRegistry.MURASAMA_SHEATHED.get()));

            //Transfer all data from Murasama to Sheathed Murasama
            if (stack.getTag() != null) {
                player.getMainHandItem().getOrCreateTag().merge(stack.getTag());
            }
            //If player isn't creative, apply a cooldown
            if (!player.isCreative()) {
                player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 60);
            }
            level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.MURASAMA_SHEATH.get(), SoundSource.PLAYERS, 0.5F, 1, false);
            level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.MURASAMA_INSERT.get(), SoundSource.PLAYERS, 0.5F, 1, false);

            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            if (stack.getTag() != null) {
                if (stack.getOrCreateTag().getInt(ABILITY_TICK) > 0) {
                    builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ABILITY_MULTIPLIER_UUID, "Active ability", attackDamageAbility, AttributeModifier.Operation.ADDITION));
                    builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ABILITY_MULTIPLIER_UUID, "Active ability", attackSpeedAbility, AttributeModifier.Operation.ADDITION));
                    builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(ABILITY_MULTIPLIER_UUID, "Active ability", attackKnockbackAbility, AttributeModifier.Operation.ADDITION));

                    attackDamage = 11 + attackDamageAbility;
                    attackSpeed = -0.4F + attackSpeedAbility;
                    attackKnockback = -2.6F + attackKnockbackAbility;
                } else {
                    attackDamage = 11;
                    attackSpeed = -0.4F;
                    attackKnockback = -2.6F;
                }
            }

            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.attackSpeed, Operation.ADDITION));
            builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.attackKnockback, Operation.ADDITION));
            builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.entityReach, Operation.ADDITION));
            return builder.build();
        }  else {
            attackDamage = 11;
            attackSpeed = -0.4F;
            attackKnockback = -2.6F;
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity entity1, LivingEntity entity2) {
        stack.hurtAndBreak(1, entity2, (entity3) -> {
            entity3.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });

        //If powered-up strike happened, finish countdown
        if (stack.getTag() != null) {
            if (stack.getOrCreateTag().getInt(ABILITY_TICK) > 0) {
                entity1.playSound(SoundRegistry.MURASAMA_SPECIAL_HIT.get(), 0.5F, 1);
                stack.getOrCreateTag().putInt(ABILITY_TICK, 0);
            }
        }
        return true;
    }

    @Override
    public @NotNull AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity target) {
        //If powered up, the sweeping box is larger
        if (stack.getOrCreateTag().getInt(ABILITY_TICK) > 0) {
            return target.getBoundingBox().inflate(2.0D, 0.5D, 2.0D);
        } else {
            return target.getBoundingBox().inflate(1.0D, 0.25D, 1.0D);
        }
    }

    @Override
    public String getItemRarity(ItemStack stack) {
        return "legendary";
    }

    @Override
    public String getTriviaType() {
        return "katana";
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {
        if (entity instanceof Player player) {
            //If item selected, play select sound
/*            if (player.getMainHandItem() == stack) {
                if (!played) {
                    level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.MURASAMA_SELECT.get(), SoundSource.PLAYERS, 0.5F, 1, false);
                    played = true;
                }
            } else if (played) {
                played = false;
            }*/

            //Ability ticker
            if (stack.getTag() != null) {
                int abilityTick = stack.getOrCreateTag().getInt(ABILITY_TICK);
                if (abilityTick > 0 && abilityTick < 20) {
                    stack.getOrCreateTag().putInt(ABILITY_TICK, abilityTick + 1);
                } else if (abilityTick == 20) {
                    stack.getOrCreateTag().putInt(ABILITY_TICK, 0);
                }
            }
        }
    }
}