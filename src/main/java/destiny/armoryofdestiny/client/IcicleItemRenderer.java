package destiny.armoryofdestiny.client;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.item.MurasamaItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class IcicleItemRenderer extends GeoItemRenderer<MurasamaItem> {
    public IcicleItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "icicle")));
        //addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
