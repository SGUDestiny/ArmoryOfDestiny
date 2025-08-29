package destiny.armoryofdestiny;

import com.mojang.logging.LogUtils;
import destiny.armoryofdestiny.client.render.entity.MetallicFeatherRenderer;
import destiny.armoryofdestiny.server.event.CommonEvents;
import destiny.armoryofdestiny.server.item.utility.*;
import destiny.armoryofdestiny.server.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ArmoryOfDestiny.MODID)
public class ArmoryOfDestiny
{
    public static final String MODID = "armoryofdestiny";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ArmoryOfDestiny()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(new CommonEvents());

        SoundRegistry.SOUNDS.register(modEventBus);
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        CreativeTabRegistry.DEF_REG.register(modEventBus);
        BlockEntityRegistry.BLOCK_ENTITIES.register(modEventBus);
        RecipeRegistry.register(modEventBus);
        LootModifierRegistry.GLOBAL_LOOT_MODIFIER_DEF_REG.register(modEventBus);
        EffectRegistry.DEF_REG.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(EntityRegistry.METALLIC_FEATHER.get(), MetallicFeatherRenderer::new);

            event.enqueueWork(() -> {
                ItemProperties.register(ItemRegistry.SHARP_IRONY.get(), new ResourceLocation(MODID, "is_open"), new SharpIronyItemProperty());
                ItemProperties.register(ItemRegistry.EDGE_OF_EXISTENCE.get(), new ResourceLocation(MODID, "active"), new EdgeOfExistenceItemProperty());
                ItemProperties.register(ItemRegistry.PUNISHER.get(), new ResourceLocation(MODID, "active"), new PunisherItemProperty());
                ItemProperties.register(ItemRegistry.MURASAMA.get(), new ResourceLocation(MODID, "active"), new MurasamaItemProperty());
                ItemProperties.register(ItemRegistry.BLOODLETTER.get(), new ResourceLocation(MODID, "active"), new BloodletterItemProperty());
            });
        }
    }
}
