package destiny.armoryofdestiny.registry;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.block.AssemblyTableBlock;
import destiny.armoryofdestiny.block.SmithingCraftingTableBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ArmoryOfDestiny.MODID);

    public static final RegistryObject<Block> SMITHING_CRAFTING_TABLE = registerBlock("smithing_crafting_table",
            () -> new SmithingCraftingTableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2.5F).sound(SoundType.NETHER_WOOD).noOcclusion()));

    public static final RegistryObject<Block> ASSEMBLY_TABLE = registerBlock("assembly_table",
            () -> new AssemblyTableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2.5F).sound(SoundType.NETHER_WOOD).noOcclusion()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, Supplier<T> block) {
        return ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}