package destiny.armoryofdestiny.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import destiny.armoryofdestiny.server.block.blockentity.ArmorersTinkeringTableBlockEntity;
import destiny.armoryofdestiny.server.block.blockentity.BloomeryTopBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class BloomeryTopRenderer implements BlockEntityRenderer<BloomeryTopBlockEntity> {
    public BloomeryTopRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {

    }

    @Override
    public void render(BloomeryTopBlockEntity bloomery, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction direction = bloomery.getBlockState().getValue(HORIZONTAL_FACING);
        Level level = bloomery.getLevel();

        if (!bloomery.getInput().isEmpty()) {
            ItemStack stack = bloomery.getInput();

            poseStack.pushPose();
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            boolean isBlockItem = itemRenderer.getModel(stack, level, null, 0).applyTransform(ItemDisplayContext.FIXED, poseStack, false).isGui3d();
            poseStack.popPose();

            if (isBlockItem) {
                renderLyingBlock(level, poseStack, stack, bufferIn, combinedLightIn, direction, bloomery);
            } else {
                renderLyingItem(level, poseStack, stack, bufferIn, combinedLightIn, direction, bloomery);
            }
        }
    }

    private void renderLyingItem(Level level, PoseStack poseStack, ItemStack stack, MultiBufferSource bufferIn, int combinedLightIn, Direction direction, BloomeryTopBlockEntity bloomery) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();
        if (direction == Direction.NORTH) {
            poseStack.translate(0.5, 0.025, 0.375);
        } else if (direction == Direction.SOUTH) {
            poseStack.translate(0.5, 0.025, 0.625);
        } else if (direction == Direction.WEST) {
            poseStack.translate(0.375, 0.025, 0.5);
        } else {
            poseStack.translate(0.625, 0.025, 0.5);
        }

        float f = -direction.toYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(f));

        poseStack.mulPose(Axis.XP.rotationDegrees(-90));

        poseStack.scale(1, 1, 1);

        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, combinedLightIn, OverlayTexture.NO_OVERLAY, poseStack, bufferIn, level, 0);
        poseStack.popPose();
    }

    private void renderLyingBlock(Level level, PoseStack poseStack, ItemStack stack, MultiBufferSource bufferIn, int combinedLightIn, Direction direction, BloomeryTopBlockEntity bloomery) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();
        poseStack.translate(0.5, -0.05, 0.5);

        float f = -direction.toYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(f));

        poseStack.scale(1, 1, 1);

        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, combinedLightIn, OverlayTexture.NO_OVERLAY, poseStack, bufferIn, level, 0);
        poseStack.popPose();
    }
}
