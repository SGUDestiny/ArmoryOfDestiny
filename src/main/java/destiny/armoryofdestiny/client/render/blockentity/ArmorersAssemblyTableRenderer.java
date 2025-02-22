package destiny.armoryofdestiny.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import destiny.armoryofdestiny.blockentity.ArmorersAssemblyTableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ArmorersAssemblyTableRenderer implements BlockEntityRenderer<ArmorersAssemblyTableBlockEntity> {
    public ArmorersAssemblyTableRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {

    }

    @Override
    public void render(ArmorersAssemblyTableBlockEntity table, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction direction = table.getBlockState().getValue(HORIZONTAL_FACING);
        Level level = table.getLevel();
        int craftingProgress = table.getCraftingProgress();

        //Check if blueprint is present
        if (table.getItem(0) != ItemStack.EMPTY) {
            if (craftingProgress > 0 && !table.getItem(table.getCraftingProgress()).isEmpty()) {
                ItemStack currentItem = table.getItem(table.getCraftingProgress());

                renderLyingItem(level, poseStack, currentItem, bufferIn, combinedLightIn, direction);
            } else if (table.getWantItem() != ItemStack.EMPTY) {
                ItemStack wantItem = table.getWantItem();

                renderSpinningItem(level, wantItem, poseStack, partialTicks, bufferIn, combinedLightIn);
            }
            //Else if result item is present, render result item
        } else if (table.getItem(10) != ItemStack.EMPTY) {
            ItemStack resultItem = table.getItem(10);

            renderSpinningItem(level, resultItem, poseStack, partialTicks, bufferIn, combinedLightIn);
        }
    }

    private void renderSpinningItem(Level level, ItemStack stack, PoseStack poseStack, float partialTicks, MultiBufferSource bufferIn, int combinedLightIn) {
        poseStack.pushPose();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        float time = (level.getGameTime() + partialTicks) * 0.8f;
        float yOffset = Mth.sin(time * 0.05f) * 0.1f + 0.25f;

        poseStack.translate(0.5, 1 + yOffset, 0.5);
        poseStack.mulPose(Axis.YP.rotation(time * 0.1f));
        poseStack.scale(1, 1, 1);

        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, combinedLightIn, OverlayTexture.NO_OVERLAY, poseStack, bufferIn, level, 0);
        poseStack.popPose();
    }

    private void renderLyingItem(Level level, PoseStack poseStack, ItemStack stack, MultiBufferSource bufferIn, int combinedLightIn, Direction direction) {
        poseStack.pushPose();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.translate(0.625, 1, 0.5);

        float f = -direction.toYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(f));

        poseStack.mulPose(Axis.XP.rotationDegrees(-90));

        poseStack.scale(1, 1, 1);

        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, combinedLightIn, OverlayTexture.NO_OVERLAY, poseStack, bufferIn, level, 0);
        poseStack.popPose();
    }
}
