package destiny.armoryofdestiny.client.render.item;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.BloodletterItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import static destiny.armoryofdestiny.server.item.BloodletterItem.*;

public class BloodletterItemRenderer extends GeoItemRenderer<BloodletterItem> {
    public BloodletterItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "bloodletter")));
    }

    @Override
    public ResourceLocation getTextureLocation(BloodletterItem animatable) {
        ItemStack stack = getCurrentItemStack();
        CompoundTag tag = stack.getTag();

        if (tag != null) {
            int animationFrame = tag.getInt(ABILITY_TICK);
            int storedBlood = tag.getInt(STORED_BLOOD);

            if (animationFrame > 0) {
                return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/bloodletter/bloodletter_" + (animationFrame - 1) + ".png");
            } else if (storedBlood >= maxBlood) {
                return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/bloodletter/bloodletter_full.png");
            } else if (storedBlood >= maxBlood / 2) {
                return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/bloodletter/bloodletter_half.png");
            } else if (storedBlood == 0) {
                return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/bloodletter/bloodletter_empty.png");
            }
        }
        return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/bloodletter/bloodletter_empty.png");
    }
}
