package destiny.armoryofdestiny.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.ForgeEventFactory;

public class ExplosiveSlugEntity extends AbstractArrow {

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    public ExplosiveSlugEntity(EntityType<ExplosiveSlugEntity> entityType, Level world) {
        super(entityType, world);
    }

    public ExplosiveSlugEntity(EntityType<ExplosiveSlugEntity> entityType, double x, double y, double z, Level world) {
        super(entityType, x, y, z, world);
    }

    public ExplosiveSlugEntity(EntityType<ExplosiveSlugEntity> entityType, LivingEntity shooter, Level world) {
        super(entityType, shooter, world);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if(!this.level().isClientSide())
        {
            boolean canDestroy = ForgeEventFactory.getMobGriefingEvent(this.level(), this.getOwner());
            this.level().explode((Entity)this.getOwner(), this.getX(), this.getY(), this.getZ(), 2, canDestroy,
                    canDestroy ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE);
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        if (!this.level().isClientSide()) {
            Entity entity = hitResult.getEntity();
            Entity attacker = this.getOwner();

            entity.invulnerableTime = 0;
            entity.hurt(level().damageSources().generic(), 30.0F);
            entity.setRemainingFireTicks(600);
            entity.isOnFire();

            this.discard();
        }
        super.onHitEntity(hitResult);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);

        if(!this.level().isClientSide())
        {
            Entity entity = this.getOwner();
            if(!(entity instanceof Mob) || ForgeEventFactory.getMobGriefingEvent(this.level(), entity))
            {
                BlockPos blockpos = hitResult.getBlockPos().relative(hitResult.getDirection());

                if(this.level().isEmptyBlock(blockpos))
                {
                    for(Direction direction : Direction.values())
                    {
                        trySetFireToBlock(blockpos, blockpos.relative(direction));
                    }
                }
            }
        }
        this.discard();
    }

    private boolean trySetFireToBlock(BlockPos blockpos, BlockPos nearbyPos)
    {
        if(!this.level().getBlockState(nearbyPos).isAir() && !this.level().getBlockState(nearbyPos).is(Blocks.FIRE))
        {
            this.level().setBlockAndUpdate(blockpos, BaseFireBlock.getState(this.level(), blockpos));
            return true;
        }
        return false;
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }
}