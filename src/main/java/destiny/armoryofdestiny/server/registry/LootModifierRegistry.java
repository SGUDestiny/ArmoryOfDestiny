package destiny.armoryofdestiny.server.registry;

import com.mojang.serialization.Codec;
import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.loot.BlueprintLootModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LootModifierRegistry {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_DEF_REG = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ArmoryOfDestiny.MODID);

    public static final RegistryObject<Codec<BlueprintLootModifier>> BLUEPRINT_LOOT_MODIFIER = GLOBAL_LOOT_MODIFIER_DEF_REG.register("blueprint", BlueprintLootModifier.CODEC);
}
