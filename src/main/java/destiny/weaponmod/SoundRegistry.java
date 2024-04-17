package destiny.weaponmod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WeaponMod.MODID);

    public static RegistryObject<SoundEvent> SHARP_IRONY_OPEN = registerSoundEvent("sharp_irony_open");

    public static RegistryObject<SoundEvent> SHARP_IRONY_CLOSE = registerSoundEvent("sharp_irony_close");

    public static RegistryObject<SoundEvent> SHARP_IRONY_THROW = registerSoundEvent("sharp_irony_throw");

    private static RegistryObject<SoundEvent> registerSoundEvent(String sound)
    {
        return SOUNDS.register(sound, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WeaponMod.MODID, sound)));
    }
}
