package destiny.armoryofdestiny.registry;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.blockentity.ArmorersAssemblyTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ArmoryOfDestiny.MODID);

    public static final RegistryObject<BlockEntityType<ArmorersAssemblyTableBlockEntity>> ASSEMBLY_TABLE = BLOCK_ENTITIES.register("armorers_assembly_table", () -> BlockEntityType.Builder.of(ArmorersAssemblyTableBlockEntity::new, BlockRegistry.ARMORERS_ASSEMBLY_TABLE.get()).build(null));
}
