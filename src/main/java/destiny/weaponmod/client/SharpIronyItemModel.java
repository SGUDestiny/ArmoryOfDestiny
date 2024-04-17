package destiny.weaponmod.client;

import destiny.weaponmod.WeaponMod;
import destiny.weaponmod.item.SharpIronyItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.GeoModel;

import static destiny.weaponmod.item.SharpIronyItem.AMMO_COUNT;

public class SharpIronyItemModel extends GeoModel<SharpIronyItem> {

    private final ResourceLocation SHARP_IRONY_5 = new ResourceLocation(WeaponMod.MODID, "textures/item/sharp_irony/sharp_irony_5.png");
    private final ResourceLocation SHARP_IRONY_4 = new ResourceLocation(WeaponMod.MODID, "textures/item/sharp_irony/sharp_irony_4.png");
    private final ResourceLocation SHARP_IRONY_3 = new ResourceLocation(WeaponMod.MODID, "textures/item/sharp_irony/sharp_irony_3.png");
    private final ResourceLocation SHARP_IRONY_2 = new ResourceLocation(WeaponMod.MODID, "textures/item/sharp_irony/sharp_irony_2.png");
    private final ResourceLocation SHARP_IRONY_1 = new ResourceLocation(WeaponMod.MODID, "textures/item/sharp_irony/sharp_irony_1.png");
    private final ResourceLocation SHARP_IRONY_0 = new ResourceLocation(WeaponMod.MODID, "textures/item/sharp_irony/sharp_irony_0.png");

    @Override
    public ResourceLocation getModelResource(SharpIronyItem sharpIronyItem) {
        return new ResourceLocation(WeaponMod.MODID, "geo/item/sharp_irony.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SharpIronyItem sharpIronyItem) {
        ItemStack stack = new ItemStack(sharpIronyItem);

        if (stack.getOrCreateTag().getInt(AMMO_COUNT) == 5){
            return SHARP_IRONY_5;
        }
        else if (stack.getOrCreateTag().getInt(AMMO_COUNT) == 4){
            return SHARP_IRONY_4;
        }
        else if (stack.getOrCreateTag().getInt(AMMO_COUNT) == 3){
            return SHARP_IRONY_3;
        }
        else if (stack.getOrCreateTag().getInt(AMMO_COUNT) == 2){
            return SHARP_IRONY_2;
        }
        else if (stack.getOrCreateTag().getInt(AMMO_COUNT) == 1){
            return SHARP_IRONY_1;
        }
        else {
            return SHARP_IRONY_0;
        }
    }



    @Override
    public ResourceLocation getAnimationResource(SharpIronyItem sharpIronyItem) {
        return new ResourceLocation(WeaponMod.MODID, "animations/item/sharp_irony.animation.json");
    }
}
