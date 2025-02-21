package destiny.armoryofdestiny.client.render.item;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.item.MurasamaItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import static destiny.armoryofdestiny.item.MurasamaItem.ABILITY_TICK;

public class MurasamaItemRenderer extends GeoItemRenderer<MurasamaItem> {
    public MurasamaItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "murasama")));
    }

    @Override
    public ResourceLocation getTextureLocation(MurasamaItem animatable) {
        ItemStack stack = getCurrentItemStack();
        CompoundTag tag = stack.getTag();

        if (tag != null) {
            int animationFrame = tag.getInt(ABILITY_TICK);

            return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/murasama/murasama_" + animationFrame + ".png");
        }
        return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/murasama/murasama_0.png");
    }
}
