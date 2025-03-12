package destiny.armoryofdestiny.server.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import destiny.armoryofdestiny.client.render.item.CrucibleItemRenderer;
import destiny.armoryofdestiny.server.item.utility.TooltipSwordItem;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import destiny.armoryofdestiny.server.registry.SoundRegistry;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

import static destiny.armoryofdestiny.server.misc.UtilityVariables.GREATSWORD;
import static destiny.armoryofdestiny.server.misc.UtilityVariables.LEGENDARY;

public class CrucibleItem extends TooltipSwordItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final String USAGES = "usages";

    private float attackDamage;
    private double attackSpeed;
    private double attackKnockback;
    private double entityReach;

    public CrucibleItem(Item.Properties build) {
        super(Tiers.NETHERITE, 0, 0, build);
        this.attackDamage = 29.0F;
        this.attackSpeed = -2.5F;
        this.attackKnockback = -1.5F;
        this.entityReach = 1;

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.attackSpeed, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.attackKnockback, AttributeModifier.Operation.ADDITION));
            builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.entityReach, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private CrucibleItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new CrucibleItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        ItemStack newStack = new ItemStack(ItemRegistry.CRUCIBLE_INACTIVE.get());

        if (stack.getTag() != null) {
            newStack.getOrCreateTag().merge(stack.getTag());
        }

        player.setItemInHand(hand, newStack);

        level.playSound(player, player.blockPosition(), SoundRegistry.CRUCIBLE_DEACTIVATE.get(), SoundSource.PLAYERS, 1, 1);

        player.getCooldowns().addCooldown(newStack.getItem(), 20);

        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity entity1, LivingEntity entity2) {
        if (stack.getTag() != null) {
            int usages = stack.getTag().getInt(USAGES);

            if (3 >= usages && usages > 1) {
                stack.hurtAndBreak(1, entity2, (entity3) -> {
                    entity3.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                });

                stack.getOrCreateTag().putInt(USAGES, usages - 1);

                entity1.playSound(SoundRegistry.CRUCIBLE_SWING.get(), 1F, 1);

                return true;
            } else if (usages == 1) {
                stack.hurtAndBreak(1, entity2, (entity3) -> {
                    entity3.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                });

                if (entity2 instanceof Player player) {
                    ItemStack newStack = new ItemStack(ItemRegistry.CRUCIBLE_INACTIVE.get(), 1);

                    if (stack.getTag() != null) {
                        newStack.getOrCreateTag().merge(stack.getTag());
                    }

                    player.setItemInHand(InteractionHand.MAIN_HAND, newStack);

                    player.getCooldowns().addCooldown(newStack.getItem(), 100);
                }

                entity1.playSound(SoundRegistry.CRUCIBLE_SWING.get(), 1F, 1);
                entity1.playSound(SoundRegistry.CRUCIBLE_DEACTIVATE.get(), 1F, 1);

                return true;
            }
        }
        return false;
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
    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb(0F, 1F, 0.9F);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getTag().getInt(USAGES) > 0;
        }
        return false;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if (stack.getTag() != null) {
            int usages = stack.getTag().getInt(USAGES);

            return Math.round((float) usages / 3 * 13.0F);
        }
        return 0;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> PlayState.STOP)
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
