package destiny.armoryofdestiny.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class SlugEntity extends AbstractArrow {

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    public SlugEntity(EntityType<SlugEntity> entityType, Level world) {
        super(entityType, world);
    }

    public SlugEntity(EntityType<SlugEntity> entityType, double x, double y, double z, Level world) {
        super(entityType, x, y, z, world);
    }

    public SlugEntity(EntityType<SlugEntity> entityType, LivingEntity shooter, Level world) {
        super(entityType, shooter, world);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        if (!this.level().isClientSide()) {
            Entity entity = hitResult.getEntity();
            Entity attacker = this.getOwner();

            entity.invulnerableTime = 0;
            entity.hurt(level().damageSources().generic(), 20.0F);
            this.discard();
        }
        super.onHitEntity(hitResult);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        this.discard();
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }
}