package destiny.armoryofdestiny.client;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.weapon.SmokenadeItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SmokenadeItemRenderer extends GeoItemRenderer<SmokenadeItem> {
    public SmokenadeItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "smokenade")));
        //addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}