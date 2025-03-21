package destiny.armoryofdestiny.server.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import destiny.armoryofdestiny.client.render.item.DragonSlayerItemRenderer;
import destiny.armoryofdestiny.server.item.utility.TooltipSwordItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
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

import static destiny.armoryofdestiny.server.misc.UtilityVariables.GREATSWORD;
import static destiny.armoryofdestiny.server.misc.UtilityVariables.LEGENDARY;

public class DragonSlayerItem extends TooltipSwordItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final UUID ABILITY_MULTIPLIER_UUID = UUID.fromString("CB3F55D3-644C-4F38-A497-9C13A23DB6CF");

    private float attackDamage;
    private double attackSpeed;
    private double attackKnockback;
    private double entityReach;

    public float attackDamageAbility;
    public int dealtDamageCap = 10240;

    public DragonSlayerItem(Item.Properties build) {
        super(Tiers.NETHERITE, 0, 0, build);
        this.attackDamage = 13.0F;
        this.attackSpeed = -2.8F;
        this.attackKnockback = -0.8F;
        this.entityReach = 2;

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private DragonSlayerItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new DragonSlayerItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> PlayState.STOP)
        );
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            if (stack.getTag() != null) {
                int damageDealt = stack.getOrCreateTag().getInt("damageDealt");
                attackDamageAbility = Math.min(24, 24 * ((float) damageDealt / dealtDamageCap ));

                if (damageDealt > 0) {
                    builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ABILITY_MULTIPLIER_UUID, "Active ability", attackDamageAbility, AttributeModifier.Operation.ADDITION));
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
    public @NotNull AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity target) {
        return target.getBoundingBox().inflate(2.0D, 0.5D, 2.0D);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack stack1) {
        return stack1.is(Items.NETHERITE_INGOT);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public String getItemRarity(ItemStack stack) {
        return LEGENDARY;
    }

    @Override
    public String getTriviaType() {
        return GREATSWORD;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);

        if (isShift(level) && stack.getTag() != null) {
            MutableComponent damage_total = Component.translatable("tooltip.line.dropdown")
                    .append(Component.translatable("item.armoryofdestiny.dragon_slayer.ability.1.damage_total")
                            .withStyle(ChatFormatting.GRAY)).append(Component.literal("" + stack.getTag().getInt("damageDealt")).withStyle(ChatFormatting.DARK_GRAY));
            components.add(damage_total);
        }
    }
}
