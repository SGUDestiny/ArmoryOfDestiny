package destiny.weaponmod.client;

import destiny.weaponmod.WeaponMod;
import destiny.weaponmod.item.DoubleTroubleItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import static destiny.weaponmod.item.DoubleTroubleItem.LEFT_BARREL;
import static destiny.weaponmod.item.DoubleTroubleItem.RIGHT_BARREL;

public class DoubleTroubleItemRenderer extends GeoItemRenderer<DoubleTroubleItem> {
    private final ResourceLocation DOUBLE_TROUBLE_FULL = new ResourceLocation(WeaponMod.MODID, "textures/item/double_trouble/double_trouble_full.png");
    private final ResourceLocation DOUBLE_TROUBLE_RIGHT = new ResourceLocation(WeaponMod.MODID, "textures/item/double_trouble/double_trouble_right.png");
    private final ResourceLocation DOUBLE_TROUBLE_LEFT = new ResourceLocation(WeaponMod.MODID, "textures/item/double_trouble/double_trouble_left.png");
    private final ResourceLocation DOUBLE_TROUBLE_EMPTY = new ResourceLocation(WeaponMod.MODID, "textures/item/double_trouble/double_trouble_empty.png");
    public DoubleTroubleItemRenderer() {
        super(new DoubleTroubleItemModel());
    }

    @Override
    public ResourceLocation getTextureLocation(DoubleTroubleItem animatable) {
        ItemStack stack = getCurrentItemStack();
        CompoundTag tag = stack.getTag();

        if (tag != null) {
            int leftBarrel = tag.getInt(LEFT_BARREL);
            int rightBarrel = tag.getInt(RIGHT_BARREL);

            if (leftBarrel >= 1 && rightBarrel >= 1) {
                return DOUBLE_TROUBLE_FULL;
            } else if (leftBarrel <= 0 && rightBarrel >= 1) {
                return DOUBLE_TROUBLE_RIGHT;
            } else if (leftBarrel >= 1) {
                return DOUBLE_TROUBLE_LEFT;
            } else {
                return DOUBLE_TROUBLE_EMPTY;
            }
        }
        return DOUBLE_TROUBLE_FULL;
    }


    @Override
    public ItemStack getCurrentItemStack() {
        return super.getCurrentItemStack();
    }
}
