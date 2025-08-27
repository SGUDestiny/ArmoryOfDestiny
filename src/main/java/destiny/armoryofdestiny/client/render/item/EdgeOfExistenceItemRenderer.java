package destiny.armoryofdestiny.client.render.item;

import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.EdgeOfExistenceItem;
import destiny.armoryofdestiny.server.item.MurasamaItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import static destiny.armoryofdestiny.server.item.MurasamaItem.ABILITY_TICK;

public class EdgeOfExistenceItemRenderer extends GeoItemRenderer<EdgeOfExistenceItem> {
    public EdgeOfExistenceItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "edge_of_existence")));
    }

    @Override
    public ResourceLocation getTextureLocation(EdgeOfExistenceItem animatable) {
        ItemStack stack = getCurrentItemStack();
        CompoundTag tag = stack.getTag();

        if (tag != null) {
            int animationFrame = tag.getInt(ABILITY_TICK);

            return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/edge_of_existence/edge_of_existence_" + animationFrame + ".png");
        }
        return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/edge_of_existence/edge_of_existence_0.png");
    }
}
