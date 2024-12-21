package destiny.armoryofdestiny.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class SparkEntity extends AbstractArrow {

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    public SparkEntity(EntityType<SparkEntity> entityType, Level world) {
        super(entityType, world);
    }

    public SparkEntity(EntityType<SparkEntity> entityType, double x, double y, double z, Level world) {
        super(entityType, x, y, z, world);
    }

    public SparkEntity(EntityType<SparkEntity> entityType, LivingEntity shooter, Level world) {
        super(entityType, shooter, world);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        if (!this.level().isClientSide()) {
            Entity entity = hitResult.getEntity();
            Entity attacker = this.getOwner();

            entity.invulnerableTime = 0;
            entity.hurt(level().damageSources().inFire(), 5.0F);
            entity.setRemainingFireTicks(300);
            entity.isOnFire();
            this.discard();
        }
        super.onHitEntity(hitResult);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        if (!this.level().isClientSide()) {
            BlockPos pos = hitResult.getBlockPos();
            BlockState above = this.level().getBlockState(pos.above());

            if (!above.isAir() && !above.is(Blocks.FIRE)) {
                this.level().setBlock(hitResult.getBlockPos().above(), Blocks.FIRE.defaultBlockState(), 3);
            }

            this.discard();
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }
}