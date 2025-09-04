package destiny.armoryofdestiny.client;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.weapon.ScarletOathItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ScarletOathItemRenderer extends GeoItemRenderer<ScarletOathItem> {
    public ScarletOathItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "scarlet_oath")));
        //addRenderLayer(new AutoGlowingGeoLayer<>(this));

    }
}
