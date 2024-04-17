package destiny.weaponmod.client;

import destiny.weaponmod.WeaponMod;
import destiny.weaponmod.item.ScarletOathItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class ScarletOathItemRenderer extends GeoItemRenderer<ScarletOathItem> {
    public ScarletOathItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(WeaponMod.MODID, "scarlet_oath")));
        //addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
