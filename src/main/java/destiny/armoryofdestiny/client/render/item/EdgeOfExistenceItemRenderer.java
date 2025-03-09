package destiny.armoryofdestiny.client.render.item;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.EdgeOfExistenceItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class EdgeOfExistenceItemRenderer extends GeoItemRenderer<EdgeOfExistenceItem> {
    public EdgeOfExistenceItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "edge_of_existence")));
    }
}
