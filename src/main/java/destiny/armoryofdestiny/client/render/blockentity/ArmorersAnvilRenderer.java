package destiny.armoryofdestiny.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import destiny.armoryofdestiny.server.block.blockentity.ArmorersAnvilBlockEntity;
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

import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ArmorersAnvilRenderer implements BlockEntityRenderer<ArmorersAnvilBlockEntity> {
    public ArmorersAnvilRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {

    }

    @Override
    public void render(ArmorersAnvilBlockEntity anvil, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction direction = anvil.getBlockState().getValue(HORIZONTAL_FACING);
        Level level = anvil.getLevel();

        if (anvil.getStoredItemAmount() > 0) {
            List<ItemStack> storedItems = anvil.getAllStoredItems();
            double itemWidth = 0.03;
            double blockWidth = 0.25;
            double totalWidth = 0;

            for (ItemStack storedItem : storedItems) {
                poseStack.pushPose();
                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                boolean isBlockItem = itemRenderer.getModel(storedItem, level, null, 0).applyTransform(ItemDisplayContext.FIXED, poseStack, false).isGui3d();
                poseStack.popPose();

                if (isBlockItem) {
                    renderLyingBlock(level, poseStack, storedItem, bufferIn, combinedLightIn, direction, anvil, totalWidth);
                    totalWidth += blockWidth;
                } else {
                    renderLyingItem(level, poseStack, storedItem, bufferIn, combinedLightIn, direction, anvil, totalWidth);
                    totalWidth += itemWidth;
                }
            }
        }
    }

    private void renderLyingItem(Level level, PoseStack poseStack, ItemStack stack, MultiBufferSource bufferIn, int combinedLightIn, Direction direction, ArmorersAnvilBlockEntity anvil, double offset) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();
        if (direction == Direction.NORTH) {
            poseStack.translate(0.5, 1.025 + offset, 0.375);
        } else if (direction == Direction.SOUTH) {
            poseStack.translate(0.5, 1.025 + offset, 0.625);
        } else if (direction == Direction.WEST) {
            poseStack.translate(0.375, 1.025 + offset, 0.5);
        } else {
            poseStack.translate(0.625, 1.025 + offset, 0.5);
        }

        float f = -direction.toYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(f));

        poseStack.mulPose(Axis.XP.rotationDegrees(-90));

        poseStack.scale(1, 1, 1);

        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, combinedLightIn, OverlayTexture.NO_OVERLAY, poseStack, bufferIn, level, 0);
        poseStack.popPose();
    }

    private void renderLyingBlock(Level level, PoseStack poseStack, ItemStack stack, MultiBufferSource bufferIn, int combinedLightIn, Direction direction, ArmorersAnvilBlockEntity anvil, double offset) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();
        poseStack.translate(0.5, 0.95 + offset, 0.5);

        float f = -direction.toYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(f));

        poseStack.scale(1, 1, 1);

        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, combinedLightIn, OverlayTexture.NO_OVERLAY, poseStack, bufferIn, level, 0);
        poseStack.popPose();
    }
}
