package destiny.armoryofdestiny.item;

import destiny.armoryofdestiny.EntityRegistry;
import destiny.armoryofdestiny.SoundRegistry;
import destiny.armoryofdestiny.client.render.Spas12ItemRenderer;
import destiny.armoryofdestiny.entity.*;
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

public class Spas12Item extends Item implements GeoItem {

    private static final RawAnimation IDLE_PUMP = RawAnimation.begin().thenPlayAndHold("idle_pump");
    private static final RawAnimation IDLE_SEMI = RawAnimation.begin().thenPlayAndHold("idle_semi");
    private static final RawAnimation IDLE_RELOAD_PUMP = RawAnimation.begin().thenPlayAndHold("idle_reload_pump");
    private static final RawAnimation IDLE_RELOAD_SEMI = RawAnimation.begin().thenPlayAndHold("idle_reload_semi");
    private static final RawAnimation RECHAMBER_PUMP = RawAnimation.begin().thenPlayAndHold("rechamber_pump");
    private static final RawAnimation RECHAMBER_SEMI = RawAnimation.begin().thenPlayAndHold("rechamber_semi");
    private static final RawAnimation RELOAD_READY_PUMP = RawAnimation.begin().thenPlayAndHold("reload_ready_pump");
    private static final RawAnimation RELOAD_READY_SEMI = RawAnimation.begin().thenPlayAndHold("reload_ready_semi");
    private static final RawAnimation RELOAD_CANCEL_PUMP = RawAnimation.begin().thenPlayAndHold("reload_cancel_pump");
    private static final RawAnimation RELOAD_CANCEL_SEMI = RawAnimation.begin().thenPlayAndHold("reload_cancel_semi");
    private static final RawAnimation INSERT_PUMP = RawAnimation.begin().thenPlayAndHold("insert_pump");
    private static final RawAnimation INSERT_SEMI = RawAnimation.begin().thenPlayAndHold("insert_semi");
    private static final RawAnimation SHOOT_PUMP = RawAnimation.begin().thenPlayAndHold("shoot_pump");
    private static final RawAnimation SHOOT_SEMI = RawAnimation.begin().thenPlayAndHold("shoot_semi");
    private static final RawAnimation SHOOT_SEMI_FAIL = RawAnimation.begin().thenPlayAndHold("shoot_semi_fail");
    private static final RawAnimation PUMP = RawAnimation.begin().thenPlayAndHold("pump");
    private static final RawAnimation EMPTY_PUMP = RawAnimation.begin().thenPlayAndHold("empty_pump");
    private static final RawAnimation EMPTY_SEMI = RawAnimation.begin().thenPlayAndHold("empty_semi");
    private static final RawAnimation MODE_TO_PUMP = RawAnimation.begin().thenPlayAndHold("mode_to_pump");
    private static final RawAnimation MODE_TO_SEMI = RawAnimation.begin().thenPlayAndHold("mode_to_semi");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final String CHAMBER = "chamber";
    public static final String SHELL_TUBE = "shellTube";
    public static final String STATE = "state";
    public static final String SHELL_TYPE = "shellType";
    private static Boolean firstLoad = true;

    private static final Timer timer = new Timer();

    private static final Random random = new Random();

    public static final Predicate<ItemStack> IS_SHELL = (stack) -> {
        return stack.getItem() == ItemRegistry.BUCKSHOT_SHELL.get()
                || stack.getItem() == ItemRegistry.SLUG_SHELL.get()
                || stack.getItem() == ItemRegistry.INCENDIARY_SHELL.get()
                || stack.getItem() == ItemRegistry.EXPLOSIVE_SLUG_SHELL.get();
    };

    public ItemStack findAmmo(Player player){
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

    public Spas12Item(Item.Properties build) {
        super(build);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private Spas12ItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new Spas12ItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> PlayState.CONTINUE)
                .triggerableAnim("idle_pump", IDLE_PUMP)
                .triggerableAnim("idle_semi", IDLE_SEMI)
                .triggerableAnim("idle_reload_pump", IDLE_RELOAD_PUMP)
                .triggerableAnim("idle_reload_semi", IDLE_RELOAD_SEMI)
                .triggerableAnim("rechamber_pump", RECHAMBER_PUMP)
                .triggerableAnim("rechamber_semi", RECHAMBER_SEMI)
                .triggerableAnim("reload_ready_pump", RELOAD_READY_PUMP)
                .triggerableAnim("reload_ready_semi", RELOAD_READY_SEMI)
                .triggerableAnim("reload_cancel_pump", RELOAD_CANCEL_PUMP)
                .triggerableAnim("reload_cancel_semi", RELOAD_CANCEL_SEMI)
                .triggerableAnim("insert_pump", INSERT_PUMP)
                .triggerableAnim("insert_semi", INSERT_SEMI)
                .triggerableAnim("shoot_pump", SHOOT_PUMP)
                .triggerableAnim("shoot_semi", SHOOT_SEMI)
                .triggerableAnim("shoot_semi_fail", SHOOT_SEMI_FAIL)
                .triggerableAnim("pump", PUMP)
                .triggerableAnim("empty_pump", EMPTY_PUMP)
                .triggerableAnim("empty_semi", EMPTY_SEMI)
                .triggerableAnim("mode_to_pump", MODE_TO_PUMP)
                .triggerableAnim("mode_to_semi", MODE_TO_SEMI)
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
            positionHandling(level, player, stack, item);
        } else if (Screen.hasControlDown()) {
            fireModeHandling(level, player, stack);
        } else {
            String state = stack.getTag().getString(STATE);

            if (state.equals("idle_pump") || state.equals("idle_semi")) {
                shootHandling(level, player, stack, item);
            } else if (state.equals("idle_reload_pump") || state.equals("idle_reload_semi")) {
                reloadHandling(level, player, stack, item);
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    public void positionHandling(Level level, Player player, ItemStack stack, Item item) {
        String state = stack.getTag().getString(STATE);

        if (state.equals("idle_reload_pump")) {
            triggerAnim(level, player, stack, "controller", "reload_cancel_pump");
            stack.getOrCreateTag().putString(STATE, "idle_pump");

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    triggerAnim(level, player, stack, "controller", "idle_pump");
                }
            }, 250);
        } else if (state.equals("idle_reload_semi")) {
            triggerAnim(level, player, stack, "controller", "reload_cancel_semi");
            stack.getOrCreateTag().putString(STATE, "idle_semi");

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    triggerAnim(level, player, stack, "controller", "idle_semi");
                }
            }, 250);
        } else if (state.equals("idle_pump")) {
            triggerAnim(level, player, stack, "controller", "reload_ready_pump");
            stack.getOrCreateTag().putString(STATE, "idle_reload_pump");

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    triggerAnim(level, player, stack, "controller", "idle_reload_pump");
                }
            }, 250);
        } else if (state.equals("idle_semi")) {
            triggerAnim(level, player, stack, "controller", "reload_ready_semi");
            stack.getOrCreateTag().putString(STATE, "idle_reload_semi");

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    triggerAnim(level, player, stack, "controller", "idle_reload_semi");
                }
            }, 250);
        }
        player.getCooldowns().addCooldown(item, 5);
        level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_SWITCH.get(), SoundSource.PLAYERS, 1, 1);
    }

    public void fireModeHandling(Level level, Player player, ItemStack stack) {
        String state = stack.getTag().getString(STATE);

        if (state.equals("idle_pump")) {
            triggerAnim(level, player, stack, "controller", "mode_to_semi");
            stack.getOrCreateTag().putString(STATE, "idle_semi");
            level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_SWITCH.get(), SoundSource.PLAYERS, 1, 1);
        } else if (state.equals("idle_semi")) {
            triggerAnim(level, player, stack, "controller", "mode_to_pump");
            stack.getOrCreateTag().putString(STATE, "idle_pump");
            level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_SWITCH.get(), SoundSource.PLAYERS, 1, 1);
        }
    }

    public void shootHandling(Level level, Player player, ItemStack stack, Item item) {
        int chamber = stack.getTag().getInt(CHAMBER);
        int tube = stack.getTag().getInt(SHELL_TUBE);
        String state = stack.getTag().getString(STATE);

        if (chamber == 1 && state.equals("idle_pump")) {
            triggerAnim(level, player, stack, "controller", "pump");
            level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_PUMP.get(), SoundSource.PLAYERS, 1, 1);
            if (tube > 0) {
                stack.getOrCreateTag().putInt(CHAMBER, 2);
                stack.getOrCreateTag().putInt(SHELL_TUBE, tube-1);
            } else {
                stack.getOrCreateTag().putInt(CHAMBER, 0);
            }
            player.getCooldowns().addCooldown(item, 10);
        } else if (chamber == 0) {
            if (state.equals("idle_pump")) {
                triggerAnim(level, player, stack, "controller", "empty_pump");

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        triggerAnim(level, player, stack, "controller", "idle_pump");
                    }
                }, 250);
                player.getCooldowns().addCooldown(item, 5);
            } else if (state.equals("idle_semi")) {
                triggerAnim(level, player, stack, "controller", "empty_semi");

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        triggerAnim(level, player, stack, "controller", "idle_semi");
                    }
                }, 250);
                player.getCooldowns().addCooldown(item, 5);
            }
            level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_EMPTY.get(), SoundSource.PLAYERS, 1, 1);
        } else if (chamber == 2) {
            if (stack.getTag().getString(SHELL_TYPE).equals("incendiary_shell") || stack.getTag().getString(SHELL_TYPE).equals("explosive_slug_shell") && state.equals("idle_semi")) {
                shoot(level, player, stack, item);
                triggerAnim(level, player, stack, "controller", "shoot_semi_fail");
                stack.getOrCreateTag().putInt(CHAMBER, 1);
                level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_SHOOT.get(), SoundSource.PLAYERS, 0.7f, 1);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        triggerAnim(level, player, stack, "controller", "idle_semi");
                    }
                }, 500);
                player.getCooldowns().addCooldown(item, 10);
            } else if (state.equals("idle_pump")) {
                shoot(level, player, stack, item);
                triggerAnim(level, player, stack, "controller", "shoot_pump");
                stack.getOrCreateTag().putInt(CHAMBER, 1);
                level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_SHOOT.get(), SoundSource.PLAYERS, 0.7f, 1);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        triggerAnim(level, player, stack, "controller", "idle_pump");
                    }
                }, 500);
                player.getCooldowns().addCooldown(item, 10);
            } else if (state.equals("idle_semi")) {
                shoot(level, player, stack, item);
                triggerAnim(level, player, stack, "controller", "shoot_semi");
                if (tube > 0) {
                    stack.getOrCreateTag().putInt(CHAMBER, 2);
                    stack.getOrCreateTag().putInt(SHELL_TUBE, tube-1);
                } else {
                    stack.getOrCreateTag().putInt(CHAMBER, 0);
                }
                level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_SHOOT.get(), SoundSource.PLAYERS, 0.7f, 1);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        triggerAnim(level, player, stack, "controller", "idle_semi");
                    }
                }, 500);
                player.getCooldowns().addCooldown(item, 10);
            }
        }
    }

    public void reloadHandling(Level level, Player player, ItemStack stack, Item item) {
        int chamber = stack.getTag().getInt(CHAMBER);
        int tube = stack.getTag().getInt(SHELL_TUBE);
        String state = stack.getTag().getString(STATE);

        ItemStack shell_stack = findAmmo(player);
        int shell_stack_amount = getAmmoCount(player);

        if (chamber < 1 && shell_stack_amount > 0) {
            stack.getOrCreateTag().putString(SHELL_TYPE, shell_stack.getItem().toString());

            if (state.equals("idle_reload_pump")) {
                triggerAnim(level, player, stack, "controller", "rechamber_pump");

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_OPEN.get(), SoundSource.PLAYERS, 1, 1);
                    }
                }, 380);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        shell_stack.shrink(1);

                        stack.getOrCreateTag().putInt(CHAMBER, 2);
                        level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_INSERT.get(), SoundSource.PLAYERS, 1, 1);
                    }
                }, 1250);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_CLOSE.get(), SoundSource.PLAYERS, 1, 1);
                    }
                }, 1500);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        triggerAnim(level, player, stack, "controller", "idle_reload_pump");
                    }
                }, 2000);
                player.getCooldowns().addCooldown(item, 40);
            } else if (state.equals("idle_reload_semi")) {
                triggerAnim(level, player, stack, "controller", "rechamber_semi");

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_OPEN.get(), SoundSource.PLAYERS, 1, 1);
                    }
                }, 380);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        shell_stack.shrink(1);

                        stack.getOrCreateTag().putInt(CHAMBER, 2);
                        level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_INSERT.get(), SoundSource.PLAYERS, 1, 1);
                    }
                }, 1250);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_CLOSE.get(), SoundSource.PLAYERS, 1, 1);
                    }
                }, 1500);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        triggerAnim(level, player, stack, "controller", "idle_reload_semi");
                    }
                }, 2000);
                player.getCooldowns().addCooldown(item, 40);
            }
        } else if (tube < 7 && shell_stack_amount > 0 && stack.getTag().getString(SHELL_TYPE).equals(shell_stack.getItem().toString())) {
            if (state.equals("idle_reload_pump")) {
                triggerAnim(level, player, stack, "controller", "insert_pump");

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        shell_stack.shrink(1);

                        stack.getOrCreateTag().putInt(SHELL_TUBE, tube+1);
                        level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_INSERT.get(), SoundSource.PLAYERS, 1, 1);
                    }
                }, 500);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        triggerAnim(level, player, stack, "controller", "idle_reload_pump");
                    }
                }, 750);
                player.getCooldowns().addCooldown(item, 15);
            } else if (state.equals("idle_reload_semi")) {
                triggerAnim(level, player, stack, "controller", "insert_semi");

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        shell_stack.shrink(1);

                        stack.getOrCreateTag().putInt(SHELL_TUBE, tube+1);
                        level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_INSERT.get(), SoundSource.PLAYERS, 1, 1);
                    }
                }, 500);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        triggerAnim(level, player, stack, "controller", "idle_reload_semi");
                    }
                }, 750);
                player.getCooldowns().addCooldown(item, 15);
            }
        }
    }

    public void triggerAnim(Level level, Player player, ItemStack stack, String controller, String name) {
        if (level instanceof ServerLevel serverLevel) {
            triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), controller, name);
        }
    }

    public void shoot(Level level, Player player, ItemStack stack, Item item) {
        String shell_type = stack.getTag().getString(SHELL_TYPE);

        switch (shell_type) {
            case "buckshot_shell" -> {
                for (int i = 0; i < 16; i++) {
                    int X = random.nextInt(-5, 5);
                    int Y = random.nextInt(-5, 5);

                    if (!level.isClientSide) {
                        BuckshotEntity buckshot = new BuckshotEntity(EntityRegistry.BUCKSHOT.get(), player, level);
                        buckshot.shootFromRotation(player, player.getXRot() + X, player.getYRot() + Y, 0.0F, 5.0F, 1.0F);
                        level.addFreshEntity(buckshot);
                    }
                }
            }
            case "slug_shell" -> {
                int X = random.nextInt(-1, 1);
                int Y = random.nextInt(-1, 1);

                if (!level.isClientSide) {
                    SlugEntity slug = new SlugEntity(EntityRegistry.SLUG.get(), player, level);
                    slug.shootFromRotation(player, player.getXRot() + X, player.getYRot() + Y, 0.0F, 5.0F, 1.0F);
                    level.addFreshEntity(slug);
                }
            }
            case "incendiary_shell" -> {
                for (int i = 0; i < 8; i++) {
                    int X = random.nextInt(-7, 7);
                    int Y = random.nextInt(-7, 7);

                    if (!level.isClientSide) {
                        SparkEntity spark = new SparkEntity(EntityRegistry.SPARK.get(), player, level);
                        spark.shootFromRotation(player, player.getXRot() + X, player.getYRot() + Y, 0.0F, 5.0F, 1.0F);
                        level.addFreshEntity(spark);
                    }
                }
            }
            case "explosive_slug_shell" -> {
                int X = random.nextInt(-1, 1);
                int Y = random.nextInt(-1, 1);

                if (!level.isClientSide) {
                    ExplosiveSlugEntity slug = new ExplosiveSlugEntity(EntityRegistry.EXPLOSIVE_SLUG.get(), player, level);
                    slug.shootFromRotation(player, player.getXRot() + X, player.getYRot() + Y, 0.0F, 5.0F, 1.0F);
                    level.addFreshEntity(slug);
                }
            }
        }
        float recoilX = random.nextInt(-8, 8);
        float recoilY = random.nextInt(-8, 8);

        player.setXRot(player.getXRot() - recoilX);
        player.setYRot(player.getYRot() + recoilY);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if (stack.getTag().getInt(CHAMBER) > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int sum = stack.getTag().getInt(SHELL_TUBE);
        if (stack.getOrCreateTag().getInt(CHAMBER) >= 2) {
            sum++;
        }
        return Math.round((float) sum * 13.0F / (float) 8);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb(0.0F, 0.0F, 5.0F);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {
        CompoundTag tag = stack.getTag();

        if (firstLoad){
            firstLoad = false;
            if (entity instanceof Player player) {
                if (stack.getTag().getString(STATE).equals("idle_pump")) {
                    triggerAnim(level, player, stack, "controller", "idle_pump");
                } else if (stack.getTag().getString(STATE).equals("idle_semi")) {
                    triggerAnim(level, player, stack, "controller", "idle_semi");
                } else if (stack.getTag().getString(STATE).equals("idle_reload_pump")) {
                    triggerAnim(level, player, stack, "controller", "idle_reload_pump");
                } else if (stack.getTag().getString(STATE).equals("idle_reload_semi")) {
                    triggerAnim(level, player, stack, "controller", "idle_reload_semi");
                }
            }
        }

        if (tag == null) {
            stack.getOrCreateTag().putInt(CHAMBER, 0);
            stack.getOrCreateTag().putInt(SHELL_TUBE, 0);
            stack.getOrCreateTag().putString(STATE, "idle_pump");
        }

        if (stack.getTag().getString(STATE).isEmpty()) {
            stack.getOrCreateTag().putString(STATE, "idle_pump");
            if (entity instanceof Player player) {
                triggerAnim(level, player, stack, "controller", "idle_pump");
            }
        }
    }
}
