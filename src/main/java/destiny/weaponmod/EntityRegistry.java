package destiny.weaponmod;

import destiny.weaponmod.entity.MetallicFeatherEntity;
import destiny.weaponmod.entity.PelletEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WeaponMod.MODID);

    public static final RegistryObject<EntityType<MetallicFeatherEntity>> METALLIC_FEATHER = ENTITY_TYPES.register("metallic_feather",
            () -> EntityType.Builder.of((EntityType.EntityFactory<MetallicFeatherEntity>) MetallicFeatherEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("metallic_feather"));

    public static final RegistryObject<EntityType<PelletEntity>> PELLET = ENTITY_TYPES.register("pellet",
            () -> EntityType.Builder.of((EntityType.EntityFactory<PelletEntity>) PelletEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("pellet"));
}
