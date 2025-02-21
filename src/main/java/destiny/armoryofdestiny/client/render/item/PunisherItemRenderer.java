package destiny.armoryofdestiny.client.render.item;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.item.MurasamaItem;
import destiny.armoryofdestiny.item.PunisherItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import static destiny.armoryofdestiny.item.MurasamaItem.ABILITY_TICK;
import static destiny.armoryofdestiny.item.PunisherItem.ANIMATION_TICK;

public class PunisherItemRenderer extends GeoItemRenderer<PunisherItem> {
    public PunisherItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "punisher")));
    }

    @Override
    public ResourceLocation getTextureLocation(PunisherItem animatable) {
        ItemStack stack = getCurrentItemStack();
        CompoundTag tag = stack.getTag();

        if (tag != null) {
            int animationFrame = tag.getInt(ANIMATION_TICK);

            return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/punisher/punisher_" + animationFrame + ".png");
        }
        return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/punisher/punisher_0.png");
    }
}
