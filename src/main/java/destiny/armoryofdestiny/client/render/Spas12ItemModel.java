package destiny.armoryofdestiny.client.render;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.item.Spas12Item;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Spas12ItemModel extends GeoModel<Spas12Item> {

    @Override
    public ResourceLocation getModelResource(Spas12Item spas12Item) {
        return new ResourceLocation(ArmoryOfDestiny.MODID, "geo/item/spas12.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Spas12Item spas12Item) {
        return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/spas12/spas12_buckshot.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Spas12Item spas12Item) {
        return new ResourceLocation(ArmoryOfDestiny.MODID, "animations/item/spas12.animation.json");
    }
}
