package destiny.armoryofdestiny.client;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.weapon.MurasamaItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SnowdropItemRenderer extends GeoItemRenderer<MurasamaItem> {
    public SnowdropItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "snowdrop")));
        //addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
