package destiny.armoryofdestiny.client.render.item;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.DragonSlayerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DragonSlayerItemRenderer extends GeoItemRenderer<DragonSlayerItem> {
    public DragonSlayerItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "dragon_slayer")));
    }
}
