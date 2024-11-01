package destiny.armoryofdestiny.item;

import destiny.armoryofdestiny.EntityRegistry;
import destiny.armoryofdestiny.SoundRegistry;
import destiny.armoryofdestiny.client.DoubleTroubleItemRenderer;
import destiny.armoryofdestiny.entity.PelletEntity;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DoubleTroubleItem extends Item implements GeoItem {

    private static final RawAnimation DOUBLE_TROUBLE_SHOOT_LEFT = RawAnimation.begin().thenPlay("double_trouble.shoot_left");
    private static final RawAnimation DOUBLE_TROUBLE_SHOOT_RIGHT = RawAnimation.begin().thenPlay("double_trouble.shoot_right");
    private static final RawAnimation DOUBLE_TROUBLE_OPEN_BOTH = RawAnimation.begin().thenPlay("double_trouble.open_both");
    private static final RawAnimation DOUBLE_TROUBLE_OPEN_LEFT = RawAnimation.begin().thenPlay("double_trouble.open_left");
    private static final RawAnimation DOUBLE_TROUBLE_OPEN_RIGHT = RawAnimation.begin().thenPlay("double_trouble.open_right");
    private static final RawAnimation DOUBLE_TROUBLE_OPEN_NONE = RawAnimation.begin().thenPlay("double_trouble.open_none");
    private static final RawAnimation DOUBLE_TROUBLE_RELOAD = RawAnimation.begin().thenPlay("double_trouble.reload");
    private static final RawAnimation DOUBLE_TROUBLE_CLOSE = RawAnimation.begin().thenPlay("double_trouble.close");
    private static final RawAnimation DOUBLE_TROUBLE_EMPTY = RawAnimation.begin().thenPlay("double_trouble.empty");
    private static final RawAnimation DOUBLE_TROUBLE_OPEN_IDLE = RawAnimation.begin().thenPlay("double_trouble.open_idle");
    private static final RawAnimation DOUBLE_TROUBLE_CLOSE_IDLE = RawAnimation.begin().thenPlay("double_trouble.close_idle");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final String LEFT_BARREL = "left_barrel";
    public static final String RIGHT_BARREL = "right_barrel";
    public static final String IS_OPEN = "is_open";
    public static final String VALUES_SET = "values_set";
    private static Boolean firstLoad = true;

    private static final Timer timer = new Timer();

    private static final Random random = new Random();

    public static final Predicate<ItemStack> IS_SHELL = (stack) -> {
        return stack.getItem() == ItemRegistry.SHELL.get();
    };

    public DoubleTroubleItem(Item.Properties build) {
        super(build);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private DoubleTroubleItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new DoubleTroubleItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "double_trouble_controller", 0, state -> PlayState.STOP)
                .triggerableAnim("shoot_left", DOUBLE_TROUBLE_SHOOT_LEFT)
                .triggerableAnim("shoot_right", DOUBLE_TROUBLE_SHOOT_RIGHT)
                .triggerableAnim("open_both", DOUBLE_TROUBLE_OPEN_BOTH)
                .triggerableAnim("open_left", DOUBLE_TROUBLE_OPEN_LEFT)
                .triggerableAnim("open_right", DOUBLE_TROUBLE_OPEN_RIGHT)
                .triggerableAnim("open_none", DOUBLE_TROUBLE_OPEN_NONE)
                .triggerableAnim("reload", DOUBLE_TROUBLE_RELOAD)
                .triggerableAnim("close", DOUBLE_TROUBLE_CLOSE)
                .triggerableAnim("empty", DOUBLE_TROUBLE_EMPTY)
                .triggerableAnim("open_idle", DOUBLE_TROUBLE_OPEN_IDLE)
                .triggerableAnim("close_idle", DOUBLE_TROUBLE_CLOSE_IDLE)
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

        if (player.isShiftKeyDown()) {
                if (stack.getTag().getBoolean(IS_OPEN)) {
                    if (level instanceof ServerLevel serverLevel) {
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "close");
                    }
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            stack.getOrCreateTag().putBoolean(IS_OPEN, false);
                            level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_CLOSE.get(), SoundSource.PLAYERS, 1, 1);
                        }
                    }, 170);

                    player.getCooldowns().addCooldown(item, 7);
                } else {
                    int leftBarrel = stack.getTag().getInt(LEFT_BARREL);
                    int rightBarrel = stack.getTag().getInt(RIGHT_BARREL);

                    if (level instanceof ServerLevel serverLevel) {
                        if (leftBarrel >= 2 && rightBarrel >= 2) {
                            triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "open_none");
                        } else if (leftBarrel <= 1 && rightBarrel >= 2) {
                            triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "open_left");
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    stack.getOrCreateTag().putInt(LEFT_BARREL, 0);
                                }
                            }, 670);
                        } else if (leftBarrel >= 2) {
                            triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "open_right");

                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    stack.getOrCreateTag().putInt(RIGHT_BARREL, 0);
                                }
                            }, 670);
                        } else {
                            triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "open_both");
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    stack.getOrCreateTag().putInt(LEFT_BARREL, 0);
                                    stack.getOrCreateTag().putInt(RIGHT_BARREL, 0);
                                }
                            }, 670);
                        }

                        player.getCooldowns().addCooldown(item, 14);
                    }

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            stack.getOrCreateTag().putBoolean(IS_OPEN, true);
                            level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_OPEN.get(), SoundSource.PLAYERS, 1, 1);
                        }
                    }, 170);
                }
            } else if (Screen.hasControlDown()) {
            if (!stack.getTag().getBoolean(IS_OPEN)) {
                int leftBarrel = stack.getTag().getInt(LEFT_BARREL);
                int rightBarrel = stack.getTag().getInt(RIGHT_BARREL);
                //Prepare RNG values for recoil
                float XRNG = random.nextInt(10, 20);
                float YRNG = random.nextInt(-10, 10);

                if (leftBarrel >= 2) {
                    stack.getOrCreateTag().putInt(LEFT_BARREL, 1);
                    if (level instanceof ServerLevel serverLevel) {
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "shoot_left");
                    }
                    //Pellet spawn logic
                    for (int i = 0; i < 6; i++) {
                        int PelletXRNG = random.nextInt(-5, 5);
                        int PelletYRNG = random.nextInt(-5, 5);

                        if (!level.isClientSide) {
                            PelletEntity pellet = new PelletEntity(EntityRegistry.PELLET.get(), player, level);
                            pellet.shootFromRotation(player, player.getXRot() + PelletXRNG, player.getYRot() + PelletYRNG, 0.0F, 5.0F, 1.0F);
                            level.addFreshEntity(pellet);
                        }
                    }

                    player.getCooldowns().addCooldown(item, 10);
                }

                if (rightBarrel >= 2) {
                    stack.getOrCreateTag().putInt(RIGHT_BARREL, 1);
                    if (level instanceof ServerLevel serverLevel) {
                            triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "shoot_right");
                    }
                    //Pellet spawn logic
                    for (int i = 0; i < 6; i++) {
                        int PelletXRNG = random.nextInt(-5, 5);
                        int PelletYRNG = random.nextInt(-5, 5);

                        if (!level.isClientSide) {
                            PelletEntity pellet = new PelletEntity(EntityRegistry.PELLET.get(), player, level);
                            pellet.shootFromRotation(player, player.getXRot() + PelletXRNG, player.getYRot() + PelletYRNG, 0.0F, 5.0F, 1.0F);
                            level.addFreshEntity(pellet);
                        }
                    }

                    player.getCooldowns().addCooldown(item, 10);
                }

                if (leftBarrel < 2 && rightBarrel < 2) {
                    if (level instanceof ServerLevel serverLevel) {
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "empty");
                    }
                    level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_EMPTY.get(), SoundSource.PLAYERS, 1, 1);

                    player.getCooldowns().addCooldown(item, 5);
                } else {
                    level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_SHOOT.get(), SoundSource.PLAYERS, 1, 1);
                    //Set player's head rotation to recoil
                    player.setXRot(player.getXRot() - XRNG);
                    player.setYRot(player.getYRot() + YRNG);

                    player.getCooldowns().addCooldown(item, 5);
                }
            }
        } else {
            if (stack.getTag().getBoolean(IS_OPEN)) {
                if (!(stack.getTag().getInt(LEFT_BARREL) >= 1 && stack.getTag().getInt(RIGHT_BARREL) >= 1)) {
                    if (player.isCreative()) {

                        if (level instanceof ServerLevel serverLevel) {
                            triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "reload");
                        }

                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (stack.getTag().getInt(LEFT_BARREL) <= 0) {
                                    stack.getOrCreateTag().putInt(LEFT_BARREL, 2);
                                    level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_RELOAD.get(), SoundSource.PLAYERS, 1, 1);
                                } else if (stack.getTag().getInt(RIGHT_BARREL) <= 0) {
                                    stack.getOrCreateTag().putInt(RIGHT_BARREL, 2);
                                    level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_RELOAD.get(), SoundSource.PLAYERS, 1, 1);
                                }
                            }
                        }, 250);

                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (level instanceof ServerLevel serverLevel) {
                                    triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "open_idle");
                                }
                            }
                        }, 500);

                        player.getCooldowns().addCooldown(item, 10);
                    } else {
                        ItemStack shell_stack = findAmmo(player);
                        int shell_stack_amount = getAmmoCount(player);
                        if (shell_stack_amount > 0) {
                            if (level instanceof ServerLevel serverLevel) {
                                triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "reload");
                            }

                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (stack.getTag().getInt(LEFT_BARREL) <= 0) {
                                        stack.getOrCreateTag().putInt(LEFT_BARREL, 2);
                                        shell_stack.shrink(1);
                                        level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_RELOAD.get(), SoundSource.PLAYERS, 1, 1);
                                    } else if (stack.getTag().getInt(RIGHT_BARREL) <= 0) {
                                        stack.getOrCreateTag().putInt(RIGHT_BARREL, 2);
                                        shell_stack.shrink(1);
                                        level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_RELOAD.get(), SoundSource.PLAYERS, 1, 1);
                                    }
                                }
                            }, 250);

                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (level instanceof ServerLevel serverLevel) {
                                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "open_idle");
                                    }
                                }
                            }, 500);

                            player.getCooldowns().addCooldown(item, 10);
                        }
                    }
                }
            } else {
                int leftBarrel = stack.getTag().getInt(LEFT_BARREL);
                int rightBarrel = stack.getTag().getInt(RIGHT_BARREL);
                //Prepare RNG values for recoil
                float XRNG = random.nextInt(10, 20);
                float YRNG = random.nextInt(-10, 10);

                if (leftBarrel >= 2) {
                    stack.getOrCreateTag().putInt(LEFT_BARREL, 1);
                    if (level instanceof ServerLevel serverLevel) {
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "shoot_left");
                    }
                    //Pellet spawn logic
                    for (int i = 0; i < 6; i++) {
                        int PelletXRNG = random.nextInt(-5, 5);
                        int PelletYRNG = random.nextInt(-5, 5);

                        if (!level.isClientSide) {
                            PelletEntity pellet = new PelletEntity(EntityRegistry.PELLET.get(), player, level);
                            pellet.shootFromRotation(player, player.getXRot() + PelletXRNG, player.getYRot() + PelletYRNG, 0.0F, 5.0F, 1.0F);
                            level.addFreshEntity(pellet);
                        }
                    }
                    level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_SHOOT.get(), SoundSource.PLAYERS, 1, 1);
                    //Set player's head rotation to recoil
                    player.setXRot(player.getXRot() - XRNG);
                    player.setYRot(player.getYRot() + YRNG);

                    player.getCooldowns().addCooldown(item, 10);
                } else if (rightBarrel >= 2) {
                    stack.getOrCreateTag().putInt(RIGHT_BARREL, 1);
                    if (level instanceof ServerLevel serverLevel) {
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "shoot_right");
                    }
                    //Pellet spawn logic
                    for (int i = 0; i < 6; i++) {
                        int PelletXRNG = random.nextInt(-5, 5);
                        int PelletYRNG = random.nextInt(-5, 5);

                        if (!level.isClientSide) {
                            PelletEntity pellet = new PelletEntity(EntityRegistry.PELLET.get(), player, level);
                            pellet.shootFromRotation(player, player.getXRot() + PelletXRNG, player.getYRot() + PelletYRNG, 0.0F, 5.0F, 1.0F);
                            level.addFreshEntity(pellet);
                        }
                    }
                    level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_SHOOT.get(), SoundSource.PLAYERS, 1, 1);
                    //Set player's head rotation to recoil
                    player.setXRot(player.getXRot() - XRNG);
                    player.setYRot(player.getYRot() + YRNG);

                    player.getCooldowns().addCooldown(item, 10);
                } else {
                    if (level instanceof ServerLevel serverLevel) {
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "empty");
                    }
                    level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_EMPTY.get(), SoundSource.PLAYERS, 1, 1);

                    player.getCooldowns().addCooldown(item, 5);
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
            if (IS_SHELL.test(ammo_item)) {
                return ammo_item;
            }
        }
        return ItemStack.EMPTY;
    }

    public int getAmmoCount(Player player){
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i){
            ItemStack ammo_item = player.getInventory().getItem(i);
            int ammo_number = player.getInventory().getItem(i).getCount();
            if (IS_SHELL.test(ammo_item)) {
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
        if (!stack.getOrCreateTag().getBoolean(IS_OPEN)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int sum = 0;
        if (stack.getOrCreateTag().getInt(LEFT_BARREL) >= 2) {
            sum++;
        }
        if (stack.getOrCreateTag().getInt(RIGHT_BARREL) >= 2) {
            sum++;
        }
        return (int) (sum * 6);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb(0.0F, 0.0F, 5.0F);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {
        CompoundTag tag = stack.getTag();

        if (tag == null) {
            stack.getOrCreateTag().putBoolean(IS_OPEN, false);
            stack.getOrCreateTag().putInt(LEFT_BARREL, 2);
            stack.getOrCreateTag().putInt(RIGHT_BARREL, 2);
            stack.getOrCreateTag().putBoolean(VALUES_SET, true);
        }
        else if (!stack.getTag().getBoolean(VALUES_SET)){
            stack.getOrCreateTag().putBoolean(IS_OPEN, false);
            stack.getOrCreateTag().putInt(LEFT_BARREL, 2);
            stack.getOrCreateTag().putInt(RIGHT_BARREL, 2);
            stack.getOrCreateTag().putBoolean(VALUES_SET, true);
        }

        if (firstLoad){
            firstLoad = false;
            if (level instanceof ServerLevel serverLevel) {
                if (entity instanceof Player player) {
                    if (!stack.getTag().getBoolean(IS_OPEN)) {
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "close_idle");
                    } else {
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "double_trouble_controller", "open_idle");
                    }
                }
            }
        }
        super.inventoryTick(stack, level, entity, i, b);
    }


}
