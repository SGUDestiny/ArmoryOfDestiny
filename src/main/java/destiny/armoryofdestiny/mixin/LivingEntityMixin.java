package destiny.armoryofdestiny.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import destiny.armoryofdestiny.server.registry.EffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    LivingEntity entity = (LivingEntity) (Object) this;

    @WrapOperation(
            method = "updateInvisibilityStatus",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z")
    )
    boolean moreInvisibility(LivingEntity instance, MobEffect effect, Operation<Boolean> original) {
        return hasEffect(EffectRegistry.NONEXISTENCE.get()) || original.call(instance, effect);
    }

    @Shadow
    public abstract boolean hasEffect(@NotNull MobEffect mobEffect);

    @SuppressWarnings("UnresolvedMixinReference")
    @WrapOperation(
            method = "checkFallDamage",
            at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;addLandingEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/LivingEntity;I)Z")
    )
    boolean checkForEffect(BlockState instance, ServerLevel serverLevel, BlockPos pos, BlockState state, LivingEntity entity, int i, Operation<Boolean> original) {
        return entity.hasEffect(EffectRegistry.NONEXISTENCE.get()) || original.call(instance, serverLevel, pos, state, entity, i);
    }

    @Inject(method = "playBlockFallSound", at = @At("HEAD"), cancellable = true)
    public void checkForEffectSwim(CallbackInfo ci) {
        if (entity instanceof Player player) {
            if (player.hasEffect(EffectRegistry.NONEXISTENCE.get())) {
                ci.cancel();
            }
        }
    }
}
