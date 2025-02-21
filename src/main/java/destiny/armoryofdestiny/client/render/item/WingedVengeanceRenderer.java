package destiny.armoryofdestiny.client.render.item;

import destiny.armoryofdestiny.client.models.item.WingedVengeanceModel;
import destiny.armoryofdestiny.item.WingedVengeanceItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class WingedVengeanceRenderer extends GeoArmorRenderer<WingedVengeanceItem> {
    public WingedVengeanceRenderer() {
        super(new WingedVengeanceModel());
    }

    @Override
    public RenderType getRenderType(WingedVengeanceItem animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick)
    {
        return RenderType.entityTranslucent(texture);
    }
}
