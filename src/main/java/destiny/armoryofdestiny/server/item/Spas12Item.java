package destiny.armoryofdestiny.server.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.entity.BuckshotEntity;
import destiny.armoryofdestiny.server.entity.ExplosiveSlugEntity;
import destiny.armoryofdestiny.server.entity.SlugEntity;
/*
import destiny.armoryofdestiny.server.entity.SparkEntity;
import destiny.armoryofdestiny.server.registry.EntityRegistry;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import destiny.armoryofdestiny.server.registry.SoundRegistry;
import destiny.armoryofdestiny.client.render.item.Spas12ItemRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.function.Predicate;
*/

/*
@Mod.EventBusSubscriber(modid = ArmoryOfDestiny.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Spas12Item extends Item implements GeoItem {

    private double attackSpeed;

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.attackSpeed, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }

        return super.getAttributeModifiers(slot, stack);
    }

    private static final RawAnimation IDLE = RawAnimation.begin().thenPlayAndHold("idle");
    private static final RawAnimation IDLE_RELOAD = RawAnimation.begin().thenPlayAndHold("idle_reload");
    private static final RawAnimation RECHAMBER = RawAnimation.begin().thenPlayAndHold("rechamber");
    private static final RawAnimation RELOAD_READY = RawAnimation.begin().thenPlayAndHold("reload_ready");
    private static final RawAnimation RELOAD_CANCEL = RawAnimation.begin().thenPlayAndHold("reload_cancel");
    private static final RawAnimation INSERT = RawAnimation.begin().thenPlayAndHold("insert");
    private static final RawAnimation SHOOT_PUMP = RawAnimation.begin().thenPlayAndHold("shoot_pump");
    private static final RawAnimation SHOOT_SEMI = RawAnimation.begin().thenPlayAndHold("shoot_semi");
    private static final RawAnimation SHOOT_SEMI_RECHAMBER_FAIL = RawAnimation.begin().thenPlayAndHold("shoot_semi_rechamber_fail");
    private static final RawAnimation SHOOT_FAIL = RawAnimation.begin().thenPlayAndHold("shoot_fail");
    private static final RawAnimation PUMP = RawAnimation.begin().thenPlayAndHold("pump");
    private static final RawAnimation MODE_TO_PUMP = RawAnimation.begin().thenPlayAndHold("mode_to_pump");
    private static final RawAnimation MODE_TO_SEMI = RawAnimation.begin().thenPlayAndHold("mode_to_semi");
    private static final RawAnimation SAFETY_ON = RawAnimation.begin().thenPlayAndHold("safety_on");
    private static final RawAnimation SAFETY_IDLE = RawAnimation.begin().thenPlayAndHold("safety_idle");
    private static final RawAnimation SAFETY_OFF = RawAnimation.begin().thenPlayAndHold("safety_off");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final String CHAMBER = "chamber";
    public static final String SHELL_TUBE = "shellTube";
    public static final String SHELL_TYPE = "shellType";
    public static final String STATE = "state";
    public static final String FIRING_MODE = "fireMode";
    private static Boolean firstLoad = true;

    private static final Timer timer = new Timer();

    private static final Random random = new Random();

*/
/*    public static final Predicate<ItemStack> IS_SHELL = (stack) -> {
        return stack.getItem() == ItemRegistry.BUCKSHOT_SHELL.get()
                || stack.getItem() == ItemRegistry.SLUG_SHELL.get()
                || stack.getItem() == ItemRegistry.INCENDIARY_SHELL.get()
                || stack.getItem() == ItemRegistry.EXPLOSIVE_SLUG_SHELL.get();
    };*//*


    public ItemStack findAmmo(Player player){
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i){
            ItemStack ammo_item = player.getInventory().getItem(i);
*/
/*            if (IS_SHELL.test(ammo_item)) {
                return ammo_item;
            }*//*

        }
        return ItemStack.EMPTY;
    }

    public int getAmmoCount(Player player){
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i){
            ItemStack ammo_item = player.getInventory().getItem(i);
            int ammo_number = player.getInventory().getItem(i).getCount();
*/
/*            if (IS_SHELL.test(ammo_item)) {
                return ammo_number;
            }*//*

        }
        return 0;
    }

    public Spas12Item(Item.Properties build) {
        super(build);
        this.attackSpeed = 1024F;

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
                .triggerableAnim("idle", IDLE)
                .triggerableAnim("idle_reload", IDLE_RELOAD)
                .triggerableAnim("rechamber", RECHAMBER)
                .triggerableAnim("reload_ready", RELOAD_READY)
                .triggerableAnim("reload_cancel", RELOAD_CANCEL)
                .triggerableAnim("insert", INSERT)
                .triggerableAnim("shoot_pump", SHOOT_PUMP)
                .triggerableAnim("shoot_semi", SHOOT_SEMI)
                .triggerableAnim("shoot_semi_rechamber_fail", SHOOT_SEMI_RECHAMBER_FAIL)
                .triggerableAnim("shoot_fail", SHOOT_FAIL)
                .triggerableAnim("pump", PUMP)
                .triggerableAnim("safety_on", SAFETY_ON)
                .triggerableAnim("safety_idle", SAFETY_IDLE)
                .triggerableAnim("safety_off", SAFETY_OFF)
        );

        controllers.add(new AnimationController<>(this, "mode_controller", 0, state -> PlayState.CONTINUE)
                .triggerableAnim("mode_to_pump", MODE_TO_PUMP)
                .triggerableAnim("mode_to_semi", MODE_TO_SEMI)
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public boolean isShift (Level level) {
        if (level instanceof ClientLevel clientLevel) {
            return Screen.hasShiftDown();
        }
        return Screen.hasShiftDown();
    }

    public boolean isControl (Level level) {
        if (level instanceof ClientLevel clientLevel) {
            return Screen.hasControlDown();
        }
        return Screen.hasControlDown();
    }

    public boolean isAlt (Level level) {
        if (level instanceof ClientLevel clientLevel) {
            return Screen.hasAltDown();
        }
        return Screen.hasAltDown();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();

        if (stack.getTag() == null) {
            stack.getOrCreateTag().putInt(CHAMBER, 2);
            stack.getOrCreateTag().putInt(SHELL_TUBE, 6);
            stack.getOrCreateTag().putString(STATE, "idle");
            stack.getOrCreateTag().putString(FIRING_MODE, "pump");
        }

        if (isAlt(level)) {
            safetyHandling(level, player, stack, item);
        } else if (isShift(level)) {
            positionHandling(level, player, stack, item);
        } else if (isControl(level)) {
            fireModeHandling(level, player, stack);
        } else if (getState(stack).equals("reload")) {
            reloadHandling(level, player, stack, item);
        }

        return InteractionResultHolder.pass(stack);
    }

    public void safetyHandling(Level level, Player player, ItemStack stack, Item item) {
        if (getState(stack).equals("idle")) {
            triggerAnim(level, player, stack, "controller", "safety_on");
            setState(stack, "safety");

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    triggerAnim(level, player, stack, "controller", "safety_idle");
                }
            }, 500);
            player.getCooldowns().addCooldown(item, 10);
            level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_SWITCH.get(), SoundSource.PLAYERS, 1, 1);
        } else if (getState(stack).equals("safety")) {
            triggerAnim(level, player, stack, "controller", "safety_off");
            setState(stack, "idle");

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    triggerAnim(level, player, stack, "controller", "idle");
                }
            }, 500);
            player.getCooldowns().addCooldown(item, 10);
            level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_SWITCH.get(), SoundSource.PLAYERS, 1, 1);
        }
    }

    public void positionHandling(Level level, Player player, ItemStack stack, Item item) {
        if (getState(stack).equals("reload")) {
            triggerAnim(level, player, stack, "controller", "reload_cancel");
            setState(stack, "idle");

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    triggerAnim(level, player, stack, "controller", "idle");
                }
            }, 250);
            player.getCooldowns().addCooldown(item, 5);
            level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_SWITCH.get(), SoundSource.PLAYERS, 1, 1);
        } else if (getState(stack).equals("idle")) {
            triggerAnim(level, player, stack, "controller", "reload_ready");
            setState(stack, "reload");

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    triggerAnim(level, player, stack, "controller", "idle_reload");
                }
            }, 250);
            player.getCooldowns().addCooldown(item, 5);
            level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_SWITCH.get(), SoundSource.PLAYERS, 1, 1);
        }
    }

    public void fireModeHandling(Level level, Player player, ItemStack stack) {
        if (getState(stack).equals("idle")) {
            if (getFireMode(stack).equals("pump")) {
                triggerAnim(level, player, stack, "mode_controller", "mode_to_semi");
                setFireMode(stack, "semi");
                level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_SWITCH.get(), SoundSource.PLAYERS, 1, 1);
            } else if (getFireMode(stack).equals("semi")) {
                triggerAnim(level, player, stack, "mode_controller", "mode_to_pump");
                setFireMode(stack, "pump");
                level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_SWITCH.get(), SoundSource.PLAYERS, 1, 1);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        ItemStack stack = player.getMainHandItem();
        Item item = stack.getItem();

        if (item instanceof Spas12Item spas) {
            if (spas.getState(stack).equals("idle") && !player.getCooldowns().isOnCooldown(spas) && level instanceof ServerLevel serverLevel) {
                spas.shootHandling(serverLevel, player, stack, spas);
            }
        }
    }

    @SubscribeEvent
    public static void onMouseClick(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        ItemStack itemStack = player.getMainHandItem();

        if(itemStack.getItem() instanceof Spas12Item && event.isAttack()) {
            event.setSwingHand(false);
            event.setCanceled(true);
        }
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    public void shootHandling(Level level, Player player, ItemStack stack, Item item) {
        int chamber = stack.getTag().getInt(CHAMBER);
        int tube = stack.getTag().getInt(SHELL_TUBE);

        if (getFireMode(stack).equals("pump")) {
            if (chamber == 1) {
                triggerAnim(level, player, stack, "controller", "pump");
                level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_PUMP.get(), SoundSource.PLAYERS, 1, 1);
                if (tube > 0) {
                    stack.getOrCreateTag().putInt(CHAMBER, 2);
                    stack.getOrCreateTag().putInt(SHELL_TUBE, tube-1);
                } else {
                    stack.getOrCreateTag().putInt(CHAMBER, 0);
                }

                if (!player.getBlockStateOn().isAir()) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            level.playSound(player, player.blockPosition(), SoundRegistry.SHELL_TINK.get(), SoundSource.PLAYERS, 1.0f, 1);
                        }
                    }, 500);
                }

                player.getCooldowns().addCooldown(item, 10);
            } else if (chamber < 1) {
                triggerAnim(level, player, stack, "controller", "shoot_fail");

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        triggerAnim(level, player, stack, "controller", "idle");
                    }
                }, 250);
                player.getCooldowns().addCooldown(item, 5);
                level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_EMPTY.get(), SoundSource.PLAYERS, 1, 1);
            } else {
                shoot(level, player, stack, item);
                triggerAnim(level, player, stack, "controller", "shoot_pump");
                stack.getOrCreateTag().putInt(CHAMBER, 1);
                level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_SHOOT.get(), SoundSource.PLAYERS, 0.7f, 1);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        triggerAnim(level, player, stack, "controller", "idle");
                    }
                }, 500);
                player.getCooldowns().addCooldown(item, 10);
            }
        } else if (getFireMode(stack).equals("semi")) {
            if (chamber <= 1) {
                triggerAnim(level, player, stack, "controller", "shoot_fail");

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        triggerAnim(level, player, stack, "controller", "idle");
                    }
                }, 250);
                player.getCooldowns().addCooldown(item, 5);
                level.playSound(player, player.blockPosition(), SoundRegistry.DOUBLE_TROUBLE_EMPTY.get(), SoundSource.PLAYERS, 1, 1);
            } else {
                String type = (stack.getOrCreateTag().getString(SHELL_TYPE));

                if (type.equals("incendiary_shell") || type.equals("explosive_slug_shell")) {
                    shoot(level, player, stack, item);
                    triggerAnim(level, player, stack, "controller", "shoot_semi_rechamber_fail");
                    stack.getOrCreateTag().putInt(CHAMBER, 1);
                    level.playSound(player, player.blockPosition(), SoundRegistry.SPAS12_SHOOT.get(), SoundSource.PLAYERS, 0.7f, 1);

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            triggerAnim(level, player, stack, "controller", "idle");
                        }
                    }, 500);
                    player.getCooldowns().addCooldown(item, 10);
                } else {
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
                            triggerAnim(level, player, stack, "controller", "idle");
                            if (!player.getBlockStateOn().isAir()) {
                                level.playSound(player, player.blockPosition(), SoundRegistry.SHELL_TINK.get(), SoundSource.PLAYERS, 1.0f, 1);
                            }
                        }
                    }, 500);
                    player.getCooldowns().addCooldown(item, 10);
                }
            }
        }
    }

    public void reloadHandling(Level level, Player player, ItemStack stack, Item item) {
        int chamber = stack.getTag().getInt(CHAMBER);
        int tube = stack.getTag().getInt(SHELL_TUBE);

        ItemStack shell_stack = findAmmo(player);
        int shell_stack_amount = getAmmoCount(player);

        if (getState(stack).equals("reload")) {
            if (shell_stack_amount > 0) {
                if (chamber < 1) {
                    stack.getOrCreateTag().putString(SHELL_TYPE, shell_stack.getItem().toString());

                    triggerAnim(level, player, stack, "controller", "rechamber");

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
                            triggerAnim(level, player, stack, "controller", "idle_reload");
                        }
                    }, 2000);
                    player.getCooldowns().addCooldown(item, 40);
                } else if (tube < 7 && stack.getTag().getString(SHELL_TYPE).equals(shell_stack.getItem().toString())) {
                    triggerAnim(level, player, stack, "controller", "insert");

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
                            triggerAnim(level, player, stack, "controller", "idle_reload");
                        }
                    }, 750);
                    player.getCooldowns().addCooldown(item, 15);
                }
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

    public void setState(ItemStack stack, String state) {
        stack.getOrCreateTag().putString(STATE, state);
    }

    public String getState(ItemStack stack) {
        return stack.getOrCreateTag().getString(STATE);
    }

    public void setFireMode(ItemStack stack, String mode) {
        stack.getOrCreateTag().putString(FIRING_MODE, mode);
    }

    public String getFireMode(ItemStack stack) {
        return stack.getOrCreateTag().getString(FIRING_MODE);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getTag().getInt(CHAMBER) > 0;
        } else {
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
    public boolean isPerspectiveAware() {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(stack.getTag() != null) {
            int sum = stack.getTag().getInt(SHELL_TUBE);
            if (stack.getOrCreateTag().getInt(CHAMBER) >= 2) {
                sum++;
            }

            MutableComponent shells = Component.literal("Shells: ").withStyle(ChatFormatting.GRAY);
            shells.append(Component.literal(sum + "").withStyle(ChatFormatting.GRAY));

            MutableComponent mode = Component.literal("Fire Mode: ").withStyle(ChatFormatting.GRAY);
            mode.append(Component.literal(stack.getOrCreateTag().getString(FIRING_MODE)).withStyle(ChatFormatting.GRAY));

            MutableComponent state = Component.literal("State: ").withStyle(ChatFormatting.GRAY);
            state.append(Component.literal(stack.getOrCreateTag().getString(STATE)).withStyle(ChatFormatting.GRAY));

            components.add(shells);
            components.add(mode);
            components.add(state);
        }
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        triggerAnim(level, player, stack, "controller", "idle");
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {
        if (stack.getTag() != null) {
            firstLoad = false;
            if (entity instanceof Player player) {
                if (getState(stack).equals("idle")) {
                    triggerAnim(level, player, stack, "controller", "idle");
                } else if (getState(stack).equals("reload")) {
                    triggerAnim(level, player, stack, "controller", "idle_reload");
                } else if (getState(stack).equals("safety")) {
                    triggerAnim(level, player, stack, "controller", "safety_idle");
                }

                if (getFireMode(stack).equals("pump")) {
                    triggerAnim(level, player, stack, "mode_controller", "pump");
                } else if (getFireMode(stack).equals("semi")) {
                    triggerAnim(level, player, stack, "mode_controller", "semi");
                }
            }
        }
    }
}
*/
