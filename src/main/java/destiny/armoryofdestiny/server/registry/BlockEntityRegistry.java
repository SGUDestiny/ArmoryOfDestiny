package destiny.armoryofdestiny.server.registry;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.block.blockentity.ArmorersTinkeringTableBlockEntity;
import destiny.armoryofdestiny.server.block.blockentity.ArmorersCraftingTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ArmoryOfDestiny.MODID);

    public static final RegistryObject<BlockEntityType<ArmorersTinkeringTableBlockEntity>> ARMORERS_TINKERING_TABLE = BLOCK_ENTITIES.register("armorers_tinkering_table", () -> BlockEntityType.Builder.of(ArmorersTinkeringTableBlockEntity::new, BlockRegistry.ARMORERS_TINKERING_TABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<ArmorersCraftingTableBlockEntity>> ARMORERS_CRAFTING_TABLE = BLOCK_ENTITIES.register("armorers_crafting_table", () -> BlockEntityType.Builder.of(ArmorersCraftingTableBlockEntity::new, BlockRegistry.ARMORERS_CRAFTING_TABLE.get()).build(null));
}
