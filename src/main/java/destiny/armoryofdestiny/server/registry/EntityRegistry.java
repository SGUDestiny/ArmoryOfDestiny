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
            () -> EntityType.Builder.of((EntityType.EntityFactory<MetallicFeatherEntity>) MetallicFeatherEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("metallic_feather"));

    public static final RegistryObject<EntityType<PelletEntity>> PELLET = ENTITY_TYPES.register("pellet",
            () -> EntityType.Builder.of((EntityType.EntityFactory<PelletEntity>) PelletEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("pellet"));

    public static final RegistryObject<EntityType<BuckshotEntity>> BUCKSHOT = ENTITY_TYPES.register("buckshot",
            () -> EntityType.Builder.of((EntityType.EntityFactory<BuckshotEntity>) BuckshotEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("buckshot"));

    public static final RegistryObject<EntityType<SlugEntity>> SLUG = ENTITY_TYPES.register("slug",
            () -> EntityType.Builder.of((EntityType.EntityFactory<SlugEntity>) SlugEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("slug"));

    public static final RegistryObject<EntityType<SparkEntity>> SPARK = ENTITY_TYPES.register("spark",
            () -> EntityType.Builder.of((EntityType.EntityFactory<SparkEntity>) SparkEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("spark"));

    public static final RegistryObject<EntityType<ExplosiveSlugEntity>> EXPLOSIVE_SLUG = ENTITY_TYPES.register("explosive_slug",
            () -> EntityType.Builder.of((EntityType.EntityFactory<ExplosiveSlugEntity>) ExplosiveSlugEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("explosive_slug"));
}
