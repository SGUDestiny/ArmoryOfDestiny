package destiny.weaponmod.client;

import destiny.weaponmod.item.SharpIronyItem;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SharpIronyItemRenderer extends GeoItemRenderer<SharpIronyItem> {

    public SharpIronyItemRenderer() {

        super(new SharpIronyItemModel());
        //addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }


}
