package destiny.armoryofdestiny.server.potion;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.registry.EffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class NonexistenceEffect extends MobEffect {
    public NonexistenceEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Mod.EventBusSubscriber(modid = ArmoryOfDestiny.MODID, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onEntityRender(RenderLivingEvent.Pre<?, ?> event) {
            if (event.getEntity().hasEffect(EffectRegistry.NONEXISTENCE.get())) {
                event.setCanceled(true);
            }
        }
    }
}
