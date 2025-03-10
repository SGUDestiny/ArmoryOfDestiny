package destiny.armoryofdestiny.server.registry;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.*;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArmoryOfDestiny.MODID);

    public static Item.Properties basicItem() {
        return new Item.Properties().stacksTo(1);
    }

    public static final RegistryObject<Item> MURASAMA_SHEATHED = ITEMS.register("murasama_sheathed",
            () -> new MurasamaSheathedItem(basicItem().rarity(Rarity.EPIC).durability(-1)));
    public static final RegistryObject<Item> MURASAMA = ITEMS.register("murasama",
            () -> new MurasamaItem(basicItem().rarity(Rarity.EPIC).durability(1300)));
    public static final RegistryObject<Item> GUN_SHEATH = ITEMS.register("gun_sheath",
            () -> new GunSheathItem(basicItem().rarity(Rarity.EPIC).durability(-1)));

    public static final RegistryObject<Item> ORIGINIUM_KATANA = ITEMS.register("originium_katana",
            () -> new OriginiumKatanaItem(basicItem().rarity(Rarity.EPIC).durability(64)));
    public static final RegistryObject<Item> ORIGINIUM_CATALYST = ITEMS.register("originium_catalyst",
            () -> new OriginiumCatalystItem(new Item.Properties().rarity(Rarity.EPIC).durability(-1).stacksTo(16)));

    public static final RegistryObject<Item> PUNISHER = ITEMS.register("punisher",
            () -> new PunisherItem(basicItem().rarity(Rarity.EPIC).durability(1800)));

    public static final RegistryObject<Item> SHARP_IRONY = ITEMS.register("sharp_irony",
            () -> new SharpIronyItem(basicItem().rarity(Rarity.EPIC).durability(1024)));
    public static final RegistryObject<Item> METALLIC_FEATHER = ITEMS.register("metallic_feather",
            () -> new MetallicFeatherItem(new Item.Properties().stacksTo(64).rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> DRAGON_SLAYER = ITEMS.register("dragon_slayer",
            () -> new DragonSlayerItem(basicItem().rarity(Rarity.EPIC).durability(2048)));

    public static final RegistryObject<Item> BLUEPRINT = ITEMS.register("blueprint",
            () -> new BlueprintItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> GOLD_SMITHING_HAMMER = ITEMS.register("gold_smithing_hammer",
            () -> new SmithingHammerItem(new Item.Properties().stacksTo(1).durability(32), Items.GOLD_INGOT, 4));
    public static final RegistryObject<Item> IRON_SMITHING_HAMMER = ITEMS.register("iron_smithing_hammer",
            () -> new SmithingHammerItem(new Item.Properties().stacksTo(1).durability(64), Items.IRON_INGOT, 6));
    public static final RegistryObject<Item> DIAMOND_SMITHING_HAMMER = ITEMS.register("diamond_smithing_hammer",
            () -> new SmithingHammerItem(new Item.Properties().stacksTo(1).durability(128), Items.DIAMOND, 8));
    public static final RegistryObject<Item> NETHERITE_SMITHING_HAMMER = ITEMS.register("netherite_smithing_hammer",
            () -> new SmithingHammerItem(new Item.Properties().stacksTo(1).durability(256), Items.NETHERITE_INGOT, 10));

    public static final RegistryObject<Item> TONGS = ITEMS.register("tongs",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BLOODLETTER = ITEMS.register("bloodletter",
            () -> new BloodletterItem(basicItem().rarity(Rarity.EPIC).durability(1024)));
    public static final RegistryObject<Item> BLOOD_VESSEL_EMPTY = ITEMS.register("blood_vessel_empty",
            () -> new BloodVesselEmptyItem(new Item.Properties().stacksTo(8)));
    public static final RegistryObject<Item> BLOOD_VESSEL_FULL = ITEMS.register("blood_vessel_full",
            () -> new BloodVesselFullItem(new Item.Properties().stacksTo(8)));

    public static final RegistryObject<Item> CRUCIBLE = ITEMS.register("crucible",
            () -> new CrucibleItem(basicItem().rarity(Rarity.EPIC).durability(-1)));
    public static final RegistryObject<Item> CRUCIBLE_INACTIVE = ITEMS.register("crucible_inactive",
            () -> new CrucibleInactiveItem(basicItem().rarity(Rarity.EPIC).durability(-1)));

    public static final RegistryObject<Item> EDGE_OF_EXISTENCE = ITEMS.register("edge_of_existence",
            () -> new EdgeOfExistenceItem(basicItem().rarity(Rarity.EPIC).durability(1150)));

//    public static final RegistryObject<Item> SPAS12 = ITEMS.register("spas12",
//            () -> new Spas12Item(basicItem().rarity(Rarity.EPIC).durability(-1)
//            ));

//    public static final RegistryObject<Item> DOUBLE_TROUBLE = ITEMS.register("double_trouble",
//            () -> new DoubleTroubleItem(basicItem().rarity(Rarity.EPIC).durability(-1)
//            ));

//    public static final RegistryObject<Item> WINGED_VENGEANCE = ITEMS.register("winged_vengeance",
//            () -> new WingedVengeanceItem(
//                    ArmorMaterials.NETHERITE,
//                    ArmorItem.Type.CHESTPLATE,
//                    basicItem()
//                            .rarity(Rarity.EPIC)
//                            .durability(1000)
//                            .fireResistant()
//            ));

//    public static final RegistryObject<Item> SNOWDROP = ITEMS.register("snowdrop",
//            () -> new SnowdropItem(basicItem()
//                    .rarity(Rarity.EPIC)
//                    .durability(-1)
//            ));
//
//    public static final RegistryObject<Item> ICICLE = ITEMS.register("icicle",
//            () -> new IcicleItem(basicItem()
//                    .rarity(Rarity.EPIC)
//                    .durability(-1)
//            ));
//
//    public static final RegistryObject<Item> KAFKAS_KATANA = ITEMS.register("kafkas_katana",
//            () -> new KafkasKatanaItem(basicItem()
//                    .rarity(Rarity.EPIC)
//                    .durability(-1)
//            ));
//
//    public static final RegistryObject<Item> SCARLET_OATH = ITEMS.register("scarlet_oath",
//            () -> new ScarletOathItem(basicItem()
//                    .rarity(Rarity.EPIC)
//                    .durability(-1)
//            ));
//
//    public static final RegistryObject<Item> SMOKENADE = ITEMS.register("smokenade",
//            () -> new Item(basicItem()
//                    .rarity(Rarity.EPIC)
//                    .durability(-1)
//            ));
//
//    public static final RegistryObject<Item> BUCKSHOT_SHELL = ITEMS.register("buckshot_shell",
//            () -> new Item(new Item.Properties().stacksTo(64)
//                    .rarity(Rarity.EPIC)
//            ));
//
//    public static final RegistryObject<Item> SLUG_SHELL = ITEMS.register("slug_shell",
//            () -> new Item(new Item.Properties().stacksTo(32)
//                    .rarity(Rarity.EPIC)
//            ));
//
//    public static final RegistryObject<Item> INCENDIARY_SHELL = ITEMS.register("incendiary_shell",
//            () -> new Item(new Item.Properties().stacksTo(16)
//                    .rarity(Rarity.EPIC)
//            ));
//
//    public static final RegistryObject<Item> EXPLOSIVE_SLUG_SHELL = ITEMS.register("explosive_slug_shell",
//            () -> new Item(new Item.Properties().stacksTo(16)
//                    .rarity(Rarity.EPIC)
//            ));
}
