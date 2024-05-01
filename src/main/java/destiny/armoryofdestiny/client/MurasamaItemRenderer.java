package destiny.armoryofdestiny.client;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.item.MurasamaItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MurasamaItemRenderer extends GeoItemRenderer<MurasamaItem> {
    public MurasamaItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "murasama")));
        //addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
