package destiny.armoryofdestiny.item;

import destiny.armoryofdestiny.client.render.Spas12ItemRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
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
    private static final RawAnimation PUMP = RawAnimation.begin().thenPlayAndHold("pump");
    private static final RawAnimation EMPTY_PUMP = RawAnimation.begin().thenPlayAndHold("empty_pump");
    private static final RawAnimation EMPTY_SEMI = RawAnimation.begin().thenPlayAndHold("empty_semi");
    private static final RawAnimation MODE_TO_PUMP = RawAnimation.begin().thenPlayAndHold("mode_to_pump");
    private static final RawAnimation MODE_TO_SEMI = RawAnimation.begin().thenPlayAndHold("mode_to_semi");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final String CHAMBER = "chamber";
    public static final String SHELL_TUBE = "shellTube";
    public static final String FIRING_MODE = "firingMode";
    public static final String STATE = "state";
    public static final String SHELL_TYPE = "shellType";
    private static Boolean firstLoad = true;

    private static final Timer timer = new Timer();

    private static final Random random = new Random();

    public static final Predicate<ItemStack> IS_SHELL = (stack) -> {
        return stack.getItem() == ItemRegistry.SHELL.get();
    };

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
        controllers.add(new AnimationController<>(this, "controller", 0, state -> PlayState.STOP)
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
                shootHandling(level, player, stack);
            } else if (state.equals("idle_reload_pump") || state.equals("idle_reload_semi")) {
                reloadHandling(level, player, stack);
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    public void positionHandling(Level level, Player player, ItemStack stack, Item item) {
        if (level instanceof ServerLevel serverLevel) {
            String state = stack.getTag().getString(STATE);

            if (state.equals("idle_reload_pump")) {
                triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "reload_cancel_pump");
                stack.getOrCreateTag().putString(STATE, "idle_pump");
            } else if (state.equals("idle_reload_semi")) {
                triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "reload_cancel_semi");
                stack.getOrCreateTag().putString(STATE, "idle_semi");
            } else if (state.equals("idle_pump")) {
                triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "reload_ready_pump");
                stack.getOrCreateTag().putString(STATE, "idle_reload_pump");
            } else if (state.equals("idle_semi")) {
                triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "reload_ready_semi");
                stack.getOrCreateTag().putString(STATE, "idle_reload_semi");
            }
        }
    }

    public void fireModeHandling(Level level, Player player, ItemStack stack) {
        if (level instanceof ServerLevel serverLevel) {
            String state = stack.getTag().getString(STATE);

            if (state.equals("idle_pump")) {
                triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "mode_to_semi");
                stack.getOrCreateTag().putString(STATE, "idle_semi");
            } else if (state.equals("idle_semi")) {
                triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "mode_to_pump");
                stack.getOrCreateTag().putString(STATE, "idle_pump");
            }
        }
    }

    public void shootHandling(Level level, Player player, ItemStack stack) {
        if (level instanceof ServerLevel serverLevel) {
            int chamber = stack.getTag().getInt(CHAMBER);
            int tube = stack.getTag().getInt(SHELL_TUBE);
            String state = stack.getTag().getString(STATE);

            if (chamber == 1) {
                triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "pump");
                if (tube > 0) {
                    stack.getOrCreateTag().putInt(CHAMBER, 2);
                    stack.getOrCreateTag().putInt(SHELL_TUBE, tube-1);
                } else {
                    stack.getOrCreateTag().putInt(CHAMBER, 0);
                }
            } else if (chamber < 1) {
                if (state.equals("idle_pump")) {
                    triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "empty_pump");
                } else if (state.equals("idle_semi")) {
                    triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "empty_semi");
                }
            } else {
                if (state.equals("idle_pump")) {
                    triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "shoot_pump");
                    stack.getOrCreateTag().putInt(CHAMBER, 1);
                } else if (state.equals("idle_semi")) {
                    triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "shoot_semi");
                    if (tube > 0) {
                        stack.getOrCreateTag().putInt(CHAMBER, 2);
                        stack.getOrCreateTag().putInt(SHELL_TUBE, tube-1);
                    } else {
                        stack.getOrCreateTag().putInt(CHAMBER, 0);
                    }
                }
            }
        }
    }

    public void reloadHandling(Level level, Player player, ItemStack stack) {
        if (level instanceof ServerLevel serverLevel) {
            int chamber = stack.getTag().getInt(CHAMBER);
            int tube = stack.getTag().getInt(SHELL_TUBE);
            String state = stack.getTag().getString(STATE);


            if (chamber < 1) {
                if (state.equals("idle_reload_pump")) {
                    triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "rechamber_pump");
                    stack.getOrCreateTag().putInt(CHAMBER, 2);
                } else if (state.equals("idle_reload_semi")) {
                    triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "rechamber_semi");
                    stack.getOrCreateTag().putInt(CHAMBER, 2);
                }
            } else if (tube < 7) {
                if (state.equals("idle_reload_pump")) {
                    triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "insert_pump");
                    stack.getOrCreateTag().putInt(SHELL_TUBE, tube+1);
                } else if (state.equals("idle_reload_semi")) {
                    triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "insert_semi");
                    stack.getOrCreateTag().putInt(SHELL_TUBE, tube+1);
                }
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {
        CompoundTag tag = stack.getTag();

        if (tag == null) {
            stack.getOrCreateTag().putInt(CHAMBER, 0);
            stack.getOrCreateTag().putInt(SHELL_TUBE, 0);
            stack.getOrCreateTag().putString(FIRING_MODE, "pump");
            stack.getOrCreateTag().putString(STATE, "idle_pump");
        }

        if (firstLoad){
            firstLoad = false;
            if (level instanceof ServerLevel serverLevel) {
                if (entity instanceof Player player) {
                    if (stack.getTag().getString(STATE).equals("idle_pump")) {
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "idle_pump");
                    } else if (stack.getTag().getString(STATE).equals("idle_semi")) {
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "idle_semi");
                    } else if (stack.getTag().getString(STATE).equals("idle_reload_pump")) {
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "idle_reload_pump");
                    } else if (stack.getTag().getString(STATE).equals("idle_reload_semi")) {
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "idle_reload_semi");
                    }
                }
            }
        }
    }
}
