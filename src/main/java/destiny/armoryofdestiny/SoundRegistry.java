package destiny.armoryofdestiny;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ArmoryOfDestiny.MODID);

    public static RegistryObject<SoundEvent> SHARP_IRONY_OPEN = registerSoundEvent("sharp_irony_open");

    public static RegistryObject<SoundEvent> SHARP_IRONY_CLOSE = registerSoundEvent("sharp_irony_close");

    public static RegistryObject<SoundEvent> SHARP_IRONY_THROW = registerSoundEvent("sharp_irony_throw");

    public static RegistryObject<SoundEvent> METALLIC_FEATHER_HIT = registerSoundEvent("metallic_feather_hit");

    public static RegistryObject<SoundEvent> SHARP_IRONY_THROW_SPECIAL = registerSoundEvent("sharp_irony_throw_special");

    public static RegistryObject<SoundEvent> DOUBLE_TROUBLE_OPEN = registerSoundEvent("double_trouble_open");

    public static RegistryObject<SoundEvent> DOUBLE_TROUBLE_CLOSE = registerSoundEvent("double_trouble_close");

    public static RegistryObject<SoundEvent> DOUBLE_TROUBLE_RELOAD = registerSoundEvent("double_trouble_reload");

    public static RegistryObject<SoundEvent> DOUBLE_TROUBLE_SHOOT = registerSoundEvent("double_trouble_shoot");

    public static RegistryObject<SoundEvent> DOUBLE_TROUBLE_EMPTY = registerSoundEvent("double_trouble_empty");

    private static RegistryObject<SoundEvent> registerSoundEvent(String sound)
    {
        return SOUNDS.register(sound, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ArmoryOfDestiny.MODID, sound)));
    }
}
