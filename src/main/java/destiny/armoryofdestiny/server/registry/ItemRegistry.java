package destiny.armoryofdestiny.server.registry;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.*;
import destiny.armoryofdestiny.server.item.weapon.*;
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
            () -> new SmithingHammerItem(new Item.Properties().stacksTo(1).durability(16), Items.GOLD_INGOT, 4));
    public static final RegistryObject<Item> IRON_SMITHING_HAMMER = ITEMS.register("iron_smithing_hammer",
            () -> new SmithingHammerItem(new Item.Properties().stacksTo(1).durability(32), Items.IRON_INGOT, 6));
    public static final RegistryObject<Item> DIAMOND_SMITHING_HAMMER = ITEMS.register("diamond_smithing_hammer",
            () -> new SmithingHammerItem(new Item.Properties().stacksTo(1).durability(64), Items.DIAMOND, 8));
    public static final RegistryObject<Item> NETHERITE_SMITHING_HAMMER = ITEMS.register("netherite_smithing_hammer",
            () -> new SmithingHammerItem(new Item.Properties().stacksTo(1).durability(128), Items.NETHERITE_INGOT, 10));

    public static final RegistryObject<Item> GOLD_SMITHING_TONGS = ITEMS.register("gold_smithing_tongs",
            () -> new SmithingTongsItem(new Item.Properties().stacksTo(1).durability(16), Items.GOLD_INGOT));
    public static final RegistryObject<Item> IRON_SMITHING_TONGS = ITEMS.register("iron_smithing_tongs",
            () -> new SmithingTongsItem(new Item.Properties().stacksTo(1).durability(32), Items.IRON_INGOT));
    public static final RegistryObject<Item> DIAMOND_SMITHING_TONGS = ITEMS.register("diamond_smithing_tongs",
            () -> new SmithingTongsItem(new Item.Properties().stacksTo(1).durability(64), Items.DIAMOND));
    public static final RegistryObject<Item> NETHERITE_SMITHING_TONGS = ITEMS.register("netherite_smithing_tongs",
            () -> new SmithingTongsItem(new Item.Properties().stacksTo(1).durability(128), Items.NETHERITE_INGOT));

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

    //Materials
    public static final RegistryObject<Item> MURASAMA_BLADE = ITEMS.register("murasama_blade",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MURASAMA_HAND_GUARD = ITEMS.register("murasama_hand_guard",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MURASAMA_HANDLE = ITEMS.register("murasama_handle",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DRAGON_SLAYER_BLADE = ITEMS.register("dragon_slayer_blade",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DRAGON_SLAYER_HANDLE = ITEMS.register("dragon_slayer_handle",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DRAGON_SLAYER_POMMEL = ITEMS.register("dragon_slayer_pommel",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GUN_SHEATH_CHAMBER = ITEMS.register("gun_sheath_chamber",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GUN_SHEATH_MAGAZINE = ITEMS.register("gun_sheath_magazine",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GUN_SHEATH_SCABBARD = ITEMS.register("gun_sheath_scabbard",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SHARP_IRONY_BEARING = ITEMS.register("sharp_irony_bearing",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SHARP_IRONY_WING = ITEMS.register("sharp_irony_wing",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CRUCIBLE_EMBLEM = ITEMS.register("crucible_emblem",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CRUCIBLE_HAND_GUARD = ITEMS.register("crucible_hand_guard",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CRUCIBLE_HANDLE = ITEMS.register("crucible_handle",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PUNISHER_HEAD = ITEMS.register("punisher_head",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PUNISHER_HANDLE = ITEMS.register("punisher_handle",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PUNISHER_ENGINE = ITEMS.register("punisher_engine",
            () -> new MaterialItem(new Item.Properties().stacksTo(1)));

    //Hot materials
    public static final RegistryObject<Item> HOT_MURASAMA_BLADE = ITEMS.register("hot_murasama_blade",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_MURASAMA_HAND_GUARD = ITEMS.register("hot_murasama_hand_guard",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_MURASAMA_HANDLE = ITEMS.register("hot_murasama_handle",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_DRAGON_SLAYER_BLADE = ITEMS.register("hot_dragon_slayer_blade",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_DRAGON_SLAYER_HANDLE = ITEMS.register("hot_dragon_slayer_handle",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_DRAGON_SLAYER_POMMEL = ITEMS.register("hot_dragon_slayer_pommel",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_GUN_SHEATH_CHAMBER = ITEMS.register("hot_gun_sheath_chamber",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_GUN_SHEATH_MAGAZINE = ITEMS.register("hot_gun_sheath_magazine",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_GUN_SHEATH_SCABBARD = ITEMS.register("hot_gun_sheath_scabbard",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_SHARP_IRONY_BEARING = ITEMS.register("hot_sharp_irony_bearing",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_SHARP_IRONY_WING = ITEMS.register("hot_sharp_irony_wing",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_CRUCIBLE_EMBLEM = ITEMS.register("hot_crucible_emblem",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_CRUCIBLE_HAND_GUARD = ITEMS.register("hot_crucible_hand_guard",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_CRUCIBLE_HANDLE = ITEMS.register("hot_crucible_handle",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_PUNISHER_HEAD = ITEMS.register("hot_punisher_head",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_PUNISHER_HANDLE = ITEMS.register("hot_punisher_handle",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_PUNISHER_ENGINE = ITEMS.register("hot_punisher_engine",
            () -> new HotItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> HOT_NETHERITE_INGOT = ITEMS.register("hot_netherite_ingot",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_DIAMOND = ITEMS.register("hot_diamond",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_GOLD_INGOT = ITEMS.register("hot_gold_ingot",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_GOLD_NUGGET = ITEMS.register("hot_gold_nugget",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_IRON_INGOT = ITEMS.register("hot_iron_ingot",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_IRON_NUGGET = ITEMS.register("hot_iron_nugget",
            () -> new HotItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_COPPER_INGOT = ITEMS.register("hot_copper_nugget",
            () -> new HotItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> HOT_IRON_BARS = ITEMS.register("hot_iron_bars",
            () -> new HotBlockItem(BlockRegistry.HOT_IRON_BARS.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_RAW_COPPER_BLOCK = ITEMS.register("hot_raw_copper_block",
            () -> new HotBlockItem(BlockRegistry.HOT_RAW_COPPER_BLOCK.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_RAW_IRON_BLOCK = ITEMS.register("hot_raw_iron_block",
            () -> new HotBlockItem(BlockRegistry.HOT_RAW_IRON_BLOCK.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_RAW_GOLD_BLOCK = ITEMS.register("hot_raw_gold_block",
            () -> new HotBlockItem(BlockRegistry.HOT_RAW_GOLD_BLOCK.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_COPPER_BLOCK = ITEMS.register("hot_copper_block",
            () -> new HotBlockItem(BlockRegistry.HOT_COPPER_BLOCK.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_IRON_BLOCK = ITEMS.register("hot_iron_block",
            () -> new HotBlockItem(BlockRegistry.HOT_IRON_BLOCK.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_GOLD_BLOCK = ITEMS.register("hot_gold_block",
            () -> new HotBlockItem(BlockRegistry.HOT_GOLD_BLOCK.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_DIAMOND_BLOCK = ITEMS.register("hot_diamond_block",
            () -> new HotBlockItem(BlockRegistry.HOT_DIAMOND_BLOCK.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOT_NETHERITE_BLOCK = ITEMS.register("hot_netherite_block",
            () -> new HotBlockItem(BlockRegistry.HOT_NETHERITE_BLOCK.get(), new Item.Properties().stacksTo(1)));

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
