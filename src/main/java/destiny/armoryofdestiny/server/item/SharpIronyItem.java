package destiny.armoryofdestiny.server.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import destiny.armoryofdestiny.server.item.utility.TooltipSwordItem;
import destiny.armoryofdestiny.server.registry.EntityRegistry;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import destiny.armoryofdestiny.server.registry.SoundRegistry;
import destiny.armoryofdestiny.client.render.item.SharpIronyItemRenderer;
import destiny.armoryofdestiny.server.entity.MetallicFeatherEntity;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static destiny.armoryofdestiny.server.misc.UtilityVariables.THROWING_FAN;
import static destiny.armoryofdestiny.server.misc.UtilityVariables.UNIQUE;

public class SharpIronyItem extends TooltipSwordItem implements GeoItem {
    private static final RawAnimation SHARP_IRONY_OPEN = RawAnimation.begin().thenPlay("sharp_irony.open");
    private static final RawAnimation SHARP_IRONY_CLOSE = RawAnimation.begin().thenPlay("sharp_irony.close");
    private static final RawAnimation SHARP_IRONY_THROW = RawAnimation.begin().thenPlay("sharp_irony.throw");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final String AMMO_COUNT = "ammoCount";
    public static final String IS_OPEN = "isOpen";

    private static Boolean firstLoad = true;

    public static final Predicate<ItemStack> IS_METALLIC_FEATHER = (stack) -> {
        return stack.getItem() == ItemRegistry.METALLIC_FEATHER.get();
    };

    private float attackDamage;
    private double attackSpeed;

    public SharpIronyItem(Item.Properties build) {
        super(Tiers.NETHERITE, 0, 0.0F, build);
        this.attackDamage = 5;
        this.attackSpeed = -0.4F;

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private SharpIronyItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new SharpIronyItemRenderer();

                return this.renderer;
            }
        });
    }

    //public static final DataTicket<Boolean> INITIAL_ANIMATION_PLAYED = new DataTicket<>("initial_animation_played", Boolean.class);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "sharp_irony_controller", 0, state -> {return PlayState.STOP;})
                .triggerableAnim("open", SHARP_IRONY_OPEN)
                .triggerableAnim("close", SHARP_IRONY_CLOSE)
                .triggerableAnim("throw", SHARP_IRONY_THROW)
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();

        if (stack.getTag() == null) {
            stack.getOrCreateTag().putBoolean(IS_OPEN, true);
            stack.getOrCreateTag().putInt(AMMO_COUNT, 5);
        }

        boolean isOpen = stack.getOrCreateTag().getBoolean(IS_OPEN);
        int ammoCount = getFanAmmo(stack);

        if (isShift(player)) {
            if (isOpen) {
                closeFan(level, player, stack, item);
            } else {
                if (ammoCount < 5) {
                    reload(level, player, stack, item);
                } else {
                    openFan(level, player, stack, item);
                }
            }
        } else if (isControl(level)) {
            if (isOpen){
                throwAll(level, player, stack, item);
            }
        } else {
            if (isOpen) {
                throwFeather(level, player, stack, item, player.getXRot(), player.getYRot());
            } else {
                if (ammoCount < 5) {
                    reload(level, player, stack, item);
                } else {
                    openFan(level, player, stack, item);
                }
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    public void throwFeather(Level level, Player player, ItemStack stack, Item item, float XRot, float YRot) {
        int ammoCount = stack.getOrCreateTag().getInt(AMMO_COUNT);

        if (ammoCount > 0) {
            if (!level.isClientSide) {
                MetallicFeatherEntity feather = new MetallicFeatherEntity(level, player);
                feather.shootFromRotation(player, XRot, YRot, 0.0F, 5.0F, 1.0F);
                level.addFreshEntity(feather);
            }
            stack.getOrCreateTag().putInt(AMMO_COUNT, ammoCount - 1);

            if (!player.isCreative()) {
                stack.setDamageValue(stack.getDamageValue() + 1);
            }

            triggerAnim(level, player, stack, "sharp_irony_controller", "throw");
            level.playSound(player, player.blockPosition(), SoundRegistry.SHARP_IRONY_THROW.get(), SoundSource.PLAYERS, 1, 1);

            player.getCooldowns().addCooldown(item, 5);
        } else {
            reload(level, player, stack, item);
        }
    }

    public void throwAll(Level level, Player player, ItemStack stack, Item item) {
        int ammoCount = stack.getOrCreateTag().getInt(AMMO_COUNT);

        if (ammoCount > 0) {
            float YRot;

            if(ammoCount % 2 == 0) {
                YRot = player.getYRot() - (float) (ammoCount * 5) / 2 + 2.5F;
            } else {
                YRot = player.getYRot() - (float) (ammoCount * 5) / 2;
            }

            for (int i = 0; i < ammoCount; i++){
                throwFeather(level, player, stack, item, player.getXRot(), YRot);
                YRot += 5;

                if (!player.isCreative()) {
                    stack.setDamageValue(stack.getDamageValue() + 1);
                }
            }
            stack.getOrCreateTag().putInt(AMMO_COUNT, 0);

            level.playSound(player, player.blockPosition(), SoundRegistry.SHARP_IRONY_THROW_SPECIAL.get(), SoundSource.PLAYERS, 1, 1);
            triggerAnim(level, player, stack, "sharp_irony_controller", "throw");

            player.getCooldowns().addCooldown(item, 30);
        }
    }

    public void reload(Level level, Player player, ItemStack stack, Item item) {
        int ammoCount = getFanAmmo(stack);

        if (ammoCount < 5) {
            ItemStack feather_stack = findAmmo(player);
            int feather_stack_amount = getAmmoCount(player);

            if (!player.isCreative()) {
                if (feather_stack_amount > 0) {
                    if (5 >= feather_stack_amount + ammoCount) {
                        feather_stack.shrink(feather_stack_amount);
                        stack.getOrCreateTag().putInt(AMMO_COUNT, ammoCount + feather_stack_amount);
                    } else if (5 < feather_stack_amount + ammoCount) {
                        feather_stack.shrink(5 - ammoCount);
                        stack.getOrCreateTag().putInt(AMMO_COUNT, ammoCount + 5 - ammoCount);
                    }
                }
            }
            else {
                stack.getOrCreateTag().putInt(AMMO_COUNT, 5);
            }

            openFan(level, player, stack, item);
        }
    }

    public int getFanAmmo(ItemStack stack) {
        return stack.getOrCreateTag().getInt(AMMO_COUNT);
    }

    public void openFan(Level level, Player player, ItemStack stack, Item item) {
        stack.getOrCreateTag().putBoolean(IS_OPEN, true);

        triggerAnim(level, player, stack, "sharp_irony_controller", "open");
        level.playSound(player, player.blockPosition(), SoundRegistry.SHARP_IRONY_OPEN.get(), SoundSource.PLAYERS, 1, 1);

        player.getCooldowns().addCooldown(item, 5);
    }

    public void closeFan(Level level, Player player, ItemStack stack, Item item) {
        stack.getOrCreateTag().putBoolean(IS_OPEN, false);

        triggerAnim(level, player, stack, "sharp_irony_controller", "close");
        level.playSound(player, player.blockPosition(), SoundRegistry.SHARP_IRONY_CLOSE.get(), SoundSource.PLAYERS, 1, 1);

        player.getCooldowns().addCooldown(item, 5);
    }

    public ItemStack findAmmo(Player player){
        if (player.isCreative()){
            return ItemStack.EMPTY;
        }
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i){
            ItemStack ammo_item = player.getInventory().getItem(i);
            if (IS_METALLIC_FEATHER.test(ammo_item)) {
                return ammo_item;
            }
        }
        return ItemStack.EMPTY;
    }

    public int getAmmoCount(Player player){
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i){
            ItemStack ammo_item = player.getInventory().getItem(i);
            int ammo_number = player.getInventory().getItem(i).getCount();
            if (IS_METALLIC_FEATHER.test(ammo_item)) {
                return ammo_number;
            }
        }
        return 0;
    }

    public void triggerAnim(Level level, Player player, ItemStack stack, String controller, String name) {
        if (level instanceof ServerLevel serverLevel) {
            triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), controller, name);
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.attackSpeed, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }

        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack stack1) {
        return stack1.is(Items.NETHERITE_SCRAP);
    }

    @Override
    public String getItemRarity(ItemStack stack) {
        return UNIQUE;
    }

    @Override
    public String getTriviaType() {
        return THROWING_FAN;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (stack.getTag() != null) {
            if (selected) {
                if (entity instanceof Player player) {
                    if (stack.getOrCreateTag().getBoolean(IS_OPEN)) {
                        triggerAnim(level, player, stack, "sharp_irony_controller", "open");
                    } else {
                        triggerAnim(level, player, stack, "sharp_irony_controller", "close");
                    }
                }
            }
        }
    }
}
