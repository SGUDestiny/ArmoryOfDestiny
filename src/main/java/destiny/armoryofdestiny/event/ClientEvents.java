package destiny.armoryofdestiny.event;

import com.mojang.logging.LogUtils;
import destiny.armoryofdestiny.block.AssemblyTableBlock;
import destiny.armoryofdestiny.item.BlueprintItem;
import destiny.armoryofdestiny.registry.BlockRegistry;
import destiny.armoryofdestiny.registry.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
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
        event.register((blockState, blockAndTintGetter, blockPos, colorIn) -> colorIn != 0 ? -1 : AssemblyTableBlock.getBlueprintColor(Minecraft.getInstance().level, blockPos), BlockRegistry.ASSEMBLY_TABLE.get());
    }
}
