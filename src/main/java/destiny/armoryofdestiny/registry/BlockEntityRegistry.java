package destiny.armoryofdestiny.registry;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.blockentity.AssemblyTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ArmoryOfDestiny.MODID);

    public static final RegistryObject<BlockEntityType<AssemblyTableBlockEntity>> ASSEMBLY_TABLE = BLOCK_ENTITIES.register("assembly_table", () -> BlockEntityType.Builder.of(AssemblyTableBlockEntity::new, BlockRegistry.ASSEMBLY_TABLE.get()).build(null));
}
