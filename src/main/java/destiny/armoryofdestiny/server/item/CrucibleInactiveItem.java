package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.client.render.item.CrucibleInactiveItemRenderer;
import destiny.armoryofdestiny.server.item.utility.TooltipItem;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import destiny.armoryofdestiny.server.registry.SoundRegistry;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

import static destiny.armoryofdestiny.server.item.CrucibleItem.USAGES;
import static destiny.armoryofdestiny.server.misc.UtilityVariables.GREATSWORD;
import static destiny.armoryofdestiny.server.misc.UtilityVariables.LEGENDARY;

public class CrucibleInactiveItem extends TooltipItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public CrucibleInactiveItem(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private CrucibleInactiveItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new CrucibleInactiveItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack foundFuelStack = getFuelStack(player);
        int foundFuelAmount = foundFuelStack.getCount();
        int fuelAmount;

        if (stack.getTag() != null) {
            fuelAmount = stack.getTag().getInt(USAGES);
        } else {
            fuelAmount = 0;
            stack.getOrCreateTag().putInt(USAGES, 0);
        }


        if (foundFuelAmount != 0 || fuelAmount != 0) {
            ItemStack newStack = new ItemStack(ItemRegistry.CRUCIBLE.get());

            if (stack.getTag() != null) {
                newStack.getOrCreateTag().merge(stack.getTag());
            }

            if (3 > fuelAmount) {
                if (!player.isCreative()) {
                    if (foundFuelAmount > 2) {
                        newStack.getOrCreateTag().putInt(USAGES, 3);
                        foundFuelStack.setCount(foundFuelAmount - 3);
                    } else {
                        newStack.getOrCreateTag().putInt(USAGES, foundFuelAmount);
                        foundFuelStack.setCount(0);
                    }
                } else {
                    newStack.getOrCreateTag().putInt(USAGES, 3);
                }
            }

            player.setItemInHand(hand, newStack);

            level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.CRUCIBLE_ACTIVATE.get(), SoundSource.PLAYERS, 1F, 1, false);

            player.getCooldowns().addCooldown(newStack.getItem(), 20);

            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    public ItemStack getFuelStack(Player player) {
        ItemStack fuel = ItemStack.EMPTY;

        for (ItemStack stack : player.getInventory().items) {
            if (stack != null && stack.getItem() == Items.BLAZE_ROD) {
                fuel = stack;
                break;
            }
        }
        return fuel;
    }

    @Override
    public String getItemName(ItemStack stack) {
        return "crucible";
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
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 20, state -> PlayState.STOP));
    }
}
