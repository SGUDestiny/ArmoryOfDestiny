package destiny.armoryofdestiny.server.entity;

import destiny.armoryofdestiny.server.registry.EntityRegistry;
import destiny.armoryofdestiny.server.registry.SoundRegistry;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class MetallicFeatherEntity extends AbstractArrow {
    public MetallicFeatherEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public MetallicFeatherEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(EntityRegistry.METALLIC_FEATHER.get(), level);
    }

    public MetallicFeatherEntity(Level level, LivingEntity shooter) {
        this(EntityRegistry.METALLIC_FEATHER.get(), level);
        float f = shooter instanceof Player ? 0.3F : 0.1F;
        this.setPos(shooter.getX(), shooter.getEyeY() - f, shooter.getZ());
        this.setOwner(shooter);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return (Packet<ClientGamePacketListener>) NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected ItemStack getPickupItem() {
        ItemStack itemStack = new ItemStack(ItemRegistry.METALLIC_FEATHER.get());
        return itemStack;
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);

        if(!this.level().isClientSide()){
            Entity entity = hitResult.getEntity();
            Entity attacker = this.getOwner();

            entity.hurt(level().damageSources().generic(), 12.0F);
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundRegistry.METALLIC_FEATHER_HIT.get();
    }
}
