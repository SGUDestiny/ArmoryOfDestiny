package destiny.weaponmod.common;

import destiny.weaponmod.WeaponMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WeaponMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    public static void onTick(TickEvent.PlayerTickEvent event){

    }
}
