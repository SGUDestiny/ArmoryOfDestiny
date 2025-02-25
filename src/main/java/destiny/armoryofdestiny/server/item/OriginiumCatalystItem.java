package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.client.render.item.OriginiumCatalystItemRenderer;
import destiny.armoryofdestiny.server.item.utility.TooltipItem;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import destiny.armoryofdestiny.server.registry.SoundRegistry;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

import static destiny.armoryofdestiny.server.item.OriginiumKatanaItem.ANIMATION_TICK;
import static destiny.armoryofdestiny.server.misc.UtilityVariables.SECONDARY;

public class OriginiumCatalystItem extends TooltipItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public OriginiumCatalystItem(Item.Properties build) {
        super(build);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private OriginiumCatalystItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new OriginiumCatalystItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() == ItemRegistry.ORIGINIUM_CATALYST.get()) {
            player.setItemInHand(hand, new ItemStack(ItemRegistry.ORIGINIUM_KATANA.get()));
            player.getItemInHand(hand).getOrCreateTag().putInt(ANIMATION_TICK, 1);

            ItemStack newCatalystStack = new ItemStack(ItemRegistry.ORIGINIUM_CATALYST.get());
            newCatalystStack.setCount(stack.getCount() - 1);

            if (stack.getTag() != null) {
                player.getItemInHand(hand).getOrCreateTag().merge(stack.getTag());
                newCatalystStack.getOrCreateTag().merge(stack.getTag());
            }

            player.addItem(newCatalystStack);
            player.getCooldowns().addCooldown(stack.getItem(), 20);

            level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.ORIGINIUM_CATALYST_ACTIVATE.get(), SoundSource.PLAYERS, 0.5F, 1, false);

            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
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
    public String getItemRarity(ItemStack stack) {
        return SECONDARY;
    }
}
