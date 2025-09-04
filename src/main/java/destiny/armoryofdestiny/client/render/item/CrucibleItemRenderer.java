package destiny.armoryofdestiny.client.render.item;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.weapon.CrucibleItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CrucibleItemRenderer extends GeoItemRenderer<CrucibleItem> {
    public CrucibleItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "crucible")));
    }

    @Override
    public ResourceLocation getTextureLocation(CrucibleItem animatable) {
        return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/crucible/crucible.png");
    }
}
