package destiny.weaponmod.client;

import destiny.weaponmod.WeaponMod;
import destiny.weaponmod.item.KafkasKatanaItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class KafkasKatanaItemRenderer extends GeoItemRenderer<KafkasKatanaItem> {
    public KafkasKatanaItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(WeaponMod.MODID, "kafkas_katana")));
        //addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
