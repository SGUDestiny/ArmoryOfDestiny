package destiny.armoryofdestiny.item;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArmoryOfDestiny.MODID);

    public static Item.Properties basicItem() {
        return new Item.Properties().stacksTo(1);
    }

    public static final RegistryObject<Item> SPAS12 = ITEMS.register("spas12",
            () -> new Spas12Item(basicItem()
                    .rarity(Rarity.EPIC)
                    .durability(-1)
            ));

    public static final RegistryObject<Item> WINGED_VENGEANCE = CreativeTabs.addToTab(ITEMS.register("winged_vengeance",
            () -> new WingedVengeanceItem(
                    ArmorMaterials.NETHERITE,
                    ArmorItem.Type.CHESTPLATE,
                    basicItem()
                            .rarity(Rarity.EPIC)
                            .durability(1000)
                            .fireResistant()
            )));

    public static final RegistryObject<Item> SHARP_IRONY = ITEMS.register("sharp_irony",
            () -> new SharpIronyItem(basicItem()
                    .rarity(Rarity.EPIC)
                    .durability(-1)
            ));

    public static final RegistryObject<Item> DOUBLE_TROUBLE = ITEMS.register("double_trouble",
            () -> new DoubleTroubleItem(basicItem()
                    .rarity(Rarity.EPIC)
                    .durability(-1)
            ));

    public static final RegistryObject<Item> DRAGONSLAYER = CreativeTabs.addToTab(ITEMS.register("dragonslayer",
            () -> new DragonslayerItem(basicItem()
                    .rarity(Rarity.EPIC)
                    .durability(-1)
            )));

    public static final RegistryObject<Item> MURASAMA = CreativeTabs.addToTab(ITEMS.register("murasama",
            () -> new MurasamaItem(basicItem()
                    .rarity(Rarity.EPIC)
                    .durability(-1)
            )));

    public static final RegistryObject<Item> SNOWDROP = CreativeTabs.addToTab(ITEMS.register("snowdrop",
            () -> new SnowdropItem(basicItem()
                    .rarity(Rarity.EPIC)
                    .durability(-1)
            )));

    public static final RegistryObject<Item> ICICLE = CreativeTabs.addToTab(ITEMS.register("icicle",
            () -> new IcicleItem(basicItem()
                    .rarity(Rarity.EPIC)
                    .durability(-1)
            )));

    public static final RegistryObject<Item> KAFKAS_KATANA = CreativeTabs.addToTab(ITEMS.register("kafkas_katana",
            () -> new KafkasKatanaItem(basicItem()
                    .rarity(Rarity.EPIC)
                    .durability(-1)
            )));

    public static final RegistryObject<Item> SCARLET_OATH = CreativeTabs.addToTab(ITEMS.register("scarlet_oath",
            () -> new ScarletOathItem(basicItem()
                    .rarity(Rarity.EPIC)
                    .durability(-1)
            )));

    public static final RegistryObject<Item> SMOKENADE = ITEMS.register("smokenade",
            () -> new Item(basicItem()
                    .rarity(Rarity.EPIC)
                    .durability(-1)
            ));

    public static final RegistryObject<Item> METALLIC_FEATHER = CreativeTabs.addToTab(ITEMS.register("metallic_feather",
            () -> new Item(new Item.Properties().stacksTo(64)
                    .rarity(Rarity.EPIC)
            )));

    public static final RegistryObject<Item> BUCKSHOT_SHELL = CreativeTabs.addToTab(ITEMS.register("buckshot_shell",
            () -> new Item(new Item.Properties().stacksTo(64)
                    .rarity(Rarity.EPIC)
            )));

    public static final RegistryObject<Item> SLUG_SHELL = CreativeTabs.addToTab(ITEMS.register("slug_shell",
            () -> new Item(new Item.Properties().stacksTo(32)
                    .rarity(Rarity.EPIC)
            )));

    public static final RegistryObject<Item> INCENDIARY_SHELL = CreativeTabs.addToTab(ITEMS.register("incendiary_shell",
            () -> new Item(new Item.Properties().stacksTo(16)
                    .rarity(Rarity.EPIC)
            )));

    public static final RegistryObject<Item> EXPLOSIVE_SLUG_SHELL = CreativeTabs.addToTab(ITEMS.register("explosive_slug_shell",
            () -> new Item(new Item.Properties().stacksTo(16)
                    .rarity(Rarity.EPIC)
            )));
}
