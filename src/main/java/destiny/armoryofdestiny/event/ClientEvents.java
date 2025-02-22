package destiny.armoryofdestiny.event;

import com.mojang.logging.LogUtils;
import destiny.armoryofdestiny.block.ArmorersAssemblyTableBlock;
import destiny.armoryofdestiny.client.render.blockentity.ArmorersAssemblyTableRenderer;
import destiny.armoryofdestiny.item.BlueprintItem;
import destiny.armoryofdestiny.registry.BlockEntityRegistry;
import destiny.armoryofdestiny.registry.BlockRegistry;
import destiny.armoryofdestiny.registry.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onItemColors(RegisterColorHandlersEvent.Item event) {
        LOGGER.info("Loaded item colors");
        event.register((stack, colorIn) -> colorIn != 0 ? -1 : BlueprintItem.getBlueprintColor(stack), ItemRegistry.BLUEPRINT.get());
    }

    @SubscribeEvent
    public static void onBlockColors(RegisterColorHandlersEvent.Block event) {
        LOGGER.info("Loaded block colors");
        event.register((blockState, blockAndTintGetter, blockPos, colorIn) -> colorIn != 0 ? -1 : ArmorersAssemblyTableBlock.getBlueprintColor(Minecraft.getInstance().level, blockPos), BlockRegistry.ARMORERS_ASSEMBLY_TABLE.get());
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityRegistry.ASSEMBLY_TABLE.get(), ArmorersAssemblyTableRenderer::new);
    }
}
