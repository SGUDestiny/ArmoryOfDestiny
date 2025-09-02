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
            () -> new ArmorersCraftingTableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY).strength(2.5F).sound(SoundType.NETHER_WOOD).noOcclusion()));

    public static final RegistryObject<Block> ARMORERS_TINKERING_TABLE = registerBlock("armorers_tinkering_table",
            () -> new ArmorersTinkeringTableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY).strength(2.5F).sound(SoundType.NETHER_WOOD).noOcclusion()));

    public static final RegistryObject<Block> ARMORERS_ANVIL = registerBlock("armorers_anvil",
            () -> new ArmorersAnvilBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY).strength(5F).sound(SoundType.ANVIL).noOcclusion()));

    public static final RegistryObject<Block> BLOOMERY_BOTTOM = registerBlock("bloomery_bottom",
            () -> new BloomeryBottomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED).strength(3F).noOcclusion().lightLevel((state) -> {return !state.getValue(LIT) ? 0 : 3 + 3 * state.getValue(LOGS);})));
    public static final RegistryObject<Block> BLOOMERY_TOP = registerBlock("bloomery_top",
            () -> new BloomeryTopBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN).strength(3F).sound(SoundType.MUD_BRICKS).noOcclusion()));
    public static final RegistryObject<Block> NETHER_BLOOMERY_BOTTOM = registerBlock("nether_bloomery_bottom",
            () -> new BloomeryBottomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED).strength(4F).sound(SoundType.NETHER_BRICKS).noOcclusion().lightLevel((state) -> {return !state.getValue(LIT) ? 0 : 3 + 3 * state.getValue(LOGS);})));
    public static final RegistryObject<Block> NETHER_BLOOMERY_TOP = registerBlock("nether_bloomery_top",
            () -> new BloomeryTopBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN).strength(4F).noOcclusion()));

    public static final RegistryObject<Block> TEMPERING_BARREL = registerBlock("tempering_barrel",
            () -> new TemperingBarrelBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN).strength(2.5F).sound(SoundType.WOOD).noOcclusion()));

    public static final RegistryObject<Block> HOT_RAW_COPPER_BLOCK = BLOCKS.register("hot_raw_copper_block",
            () -> new HotBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW).strength(5.0F, 6.0F), 2, 20));
    public static final RegistryObject<Block> HOT_RAW_IRON_BLOCK = BLOCKS.register("hot_raw_iron_block",
            () -> new HotBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW).strength(5.0F, 6.0F), 2, 20));
    public static final RegistryObject<Block> HOT_RAW_GOLD_BLOCK = BLOCKS.register("hot_raw_gold_block",
            () -> new HotBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW).strength(5.0F, 6.0F), 2, 20));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, Supplier<T> block) {
        return ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}