package destiny.armoryofdestiny;

import com.mojang.logging.LogUtils;
import destiny.armoryofdestiny.client.MetallicFeatherRenderer;
import destiny.armoryofdestiny.client.render.blockentity.AssemblyTableRenderer;
import destiny.armoryofdestiny.client.render.entity.PelletRenderer;
import destiny.armoryofdestiny.client.render.entity.BuckshotRenderer;
import destiny.armoryofdestiny.client.render.entity.ExplosiveSlugRenderer;
import destiny.armoryofdestiny.client.render.entity.SlugRenderer;
import destiny.armoryofdestiny.client.render.entity.SparkRenderer;
import destiny.armoryofdestiny.event.ClientEvents;
import destiny.armoryofdestiny.event.CommonEvents;
import destiny.armoryofdestiny.item.utility.MurasamaItemProperty;
import destiny.armoryofdestiny.item.utility.PunisherItemProperty;
import destiny.armoryofdestiny.item.utility.SharpIronyItemProperty;
import destiny.armoryofdestiny.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
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
        RecipeTypeRegistry.SERIALIZERS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

            EntityRenderers.register(EntityRegistry.METALLIC_FEATHER.get(), MetallicFeatherRenderer::new);
            EntityRenderers.register(EntityRegistry.PELLET.get(), PelletRenderer::new);
            EntityRenderers.register(EntityRegistry.BUCKSHOT.get(), BuckshotRenderer::new);
            EntityRenderers.register(EntityRegistry.SLUG.get(), SlugRenderer::new);
            EntityRenderers.register(EntityRegistry.SPARK.get(), SparkRenderer::new);
            EntityRenderers.register(EntityRegistry.EXPLOSIVE_SLUG.get(), ExplosiveSlugRenderer::new);
            //BlockEntityRenderers.register(BlockEntityRegistry.ASSEMBLY_TABLE.get(), AssemblyTableRenderer::new);

            event.enqueueWork(() -> {
                ItemProperties.register(ItemRegistry.SHARP_IRONY.get(), new ResourceLocation(MODID, "is_open"), new SharpIronyItemProperty());
                ItemProperties.register(ItemRegistry.PUNISHER.get(), new ResourceLocation(MODID, "active"), new PunisherItemProperty());
                ItemProperties.register(ItemRegistry.MURASAMA.get(), new ResourceLocation(MODID, "active"), new MurasamaItemProperty());
            });
        }
    }
}
