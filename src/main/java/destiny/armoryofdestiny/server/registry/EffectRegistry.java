package destiny.armoryofdestiny.server.registry;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.potion.NonexistenceEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectRegistry {
    public static final DeferredRegister<MobEffect> DEF_REG = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ArmoryOfDestiny.MODID);

    public static final RegistryObject<MobEffect> NONEXISTENCE = DEF_REG.register("nonexistence", () -> new NonexistenceEffect(MobEffectCategory.NEUTRAL, 0x530000));
}
