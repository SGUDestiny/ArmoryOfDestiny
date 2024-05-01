package destiny.armoryofdestiny.client;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.item.SharpIronyItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import static destiny.armoryofdestiny.item.SharpIronyItem.AMMO_COUNT;

public class SharpIronyItemRenderer extends GeoItemRenderer<SharpIronyItem> {
    private final ResourceLocation SHARP_IRONY_5 = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/sharp_irony/sharp_irony_5.png");
    private final ResourceLocation SHARP_IRONY_4 = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/sharp_irony/sharp_irony_4.png");
    private final ResourceLocation SHARP_IRONY_3 = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/sharp_irony/sharp_irony_3.png");
    private final ResourceLocation SHARP_IRONY_2 = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/sharp_irony/sharp_irony_2.png");
    private final ResourceLocation SHARP_IRONY_1 = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/sharp_irony/sharp_irony_1.png");
    private final ResourceLocation SHARP_IRONY_0 = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/sharp_irony/sharp_irony_0.png");
    public SharpIronyItemRenderer() {

        super(new SharpIronyItemModel());
        //addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(SharpIronyItem animatable) {
        ItemStack stack = getCurrentItemStack();
        CompoundTag tag = stack.getTag();


        if (tag != null) {
            int ammoCount = tag.getInt(AMMO_COUNT);

            switch (ammoCount) {
                case 4 -> {
                    return SHARP_IRONY_4;
                }
                case 3 -> {
                    return SHARP_IRONY_3;
                }
                case 2 -> {
                    return SHARP_IRONY_2;
                }
                case 1 -> {
                    return SHARP_IRONY_1;
                }
                case 0 -> {
                    return SHARP_IRONY_0;
                }
                default -> {
                    return SHARP_IRONY_5;
                }
            }
        }
        return SHARP_IRONY_5;
    }

    @Override
    public ItemStack getCurrentItemStack() {
        return super.getCurrentItemStack();
    }
}
