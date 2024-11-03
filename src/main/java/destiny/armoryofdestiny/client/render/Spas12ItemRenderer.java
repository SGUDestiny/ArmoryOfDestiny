package destiny.armoryofdestiny.client.render;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.item.DragonslayerItem;
import destiny.armoryofdestiny.item.Spas12Item;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class Spas12ItemRenderer extends GeoItemRenderer<Spas12Item> {

    private final ResourceLocation SPAS12 = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/spas12/spas12.png");

    public Spas12ItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "spas12")));
    }

    @Override
    public ResourceLocation getTextureLocation(Spas12Item animatable) {
        return SPAS12;
    }
}
