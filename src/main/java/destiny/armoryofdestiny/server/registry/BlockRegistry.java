package destiny.armoryofdestiny.server.registry;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static destiny.armoryofdestiny.server.block.BloomeryBottomBlock.LIT;
import static destiny.armoryofdestiny.server.block.BloomeryBottomBlock.LOGS;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ArmoryOfDestiny.MODID);

    public static final RegistryObject<Block> ARMORERS_CRAFTING_TABLE = registerBlock("armorers_crafting_table",
            () -> new ArmorersCraftingTableBlock(BlockBehaviour.Properties.copy(Blocks.SMITHING_TABLE)
                    .mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHER_WOOD).noOcclusion()));

    public static final RegistryObject<Block> ARMORERS_TINKERING_TABLE = registerBlock("armorers_tinkering_table",
            () -> new ArmorersTinkeringTableBlock(BlockBehaviour.Properties.copy(Blocks.SMITHING_TABLE)
                    .mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHER_WOOD).noOcclusion()));

    public static final RegistryObject<Block> ARMORERS_ANVIL = registerBlock("armorers_anvil",
            () -> new ArmorersAnvilBlock(BlockBehaviour.Properties.copy(Blocks.ANVIL)
                    .mapColor(MapColor.COLOR_LIGHT_GRAY).noOcclusion()));

    public static final RegistryObject<Block> BLOOMERY_BOTTOM = registerBlock("bloomery_bottom",
            () -> new BloomeryBottomBlock(BlockBehaviour.Properties.copy(Blocks.BRICKS)
                    .mapColor(MapColor.COLOR_RED).noOcclusion().lightLevel((state) -> {return !state.getValue(LIT) ? 0 : 3 + 3 * state.getValue(LOGS);})));
    public static final RegistryObject<Block> BLOOMERY_TOP = registerBlock("bloomery_top",
            () -> new BloomeryTopBlock(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS)
                    .mapColor(MapColor.COLOR_BROWN).noOcclusion()));
    public static final RegistryObject<Block> NETHER_BLOOMERY_BOTTOM = registerBlock("nether_bloomery_bottom",
            () -> new NetherBloomeryBottomBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_BRICKS)
                    .mapColor(MapColor.COLOR_RED).noOcclusion().lightLevel((state) -> {return !state.getValue(LIT) ? 0 : 3 + 3 * state.getValue(LOGS);})));
    public static final RegistryObject<Block> NETHER_BLOOMERY_TOP = registerBlock("nether_bloomery_top",
            () -> new NetherBloomeryTopBlock(BlockBehaviour.Properties.copy(Blocks.POLISHED_BLACKSTONE_BRICKS)
                    .mapColor(MapColor.COLOR_BROWN).noOcclusion()));

    public static final RegistryObject<Block> TEMPERING_BARREL = registerBlock("tempering_barrel",
            () -> new TemperingBarrelBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)
                    .mapColor(MapColor.COLOR_BROWN).noOcclusion()));

    //Materials
    public static final RegistryObject<Block> HOT_RAW_COPPER_BLOCK = BLOCKS.register("hot_raw_copper_block",
            () -> new HotBlock(BlockBehaviour.Properties.copy(Blocks.RAW_COPPER_BLOCK).mapColor(MapColor.COLOR_YELLOW)));
    public static final RegistryObject<Block> HOT_RAW_IRON_BLOCK = BLOCKS.register("hot_raw_iron_block",
            () -> new HotBlock(BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK).mapColor(MapColor.COLOR_YELLOW)));
    public static final RegistryObject<Block> HOT_RAW_GOLD_BLOCK = BLOCKS.register("hot_raw_gold_block",
            () -> new HotBlock(BlockBehaviour.Properties.copy(Blocks.RAW_GOLD_BLOCK).mapColor(MapColor.COLOR_YELLOW)));
    public static final RegistryObject<Block> HOT_IRON_BARS = BLOCKS.register("hot_iron_bars",
            () -> new HotIronBarsBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).mapColor(MapColor.COLOR_YELLOW)));
    public static final RegistryObject<Block> HOT_COPPER_BLOCK = BLOCKS.register("hot_copper_block",
            () -> new HotBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK).mapColor(MapColor.COLOR_YELLOW)));
    public static final RegistryObject<Block> HOT_IRON_BLOCK = BLOCKS.register("hot_iron_block",
            () -> new HotBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_YELLOW)));
    public static final RegistryObject<Block> HOT_GOLD_BLOCK = BLOCKS.register("hot_gold_block",
            () -> new HotBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).mapColor(MapColor.COLOR_YELLOW)));
    public static final RegistryObject<Block> HOT_DIAMOND_BLOCK = BLOCKS.register("hot_diamond_block",
            () -> new HotBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK).mapColor(MapColor.COLOR_YELLOW)));
    public static final RegistryObject<Block> HOT_NETHERITE_BLOCK = BLOCKS.register("hot_netherite_block",
            () -> new HotBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).mapColor(MapColor.COLOR_YELLOW)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, Supplier<T> block) {
        return ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}