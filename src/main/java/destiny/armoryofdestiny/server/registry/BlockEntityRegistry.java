package destiny.armoryofdestiny.server.registry;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.block.blockentity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ArmoryOfDestiny.MODID);

    public static final RegistryObject<BlockEntityType<ArmorersTinkeringTableBlockEntity>> ARMORERS_TINKERING_TABLE = BLOCK_ENTITIES.register("armorers_tinkering_table", () -> BlockEntityType.Builder.of(ArmorersTinkeringTableBlockEntity::new, BlockRegistry.ARMORERS_TINKERING_TABLE.get()).build(null));
    public static final RegistryObject<BlockEntityType<ArmorersCraftingTableBlockEntity>> ARMORERS_CRAFTING_TABLE = BLOCK_ENTITIES.register("armorers_crafting_table", () -> BlockEntityType.Builder.of(ArmorersCraftingTableBlockEntity::new, BlockRegistry.ARMORERS_CRAFTING_TABLE.get()).build(null));
    public static final RegistryObject<BlockEntityType<BloomeryBottomBlockEntity>> BLOOMERY_BOTTOM = BLOCK_ENTITIES.register("bloomery_bottom", () -> BlockEntityType.Builder.of(BloomeryBottomBlockEntity::new, BlockRegistry.BLOOMERY_BOTTOM.get()).build(null));
    public static final RegistryObject<BlockEntityType<NetherBloomeryBottomBlockEntity>> NETHER_BLOOMERY_BOTTOM = BLOCK_ENTITIES.register("nether_bloomery_bottom", () -> BlockEntityType.Builder.of(NetherBloomeryBottomBlockEntity::new, BlockRegistry.NETHER_BLOOMERY_BOTTOM.get()).build(null));
    public static final RegistryObject<BlockEntityType<BloomeryTopBlockEntity>> BLOOMERY_TOP = BLOCK_ENTITIES.register("bloomery_top", () -> BlockEntityType.Builder.of(BloomeryTopBlockEntity::new, BlockRegistry.BLOOMERY_TOP.get()).build(null));
    public static final RegistryObject<BlockEntityType<NetherBloomeryTopBlockEntity>> NETHER_BLOOMERY_TOP = BLOCK_ENTITIES.register("nether_bloomery_top", () -> BlockEntityType.Builder.of(NetherBloomeryTopBlockEntity::new, BlockRegistry.NETHER_BLOOMERY_TOP.get()).build(null));
    public static final RegistryObject<BlockEntityType<ArmorersAnvilBlockEntity>> ARMORERS_ANVIL = BLOCK_ENTITIES.register("armorers_anvil", () -> BlockEntityType.Builder.of(ArmorersAnvilBlockEntity::new, BlockRegistry.ARMORERS_ANVIL.get()).build(null));
}
