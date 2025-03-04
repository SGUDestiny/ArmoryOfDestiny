package destiny.armoryofdestiny.client.render.item;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.CrucibleInactiveItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CrucibleInactiveItemRenderer extends GeoItemRenderer<CrucibleInactiveItem> {
    public CrucibleInactiveItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "crucible_inactive")));
    }

    @Override
    public ResourceLocation getTextureLocation(CrucibleInactiveItem animatable) {
        ItemStack stack = getCurrentItemStack();
        CompoundTag tag = stack.getTag();

        if (tag != null) {
            if (tag.getBoolean("isActive")) {
                return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/crucible_inactive_awoken.png");
            }
        }
        return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/crucible_inactive_dormant.png");
    }
}
