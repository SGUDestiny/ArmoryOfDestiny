package destiny.armoryofdestiny.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import destiny.armoryofdestiny.EntityRegistry;
import destiny.armoryofdestiny.SoundRegistry;
import destiny.armoryofdestiny.client.SharpIronyItemRenderer;
import destiny.armoryofdestiny.entity.MetallicFeatherEntity;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.ClientUtils;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class SharpIronyItem extends SwordItem implements GeoItem {
    private static final RawAnimation SHARP_IRONY_OPEN = RawAnimation.begin().thenPlay("sharp_irony.open");
    private static final RawAnimation SHARP_IRONY_CLOSE = RawAnimation.begin().thenPlay("sharp_irony.close");
    private static final RawAnimation SHARP_IRONY_THROW = RawAnimation.begin().thenPlay("sharp_irony.throw");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final String AMMO_COUNT = "feathers";
    public static final String IS_OPEN = "is_open";
    public static final String VALUES_SET = "values_set";

    private static Boolean firstLoad = true;

    public static final Predicate<ItemStack> IS_METALLIC_FEATHER = (stack) -> {
        return stack.getItem() == ItemRegistry.METALLIC_FEATHER.get();
    };

    private float attackDamage;
    private double attackSpeed;
    private float attackKnockback;

    public SharpIronyItem(Item.Properties build) {
        super(Tiers.NETHERITE, 0, 0.0F, build);
        this.attackDamage = 12;
        this.attackSpeed = -0.4F;
        this.attackKnockback = 6.0F;

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
        controllers.add(new AnimationController<>(this, "sharp_irony_controller", 0, state -> {
//            if(state.getData(INITIAL_ANIMATION_PLAYED) == null){
//                CompoundTag tag = state.getData(DataTickets.ITEMSTACK).getTag();
//
//                if (tag != null){
//                    boolean is_open = tag.getBoolean(IS_OPEN);
//
//                    if (is_open){
//                        state.getController().setAnimation(SHARP_IRONY_OPEN);
//                    }
//                    else {
//                        state.getController().setAnimation(SHARP_IRONY_CLOSE);
//                    }
//                    state.setData(INITIAL_ANIMATION_PLAYED, true);
//                    return PlayState.CONTINUE;
//                }
//            }
            return PlayState.STOP;
        })
                .triggerableAnim("open", SHARP_IRONY_OPEN)
                .triggerableAnim("close", SHARP_IRONY_CLOSE)
                .triggerableAnim("throw", SHARP_IRONY_THROW)
                // We've marked the "box_open" animation as being triggerable from the server
                .setSoundKeyframeHandler(state -> {
                    // Use helper method to avoid client-code in common class
                    Player player = ClientUtils.getClientPlayer();
                }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();

        if (player.isShiftKeyDown()) {
            if (!stack.getOrCreateTag().getBoolean(IS_OPEN)) {
                if (stack.getOrCreateTag().getInt(AMMO_COUNT) < 5) {
                    ItemStack feather_stack = findAmmo(player);
                    int feather_stack_amount = getAmmoCount(player);
                    int feather_stored = stack.getOrCreateTag().getInt(AMMO_COUNT);

                    if (!player.isCreative()) {
                        if (feather_stack_amount > 0) {
                            if (5 >= feather_stack_amount + feather_stored) {
                                feather_stack.shrink(feather_stack_amount);
                                stack.getOrCreateTag().putInt(AMMO_COUNT, feather_stored + feather_stack_amount);
                            } else if (5 < feather_stack_amount + feather_stored) {
                                feather_stack.shrink(5 - feather_stored);
                                stack.getOrCreateTag().putInt(AMMO_COUNT, feather_stored + 5 - feather_stored);
                            }
                        }
                    }
                    else {
                        stack.getOrCreateTag().putInt(AMMO_COUNT, 5);
                    }
                }
                stack.getOrCreateTag().putBoolean(IS_OPEN, true);

                if (level instanceof ServerLevel serverLevel) {
                    triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "sharp_irony_controller", "open");
                }
                level.playSound(player, player.blockPosition(), SoundRegistry.SHARP_IRONY_OPEN.get(), SoundSource.PLAYERS, 1, 1);

                player.getCooldowns().addCooldown(item, 5);
            } else {
                stack.getOrCreateTag().putBoolean(IS_OPEN, false);
                if (level instanceof ServerLevel serverLevel) {
                    triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "sharp_irony_controller", "close");
                }
                level.playSound(player, player.blockPosition(), SoundRegistry.SHARP_IRONY_CLOSE.get(), SoundSource.PLAYERS, 1, 1);

                player.getCooldowns().addCooldown(item, 5);
            }
        } else if (Screen.hasControlDown()) {
            if (stack.getOrCreateTag().getBoolean(IS_OPEN)){
                if (stack.getOrCreateTag().getInt(AMMO_COUNT) > 0) {
                    int ammo = stack.getOrCreateTag().getInt(AMMO_COUNT);

                    if (level instanceof ServerLevel serverLevel){
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "sharp_irony_controller", "throw");
                    }
                    level.playSound(player, player.blockPosition(), SoundRegistry.SHARP_IRONY_THROW.get(), SoundSource.PLAYERS, 1, 1);

                    float YRot;

                    if(ammo % 2 == 0) {
                        YRot = player.getYRot() - (float) (ammo * 5) / 2 + 2.5F;
                    } else {
                        YRot = player.getYRot() - (float) (ammo * 5) / 2;
                    }

                    for (int i = 0; i < ammo; i++){
                        if(!level.isClientSide) {
                            MetallicFeatherEntity feather = new MetallicFeatherEntity(EntityRegistry.METALLIC_FEATHER.get(), player, level);
                            feather.setDeltaMovement(0, 0, 1);
                            feather.shootFromRotation(player, player.getXRot(), YRot, 0.0F, 5.0F, 1.0F);
                            level.addFreshEntity(feather);
                            YRot += 5;
                        }
                        level.playSound(player, player.blockPosition(), SoundRegistry.SHARP_IRONY_THROW_SPECIAL.get(), SoundSource.PLAYERS, 1, 1);
                    }
                    player.getCooldowns().addCooldown(stack.getItem(), 30);
                    stack.getOrCreateTag().putInt(AMMO_COUNT, stack.getOrCreateTag().getInt(AMMO_COUNT) - ammo);

                    player.getCooldowns().addCooldown(item, 5);
                }
            }
        } else {
            if (stack.getOrCreateTag().getBoolean(IS_OPEN)){
                if (stack.getOrCreateTag().getInt(AMMO_COUNT) > 0) {
                    stack.getOrCreateTag().putInt(AMMO_COUNT, stack.getOrCreateTag().getInt(AMMO_COUNT) - 1);
                    if (level instanceof ServerLevel serverLevel) {
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "sharp_irony_controller", "throw");
                    }
                    level.playSound(player, player.blockPosition(), SoundRegistry.SHARP_IRONY_THROW.get(), SoundSource.PLAYERS, 1, 1);

                    if(!level.isClientSide) {
                        MetallicFeatherEntity feather = new MetallicFeatherEntity(EntityRegistry.METALLIC_FEATHER.get(), player, level);
                        feather.setDeltaMovement(0, 0, 1);
                        feather.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 5.0F, 1.0F);
                        level.addFreshEntity(feather);
                    }

                    player.getCooldowns().addCooldown(item, 5);
                } else {
                    ItemStack feather_stack = findAmmo(player);
                    int feather_stack_amount = getAmmoCount(player);
                    if (player.isCreative()){
                        stack.getOrCreateTag().putInt(AMMO_COUNT, 5);
                        if (level instanceof ServerLevel serverLevel) {
                            triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "sharp_irony_controller", "open");
                        }
                        level.playSound(player, player.blockPosition(), SoundRegistry.SHARP_IRONY_OPEN.get(), SoundSource.PLAYERS, 1, 1);
                    } else if(feather_stack_amount > 0) {
                        if (feather_stack_amount >= 5) {
                            feather_stack.shrink(5);
                            stack.getOrCreateTag().putInt(AMMO_COUNT, 5);
                        }
                        else {
                            feather_stack.shrink(feather_stack_amount);
                            stack.getOrCreateTag().putInt(AMMO_COUNT, feather_stack_amount);
                        }
                        if (level instanceof ServerLevel serverLevel) {
                            triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "sharp_irony_controller", "open");
                        }
                        level.playSound(player, player.blockPosition(), SoundRegistry.SHARP_IRONY_OPEN.get(), SoundSource.PLAYERS, 1, 1);

                        player.getCooldowns().addCooldown(item, 5);
                    }
                }
            }
        }
        return super.use(level, player, hand);
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

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if (stack.getOrCreateTag().getBoolean(IS_OPEN)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) (stack.getOrCreateTag().getInt(AMMO_COUNT) * 2.5);
    }

    @Override
    public int getBarColor(ItemStack p_150901_) {
        return Mth.hsvToRgb(0.0F, 0.0F, 90.0F);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.attackSpeed, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackKnockback, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }

        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {
        CompoundTag tag = stack.getTag();

        if (tag == null) {
            stack.getOrCreateTag().putBoolean(IS_OPEN, true);
            stack.getOrCreateTag().putInt(AMMO_COUNT, 5);
            stack.getOrCreateTag().putBoolean(VALUES_SET, true);
        }
        else if (!stack.getTag().getBoolean(VALUES_SET)){
            stack.getOrCreateTag().putBoolean(IS_OPEN, true);
            stack.getOrCreateTag().putInt(AMMO_COUNT, 5);
            stack.getOrCreateTag().putBoolean(VALUES_SET, true);
        }

        if (firstLoad){
            firstLoad = false;
            if (level instanceof ServerLevel serverLevel) {
                if (entity instanceof Player player) {
                    if (!stack.getOrCreateTag().getBoolean(IS_OPEN)) {
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "sharp_irony_controller", "close");
                    } else {
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "sharp_irony_controller", "open");
                    }
                }
            }
        }
        super.inventoryTick(stack, level, entity, i, b);
    }
}
