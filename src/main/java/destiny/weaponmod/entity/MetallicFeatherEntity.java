package destiny.weaponmod.entity;

import destiny.weaponmod.SoundRegistry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;

public class MetallicFeatherEntity extends AbstractArrow {
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    public MetallicFeatherEntity(EntityType<MetallicFeatherEntity> entityType, Level world) {
        super(entityType, world);
    }

    public MetallicFeatherEntity(EntityType<MetallicFeatherEntity> entityType, double x, double y, double z, Level world) {
        super(entityType, x, y, z, world);
    }

    public MetallicFeatherEntity(EntityType<MetallicFeatherEntity> entityType, LivingEntity shooter, Level world) {
        super(entityType, shooter, world);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);

        if(!this.level().isClientSide()){
            Entity entity = hitResult.getEntity();
            Entity attacker = this.getOwner();

            entity.hurt(level().damageSources().generic(), 20.0F);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
