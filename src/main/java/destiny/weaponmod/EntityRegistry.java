package destiny.weaponmod;

import destiny.weaponmod.entity.MetallicFeatherEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WeaponMod.MODID);

    public static final RegistryObject<EntityType<MetallicFeatherEntity>> METALLIC_FEATHER = ENTITY_TYPES.register("explosive_arrow",
            () -> EntityType.Builder.of((EntityType.EntityFactory<MetallicFeatherEntity>) MetallicFeatherEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("metallic_feather"));
}
