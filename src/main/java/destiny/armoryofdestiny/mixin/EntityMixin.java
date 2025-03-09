package destiny.armoryofdestiny.mixin;

import destiny.armoryofdestiny.server.registry.EffectRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract InteractionResult interact(Player p_19978_, InteractionHand p_19979_);

    Entity entity = (Entity) (Object) this;

    @Inject(method = "spawnSprintParticle()V", at = @At("HEAD"), cancellable = true)
    public void checkForEffect(CallbackInfo ci) {
        if (entity instanceof Player player) {
            if (player.hasEffect(EffectRegistry.NONEXISTENCE.get())) {
                ci.cancel();
            }
        }
    }

    @Inject(
            method = "doWaterSplashEffect()V",
            at = @At("HEAD"), cancellable = true
    )
    public void checkForEffectSplash(CallbackInfo ci) {
        if (entity instanceof Player player && player.hasEffect(EffectRegistry.NONEXISTENCE.get())) {
            ci.cancel();
        }
    }

    @Inject(method = "waterSwimSound()V", at = @At("HEAD"), cancellable = true)
    public void checkForEffectSwim(CallbackInfo ci) {
        if (entity instanceof Player player) {
            if (player.hasEffect(EffectRegistry.NONEXISTENCE.get())) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "walkingStepSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", at = @At("HEAD"), cancellable = true)
    public void checkForEffectWalk(CallbackInfo ci) {
        if (entity instanceof Player player) {
            if (player.hasEffect(EffectRegistry.NONEXISTENCE.get())) {
                ci.cancel();
            }
        }
    }
}
