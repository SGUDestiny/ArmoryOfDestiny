package destiny.armoryofdestiny.client;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.item.SharpIronyItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SharpIronyItemModel extends GeoModel<SharpIronyItem> {

    @Override
    public ResourceLocation getModelResource(SharpIronyItem sharpIronyItem) {
        return new ResourceLocation(ArmoryOfDestiny.MODID, "geo/item/sharp_irony.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SharpIronyItem sharpIronyItem) {
        return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/sharp_irony/sharp_irony_5.png");
    }



    @Override
    public ResourceLocation getAnimationResource(SharpIronyItem sharpIronyItem) {
        return new ResourceLocation(ArmoryOfDestiny.MODID, "animations/item/sharp_irony.animation.json");
    }
}
