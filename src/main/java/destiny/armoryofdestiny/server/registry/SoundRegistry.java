package destiny.armoryofdestiny.server.registry;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ArmoryOfDestiny.MODID);

    public static RegistryObject<SoundEvent> ARMORERS_ANVIL_HIT = registerSoundEvent("armorers_anvil_hit");

    public static RegistryObject<SoundEvent> EDGE_OF_EXISTENCE_ACTIVATE = registerSoundEvent("edge_of_existence_activate");
    public static RegistryObject<SoundEvent> EDGE_OF_EXISTENCE_DEACTIVATE = registerSoundEvent("edge_of_existence_deactivate");

    public static RegistryObject<SoundEvent> CRUCIBLE_SWING = registerSoundEvent("crucible_swing");
    public static RegistryObject<SoundEvent> CRUCIBLE_DEACTIVATE = registerSoundEvent("crucible_deactivate");
    public static RegistryObject<SoundEvent> CRUCIBLE_ACTIVATE = registerSoundEvent("crucible_activate");

    public static RegistryObject<SoundEvent> BLOODLETTER_HIT = registerSoundEvent("bloodletter_hit");
    public static RegistryObject<SoundEvent> BLOODLETTER_DEACTIVATE = registerSoundEvent("bloodletter_deactivate");
    public static RegistryObject<SoundEvent> BLOODLETTER_ACTIVATE = registerSoundEvent("bloodletter_activate");

    public static RegistryObject<SoundEvent> SMITHING_HAMMER_HIT = registerSoundEvent("smithing_hammer_hit");

    public static RegistryObject<SoundEvent> PUNISHER_ATTACK = registerSoundEvent("punisher_attack");
    public static RegistryObject<SoundEvent> PUNISHER_DEACTIVATE = registerSoundEvent("punisher_deactivate");
    public static RegistryObject<SoundEvent> PUNISHER_ACTIVATE = registerSoundEvent("punisher_activate");

    public static RegistryObject<SoundEvent> ORIGINIUM_CATALYST_ACTIVATE = registerSoundEvent("originium_catalyst_activate");

    public static RegistryObject<SoundEvent> MURASAMA_INSERT = registerSoundEvent("murasama_insert");
    public static RegistryObject<SoundEvent> MURASAMA_SELECT = registerSoundEvent("murasama_select");
    public static RegistryObject<SoundEvent> MURASAMA_SHOOT = registerSoundEvent("murasama_shoot");
    public static RegistryObject<SoundEvent> MURASAMA_SPECIAL_HIT = registerSoundEvent("murasama_special_hit");
    public static RegistryObject<SoundEvent> MURASAMA_SHEATH = registerSoundEvent("murasama_sheath");

    public static RegistryObject<SoundEvent> SHARP_IRONY_OPEN = registerSoundEvent("sharp_irony_open");
    public static RegistryObject<SoundEvent> SHARP_IRONY_CLOSE = registerSoundEvent("sharp_irony_close");
    public static RegistryObject<SoundEvent> SHARP_IRONY_THROW = registerSoundEvent("sharp_irony_throw");
    public static RegistryObject<SoundEvent> SHARP_IRONY_THROW_SPECIAL = registerSoundEvent("sharp_irony_throw_special");
    public static RegistryObject<SoundEvent> METALLIC_FEATHER_HIT = registerSoundEvent("metallic_feather_hit");

    public static RegistryObject<SoundEvent> DOUBLE_TROUBLE_OPEN = registerSoundEvent("double_trouble_open");
    public static RegistryObject<SoundEvent> DOUBLE_TROUBLE_CLOSE = registerSoundEvent("double_trouble_close");
    public static RegistryObject<SoundEvent> DOUBLE_TROUBLE_RELOAD = registerSoundEvent("double_trouble_reload");
    public static RegistryObject<SoundEvent> DOUBLE_TROUBLE_SHOOT = registerSoundEvent("double_trouble_shoot");
    public static RegistryObject<SoundEvent> DOUBLE_TROUBLE_EMPTY = registerSoundEvent("double_trouble_empty");

    public static RegistryObject<SoundEvent> SPAS12_INSERT = registerSoundEvent("spas12_insert");
    public static RegistryObject<SoundEvent> SPAS12_PUMP = registerSoundEvent("spas12_pump");
    public static RegistryObject<SoundEvent> SPAS12_SHOOT = registerSoundEvent("spas12_shoot");
    public static RegistryObject<SoundEvent> SPAS12_SWITCH = registerSoundEvent("spas12_switch");

    public static RegistryObject<SoundEvent> SHELL_TINK = registerSoundEvent("shell_tink");

    private static RegistryObject<SoundEvent> registerSoundEvent(String sound)
    {
        return SOUNDS.register(sound, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ArmoryOfDestiny.MODID, sound)));
    }
}
