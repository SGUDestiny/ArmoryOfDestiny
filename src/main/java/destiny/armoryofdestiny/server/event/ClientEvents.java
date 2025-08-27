package destiny.armoryofdestiny.server.event;

import com.mojang.logging.LogUtils;
import destiny.armoryofdestiny.client.render.blockentity.ArmorersTinkeringTableRenderer;
import destiny.armoryofdestiny.server.block.ArmorersTinkeringTableBlock;
import destiny.armoryofdestiny.server.util.UtilityVariables;
import destiny.armoryofdestiny.server.registry.BlockEntityRegistry;
import destiny.armoryofdestiny.server.registry.BlockRegistry;
import destiny.armoryofdestiny.server.registry.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onItemColors(RegisterColorHandlersEvent.Item event) {
        LOGGER.info("Loaded item colors");
        event.register((stack, colorIn) -> colorIn != 1 ? -1 : UtilityVariables.getBlueprintColor(stack), ItemRegistry.BLUEPRINT.get());
    }

    @SubscribeEvent
    public static void onBlockColors(RegisterColorHandlersEvent.Block event) {
        LOGGER.info("Loaded block colors");
        event.register((blockState, blockAndTintGetter, blockPos, colorIn) -> colorIn != 1 ? -1 : UtilityVariables.getBlueprintColor(Minecraft.getInstance().level, blockPos), BlockRegistry.ARMORERS_FORGING_TABLE.get());
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityRegistry.ARMORERS_ASSEMBLY_TABLE.get(), ArmorersTinkeringTableRenderer::new);
    }
}
