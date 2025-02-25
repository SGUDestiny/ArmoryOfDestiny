package destiny.armoryofdestiny.server.registry;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static destiny.armoryofdestiny.server.item.SharpIronyItem.AMMO_COUNT;
import static destiny.armoryofdestiny.server.item.SharpIronyItem.IS_OPEN;

public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> DEF_REG  = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArmoryOfDestiny.MODID);

    public static final List<Supplier<? extends ItemLike>> CREATIVE_TAB_ITEMS = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> ARMORY_OF_DESTINY = DEF_REG .register("armoryofdestiny", () -> CreativeModeTab.builder()
            .icon(() -> ItemRegistry.MURASAMA.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.armoryofdestiny.tab"))
            .displayItems((parameters, output) -> {
                output.accept(ItemRegistry.MURASAMA.get());
                output.accept(ItemRegistry.MURASAMA_SHEATHED.get());
                output.accept(ItemRegistry.GUN_SHEATH.get());
                output.accept(ItemRegistry.DRAGON_SLAYER.get());
                output.accept(ItemRegistry.ORIGINIUM_KATANA.get());
                output.accept(ItemRegistry.ORIGINIUM_CATALYST.get());
                output.accept(ItemRegistry.PUNISHER.get());
                output.accept(createSharpIrony(ItemRegistry.SHARP_IRONY.get()));
                output.accept(ItemRegistry.METALLIC_FEATHER.get());

                output.accept(ItemRegistry.TONGS.get());

                output.accept(ItemRegistry.SMITHING_HAMMER.get());
                output.accept(BlockRegistry.ARMORERS_CRAFTING_TABLE.get());
                output.accept(BlockRegistry.ARMORERS_FORGING_TABLE.get());

                output.accept(createBlueprint(ItemRegistry.MURASAMA.getKey(), "legendary"));
                output.accept(createBlueprint(ItemRegistry.GUN_SHEATH.getKey(), "legendary"));
                output.accept(createBlueprint(ItemRegistry.DRAGON_SLAYER.getKey(), "legendary"));
                output.accept(createBlueprint(ItemRegistry.ORIGINIUM_CATALYST.getKey(), "unique"));
                output.accept(createBlueprint(ItemRegistry.PUNISHER.getKey(), "legendary"));
                output.accept(createBlueprint(ItemRegistry.SHARP_IRONY.getKey(), "unique"));
            })
            .build()
    );

    private static ItemStack createSharpIrony(Item item) {
        ItemStack sharpIrony = new ItemStack(item);

        sharpIrony.getOrCreateTag().putBoolean(IS_OPEN, true);
        sharpIrony.getOrCreateTag().putInt(AMMO_COUNT, 5);
        return sharpIrony;
    }

    private static ItemStack createBlueprint(ResourceKey<Item> itemName, String blueprintRarity) {
        ItemStack blueprint = new ItemStack(ItemRegistry.BLUEPRINT.get());

        blueprint.getOrCreateTag().putString("blueprintItem", itemName.location().toString());
        blueprint.getOrCreateTag().putString("blueprintRarity", blueprintRarity);
        return blueprint;
    }
}
