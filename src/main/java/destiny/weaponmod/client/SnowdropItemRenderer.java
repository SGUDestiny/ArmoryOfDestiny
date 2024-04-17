package destiny.weaponmod.client;

import destiny.weaponmod.WeaponMod;
import destiny.weaponmod.item.MurasamaItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class SnowdropItemRenderer extends GeoItemRenderer<MurasamaItem> {
    public SnowdropItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(WeaponMod.MODID, "snowdrop")));
        //addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
