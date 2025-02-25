package destiny.armoryofdestiny.client.render.item;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.SharpIronyItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import static destiny.armoryofdestiny.server.item.SharpIronyItem.AMMO_COUNT;

public class SharpIronyItemRenderer extends GeoItemRenderer<SharpIronyItem> {
    public SharpIronyItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "sharp_irony")));
    }

    @Override
    public ResourceLocation getTextureLocation(SharpIronyItem animatable) {
        ItemStack stack = getCurrentItemStack();

        if (stack.getTag() != null) {
            int ammoCount = stack.getOrCreateTag().getInt(AMMO_COUNT);

            return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/sharp_irony/sharp_irony_" + ammoCount + ".png");
        } else {
            return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/sharp_irony/sharp_irony_5.png");
        }
    }
}
