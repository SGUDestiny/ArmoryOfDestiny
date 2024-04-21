package destiny.weaponmod.client;

import destiny.weaponmod.WeaponMod;
import destiny.weaponmod.item.DoubleTroubleItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DoubleTroubleItemModel extends GeoModel<DoubleTroubleItem> {
    @Override
    public ResourceLocation getModelResource(DoubleTroubleItem doubleTroubleItem) {
        return new ResourceLocation(WeaponMod.MODID, "geo/item/double_trouble.geo.json");
    }
    @Override public ResourceLocation getTextureResource(DoubleTroubleItem doubleTroubleItem) {
        return new ResourceLocation(WeaponMod.MODID, "textures/item/double_trouble/double_trouble_both.png");
    }

    @Override public ResourceLocation getAnimationResource(DoubleTroubleItem doubleTroubleItem) {
        return new ResourceLocation(WeaponMod.MODID, "animations/item/double_trouble.animation.json");
    }
}
