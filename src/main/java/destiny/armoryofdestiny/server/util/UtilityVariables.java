package destiny.armoryofdestiny.server.util;

import destiny.armoryofdestiny.server.block.blockentity.ArmorersTinkeringTableBlockEntity;
import destiny.armoryofdestiny.server.item.BlueprintItem;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class UtilityVariables {
    public static final String NONE = "tooltip.armoryofdestiny.rarity.none";

    //Rarities
    public static final String ARMORERS_WORKSHOP_PART = "tooltip.armoryofdestiny.rarity.armorers_workshop_part";
    public static final String SECONDARY = "tooltip.armoryofdestiny.rarity.secondary";
    public static final String UNIQUE = "tooltip.armoryofdestiny.rarity.unique";
    public static final String LEGENDARY = "tooltip.armoryofdestiny.rarity.legendary";

    //Trivia
    public static final String KATANA = "tooltip.armoryofdestiny.trivia.katana";
    public static final String THROWING_FAN = "tooltip.armoryofdestiny.trivia.throwing_fan";
    public static final String HAMMER = "tooltip.armoryofdestiny.trivia.hammer";
    public static final String GREATSWORD = "tooltip.armoryofdestiny.trivia.greatsword";
    public static final String BLUEPRINT = "tooltip.armoryofdestiny.trivia.blueprint";
    public static final String SMITHING_HAMMER = "tooltip.armoryofdestiny.trivia.smithing_hammer";
    public static final String ARMORERS_CRAFTING_TABLE = "tooltip.armoryofdestiny.trivia.armorers_crafting_table";
    public static final String ARMORERS_TINKERING_TABLE = "tooltip.armoryofdestiny.trivia.armorers_tinkering_table";
    public static final String RAPIER = "tooltip.armoryofdestiny.trivia.rapier";

    public static int getBlueprintColor(ItemStack stack) {
        if (stack.getTag() != null) {
            String blueprintItem = stack.getOrCreateTag().getString("blueprintItem");
            return getBlueprintColor(blueprintItem);
        }
        return 0xFFFFFF;
    }

    public static int getBlueprintColor(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof ArmorersTinkeringTableBlockEntity table) {
            String blueprintItem = table.getItemFromBlueprint();
            return getBlueprintColor(blueprintItem);
        }
        return 0xFFFFFF;
    }

    public static int getBlueprintColor(String itemId) {
        if (itemId.equals(ItemRegistry.MURASAMA.getKey().location().toString())) {
            return 0xE63C3C;
        } else if (itemId.equals(ItemRegistry.GUN_SHEATH.getKey().location().toString())) {
            return 0x99B3CC;
        } else if (itemId.equals(ItemRegistry.ORIGINIUM_CATALYST.getKey().location().toString())) {
            return 0xFFB366;
        } else if (itemId.equals(ItemRegistry.DRAGON_SLAYER.getKey().location().toString())) {
            return 0x4D4D4D;
        }  else if (itemId.equals(ItemRegistry.SHARP_IRONY.getKey().location().toString())) {
            return 0x4E5F80;
        } else if (itemId.equals(ItemRegistry.PUNISHER.getKey().location().toString())) {
            return 0x5CCFE6;
        } else if (itemId.equals(ItemRegistry.BLOODLETTER.getKey().location().toString())) {
            return 0xB31432;
        }  else if (itemId.equals(ItemRegistry.CRUCIBLE_INACTIVE.getKey().location().toString())) {
            return 0xFF3D33;
        } else {
            return 0xFFFFFF;
        }
    }
}
