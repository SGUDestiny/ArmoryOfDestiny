package destiny.armoryofdestiny.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import destiny.armoryofdestiny.server.block.blockentity.ArmorersTinkeringTableBlockEntity;
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
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ArmorersAssemblyTableRenderer implements BlockEntityRenderer<ArmorersTinkeringTableBlockEntity> {
    public ArmorersAssemblyTableRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {

    }

    @Override
    public void render(ArmorersTinkeringTableBlockEntity table, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction direction = table.getBlockState().getValue(HORIZONTAL_FACING);
        Level level = table.getLevel();

        //Check if blueprint is present
        if (table.getBlueprintItem() != ItemStack.EMPTY) {
            if (table.getInputItem() != ItemStack.EMPTY) {
                ItemStack currentItem = table.getInputItem();

                poseStack.pushPose();
                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                boolean isBlockItem = itemRenderer.getModel(currentItem, table.getLevel(), null, 0).applyTransform(ItemDisplayContext.FIXED, poseStack, false).isGui3d();
                poseStack.popPose();

                if (isBlockItem) {
                    renderLyingBlock(level, poseStack, currentItem, bufferIn, combinedLightIn, direction, table);
                } else {
                    renderLyingItem(level, poseStack, currentItem, bufferIn, combinedLightIn, direction, table);
                }
            } else if (table.getWantItem() != ItemStack.EMPTY) {
                ItemStack wantItem = table.getWantItem();

                renderSpinningItem(level, wantItem, poseStack, partialTicks, bufferIn, combinedLightIn);
            }
            //Else if result item is present, render result item
        } else if (table.getInputItem() != ItemStack.EMPTY) {
            ItemStack resultItem = table.getInputItem();

            renderSpinningItem(level, resultItem, poseStack, partialTicks, bufferIn, combinedLightIn);
        }

        if (table.getHammerSlot() != ItemStack.EMPTY) {
            ItemStack hammer = table.getHammerSlot();

            renderHungHammer(level, poseStack, hammer, bufferIn, combinedLightIn, direction, table);
        }
    }

    private void renderSpinningItem(Level level, ItemStack stack, PoseStack poseStack, float partialTicks, MultiBufferSource bufferIn, int combinedLightIn) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();
        float time = (level.getGameTime() + partialTicks) * 0.8f;
        float yOffset = Mth.sin(time * 0.05f) * 0.1f + 0.25f;

        poseStack.translate(0.5, 1 + yOffset, 0.5);
        poseStack.mulPose(Axis.YP.rotation(time * 0.1f));
        poseStack.scale(1, 1, 1);

        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, combinedLightIn, OverlayTexture.NO_OVERLAY, poseStack, bufferIn, level, 0);
        poseStack.popPose();
    }

    private void renderLyingItem(Level level, PoseStack poseStack, ItemStack stack, MultiBufferSource bufferIn, int combinedLightIn, Direction direction, ArmorersTinkeringTableBlockEntity table) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();
        if (direction == Direction.NORTH) {
            poseStack.translate(0.5, 1, 0.375);
        } else if (direction == Direction.SOUTH) {
            poseStack.translate(0.5, 1, 0.625);
        } else if (direction == Direction.WEST) {
            poseStack.translate(0.375, 1, 0.5);
        } else {
            poseStack.translate(0.625, 1, 0.5);
        }

        float f = -direction.toYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(f));

        poseStack.mulPose(Axis.XP.rotationDegrees(-90));

        poseStack.scale(1, 1, 1);

        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, combinedLightIn, OverlayTexture.NO_OVERLAY, poseStack, bufferIn, level, 0);
        poseStack.popPose();
    }

    private void renderLyingBlock(Level level, PoseStack poseStack, ItemStack stack, MultiBufferSource bufferIn, int combinedLightIn, Direction direction, ArmorersTinkeringTableBlockEntity table) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();
        poseStack.translate(0.5, 0.95, 0.5);

        float f = -direction.toYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(f));

        poseStack.scale(1, 1, 1);

        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, combinedLightIn, OverlayTexture.NO_OVERLAY, poseStack, bufferIn, level, 0);
        poseStack.popPose();
    }

    private void renderHungHammer(Level level, PoseStack poseStack, ItemStack hammer, MultiBufferSource bufferIn, int combinedLightIn, Direction direction, ArmorersTinkeringTableBlockEntity table) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();
        if (direction == Direction.NORTH) {
            poseStack.translate(0.4325, 0.72, -0.025);
        } else if (direction == Direction.SOUTH) {
            poseStack.translate(0.5675, 0.72, 1.025);
        } else if (direction == Direction.WEST) {
            poseStack.translate(-0.025, 0.72, 0.4325);
        } else {
            poseStack.translate(1.025, 0.72, 0.5675);
        }

        float f = direction.toYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(f));

        poseStack.mulPose(Axis.ZP.rotationDegrees(45));

        poseStack.scale(1, 1, 1);

        itemRenderer.renderStatic(hammer, ItemDisplayContext.GROUND, combinedLightIn, OverlayTexture.NO_OVERLAY, poseStack, bufferIn, level, 0);
        poseStack.popPose();
    }
}
