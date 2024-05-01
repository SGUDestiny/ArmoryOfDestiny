package destiny.weaponmod.client;

import destiny.weaponmod.WeaponMod;
import destiny.weaponmod.item.DragonslayerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DragonslayerItemRenderer extends GeoItemRenderer<DragonslayerItem> {
    public DragonslayerItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(WeaponMod.MODID, "dragonslayer")));
    }
}
