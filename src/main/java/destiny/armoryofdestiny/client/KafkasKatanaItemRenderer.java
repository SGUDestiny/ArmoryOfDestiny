package destiny.armoryofdestiny.client;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.weapon.KafkasKatanaItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class KafkasKatanaItemRenderer extends GeoItemRenderer<KafkasKatanaItem> {
    public KafkasKatanaItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "kafkas_katana")));
        //addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
