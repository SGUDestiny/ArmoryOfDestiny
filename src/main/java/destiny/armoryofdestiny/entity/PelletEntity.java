package destiny.armoryofdestiny.entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
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
import net.minecraftforge.network.NetworkHooks;

public class PelletEntity extends AbstractArrow {

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    public PelletEntity(EntityType<PelletEntity> entityType, Level world) {
        super(entityType, world);
    }

    public PelletEntity(EntityType<PelletEntity> entityType, double x, double y, double z, Level world) {
        super(entityType, x, y, z, world);
    }

    public PelletEntity(EntityType<PelletEntity> entityType, LivingEntity shooter, Level world) {
        super(entityType, shooter, world);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        if(!this.level().isClientSide()){
            Entity entity = hitResult.getEntity();
            Entity attacker = this.getOwner();

            entity.invulnerableTime = 0;
            entity.hurt(level().damageSources().generic(), 25.0F);
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



    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
