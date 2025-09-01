package destiny.armoryofdestiny.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import destiny.armoryofdestiny.ArmoryOfDestiny;
import destiny.armoryofdestiny.server.item.SmithingTongsItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import static destiny.armoryofdestiny.server.item.SmithingTongsItem.HELD_ITEM;

public class SmithingTongsItemRenderer extends GeoItemRenderer<SmithingTongsItem> {
    public SmithingTongsItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "smithing_tongs")));
    }

    @Override
    public GeoModel<SmithingTongsItem> getGeoModel() {
        if (getCurrentItemStack().getTag().get(HELD_ITEM) != null) {
            ItemStack held_item = ItemStack.of(getCurrentItemStack().getTag().getCompound(HELD_ITEM));

            if (held_item.getItem() != Items.AIR){
                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                boolean isBlockItem = itemRenderer.getModel(held_item, Minecraft.getInstance().level, null, 0).applyTransform(ItemDisplayContext.FIXED, new PoseStack(), false).isGui3d();

                if (isBlockItem) {
                    return new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "smithing_tongs_block"));
                } else {
                    return new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "smithing_tongs_item"));
                }
            }
        }
        return new DefaultedItemGeoModel<>(new ResourceLocation(ArmoryOfDestiny.MODID, "smithing_tongs"));
    }

    @Override
    public ResourceLocation getTextureLocation(SmithingTongsItem animatable) {
        return new ResourceLocation(ArmoryOfDestiny.MODID, "textures/item/smithing_tongs/" + getCurrentItemStack().getItem() + ".png");
    }

    @Override
    public void actuallyRender(PoseStack poseStack, SmithingTongsItem animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (getCurrentItemStack().getTag().get(HELD_ITEM) != null) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            ItemStack held_item = ItemStack.of(getCurrentItemStack().getTag().getCompound(HELD_ITEM));

            poseStack.pushPose();
            poseStack.translate(0, 2.2, 0);

            itemRenderer.renderStatic(held_item, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, Minecraft.getInstance().level, 0);
            poseStack.popPose();
        }
    }
}
