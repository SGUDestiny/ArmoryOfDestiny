package destiny.weaponmod.client;

import destiny.weaponmod.WeaponMod;
import destiny.weaponmod.item.SharpIronyItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.GeoModel;

import static destiny.weaponmod.item.SharpIronyItem.AMMO_COUNT;

public class SharpIronyItemModel extends GeoModel<SharpIronyItem> {

    @Override
    public ResourceLocation getModelResource(SharpIronyItem sharpIronyItem) {
        return new ResourceLocation(WeaponMod.MODID, "geo/item/sharp_irony.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SharpIronyItem sharpIronyItem) {
        return new ResourceLocation(WeaponMod.MODID, "textures/item/sharp_irony/sharp_irony_5.png");
    }



    @Override
    public ResourceLocation getAnimationResource(SharpIronyItem sharpIronyItem) {
        return new ResourceLocation(WeaponMod.MODID, "animations/item/sharp_irony.animation.json");
    }
}
