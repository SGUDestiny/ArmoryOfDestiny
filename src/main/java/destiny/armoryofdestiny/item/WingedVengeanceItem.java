package destiny.armoryofdestiny.item;

import com.google.common.collect.Multimap;
import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.client.render.item.WingedVengeanceRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.function.Consumer;

public class WingedVengeanceItem extends ArmorItem implements GeoItem {
    private static final RawAnimation IDLE = RawAnimation.begin().thenPlayAndHold("idle");
    private static final RawAnimation FLYING = RawAnimation.begin().thenLoop("flying");

    private static Boolean isFlying = false;
    private static Boolean isIdle = false;

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public WingedVengeanceItem(ArmorMaterial pMaterial, Type pType, Properties pProperties)
    {
        super(pMaterial, pType, pProperties);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null)
                    this.renderer = new WingedVengeanceRenderer();

                // This prepares our GeoArmorRenderer for the current render frame.
                // These parameters may be null however, so we don't do anything further with them
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.renderer;
            }
        });
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type)
    {
        return ArmoryOfDestiny.MODID + ":textures/item/armor/winged_vengeance.png";
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 20, state -> PlayState.CONTINUE)
                .triggerableAnim("idle", IDLE)
                .triggerableAnim("flying", FLYING)
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {
        if (entity instanceof Player player) {
            if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() == WingedVengeanceItem.this) {
                if (!player.isCreative() && !player.isSpectator()) {
                    player.getAbilities().mayfly = true;
                    player.onUpdateAbilities();
                }
            } else if (!player.isCreative() && !player.isSpectator()) {
                player.getAbilities().flying = false;
                player.getAbilities().mayfly = false;
                player.onUpdateAbilities();
            }

            if (level instanceof ServerLevel serverLevel) {
                if (player.getAbilities().flying && !isFlying) {
                    triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "flying");
                    isFlying = true;
                    isIdle = false;
                    System.out.println("Flying animation triggered");
                } else if (!player.getAbilities().flying && !isIdle){
                    triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "idle");
                    isIdle = true;
                    isFlying = false;
                    System.out.println("Idle animation triggered");
                }
            }
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_40390_) {
        return super.getDefaultAttributeModifiers(p_40390_);
    }
}
