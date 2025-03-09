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
import static destiny.armoryofdestiny.server.item.CrucibleItem.USAGES;

public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> DEF_REG  = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArmoryOfDestiny.MODID);

    public static final List<Supplier<? extends ItemLike>> CREATIVE_TAB_ITEMS = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> ARMORY_OF_DESTINY = DEF_REG .register("armoryofdestiny", () -> CreativeModeTab.builder()
            .icon(() -> ItemRegistry.MURASAMA.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.armoryofdestiny.tab"))
            .displayItems((parameters, output) -> {
                //Craftable items
                output.accept(ItemRegistry.MURASAMA.get());
                output.accept(ItemRegistry.MURASAMA_SHEATHED.get());
                output.accept(ItemRegistry.GUN_SHEATH.get());
                output.accept(ItemRegistry.BLOODLETTER.get());
                output.accept(createCrucible(ItemRegistry.CRUCIBLE.get()));
                output.accept(createCrucible(ItemRegistry.CRUCIBLE_INACTIVE.get()));
                output.accept(ItemRegistry.BLOOD_VESSEL_FULL.get());
                output.accept(ItemRegistry.BLOOD_VESSEL_EMPTY.get());
                output.accept(ItemRegistry.DRAGON_SLAYER.get());
                output.accept(ItemRegistry.ORIGINIUM_KATANA.get());
                output.accept(ItemRegistry.ORIGINIUM_CATALYST.get());
                output.accept(ItemRegistry.PUNISHER.get());
                output.accept(createSharpIrony(ItemRegistry.SHARP_IRONY.get()));
                output.accept(ItemRegistry.METALLIC_FEATHER.get());

                //Armorer's workshop
                output.accept(BlockRegistry.ARMORERS_CRAFTING_TABLE.get());
                output.accept(BlockRegistry.ARMORERS_FORGING_TABLE.get());
                output.accept(ItemRegistry.GOLD_SMITHING_HAMMER.get());
                output.accept(ItemRegistry.IRON_SMITHING_HAMMER.get());
                output.accept(ItemRegistry.DIAMOND_SMITHING_HAMMER.get());
                output.accept(ItemRegistry.NETHERITE_SMITHING_HAMMER.get());

                //Armorer's forge
                output.accept(ItemRegistry.TONGS.get());

                //Blueprints
                output.accept(createBlueprint(ItemRegistry.MURASAMA.getKey(), "legendary"));
                output.accept(createBlueprint(ItemRegistry.GUN_SHEATH.getKey(), "legendary"));
                output.accept(createBlueprint(ItemRegistry.BLOODLETTER.getKey(), "legendary"));
                output.accept(createBlueprint(ItemRegistry.CRUCIBLE_INACTIVE.getKey(), "legendary"));
                output.accept(createBlueprint(ItemRegistry.DRAGON_SLAYER.getKey(), "legendary"));
                output.accept(createBlueprint(ItemRegistry.ORIGINIUM_CATALYST.getKey(), "unique"));
                output.accept(createBlueprint(ItemRegistry.PUNISHER.getKey(), "legendary"));
                output.accept(createBlueprint(ItemRegistry.SHARP_IRONY.getKey(), "unique"));
            })
            .build()
    );

    private static ItemStack createCrucible(Item item) {
        ItemStack crucible = new ItemStack(item);

        crucible.getOrCreateTag().putInt(USAGES, 3);
        return crucible;
    }

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
