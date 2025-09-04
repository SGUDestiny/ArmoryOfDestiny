package destiny.armoryofdestiny.server.registry;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ArmoryOfDestiny.MODID);

    public static final RegistryObject<EntityType<MetallicFeatherEntity>> METALLIC_FEATHER = ENTITY_TYPES.register("metallic_feather",
            () -> (EntityType) EntityType.Builder.of(MetallicFeatherEntity::new, MobCategory.MISC).sized(0.6F, 0.6F).setCustomClientFactory(MetallicFeatherEntity::new).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).build("metallic_feather"));
}
