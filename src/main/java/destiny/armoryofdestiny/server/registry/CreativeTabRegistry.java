package destiny.armoryofdestiny.server.registry;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.recipe.TinkeringRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static destiny.armoryofdestiny.server.item.BlueprintItem.RECIPE;
import static destiny.armoryofdestiny.server.item.weapon.SharpIronyItem.AMMO_COUNT;
import static destiny.armoryofdestiny.server.item.weapon.SharpIronyItem.IS_OPEN;
import static destiny.armoryofdestiny.server.item.weapon.CrucibleItem.USAGES;

public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> DEF_REG  = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArmoryOfDestiny.MODID);

    public static final RegistryObject<CreativeModeTab> WEAPONS = DEF_REG.register("weapons", () -> CreativeModeTab.builder()
            .icon(() -> ItemRegistry.MURASAMA.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.armoryofdestiny.weapons"))
            .displayItems((parameters, output) -> {
                //Craftable items
                output.accept(ItemRegistry.MURASAMA.get());
                output.accept(ItemRegistry.MURASAMA_SHEATHED.get());
                output.accept(ItemRegistry.GUN_SHEATH.get());
                output.accept(ItemRegistry.EDGE_OF_EXISTENCE.get());
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
            })
            .build()
    );

    public static final RegistryObject<CreativeModeTab> TOOLS = DEF_REG.register("tools", () -> CreativeModeTab.builder()
            .icon(() -> ItemRegistry.NETHERITE_SMITHING_TONGS.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.armoryofdestiny.tools"))
            .withTabsBefore(WEAPONS.getKey())
            .displayItems((parameters, output) -> {
                //Armorer's workshop
                output.accept(BlockRegistry.ARMORERS_CRAFTING_TABLE.get());
                output.accept(BlockRegistry.ARMORERS_TINKERING_TABLE.get());

                output.accept(ItemRegistry.GOLD_SMITHING_HAMMER.get());
                output.accept(ItemRegistry.IRON_SMITHING_HAMMER.get());
                output.accept(ItemRegistry.DIAMOND_SMITHING_HAMMER.get());
                output.accept(ItemRegistry.NETHERITE_SMITHING_HAMMER.get());

                //Armorer's forge
                output.accept(BlockRegistry.ARMORERS_ANVIL.get());
                output.accept(BlockRegistry.BLOOMERY_TOP.get());
                output.accept(BlockRegistry.BLOOMERY_BOTTOM.get());
                output.accept(BlockRegistry.NETHER_BLOOMERY_TOP.get());
                output.accept(BlockRegistry.NETHER_BLOOMERY_BOTTOM.get());
                output.accept(BlockRegistry.TEMPERING_BARREL.get());

                output.accept(ItemRegistry.GOLD_SMITHING_TONGS.get());
                output.accept(ItemRegistry.IRON_SMITHING_TONGS.get());
                output.accept(ItemRegistry.DIAMOND_SMITHING_TONGS.get());
                output.accept(ItemRegistry.NETHERITE_SMITHING_TONGS.get());
            })
            .build()
    );

    public static final RegistryObject<CreativeModeTab> MATERIALS = DEF_REG.register("materials", () -> CreativeModeTab.builder()
            .icon(() -> ItemRegistry.HOT_NETHERITE_INGOT.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.armoryofdestiny.materials"))
            .withTabsBefore(TOOLS.getKey())
            .displayItems((parameters, output) -> {
                //Components
                output.accept(ItemRegistry.MURASAMA_BLADE.get());
                output.accept(ItemRegistry.MURASAMA_HAND_GUARD.get());
                output.accept(ItemRegistry.MURASAMA_HANDLE.get());
                output.accept(ItemRegistry.DRAGON_SLAYER_BLADE.get());
                output.accept(ItemRegistry.DRAGON_SLAYER_HANDLE.get());
                output.accept(ItemRegistry.DRAGON_SLAYER_POMMEL.get());
                output.accept(ItemRegistry.GUN_SHEATH_CHAMBER.get());
                output.accept(ItemRegistry.GUN_SHEATH_MAGAZINE.get());
                output.accept(ItemRegistry.GUN_SHEATH_SCABBARD.get());
                output.accept(ItemRegistry.SHARP_IRONY_BEARING.get());
                output.accept(ItemRegistry.SHARP_IRONY_WING.get());
                output.accept(ItemRegistry.CRUCIBLE_EMBLEM.get());
                output.accept(ItemRegistry.CRUCIBLE_HAND_GUARD.get());
                output.accept(ItemRegistry.CRUCIBLE_HANDLE.get());
                output.accept(ItemRegistry.PUNISHER_HEAD.get());
                output.accept(ItemRegistry.PUNISHER_HANDLE.get());
                output.accept(ItemRegistry.PUNISHER_ENGINE.get());

                output.accept(ItemRegistry.HOT_MURASAMA_BLADE.get());
                output.accept(ItemRegistry.HOT_MURASAMA_HAND_GUARD.get());
                output.accept(ItemRegistry.HOT_MURASAMA_HANDLE.get());
                output.accept(ItemRegistry.HOT_DRAGON_SLAYER_BLADE.get());
                output.accept(ItemRegistry.HOT_DRAGON_SLAYER_HANDLE.get());
                output.accept(ItemRegistry.HOT_DRAGON_SLAYER_POMMEL.get());
                output.accept(ItemRegistry.HOT_GUN_SHEATH_CHAMBER.get());
                output.accept(ItemRegistry.HOT_GUN_SHEATH_MAGAZINE.get());
                output.accept(ItemRegistry.HOT_GUN_SHEATH_SCABBARD.get());
                output.accept(ItemRegistry.HOT_SHARP_IRONY_BEARING.get());
                output.accept(ItemRegistry.HOT_SHARP_IRONY_WING.get());
                output.accept(ItemRegistry.HOT_CRUCIBLE_EMBLEM.get());
                output.accept(ItemRegistry.HOT_CRUCIBLE_HAND_GUARD.get());
                output.accept(ItemRegistry.HOT_CRUCIBLE_HANDLE.get());
                output.accept(ItemRegistry.HOT_PUNISHER_HEAD.get());
                output.accept(ItemRegistry.HOT_CRUCIBLE_HANDLE.get());
                output.accept(ItemRegistry.HOT_PUNISHER_ENGINE.get());

                output.accept(ItemRegistry.HOT_NETHERITE_INGOT.get());
                output.accept(ItemRegistry.HOT_DIAMOND.get());
                output.accept(ItemRegistry.HOT_GOLD_INGOT.get());
                output.accept(ItemRegistry.HOT_GOLD_NUGGET.get());
                output.accept(ItemRegistry.HOT_IRON_INGOT.get());
                output.accept(ItemRegistry.HOT_IRON_NUGGET.get());
                output.accept(ItemRegistry.HOT_COPPER_INGOT.get());

                output.accept(ItemRegistry.HOT_NETHERITE_BLOCK.get());
                output.accept(ItemRegistry.HOT_DIAMOND_BLOCK.get());
                output.accept(ItemRegistry.HOT_GOLD_BLOCK.get());
                output.accept(ItemRegistry.HOT_IRON_BLOCK.get());
                output.accept(ItemRegistry.HOT_IRON_BARS.get());
                output.accept(ItemRegistry.HOT_COPPER_BLOCK.get());

                output.accept(ItemRegistry.HOT_RAW_GOLD_BLOCK.get());
                output.accept(ItemRegistry.HOT_RAW_IRON_BLOCK.get());
                output.accept(ItemRegistry.HOT_RAW_COPPER_BLOCK.get());

                //Blueprints
                List<TinkeringRecipe> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(TinkeringRecipe.Type.INSTANCE);
                for (TinkeringRecipe recipe : recipes)
                {
                    output.accept(createBlueprint(recipe.recipeID));
                }
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

    private static ItemStack createBlueprint(ResourceLocation recipeID) {
        ItemStack blueprint = new ItemStack(ItemRegistry.BLUEPRINT.get());

        blueprint.getOrCreateTag().putString(RECIPE, recipeID.toString());
        return blueprint;
    }
}
