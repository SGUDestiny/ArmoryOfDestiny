package destiny.armoryofdestiny.client.render.item;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.Spas12Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import static destiny.armoryofdestiny.server.item.Spas12Item.SHELL_TYPE;

public class Spas12ItemRenderer extends GeoItemRenderer<Spas12Item> {

    private final ResourceLocation BUCKSHOT = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/spas12/spas12_buckshot.png");
    private final ResourceLocation SLUG = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/spas12/spas12_slug.png");
    private final ResourceLocation INCENDIARY = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/spas12/spas12_incendiary.png");
    private final ResourceLocation EXPLOSIVE_SLUG = new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/spas12/spas12_explosive_slug.png");

    public Spas12ItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "spas12")));
    }

    @Override
    public ResourceLocation getTextureLocation(Spas12Item animatable) {
        ItemStack stack = getCurrentItemStack();
        CompoundTag tag = stack.getTag();

        if (tag != null) {
            String type = tag.getString(SHELL_TYPE);

            switch (type) {
                case "slug_shell" -> {
                    return SLUG;
                }
                case "incendiary_shell" -> {
                    return INCENDIARY;
                }
                case "explosive_slug_shell" -> {
                    return EXPLOSIVE_SLUG;
                }
                default -> {
                    return BUCKSHOT;
                }
            }
        }
        return BUCKSHOT;
    }

    @Override
    public ItemStack getCurrentItemStack() {
        return super.getCurrentItemStack();
    }
}