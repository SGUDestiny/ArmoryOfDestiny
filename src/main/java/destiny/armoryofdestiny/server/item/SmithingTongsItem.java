package destiny.armoryofdestiny.server.item;

import destiny.armoryofdestiny.client.render.item.SmithingTongsItemRenderer;
import destiny.armoryofdestiny.server.item.utility.TooltipItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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

import static destiny.armoryofdestiny.server.util.UtilityVariables.SMITHING_TONGS;

public class SmithingTongsItem extends TooltipItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final String HELD_ITEM = "held_item";
    Item repairItem;

    public SmithingTongsItem(Properties properties, Item repairItem) {
        super(properties);
        this.repairItem = repairItem;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack mainStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack secondaryStack = player.getItemInHand(InteractionHand.OFF_HAND);

        if (player.isCrouching()) {
            if (mainStack.getTag() != null && mainStack.getTag().get(HELD_ITEM) != null) {
                ItemStack held_item = ItemStack.of(mainStack.getTag().getCompound(HELD_ITEM));

                if (held_item.getItem() != Items.AIR) {
                    level.playSound(null, player.blockPosition().above(), SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS, 1, 1);

                    player.addItem(held_item.copy());
                    mainStack.getOrCreateTag().put(HELD_ITEM, ItemStack.EMPTY.serializeNBT());

                    return InteractionResultHolder.success(mainStack);
                }

                if (held_item.getItem() == Items.AIR) {
                    if (secondaryStack.getItem() != Items.AIR) {
                        level.playSound(null, player.blockPosition().above(), SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 1, 1);

                        ItemStack putStack = secondaryStack.copy();
                        putStack.setCount(1);
                        mainStack.getOrCreateTag().put(HELD_ITEM, putStack.serializeNBT());
                        secondaryStack.shrink(1);

                        if (!player.isCreative()) {
                            mainStack.setDamageValue(mainStack.getDamageValue() + 1);
                        }

                        return InteractionResultHolder.success(mainStack);
                    }
                }
            }
        }

        return InteractionResultHolder.pass(mainStack);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private SmithingTongsItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new SmithingTongsItemRenderer();

                return this.renderer;
            }
        });
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> PlayState.STOP));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level lava, Entity entity, int i, boolean b) {
        if (stack.getTag() == null || stack.getTag().get(HELD_ITEM) == null) {
            stack.getOrCreateTag().put(HELD_ITEM, ItemStack.EMPTY.serializeNBT());
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack stack1) {
        return stack1.is(repairItem);
    }

    @Override
    public boolean hasAbilities() {
        return false;
    }

    @Override
    public String getTriviaTranslatable() {
        return SMITHING_TONGS;
    }
}
