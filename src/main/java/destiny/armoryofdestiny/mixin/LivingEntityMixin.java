package destiny.armoryofdestiny.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import destiny.armoryofdestiny.server.registry.EffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @WrapOperation(
            method = "updateInvisibilityStatus",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z")
    )
    boolean moreInvisibility(LivingEntity instance, MobEffect effect, Operation<Boolean> original) {
        return hasEffect(EffectRegistry.NONEXISTENCE.get()) || original.call(instance, effect);
    }

    @Shadow
    public abstract boolean hasEffect(@NotNull MobEffect mobEffect);
}
