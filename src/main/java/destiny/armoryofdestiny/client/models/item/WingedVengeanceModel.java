package destiny.armoryofdestiny.client.models.item;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.WingedVengeanceItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WingedVengeanceModel extends GeoModel<WingedVengeanceItem> {
    @Override
    public ResourceLocation getModelResource(WingedVengeanceItem animatable) {
        return new ResourceLocation(ArmoryOfDestiny.MODID, "geo/armor/winged_vengeance.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WingedVengeanceItem animatable) {
        return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/armor/winged_vengeance.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WingedVengeanceItem animatable) {
        return new ResourceLocation(ArmoryOfDestiny.MODID, "animations/armor/winged_vengeance.animation.json");
    }

    @Override
    public RenderType getRenderType(WingedVengeanceItem animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }
}
