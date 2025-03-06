package destiny.armoryofdestiny.server.registry;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.block.ArmorersTinkeringTableBlock;
import destiny.armoryofdestiny.server.block.ArmorersCraftingTableBlock;
import destiny.armoryofdestiny.server.block.BloomeryBottomBlock;
import destiny.armoryofdestiny.server.block.BloomeryTopBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ListIterator;
import java.util.function.Supplier;

import static destiny.armoryofdestiny.server.block.BloomeryBottomBlock.LIT;
import static destiny.armoryofdestiny.server.block.BloomeryBottomBlock.LOGS;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ArmoryOfDestiny.MODID);

    public static final RegistryObject<Block> ARMORERS_CRAFTING_TABLE = registerBlock("armorers_crafting_table",
            () -> new ArmorersCraftingTableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY).strength(2.5F).sound(SoundType.NETHER_WOOD).noOcclusion()));

    public static final RegistryObject<Block> ARMORERS_FORGING_TABLE = registerBlock("armorers_tinkering_table",
            () -> new ArmorersTinkeringTableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY).strength(2.5F).sound(SoundType.NETHER_WOOD).noOcclusion()));

    public static final RegistryObject<Block> BLOOMERY_BOTTOM = registerBlock("bloomery_bottom",
            () -> new BloomeryBottomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED).strength(2.5F).noOcclusion().lightLevel((state) -> {return !state.getValue(LIT) ? 0 : 3 + 3 * state.getValue(LOGS);})));
    public static final RegistryObject<Block> BLOOMERY_TOP = registerBlock("bloomery_top",
            () -> new BloomeryTopBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN).strength(2.5F).sound(SoundType.MUD_BRICKS).noOcclusion()));
    public static final RegistryObject<Block> NETHER_BLOOMERY_BOTTOM = registerBlock("nether_bloomery_bottom",
            () -> new BloomeryBottomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED).strength(2.5F).sound(SoundType.NETHER_BRICKS).noOcclusion().lightLevel((state) -> {return !state.getValue(LIT) ? 0 : 3 + 3 * state.getValue(LOGS);})));
    public static final RegistryObject<Block> NETHER_BLOOMERY_TOP = registerBlock("nether_bloomery_top",
            () -> new BloomeryTopBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN).strength(2.5F).noOcclusion()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, Supplier<T> block) {
        return ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}