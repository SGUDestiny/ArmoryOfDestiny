package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.server.item.utility.TooltipItem;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import destiny.armoryofdestiny.server.registry.SoundRegistry;
import destiny.armoryofdestiny.client.render.item.MurasamaSheathedItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

import static destiny.armoryofdestiny.server.item.MurasamaItem.ABILITY_TICK;
import static destiny.armoryofdestiny.server.util.UtilityVariables.KATANA;
import static destiny.armoryofdestiny.server.util.UtilityVariables.LEGENDARY;

public class MurasamaSheathedItem extends TooltipItem implements GeoItem {
    boolean played;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public MurasamaSheathedItem(Properties properties) {
        super(properties);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private MurasamaSheathedItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new MurasamaSheathedItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 20, state -> PlayState.STOP));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        //If off-hand is empty, do special ability
        if (player.getOffhandItem().isEmpty()) {
            shootSword(level, player, stack);

            //If player is shifting, launch them
            if (isShift(player)) {
                double force = 2;
                Vec3 pushVec = player.getLookAngle().scale(force);
                pushVec = pushVec.add(0, 0.2, 0);
                player.push(pushVec.x, pushVec.y, pushVec.z);
            }

            return InteractionResultHolder.success(stack);
        } else {
            return InteractionResultHolder.pass(stack);
        }
    }

    public void shootSword(Level level, Player player, ItemStack stack) {
        player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegistry.GUN_SHEATH.get()));
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.MURASAMA.get()));

        ItemStack newItem = player.getMainHandItem();
        //Transfer all data from Sheathed Murasama to Murasama
        if (stack.getTag() != null) {
            newItem.getOrCreateTag().merge(stack.getTag());
        }
        newItem.getOrCreateTag().putInt(ABILITY_TICK, 1);

        level.playSound(player, player.blockPosition(), SoundRegistry.MURASAMA_SHOOT.get(), SoundSource.PLAYERS, 0.5F, 1);
        level.addParticle(ParticleTypes.EXPLOSION, false, player.getX(), player.getY() + 1, player.getZ(), 0, 0, 0);
    }

    @Override
    public String getItemName(ItemStack stack) {
        return "murasama";
    }

    @Override
    public String getRarityTranslatable(ItemStack stack) {
        return LEGENDARY;
    }

    @Override
    public String getTriviaTranslatable() {
        return KATANA;
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
        }
    }
}
