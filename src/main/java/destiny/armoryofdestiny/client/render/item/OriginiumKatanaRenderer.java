package destiny.armoryofdestiny.client.render.item;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.item.OriginiumKatanaItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import static destiny.armoryofdestiny.item.OriginiumKatanaItem.ANIMATION_TICK;

public class OriginiumKatanaRenderer extends GeoItemRenderer<OriginiumKatanaItem> {
    public OriginiumKatanaRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "originium_katana")));
    }

    @Override
    public ResourceLocation getTextureLocation(OriginiumKatanaItem animatable) {
        ItemStack stack = getCurrentItemStack();
        CompoundTag tag = stack.getTag();

        if (tag != null) {
            int animationTick = tag.getInt(ANIMATION_TICK);

            return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/originium_katana/originium_katana_" + animationTick / 2 + ".png");
        }
        return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/originium_katana/originium_katana_0.png");
    }
}
