package destiny.weaponmod.client;

import destiny.weaponmod.WeaponMod;
import destiny.weaponmod.item.MurasamaItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class IcicleItemRenderer extends GeoItemRenderer<MurasamaItem> {
    public IcicleItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(WeaponMod.MODID, "icicle")));
        //addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
