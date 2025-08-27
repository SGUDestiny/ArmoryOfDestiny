package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.client.render.item.GunSheathItemRenderer;
import destiny.armoryofdestiny.server.item.utility.TooltipItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

import static destiny.armoryofdestiny.server.util.UtilityVariables.SECONDARY;

public class GunSheathItem extends TooltipItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    boolean played = false;

    public GunSheathItem(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GunSheathItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new GunSheathItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 20, state -> PlayState.STOP));
    }

    @Override
    public String getRarityTranslatable(ItemStack stack) {
        return SECONDARY;
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
