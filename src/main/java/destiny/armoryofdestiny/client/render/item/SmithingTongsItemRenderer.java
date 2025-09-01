package destiny.armoryofdestiny.client.render.item;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.SmithingTongsItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SmithingTongsItemRenderer extends GeoItemRenderer<SmithingTongsItem> {
    public SmithingTongsItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "smithing_tongs")));
    }

/*    @Override
    public GeoModel<SmithingTongsItem> getGeoModel() {
        return new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, getCurrentItemStack().getItem().toString()));
    }*/

    @Override
    public ResourceLocation getTextureLocation(SmithingTongsItem animatable) {
        return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/smithing_tongs/" + getCurrentItemStack().getItem() + ".png");
    }
}
